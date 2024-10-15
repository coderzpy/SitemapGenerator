package org.coderzpy.sitemapgenerator.controller;

import org.coderzpy.sitemapgenerator.service.SitemapGeneratorService;
import org.coderzpy.sitemapgenerator.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@lombok.extern.slf4j.Slf4j
public class SitemapController {
    
    @Autowired
    private WebCrawlerService webCrawlerService;
    @Autowired
    private SitemapGeneratorService sitemapGeneratorService;

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public ResponseEntity<String> generatedSitemap(@RequestParam String url) {
        try {
            Set<String> crawledUrls = webCrawlerService.crawl(url);
            Map<String, String[]> urlDetails = new HashMap<>();

            String lastmod = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // Use current time for demo

            for (String crawledUrl : crawledUrls) {
                String[] details = {lastmod, "0.5"}; // Static priority, you can modify based on logic
                urlDetails.put(crawledUrl, details);
            }

            String sitemap = sitemapGeneratorService.generateSiteMap(urlDetails);
            return ResponseEntity.ok(sitemap);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(500).body("<error>Error generating sitemap</error>");
        }
    }
}
