package com.globo.main;

import com.globo.audit.GloboAuthPermissions;
import com.globo.GloboAuthRealm;
import com.globo.GloboAuthRest;
import com.globo.audit.GloboAuthAuditEventTypes;
import com.google.inject.Scopes;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;


public class GloboAuthModule extends PluginModule {

    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        authenticationRealmBinder().addBinding(GloboAuthRealm.NAME).to(GloboAuthRealm.class).in(Scopes.SINGLETON);
        addRestResource(GloboAuthRest.class);
        addPermissions(GloboAuthPermissions.class);
        addAuditEventTypes(GloboAuthAuditEventTypes.class);
    }
}
