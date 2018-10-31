package com.globo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GloboAuth {
    private static final Logger LOG = LoggerFactory.getLogger(GloboAuth.class);

    private String code;

    public String getCodeFromReferer(String referer) {
        code = referer.split("code=")[1];

        return code;
    }

    public void getAuthorizationCode(String code, String clientId, String clientSecret, String url) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        String authorizationString =   "Basic " + Base64.getEncoder().encodeToString(
                (clientId + clientSecret).getBytes());

        httppost.setHeader("Authorization", authorizationString);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", code));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

        } catch (IOException e) {
            LOG.error(e.toString());
        }

    }
}
