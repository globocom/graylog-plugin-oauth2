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
import com.globo.graylog.plugins.oauth2.service.GroupRoleImpl;
import com.globo.graylog.plugins.oauth2.service.GroupRoleInterface;
import com.globo.graylog.plugins.oauth2.service.GroupRoleServiceImpl;
import com.google.common.collect.Lists;
import org.apache.shiro.authc.AuthenticationException;
import org.bson.types.ObjectId;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.database.users.User;
import org.graylog2.shared.users.UserService;
import org.graylog2.users.RoleService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class UserHelperTest {

    private UserHelper userHelper;

    @Test(expected = NullPointerException.class)
    public void whenUserIsNull() {
        OAuth2Config configMock = mock(OAuth2Config.class);
        userHelper = new UserHelper(null, null, null);
        userHelper.saveUserIfNecessary(null, configMock, mock(UserOAuth.class));
    }

    @Test(expected = AuthenticationException.class)
    public void whenCouldNotSaveTheUser() throws ValidationException {
        User dummyUser = mock(User.class);
        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.create()).thenReturn(dummyUser);
        when(userServiceMock.save(any())).thenThrow(new ValidationException("Invalid user"));

        RoleService roleServiceMock = mock(RoleService.class);
        GroupRoleServiceImpl groupRoleServiceMock = mock(GroupRoleServiceImpl.class);

        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(true);

        userHelper = new UserHelper(userServiceMock, roleServiceMock, groupRoleServiceMock);
        userHelper.saveUserIfNecessary(dummyUser, configMock, mock(UserOAuth.class));
    }

    @Test
    public void whenEverythingIsOkRoleDefault() {
        User dummyUser = mock(User.class);
        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.create()).thenReturn(dummyUser);

        RoleService roleServiceMock = mock(RoleService.class);
        GroupRoleServiceImpl groupRoleServiceMock = mock(GroupRoleServiceImpl.class);


        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(true);

        User user = mock(User.class);

        userHelper = new UserHelper(userServiceMock, roleServiceMock, groupRoleServiceMock);
        User savedUser = userHelper.saveUserIfNecessary(user, configMock, mock(UserOAuth.class));

        assertSame(user, savedUser);
    }

    @Test
    public void whenEverythingIsOk() {
        User dummyUser = mock(User.class);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.create()).thenReturn(dummyUser);

        RoleService roleServiceMock = mock(RoleService.class);

        List<GroupRoleInterface> groups = Lists.newArrayList();
        Object id = 1;
        groups.add(new GroupRoleImpl(null,null));

        GroupRoleServiceImpl groupRoleServiceMock = mock(GroupRoleServiceImpl.class);
        when(groupRoleServiceMock.loadAll()).thenReturn(groups);

        UserOAuth userOAuth =  mock(UserOAuth.class);
        Set<String> userGroups = new HashSet<String>();
        userGroups.add("test");

        when(userOAuth.getGroups()).thenReturn(userGroups);

        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(true);

        User user = mock(User.class);

        userHelper = new UserHelper(userServiceMock, roleServiceMock, groupRoleServiceMock);
        userHelper.syncRoles(user, userOAuth);
        User savedUser = userHelper.saveUserIfNecessary(user, configMock,userOAuth);
        assertSame(user, savedUser);
    }
}