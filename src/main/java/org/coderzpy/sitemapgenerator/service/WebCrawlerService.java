package org.coderzpy.sitemapgenerator.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class WebCrawlerService {

    private Set<String> visitedUrls = new HashSet<>();
    private String baseUrl;

    public Set<String> crawl(String startUrl) throws Exception {

        this.baseUrl = startUrl;
        crawlPage(startUrl);
        return visitedUrls;
    }

    private void crawlPage(String url) throws IOException {

        if(!visitedUrls.contains(url) && url.startsWith(baseUrl)) {

            visitedUrls.add(url);
            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                int count = 0;
                for (Element link: links) {
                    if(count == 10) break;
                    String absoluteUrl = link.absUrl("href");
                    if(absoluteUrl.startsWith(baseUrl)) {
                        count++;
                        crawlPage(absoluteUrl);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error accessing URL: " + url + " - " + e.getMessage());
            }
        }
    }
}
