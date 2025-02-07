package org.example;
import java.util.Set;
import java.net.URI;
import java.net.URISyntaxException;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.parser.*;
import edu.uci.ics.crawler4j.url.*;

public class NewsCrawler extends WebCrawler {

        private final String domainName = "latimes";

        private FetchList fetchList = new FetchList();
        private VisitedList visitedList = new VisitedList();
        private UrlsList urlsList = new UrlsList();
        // Function fetched the domain name from the url
        private static String extractDomain(String href) throws URISyntaxException {
            // Replace commas with hyphens in the entire URL
            href = href.replace(",", "-");

            URI uri = new URI(href);
            String domain = uri.getHost();

            if (domain != null) {
                domain = domain.toLowerCase();

                // Remove "www." if present
                if (domain.startsWith("www.")) {
                    domain = domain.substring(4);
                }
                // Remove ".com" if it's at the end
                if (domain.endsWith(".com")) {
                    domain = domain.substring(0, domain.length() - 4);
                }
                return domain;
            }
            return "";
        }

        // Function checks if the url fetched is of the required type or not
        private static Boolean filterType(String contentType){
            // Remove the charset information from the content type
            contentType = getFilterType(contentType);

            // Return only if the content type is html, pdf, word, or images
            return contentType.startsWith("text/html") || contentType.startsWith("application/pdf")
                    || contentType.startsWith("application/msword") ||
                    contentType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                    contentType.startsWith("image");
        }

        private static String getFilterType(String contentType){
            if (contentType.contains("image/jpeg")){
                return "image/jpeg";
            }
            return contentType.split(";")[0].toLowerCase();
        }

        // Overridden function - check if a url can be visited
        @Override
        public boolean shouldVisit(Page referringPage, WebURL url) {
            String href = null;
            try {
                href = extractDomain(url.getURL());
                String referringPageURLDomain = extractDomain(referringPage.getWebURL().getURL());
                if(href.equals(referringPageURLDomain)){
                    urlsList.addToUrls(new Urls(url.getURL(), "OK"));
                }
                else{
                    urlsList.addToUrls(new Urls(url.getURL(), "N_OK"));
                }
            } catch (URISyntaxException e) {
                System.out.println("Issue here");
                urlsList.addToUrls(new Urls(href, "N_OK"));
                return false;
            }
//
            return href.startsWith(domainName);

        }

        @Override
        protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
            String url = webUrl.getURL();
            fetchList.addToFetchList(new Fetch(statusCode, url));
        }

        // Overridden function - retrieve the contents of the downloaded/visited page
        @Override
        public void visit(Page page) {
            String url = page.getWebURL().getURL();
//            System.out.println("Visiting: " + url);

            // Check domain
            try {
                if (!extractDomain(url).contains(domainName)) {
                    System.out.println("Incorrect domain: " + url);
                    return; // Skip processing for incorrect domains
                }
            } catch (URISyntaxException e) {
                System.out.println("Exception while extracting domain: " + url);
                return; // Skip processing if there's an exception
            }

            // Process the page if status is 200 and content type is valid
            if (page.getStatusCode() == 200 && filterType(page.getContentType())) {
                int contentLength = page.getContentData().length;
                String contentType = getFilterType(page.getContentType());

                if (page.getParseData() instanceof HtmlParseData) {
                    // Handle HTML content
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    Set<WebURL> links = htmlParseData.getOutgoingUrls();
                    visitedList.addToVisitedList(new Visit(url, contentLength, links.size(), contentType));
                } else if (page.getParseData() instanceof BinaryParseData) {
                    // Handle binary content (like images)
                    visitedList.addToVisitedList(new Visit(url, contentLength, 0, contentType));
                } else {
                    // Handle other types of content
                    System.out.println("Unhandled content type for URL: " + url + ", Type: " + contentType);
                }
            } else {
                System.out.println("Skipping URL due to status code or content type: " + url +
                        ", Status: " + page.getStatusCode() +
                        ", Content-Type: " + page.getContentType());
            }

            // Log fetch and visit counts
            if (fetchList.getSize() % 100 == 0) {
                System.out.println("Fetched: " + fetchList.getSize());
            }
            if (visitedList.getSize() % 100 == 0) {
                System.out.println("Visited: " + visitedList.getSize());
            }
        }


//        @Override
//        public void visit(Page page) {
//            String url = page.getWebURL().getURL();
//
//            // If we get a re-route, then we should not include it as a valid url
//            try {
//                if (!extractDomain(url).contains(domainName)){
//                    System.out.println("Incorrect domain: " + url);
//                }
//            } catch (URISyntaxException e) {
//                System.out.println("Exception while extracting domain: " + url);
//            }
//
//            // add to the fetched list
////            fetchList.addToFetchList(new Fetch(page.getStatusCode(), url));
//
//            // add to the visited list
//            if (page.getStatusCode() == 200 && filterType(page.getContentType())){
//                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//                Set<WebURL> links = htmlParseData.getOutgoingUrls();
//                visitedList.addToVisitedList(new Visit(url, page.getContentData().length,
//                        links.size(), getFilterType(page.getContentType())));
//            }
//
//
//            if (fetchList.getSize() % 100 == 0){
//                System.out.println("Fetched: " + fetchList.getSize());
//            }
//            if (visitedList.getSize() % 100 == 0){
//                System.out.println("Visited: " + visitedList.getSize());
//            }
//
//        }

}
