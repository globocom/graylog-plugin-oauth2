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

package com.globo.graylog.plugins.oauth2.realm;

import com.globo.graylog.plugins.oauth2.models.UserOAuth;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Config;
import com.globo.graylog.plugins.oauth2.service.GroupRoleInterface;
import com.globo.graylog.plugins.oauth2.service.GroupRoleService;
import com.globo.graylog.plugins.oauth2.service.GroupRoleServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.database.users.User;
import org.graylog2.shared.users.Role;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserHelper {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Realm.class);
    private UserService userService;
    private RoleService roleService;
    private GroupRoleService groupRoleService;

    @Inject
    public UserHelper(UserService userService, RoleService roleService, GroupRoleServiceImpl groupRoleServiceImpl) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupRoleService = groupRoleServiceImpl;
    }

    public User saveUserIfNecessary(User user, OAuth2Config config, UserOAuth oAuthUser) throws AuthenticationException {

        Set<String> roles = new HashSet<>();

        if (user == null) {
            try {
                user = userService.create();
            } catch (NullPointerException e){
                throw new AuthenticationException("Unable to create an user object.");
            }
        }

        user.setName(oAuthUser.getEmail());
        user.setExternal(true);
        user.setFullName(oAuthUser.getName() + " " + oAuthUser.getSurName());
        user.setEmail(oAuthUser.getEmail());
        user.setPassword("discovery");
        user.setPermissions(Collections.emptyList());

        String defaultGroup = config.defaultGroup();
        List<GroupRoleInterface> groups = groupRoleService.loadAll();

        if (groups.isEmpty() || oAuthUser.getGroups().isEmpty()){
            user.setRoleIds(Collections.singleton(getRole(defaultGroup)));
        } else {
            for (GroupRoleInterface group : groups) {
                for(String groupUser: oAuthUser.getGroups()) {
                    if (group.getGroup().equals(groupUser)) {
                        getRolesIds(roles, group);
                    }
                }
            }
        }

        if (roles.isEmpty()) {
            user.setRoleIds(Collections.singleton(getRole(defaultGroup)));
        } else {
            user.setRoleIds(roles);
        }

        try {
            userService.save(user);
            return user;
        } catch (ValidationException e) {
            LOG.error("Unable to save user {}", user, e);
            throw new AuthenticationException("Unable to save the user on the local database");
        }

    }

    public String getRole(String group) {
        try{
            Role role = roleService.loadAllLowercaseNameMap().get(group.toLowerCase());
            if (role != null) {
                group = role.getId();
            }
        }  catch (NotFoundException e) {
            LOG.error("Unable to retrieve roles, giving user reader role");
            group = roleService.getReaderRoleObjectId();
        }

        return group;
    }

    public void getRolesIds(Set<String> roles, GroupRoleInterface group) {

        try{
            Role role = roleService.loadAllLowercaseNameMap().get(group.getRole().toLowerCase());
            if (role != null) {
                roles.add(role.getId());
            }
        }  catch (NotFoundException e) {
            LOG.error("Unable to retrieve roles, giving user reader role");
        }
    }

}
