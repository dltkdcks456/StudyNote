package http;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    HttpCookie(String cookieValue) {
        cookies = HttpRequestUtils.parseQueryString(cookieValue);
    }

    public String getCookies(String name) {
        return cookies.get(name);
    }
}
