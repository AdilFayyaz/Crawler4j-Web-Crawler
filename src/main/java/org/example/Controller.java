package org.example;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.robotstxt.*;


public class Controller {

    public static void main(String[] args) throws Exception{
        final String seed1 = "https://www.latimes.com";
//        final String seed2 = "http://www.latimes.com";
        final int maxDepth = 16;
        final int maxPages = 20000;
        final int politenessDelay = 1000;
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36";
        String crawlStorageFolder = "./src/main/crawl_storage";
        int numberOfCrawlers = 7;

        FetchList fetchList = new FetchList();
        VisitedList visitedList = new VisitedList();
        UrlsList urlsList = new UrlsList();
        Stats stats = new Stats();
        String fetchFileName = "./src/main/results/fetch_latimes.csv";
        String visitedFileName = "./src/main/results/visit_latimes.csv";
        String urlsFileName = "./src/main/results/urls_latimes.csv";

        // Set configurations of the crawlers
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxPagesToFetch(maxPages);
        config.setMaxDepthOfCrawling(maxDepth);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString(userAgent);
        config.setIncludeBinaryContentInCrawling(true);

        // Instantiate the controller for this crawl

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

//        Add the seed and start the controller
        controller.addSeed(seed1);
//        controller.addSeed(seed2);
        controller.start(NewsCrawler.class, numberOfCrawlers);

        fetchList.writeToCSV(fetchFileName);
        visitedList.writeToCSV(visitedFileName);
        urlsList.writeToCSV(urlsFileName);
        stats.statistics();

    }
}