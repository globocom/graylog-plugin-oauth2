package com.globo;

import com.globo.models.AcessToken;
import com.globo.models.UserBackStage;
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
import java.util.Optional;

public class GloboAuthRealm extends AuthenticatingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(GloboAuthRealm.class);

    public static final String NAME = "globo-oauth";

    private final LdapUserAuthenticator ldapAuthenticator;
    private final UserService userService;
    private final ClusterConfigService clusterConfigService;
    private final RoleService roleService;
    private final GloboAuth globoAuth;

    @Inject
    public GloboAuthRealm(UserService userService,
                          ClusterConfigService clusterConfigService,
                          RoleService roleService,
                          LdapUserAuthenticator ldapAuthenticator,
                          GloboAuth globoAuth) {
        this.userService = userService;
        this.clusterConfigService = clusterConfigService;
        this.roleService = roleService;
        this.ldapAuthenticator = ldapAuthenticator;
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

        AcessToken acessToken = globoAuth.getAuthorization(code, config.clientId(), config.clientSecret(),
                config.urlBackstage());

        UserBackStage userBackStage = globoAuth.getUser(config.urlBackstage(), acessToken);

        final String username = userBackStage.getUserName();
        User user = null;

        if (userBackStage != null) {

            if (ldapAuthenticator.isEnabled()) {
                user = ldapAuthenticator.syncLdapUser(username);
            }

            if (user == null) {
                user = userService.load(username);
            }

            if (user == null) {
                if (config.autoCreateUser()) {
                    user = userService.create();

                    user.setName(username);
                    user.setExternal(true);
                    user.setPassword("globo123456");
                    user.setPermissions(Collections.emptyList());
                    user.setFullName(username);
                    user.setEmail(userBackStage.getEmail());

                    if (userBackStage.getRoleIds().isEmpty()){
                        try {
                            Role role = roleService.loadAllLowercaseNameMap().get("Reader");
                            if (role != null) {
                                user.setRoleIds(Collections.singleton(role.getId()));
                            } else {
                                LOG.warn("Could not find group named {}, giving user reader role instead", "\"Reader");
                                user.setRoleIds(Collections.singleton(roleService.getReaderRoleObjectId()));
                            }
                        } catch (NotFoundException e) {
                            LOG.info("Unable to retrieve roles, giving user reader role");
                            user.setRoleIds(Collections.singleton(roleService.getReaderRoleObjectId()));
                        }
                    } else {
                        user.setRoleIds(Collections.singleton(roleService.getReaderRoleObjectId()));
                    }

                    try {
                        userService.save(user);
                    } catch (ValidationException e) {
                        LOG.error("Unable to save user {}", user, e);
                        return null;
                    }
                }
            }

            LOG.info("Logged with user {}", user.getName());

            ShiroSecurityContext.requestSessionCreation(true);
            return new SimpleAccount(user.getName(), null, NAME);
        }

        return null;
    }

    private String headerValue(MultivaluedMap<String, String> headers, @Nullable String headerName) {

        return headers.getFirst(headerName.toLowerCase());
    }
}
