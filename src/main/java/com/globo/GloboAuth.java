package com.globo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GloboAuth {
    private static final Logger LOG = LoggerFactory.getLogger(GloboAuth.class);

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
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url + "token");

        String authorizationString =   "Basic " + Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes());

        httppost.addHeader("Authorization", authorizationString);
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));

        ObjectMapper mapper = new ObjectMapper();
        AcessToken acessToken = new AcessToken();

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            acessToken = mapper.readValue(builder.toString(), AcessToken.class);
            bufReader.close();
        } catch (IOException e) {
            LOG.error(e.toString());
        }

        return acessToken;
    }
}


