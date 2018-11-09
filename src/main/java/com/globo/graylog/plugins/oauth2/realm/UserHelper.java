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

import com.globo.graylog.plugins.oauth2.models.UserBackStage;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Config;
import org.apache.shiro.authc.AuthenticationException;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.database.users.User;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;

public class UserHelper {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Realm.class);
    private UserService userService;
    private RoleService roleService;

    @Inject
    public UserHelper(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public User saveUserIfNecessary(User user, OAuth2Config config, UserBackStage oAuthUser) throws AuthenticationException {
        if(user == null && config.autoCreateUser()) {
            user = userService.create();
            user.setName(oAuthUser.getEmail());
            user.setExternal(true);
            user.setFullName(oAuthUser.getName() + " " + oAuthUser.getSurName());
            user.setEmail(oAuthUser.getEmail());
            user.setPassword("dummy password");
            user.setPermissions(Collections.emptyList());

            //TODO: Review this code, implementing configuration mappings.
            user.setRoleIds(Collections.singleton(roleService.getReaderRoleObjectId()));

            try {
                userService.save(user);
                return user;
            } catch (ValidationException e) {
                LOG.error("Unable to save user {}", user, e);
                throw new AuthenticationException("Unable to save the user on the local database");
            }
        } else {
            return user;
        }
    }


}
