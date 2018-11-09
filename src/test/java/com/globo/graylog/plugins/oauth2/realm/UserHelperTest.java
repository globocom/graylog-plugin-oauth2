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
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class UserHelperTest {

    private UserHelper userHelper;

    @Test
    public void whenUserIsNotNullPreserveTheOriginalUser() {
        userHelper = new UserHelper(null, null);
        User mockedUser = mock(User.class);
        User user = userHelper.saveUserIfNecessary(mockedUser, mock(OAuth2Config.class), mock(UserBackStage.class));

        assertSame(user, mockedUser);
    }

    @Test(expected = AuthenticationException.class)
    public void whenUserIsNullAndAutoCreateIsNotEnabled() {
        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(false);
        userHelper = new UserHelper(null, null);
        userHelper.saveUserIfNecessary(null, configMock, mock(UserBackStage.class));
    }

    @Test(expected = AuthenticationException.class)
    public void whenCouldNotSaveTheUser() throws ValidationException {
        User dummyUser = mock(User.class);
        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.create()).thenReturn(dummyUser);
        when(userServiceMock.save(any())).thenThrow(new ValidationException("Invalid user"));

        RoleService roleServiceMock = mock(RoleService.class);

        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(true);
        userHelper = new UserHelper(userServiceMock, roleServiceMock);
        userHelper.saveUserIfNecessary(null, configMock, mock(UserBackStage.class));
    }

    @Test
    public void whenEverythingIsOk() {
        User dummyUser = mock(User.class);
        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.create()).thenReturn(dummyUser);

        RoleService roleServiceMock = mock(RoleService.class);

        OAuth2Config configMock = mock(OAuth2Config.class);
        when(configMock.autoCreateUser()).thenReturn(true);
        userHelper = new UserHelper(userServiceMock, roleServiceMock);
        User savedUser = userHelper.saveUserIfNecessary(null, configMock, mock(UserBackStage.class));

        assertSame(dummyUser, savedUser);
    }
}