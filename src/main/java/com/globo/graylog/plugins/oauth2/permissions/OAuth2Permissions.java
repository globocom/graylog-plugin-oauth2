package com.globo.graylog.plugins.oauth2.permissions;

import com.google.common.collect.ImmutableSet;
import org.graylog2.plugin.security.Permission;
import org.graylog2.plugin.security.PluginPermissions;

import java.util.Collections;
import java.util.Set;

import static org.graylog2.plugin.security.Permission.create;

public class OAuth2Permissions implements PluginPermissions {
    public static final String CONFIG_READ = "oauthconfig:read";
    public static final String CONFIG_UPDATE = "oauthconfig:edit";

    private final ImmutableSet<Permission> permissions = ImmutableSet.of(
            create(CONFIG_READ, "Read Oauth authenticator config"),
            create(CONFIG_UPDATE, "Update Oauth authenticator config")
    );

    @Override
    public Set<Permission> permissions() {
        return permissions;
    }

    @Override
    public Set<Permission> readerBasePermissions() {
        return Collections.emptySet();
    }
}
