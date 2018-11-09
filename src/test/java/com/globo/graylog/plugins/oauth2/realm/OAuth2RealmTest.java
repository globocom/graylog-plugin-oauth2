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

import com.globo.graylog.plugins.oauth2.models.AcessToken;
import com.globo.graylog.plugins.oauth2.models.UserBackStage;
import com.globo.graylog.plugins.oauth2.rest.OAuth2Config;
import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.graylog2.plugin.cluster.ClusterConfigService;
import org.graylog2.plugin.database.users.User;
import org.graylog2.shared.security.HttpHeadersToken;
import org.graylog2.shared.users.UserService;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OAuth2RealmTest {

    private OAuth2Realm realm;

    @Test(expected = AuthenticationException.class)
    public void doGetAuthenticationInfoWhenReturnedUserIsNull() {
        ClusterConfigService configServiceMock = mock(ClusterConfigService.class);
        when(configServiceMock.getOrDefault(any(), any())).thenReturn(getOAuth2Config());

        OAuth2 oAuth2Mock = mock(OAuth2.class);
        String refererValue = "single referer value";
        when(oAuth2Mock.getCodeFromReferer(refererValue)).thenReturn("MockedCode");

        AcessToken dummyToken = new AcessToken();
        when(oAuth2Mock.getAuthorization(
        "MockedCode", "clientId", "clientSecret", "url server", "url redirect")
        ).thenReturn(dummyToken);

        when(oAuth2Mock.getUser("url server", dummyToken)).thenReturn(null);

        realm = new OAuth2Realm(null, configServiceMock, oAuth2Mock, null);

        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.put("referer", Collections.singletonList(refererValue));
        HttpHeadersToken token = new HttpHeadersToken(map, "myhost", "remoteAddr");

        realm.doGetAuthenticationInfo(token);
    }

    @Test
    public void doGetAuthenticationInfo() {
        OAuth2Config oAuth2Config = getOAuth2Config();
        ClusterConfigService configServiceMock = mock(ClusterConfigService.class);
        when(configServiceMock.getOrDefault(any(), any())).thenReturn(oAuth2Config);

        OAuth2 oAuth2Mock = mock(OAuth2.class);
        String refererValue = "single referer value";
        when(oAuth2Mock.getCodeFromReferer(refererValue)).thenReturn("MockedCode");

        AcessToken dummyToken = new AcessToken();
        when(oAuth2Mock.getAuthorization(
                "MockedCode", "clientId", "clientSecret", "url server", "url redirect")
        ).thenReturn(dummyToken);

        UserBackStage dummyUserPlugin = new UserBackStage();
        dummyUserPlugin.setEmail("user@email");
        when(oAuth2Mock.getUser("url server", dummyToken)).thenReturn(dummyUserPlugin);

        User dummyGraylogUser = mock(User.class);
        when(dummyGraylogUser.getName()).thenReturn("Graylog user name");
        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.load("user@email")).thenReturn(dummyGraylogUser);

        UserHelper userHelperMock = mock(UserHelper.class);
        when(userHelperMock.saveUserIfNecessary(dummyGraylogUser, oAuth2Config, dummyUserPlugin)).thenReturn(dummyGraylogUser);

        realm = new OAuth2Realm(userServiceMock, configServiceMock, oAuth2Mock, userHelperMock);

        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.put("referer", Collections.singletonList(refererValue));
        HttpHeadersToken token = new HttpHeadersToken(map, "myhost", "remoteAddr");

        AuthenticationInfo authenticationInfo = realm.doGetAuthenticationInfo(token);
        assertEquals("Graylog user name", authenticationInfo.getPrincipals().getPrimaryPrincipal());
    }

    private OAuth2Config getOAuth2Config() {
        return OAuth2Config.builder()
            .autoCreateUser(true)
            .clientId("clientId")
            .clientSecret("clientSecret")
            .name("username")
            .urlRedirect("url redirect")
            .urlBackstage("url server")
            .build();
    }


}