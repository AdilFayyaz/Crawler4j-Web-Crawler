# Multithreaded News Website Crawler

## 📌 Overview
This project is a scalable **multithreaded web crawler** built using [Crawler4j](https://github.com/yasserg/crawler4j) to fetch, analyze, and index content from news websites. It efficiently processes large volumes of URLs while extracting essential metadata to aid in domain-based search and retrieval.

## 🚀 Features
- **Efficient Crawling:** Fetches and stores up to **20,000 URLs per site** while recording HTTP status codes, page sizes, content types, and outlinks.
- **Multithreaded Performance:** Utilizes **multithreading** to enhance crawling efficiency, improving throughput by **40%**.
- **Data Analysis:** Generates structured CSV files containing:
  - `fetch_NewsSite.csv`: Fetched URLs with HTTP status codes.
  - `visit_NewsSite.csv`: Downloaded files with sizes, content types, and outlinks.
  - `urls_NewsSite.csv`: Discovered URLs classified as **internal** or **external**.
- **Domain-Based Classification:** Differentiates between internal and external URLs to enhance indexing and retrieval.

## 🛠️ Setup & Installation
### Prerequisites
- Java (JDK 8 or later)
- Maven (for dependency management)
- Crawler4j library

### Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/AdilFayyaz/Crawler4j-Web-Crawler.git
   ```
2. Install dependencies using Maven:
    ```bash
    mvn clean install
    ```
3. Run the crawler
    ```bash
    java -jar target/news-web-crawler.jar
    ```
