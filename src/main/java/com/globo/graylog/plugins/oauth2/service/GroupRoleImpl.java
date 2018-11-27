/**
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

package com.globo.graylog.plugins.oauth2.service;


import com.google.common.collect.Maps;
import org.bson.types.ObjectId;
import org.graylog2.database.CollectionName;
import org.graylog2.database.PersistedImpl;
import org.graylog2.database.validators.FilledStringValidator;
import org.graylog2.plugin.database.validators.Validator;

import java.util.Map;

@CollectionName("group_role")
public class GroupRoleImpl extends PersistedImpl implements GroupRole {

    public static final String GROUP = "group";
    public static final String ROLE = "role";

    public GroupRoleImpl(Map<String, Object> fields) {
    super(fields);
    }

    public GroupRoleImpl(ObjectId id, Map<String, Object> fields) {
    super(id, fields);
    }

    public Map<String, Validator> getValidations() {
        Map<String, Validator> validations = Maps.newHashMap();
        validations.put("group", new FilledStringValidator());
        validations.put("name", new FilledStringValidator());
        return validations;
    }

    @Override
    public Map<String, Validator> getEmbeddedValidations(String key) {
    return null;
    }

    @Override
    public String getGroup() {
        return String.valueOf(this.fields.get("group"));
    }

    @Override
    public void setGroup(String group) {
    this.fields.put("group", group);
    }

    @Override
    public String getRole() {
    return String.valueOf(this.fields.get("role"));
    }

    @Override
    public void setRole(String role) {
    this.fields.put("role", role);
    }

}
