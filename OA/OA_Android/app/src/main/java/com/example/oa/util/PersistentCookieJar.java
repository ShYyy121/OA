package com.example.oa.util;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public  class PersistentCookieJar implements CookieJar {
    private List<Cookie> cookies;

    public PersistentCookieJar() {
        cookies = new ArrayList<>();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookies;
    }

    public static class six {
        public static PersistentCookieJar persistentCookieJar = new PersistentCookieJar();
    }
}
