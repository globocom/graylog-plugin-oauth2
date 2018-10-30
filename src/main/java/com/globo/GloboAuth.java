package com.globo;

public class GloboAuth {

    private String code;

    public String getCodeFromReferer(String referer) {
        code = referer.split("code")[1];

        return code;
    }
}
