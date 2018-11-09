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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.UnknownServiceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class OAuth2Test {

    private OAuth2 oAuth2;

    @Before
    public void setUp() {
        oAuth2 = new OAuth2();
    }

    @Test(expected = AuthenticationException.class)
    public void getCodeFromRefererWhenTheStringDoesNotHaveTheCorrectFormat() {
        oAuth2.getCodeFromReferer("code-0b84f019d22072199d26628bad7f51f7");
    }

    @Test(expected = AuthenticationException.class)
    public void getCodeFromRefererWhenTheCodeTokenIsNull() {
        oAuth2.getCodeFromReferer(null);
    }

    @Test
    public void getCodeFromRefererWhenTheCodeIsOK() {
        String result = oAuth2.getCodeFromReferer("code=0b84f019d22072199d26628bad7f51f7");
        assertEquals("0b84f019d22072199d26628bad7f51f7", result);
    }

    @Test
    public void getAuthorization() throws Exception {
        BasicHttpResponse response = new BasicHttpResponse(null, 200, "OK");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new StringBufferInputStream(getResponseTokenString()));
        response.setEntity(entity);

        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpPost.class))).thenReturn(response);
        oAuth2.setHttpclient(httpClientMock);

        AcessToken authorization = oAuth2.getAuthorization(
                "0b84f019d22072199d26628bad7f51f7", "55aabbeeff",
                "secretclient", "http://server.url/",
                "http://redirect.url:8888/"
        );

        assertEquals("a_token_test", authorization.getAcessToken());
    }

    @Test(expected = AuthenticationException.class)
    public void getAuthorizationNotOk() throws IOException {
        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpPost.class))).thenThrow(UnknownServiceException.class);
        oAuth2.setHttpclient(httpClientMock);

        oAuth2.getAuthorization(
                "0b84f019d22072199d26628bad7f51f7", "55aabbeeff",
                "secretclient", "http://server.url/",
                "http://redirect.url:8888/"
        );
    }

    @Test(expected = AuthenticationException.class)
    public void getUserBadFormatedJson() throws IOException {
        BasicHttpResponse response = new BasicHttpResponse(null, 200, "OK");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new StringBufferInputStream(getBadFormatedResponseUserString()));
        response.setEntity(entity);

        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpGet.class))).thenReturn(response);
        oAuth2.setHttpclient(httpClientMock);

        oAuth2.getUser(
                "http://server.url/user/",
                getAccessToken()
        );
    }

    @Test(expected = AuthenticationException.class)
    public void getUserRequestProblem() throws IOException {
        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpGet.class))).thenThrow(new UnknownServiceException("Unknow Service"));
        oAuth2.setHttpclient(httpClientMock);

        oAuth2.getUser(
                "http://server.url/user/",
                getAccessToken()
        );
    }

    @Test
    public void getUserOK() throws IOException {
        BasicHttpResponse response = new BasicHttpResponse(null, 200, "OK");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new StringBufferInputStream(getResponseUserString()));
        response.setEntity(entity);

        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpGet.class))).thenReturn(response);
        oAuth2.setHttpclient(httpClientMock);

        UserBackStage user = oAuth2.getUser(
                "http://server.url/user/",
                getAccessToken()
        );

        assertEquals("gcom.globo.com", user.getUserName());
        assertEquals(3, user.getGroups().size());
        assertTrue(user.getGroups().contains("admin"));
    }

    private AcessToken getAccessToken() {
        AcessToken accessToken = new AcessToken();
        accessToken.setAcessToken("a_token_test");
        accessToken.setExpiresIn(1122334455);
        accessToken.setRefreshToken("r_token_test");
        accessToken.setTokenType("code");
        return accessToken;
    }

    private String getResponseTokenString() {
        return "{\"access_token\": \"a_token_test\", \"token_type\": \"code\", \"expires_in\": 1122334455, \"refresh_token\": \"r_token_test\"}";
    }

    private String getResponseUserString() {
        return "{\"name\": \"Gcom\", \"username\": \"gcom.globo.com\", \"email\": \"gcom.globo.com\", \"groups\": [\"admin\", \"user\", \"audit\"]}";
    }

    private String getBadFormatedResponseUserString() {
        return "{\"name\",,,,,: \"Gcom\", \"username\": \"gcom.globo.com\", \"email\": \"gcom.globo.com\", \"groups\": [\"admin\", \"user\", \"audit\"]}";
    }

}