package com.globo.graylog.plugins.oauth2.realm;

import com.globo.graylog.plugins.oauth2.models.AcessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.shiro.authc.AuthenticationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.UnknownServiceException;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class OAuth2Test {

    private OAuth2 oAuth2;

    @Before
    public void setUp() throws Exception {
        oAuth2 = new OAuth2();
    }

    @After
    public void tearDown() throws Exception {
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
        entity.setContent(new StringBufferInputStream(getResponseToken()));
        response.setEntity(entity);

        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpPost.class))).thenReturn(response);
        oAuth2.httpclient = httpClientMock;

        AcessToken authorization = oAuth2.getAuthorization(
                "0b84f019d22072199d26628bad7f51f7", "55aabbeeff",
                "secretclient", "http://server.url/",
                "http://redirect.url:8888/",
                "authorization_code"
        );

        assertEquals("a_token_test", authorization.getAcessToken());
    }

    @Test(expected = AuthenticationException.class)
    public void getAuthorizationNotOk() throws Exception {
        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.execute(any(HttpPost.class))).thenThrow(UnknownServiceException.class);
        oAuth2.httpclient = httpClientMock;

        AcessToken authorization = oAuth2.getAuthorization(
                "0b84f019d22072199d26628bad7f51f7", "55aabbeeff",
                "secretclient", "http://server.url/",
                "http://redirect.url:8888/",
                "authorization_code"
        );
    }

    @Test
    public void getUser() {
    }

    public String getResponseToken() {
        return "{\"access_token\": \"a_token_test\", \"token_type\": \"code\", \"expires_in\": 1122334455, \"refresh_token\": \"r_token_test\"}";
    }

}