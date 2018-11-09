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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globo.graylog.plugins.oauth2.models.AcessToken;
import com.globo.graylog.plugins.oauth2.models.UserBackStage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class OAuth2 {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2.class);
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient httpclient = HttpClients.createDefault();

    public String getCodeFromReferer(String referer) throws AuthenticationException {
        String code;

        try {
            code = referer.split("code=")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error(e.toString());
            throw new AuthenticationException("Bad formated code token");
        } catch (NullPointerException e) {
            LOG.error(e.toString());
            throw new AuthenticationException("The code token should not be null");
        }

        return code;
    }

    public AcessToken getAuthorization(
        String code, String clientId, String clientSecret, String url, String redirectUrl
    ) {
        HttpPost httpPost = new HttpPost(url + "token");
        HttpResponse response = null;

        httpPost.setHeader("Authorization", getAuthorizationString(clientId, clientSecret));

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("redirect_uri", redirectUrl));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        try {
            params.add(new BasicNameValuePair("code",  URLDecoder.decode(code, "UTF-8")));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = httpclient.execute(httpPost);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String content = buffer.lines().collect(Collectors.joining("\n"));
            return mapper.readValue(content, AcessToken.class);
        } catch (IOException e) {
            LOG.error(e.toString());
            throw  new AuthenticationException("Something went wrong when fetching the OAuth url API: " + url);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public UserBackStage getUser(String url, AcessToken acessToken) {
        HttpGet httpGet = new  HttpGet(url + "user");

        httpGet.setHeader("Authorization", "Bearer " + acessToken.getAcessToken());
        HttpResponse response = null;
        String content = null;
        try {
            response = httpclient.execute(httpGet);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            content = buffer.lines().collect(Collectors.joining("\n"));
            return mapper.readValue(content, UserBackStage.class);
        } catch (JsonParseException e) {
            LOG.error(e.toString());
            throw  new AuthenticationException("Wrong json format: " + content);
        } catch (IOException e) {
            LOG.error(e.toString());
            throw  new AuthenticationException("Something went wrong when fetching the User url API: " + url);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    private String getAuthorizationString(String clientId, String clientSecret) {
        return  "Basic " + Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes());
    }
}


