package com.globo.graylog.plugins.oauth2.realm;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class OAuth2 {
    private static final Logger LOG = LoggerFactory.getLogger(OAuth2.class);
    ObjectMapper mapper = new ObjectMapper();
    AcessToken acessToken = new AcessToken();
    UserBackStage user = new  UserBackStage();
    HttpClient httpclient = HttpClients.createDefault();

    public String getCodeFromReferer(String referer) {
        String code = new String();

        try {
            code = referer.split("code=")[1];
        } catch (StringIndexOutOfBoundsException e) {
            LOG.error(e.toString());
        }

        return code;
    }

    public AcessToken getAuthorization(String code, String clientId, String clientSecret, String url) {
        HttpPost httpPost = new HttpPost(url + "token");
        HttpResponse response = null;

        httpPost.setHeader("Authorization", getAuthorizationString(clientId, clientSecret));

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/"));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        try {
            params.add(new BasicNameValuePair("code",  URLDecoder.decode(code, "UTF-8")));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = httpclient.execute(httpPost);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }

            acessToken = mapper.readValue(builder.toString(), AcessToken.class);
            bufReader.close();
        } catch (IOException e) {
            LOG.error(e.toString());
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        return acessToken;
    }

    public UserBackStage getUser(String url, AcessToken acessToken) {
        HttpGet httpGet = new  HttpGet(url + "user");
        HttpResponse response = null;

        httpGet.setHeader("Authorization", "Bearer " + acessToken.getAcessToken());

        try {
            response = httpclient.execute(httpGet);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }

            user = mapper.readValue(builder.toString(),  UserBackStage.class);
            bufReader.close();
        } catch (IOException e) {
            LOG.error(e.toString());
        } finally {
            HttpClientUtils.closeQuietly(response);
        }


        return user;
    }

    public String getAuthorizationString(String clientId, String clientSecret) {
        return  "Basic " + Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes());
    }
}


