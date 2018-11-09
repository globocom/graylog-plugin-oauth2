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
