package com.globo.audit;

import com.google.common.collect.ImmutableSet;
import org.graylog2.audit.PluginAuditEventTypes;

import java.util.Set;

public class GloboAuthAuditEventTypes implements PluginAuditEventTypes {
    private static final String NAMESPACE = "oauth_auth:";

    public static final String CONFIG_UPDATE = NAMESPACE + "oauth:update";

    private static final Set<String> EVENT_TYPES = ImmutableSet.<String>builder()
            .add(CONFIG_UPDATE)
            .build();

    @Override
    public Set<String> auditEventTypes() {
        return EVENT_TYPES;
    }
}
