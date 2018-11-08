package com.globo.graylog.plugins.oauth2.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    public void getAuthorization() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void getAuthorizationString() {
    }
}