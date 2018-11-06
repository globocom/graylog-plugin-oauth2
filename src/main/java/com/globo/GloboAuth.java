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
import java.net.URLDecoder;
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

        httppost.setHeader("Authorization", authorizationString);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/"));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        ObjectMapper mapper = new ObjectMapper();
        AcessToken acessToken = new AcessToken();

        try {
            params.add(new BasicNameValuePair("code",  URLDecoder.decode(code, "UTF-8")));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }

            acessToken = mapper.readValue(builder.toString(), AcessToken.class);
            System.out.println(acessToken);
            bufReader.close();
        } catch (IOException e) {
            LOG.error(e.toString());
        }

        return acessToken;
    }
}


