package org.coderzpy.sitemapgenerator.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@Service
public class WebCrawlerService {

    private final Set<String> visitedUrls = new HashSet<>();
    private String baseUrl;

    public Set<String> crawl(String startUrl, int maxDepth) throws Exception {

        this.baseUrl = startUrl;
        //crawlPage(startUrl);
        Queue<UrlDepthPair> queue = new LinkedList<>();
        queue.add(new UrlDepthPair(baseUrl, 0));

        while(!queue.isEmpty()) {
            UrlDepthPair current = queue.poll();
            String currentUrl = current.url;
            int currentDepth = current.depth;

            if(!visitedUrls.contains(currentUrl) && currentDepth <= maxDepth) {

                visitedUrls.add(currentUrl);
                Set<String> newLinks = fetchLinks(currentUrl);

                for (String link: newLinks) {
                    if (!visitedUrls.contains(link)) {
                        queue.add(new UrlDepthPair(link, currentDepth + 1));
                    }
                }
            }
        }
        return visitedUrls;
    }

    private Set<String> fetchLinks(String url) {
        Set<String> links = new HashSet<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements anchorTags = document.select("a[href]");

            for (Element anchor: anchorTags) {
                String href = anchor.absUrl("href");
                if (href.startsWith(baseUrl)) {
                    links.add(href);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
        }

        return links;
    }

    private static class UrlDepthPair {
        String url;
        int depth;

        public UrlDepthPair(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }

//    private void crawlPage(String url) throws IOException {
//
//        if(!visitedUrls.contains(url) && url.startsWith(baseUrl)) {
//
//            visitedUrls.add(url);
//            try {
//                Document document = Jsoup.connect(url).get();
//                Elements links = document.select("a[href]");
//                int count = 0;
//                for (Element link: links) {
//                    if(count == 10) break;
//                    String absoluteUrl = link.absUrl("href");
//                    if(absoluteUrl.startsWith(baseUrl)) {
//                        count++;
//                        crawlPage(absoluteUrl);
//                    }
//                }
//            } catch (IOException e) {
//                System.err.println("Error accessing URL: " + url + " - " + e.getMessage());
//            }
//        }
//    }
}
