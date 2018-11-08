package com.globo.graylog.plugins.oauth2;

import com.globo.graylog.plugins.oauth2.permissions.OAuth2Permissions;
import com.globo.graylog.plugins.oauth2.audit.OAuth2AuditEventTypes;
import com.globo.graylog.plugins.oauth2.realm.OAuth2Realm;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Rest;
import com.google.inject.Scopes;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import java.util.Collections;
import java.util.Set;


public class OAuth2Module extends PluginModule {

    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        authenticationRealmBinder().addBinding(OAuth2Realm.NAME).to(OAuth2Realm.class).in(Scopes.SINGLETON);
        addRestResource(OAuth2Rest.class);
        addPermissions(OAuth2Permissions.class);
        addAuditEventTypes(OAuth2AuditEventTypes.class);
    }
}
