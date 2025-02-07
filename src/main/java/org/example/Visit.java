package org.example;

public class Visit {
    private final String url;
    private final int size;
    private final int noOfOutlinks;
    private final String contentType;

    public Visit(String url, int size, int noOfOutlinks, String contentType) {
        this.url = url;
        this.size = size;
        this.noOfOutlinks = noOfOutlinks;
        this.contentType = contentType;
    }
    public String getUrl() {
        return url;
    }
    public int getSize() {
        return size;
    }
    public int getNoOfOutlinks() {
        return noOfOutlinks;
    }
    public String getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "url='" + url + '\'' +
                ", size=" + size +
                ", noOfOutlinks=" + noOfOutlinks +
                ", contentType='" + contentType +
                '}';
    }
}
