package org.example;

public class Urls {
    private final String url;
    private final String isOk;

    public Urls(String url, String isOk) {
        this.url = url;
        this.isOk = isOk;
    }

    public String getUrl() {
        return url;
    }
    public String getIsOk() {
        return isOk;
    }

    @Override
    public String toString() {
        return "URL{" +
                "url='" + url + '\'' +
                ", isOk=" + isOk +
                '}';
    }

}
