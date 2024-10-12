package org.coderzpy.sitemapgenerator.service;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;
import java.util.Set;

@Service
public class SitemapGeneratorService {

    public String generateSiteMap(Map<String, String[]> urls) {

        StringBuilder sitemap = new StringBuilder();
        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        for (Map.Entry<String, String[]> entry : urls.entrySet()) {
            String url = entry.getKey();
            String lastmod = entry.getValue()[0];
            String priority = entry.getValue()[1];

            sitemap.append("  <url>\n");
            sitemap.append("    <loc>").append(url).append("</loc>\n");
            sitemap.append("    <lastmod>").append(lastmod).append("</lastmod>\n");
            sitemap.append("    <priority>").append(priority).append("</priority>\n");
            sitemap.append("  </url>\n");
        }

        sitemap.append("</urlset>\n");
        return sitemap.toString();
    }


}
