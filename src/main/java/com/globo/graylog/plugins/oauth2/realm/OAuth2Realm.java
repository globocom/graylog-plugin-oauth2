/*
 * This file is part of Graylog Plugin Oauth.
 *
 * Graylog Plugin Oauth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Graylog Plugin Oauth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>
 */

package com.globo.graylog.plugins.oauth2.realm;

import com.globo.graylog.plugins.oauth2.models.AcessToken;
import com.globo.graylog.plugins.oauth2.models.UserOAuth;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Config;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.database.users.User;
import org.graylog2.shared.security.HttpHeadersToken;
import org.graylog2.shared.security.ShiroSecurityContext;
import org.graylog2.shared.users.UserService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

public class OAuth2Realm extends AuthenticatingRealm {
    public static final String NAME = "oauth2";

    private final UserService userService;
    private final ClusterConfigService clusterConfigService;
    private final OAuth2 oAuth2;
    private final UserHelper userHelper;

    @Inject
    public OAuth2Realm(UserService userService,
                       ClusterConfigService clusterConfigService,
                       OAuth2 oAuth2,
                       UserHelper userHelper
                       ) {
        this.userService = userService;
        this.clusterConfigService = clusterConfigService;
        this.oAuth2 = oAuth2;
        this.userHelper = userHelper;
        setAuthenticationTokenClass(HttpHeadersToken.class);
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setCachingEnabled(false);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final OAuth2Config config = clusterConfigService.getOrDefault(OAuth2Config.class, OAuth2Config.defaultConfig());
        final String referer = getReferer((HttpHeadersToken) authenticationToken);
        UserOAuth oAuthUser = getOAuthUser(referer, config);
        User user = null;

        if (oAuthUser != null) {
            user = userService.load(oAuthUser.getEmail());

            if (user == null && config.autoCreateUser()) {
                    user = userService.create();
                    user = userHelper.saveUserIfNecessary(user, config, oAuthUser);
            }

            ShiroSecurityContext.requestSessionCreation(true);
            return new SimpleAccount(user.getName(), null, NAME);
        } else {
            throw new AuthenticationException("The user could not be found");
        }

    }

    private UserOAuth getOAuthUser(String referer, OAuth2Config config) {
        String code = oAuth2.getCodeFromReferer(referer);
        AcessToken acessToken = oAuth2.getAuthorization(code, config.clientId(), config.clientSecret(), config.tokenServerUrl(), config.redirectUrl());
        return oAuth2.getUser(config.dataServerUrl(), acessToken);
    }

    private String getReferer(HttpHeadersToken headersToken) {
        final MultivaluedMap<String, String> requestHeaders = headersToken.getHeaders();
        return headerValue(requestHeaders, "referer");
    }

    private String headerValue(MultivaluedMap<String, String> headers, @Nullable String headerName) {
        return headers.getFirst(headerName.toLowerCase());
    }
}
