package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Stats {
    private int noOfFetchesAttempted;
    private int noOfFetchesSucceeded;
    private int noOfFetchesFailed;
    private int totalUrlsExtracted;
    private int noOfUniqueUrlsExtracted;
    private int noOfUniqueUrlsExtractedNewsSite;
    private int noOfUniqueUrlsExtractedNotNews;
    private final Map<Integer,Integer> statusCodes =  new HashMap<>();
    private final Map<String, Integer> contentTypes = new HashMap<>();
    private final Map<String, Integer> fileSizes = new HashMap<>();

    public void statistics() throws IOException {
//        we can compile all the statistics here from the csv files
        Fetch();
        Visit();
        Urls();
        String newsSite = "latimes";
        int noOfThreads = 7;
        String studentId = "4685210691";
        String studentName = "Muhammad Adil Fayyaz";
        writeStatisticsToFile(studentName, studentId, newsSite, noOfThreads);
    }

    public void Fetch() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader("./src/main/results/fetch_latimes.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int statusCode = Integer.parseInt(values[1]);
                noOfFetchesAttempted++;
                statusCodes.put(statusCode, statusCodes.getOrDefault(statusCode, 0) + 1);
                if (statusCode >= 200 && statusCode < 300) {
                    noOfFetchesSucceeded++;
                } else {
                    noOfFetchesFailed++;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void Visit() {
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/results/visit_latimes.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String contentType = values[3].replaceAll("\\s+", "");
                int size = Integer.parseInt(values[1]);
                contentTypes.put(contentType, contentTypes.getOrDefault(contentType, 0) + 1);
                categorizeFileSize(size);
                totalUrlsExtracted += Integer.parseInt(values[2].replaceAll("\\s+", ""));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void Urls() {
        Set<String> uniqueUrls = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./src/main/results/urls_latimes.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String url = values[0];
                String status = values[1];
                boolean added = uniqueUrls.add(url);
                if (added) {
                    if (status.equals("OK")) {
                        noOfUniqueUrlsExtractedNewsSite++;
                    } else {
                        noOfUniqueUrlsExtractedNotNews++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        noOfUniqueUrlsExtracted = uniqueUrls.size();
    }

    private void categorizeFileSize(int size) {
        if (size < 1024) {
            fileSizes.put("< 1KB", fileSizes.getOrDefault("< 1KB", 0) + 1);
        } else if (size < 10 * 1024) {
            fileSizes.put("1KB ~ <10KB", fileSizes.getOrDefault("1KB ~ <10KB", 0) + 1);
        } else if (size < 100 * 1024) {
            fileSizes.put("10KB ~ <100KB", fileSizes.getOrDefault("10KB ~ <100KB", 0) + 1);
        } else if (size < 1024 * 1024) {
            fileSizes.put("100KB ~ <1MB", fileSizes.getOrDefault("100KB ~ <1MB", 0) + 1);
        } else {
            fileSizes.put("≥ 1MB", fileSizes.getOrDefault("≥ 1MB", 0) + 1);
        }
    }

    private void writeStatisticsToFile(String name, String uscId, String newsSite, int numberOfThreads) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("./src/main/results/CrawlReport_latimes.txt"))) {
            writer.println("Name: " + name);
            writer.println("USC ID: " + uscId);
            writer.println("News site crawled: " + newsSite);
            writer.println("Number of threads: " + numberOfThreads);

            writer.println("\nFetch Statistics");
            writer.println("====================");
            writer.println("# fetches attempted: " + noOfFetchesAttempted);
            writer.println("# fetches succeeded: " + noOfFetchesSucceeded);
            writer.println("# fetches failed or aborted: " + noOfFetchesFailed);

            writer.println("\nOutgoing URLs:");
            writer.println("====================");
            writer.println("Total URLs extracted: " + totalUrlsExtracted);
            writer.println("# unique URLs extracted: " + noOfUniqueUrlsExtracted);
            writer.println("# unique URLs within News Site: " + noOfUniqueUrlsExtractedNewsSite);
            writer.println("# unique URLs outside News Site: " + noOfUniqueUrlsExtractedNotNews);

            writer.println("\nStatus Codes:");
            writer.println("====================");
            for (Map.Entry<Integer, Integer> entry : statusCodes.entrySet()) {
                writer.println(entry.getKey() + " " + getStatusCodeDescription(entry.getKey()) + ": " + entry.getValue());
            }

            writer.println("\nFile Sizes:");
            writer.println("====================");
            writer.println("< 1KB: " + fileSizes.getOrDefault("< 1KB", 0));
            writer.println("1KB ~ <10KB: " + fileSizes.getOrDefault("1KB ~ <10KB", 0));
            writer.println("10KB ~ <100KB: " + fileSizes.getOrDefault("10KB ~ <100KB", 0));
            writer.println("100KB ~ <1MB: " + fileSizes.getOrDefault("100KB ~ <1MB", 0));
            writer.println("≥ 1MB: " + fileSizes.getOrDefault("≥ 1MB", 0));

            writer.println("\nContent Types:");
            writer.println("====================");
            for (Map.Entry<String, Integer> entry : contentTypes.entrySet()) {
                String contentType = entry.getKey();
                int count = entry.getValue();
                writer.println(contentType + ": " + count);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getStatusCodeDescription(int statusCode) {
        return switch (statusCode) {
            // 1xx Informational
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 102 -> "Processing";
            case 103 -> "Early Hints";

            // 2xx Success
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 203 -> "Non-Authoritative Information";
            case 204 -> "No Content";
            case 205 -> "Reset Content";
            case 206 -> "Partial Content";
            case 207 -> "Multi-Status";
            case 208 -> "Already Reported";
            case 226 -> "IM Used";

            // 3xx Redirection
            case 300 -> "Multiple Choices";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 303 -> "See Other";
            case 304 -> "Not Modified";
            case 305 -> "Use Proxy";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";

            // 4xx Client Errors
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Payment Required";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Payload Too Large";
            case 414 -> "URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 418 -> "I'm a teapot";
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Entity";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 425 -> "Too Early";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";

            // 5xx Server Errors
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";

            default -> "Unknown Status Code";
        };
    }

    public static void main(String[] args) throws Exception{
        Stats stats = new Stats();
        stats.statistics();
    }
}




