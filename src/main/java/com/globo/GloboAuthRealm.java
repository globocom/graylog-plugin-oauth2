package com.globo;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.shared.security.HttpHeadersToken;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.annotation.Nullable;

public class GloboAuthRealm extends AuthenticatingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(GloboAuthRealm.class);

    public static final String NAME = "globo-oauth";

    private final UserService userService;
    private final ClusterConfigService clusterConfigService;
    private final RoleService roleService;
    private final GloboAuth globoAuth;

    @Inject
    public GloboAuthRealm(UserService userService,
                          ClusterConfigService clusterConfigService,
                          RoleService roleService,
                          GloboAuth globoAuth) {
        this.userService = userService;
        this.clusterConfigService = clusterConfigService;
        this.roleService = roleService;
        this.globoAuth = globoAuth;
        setAuthenticationTokenClass(HttpHeadersToken.class);
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setCachingEnabled(false);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws
            AuthenticationException {

        HttpHeadersToken headersToken = (HttpHeadersToken) authenticationToken;
        final MultivaluedMap<String, String> requestHeaders = headersToken.getHeaders();

        final GloboAuthConfig config = clusterConfigService.getOrDefault(
                GloboAuthConfig.class,
                GloboAuthConfig.defaultConfig());

        final String referer = headerValue(requestHeaders, "referer");

        String code = globoAuth.getCodeFromReferer(referer);
        globoAuth.getAuthorizationCode(code, config.clientId(), config.clientSecret(), config.urlBackstage());

        return null;
    }

    private String headerValue(MultivaluedMap<String, String> headers, @Nullable String headerName) {

        return headers.getFirst(headerName.toLowerCase());
    }
}
