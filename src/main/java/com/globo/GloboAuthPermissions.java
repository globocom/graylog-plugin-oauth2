package com.globo;

import com.google.common.collect.ImmutableSet;
import org.graylog2.plugin.security.Permission;
import org.graylog2.plugin.security.PluginPermissions;

import java.util.Collections;
import java.util.Set;

import static org.graylog2.plugin.security.Permission.create;

public class GloboAuthPermissions implements PluginPermissions {

    public static final String CONFIG_READ = "globoauthconfig:read";
    public static final String CONFIG_UPDATE = "globoauthconfig:edit";

    private final ImmutableSet<Permission> permissions = ImmutableSet.of(
            create(CONFIG_READ, "Read Auth authenticator config"),
            create(CONFIG_UPDATE, "Update Auth authenticator config")
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
