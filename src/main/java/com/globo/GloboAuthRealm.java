package com.globo;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.shared.security.HttpHeadersToken;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;

public class GloboAuthRealm extends AuthenticatingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(GloboAuthRealm.class);

    public static final String NAME = "Globo Auth";

    private final UserService userService = null;
    private final ClusterConfigService clusterConfigService = null;
    private final RoleService roleService = null;

    public GloboAuthRealm() {

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        HttpHeadersToken headersToken = (HttpHeadersToken) authenticationToken;
        final MultivaluedMap<String, String> requestHeaders = headersToken.getHeaders();

        return null;
    }
}
