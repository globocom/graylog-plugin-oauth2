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
import com.globo.graylog.plugins.oauth2.models.UserBackStage;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Config;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.database.users.User;
import org.graylog2.security.realm.LdapUserAuthenticator;
import org.graylog2.shared.security.HttpHeadersToken;
import org.graylog2.shared.security.ShiroSecurityContext;
import org.graylog2.shared.users.Role;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class OAuth2Realm extends AuthenticatingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Realm.class);

    public static final String NAME = "oauth2";

    private final LdapUserAuthenticator ldapAuthenticator;
    private final UserService userService;
    private final ClusterConfigService clusterConfigService;
    private final RoleService roleService;
    private final OAuth2 oAuth2;

    @Inject
    public OAuth2Realm(UserService userService,
                       ClusterConfigService clusterConfigService,
                       RoleService roleService,
                       LdapUserAuthenticator ldapAuthenticator,
                       OAuth2 oAuth2) {
        this.userService = userService;
        this.clusterConfigService = clusterConfigService;
        this.roleService = roleService;
        this.ldapAuthenticator = ldapAuthenticator;
        this.oAuth2 = oAuth2;
        setAuthenticationTokenClass(HttpHeadersToken.class);
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setCachingEnabled(false);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws
            AuthenticationException {

        HttpHeadersToken headersToken = (HttpHeadersToken) authenticationToken;
        final MultivaluedMap<String, String> requestHeaders = headersToken.getHeaders();

        final OAuth2Config config = clusterConfigService.getOrDefault(
                OAuth2Config.class,
                OAuth2Config.defaultConfig());

        final String referer = headerValue(requestHeaders, "referer");

        String code = oAuth2.getCodeFromReferer(referer);

        String redirectUrl = "http://localhost:8080/";
        String grantType = "authorization_code";

        AcessToken acessToken = oAuth2.getAuthorization(
                code, config.clientId(), config.clientSecret(),
                config.urlBackstage(), redirectUrl, grantType
        );

        UserBackStage userBackStage = oAuth2.getUser(config.urlBackstage(), acessToken);

        final String username = userBackStage.getUserName();
        User user = null;

        if (userBackStage != null) {

            if (user == null) {
                user = userService.load(userBackStage.getEmail());
            }

            if (user == null) {
                if (config.autoCreateUser()) {
                    user = userService.create();

                    user.setName(userBackStage.getEmail());
                    user.setExternal(true);
                    user.setPassword("dummy password");
                    user.setPermissions(Collections.emptyList());
                    user.setFullName(userBackStage.getEmail());
                    user.setEmail(userBackStage.getEmail());

                    //TODO: Review this code, implementing configuration mappings.
                    user.setRoleIds(Collections.singleton(roleService.getReaderRoleObjectId()));

                    try {
                        userService.save(user);
                    } catch (ValidationException e) {
                        LOG.error("Unable to save user {}", user, e);
                        return null;
                    }
                }
            }

            ShiroSecurityContext.requestSessionCreation(true);
            return new SimpleAccount(user.getName(), null, NAME);
        }

        return null;
    }

    private String headerValue(MultivaluedMap<String, String> headers, @Nullable String headerName) {

        return headers.getFirst(headerName.toLowerCase());
    }
}
