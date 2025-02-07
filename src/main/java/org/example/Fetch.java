package org.example;

public class Fetch {
    private final int httpCode;
    private final String url;

    public Fetch(int httpCode, String url) {
        this.httpCode = httpCode;
        this.url = url;
    }

    public int getHttpCode() {
        return httpCode;
    }
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Fetch{" +
                "url='" + url + '\'' +
                ", statusCode=" + httpCode +
                '}';
    }

}
