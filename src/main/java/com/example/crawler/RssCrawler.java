package com.example.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RssCrawler {

    public static class Article {
        public String title;
        public String link;
        public String pubDate;
        public String author;
        public String description;
    }

    public List<Article> fetchArticles(String rssUrl) throws IOException {
        List<Article> articles = new ArrayList<>();
        Document doc = Jsoup.connect(rssUrl).get();
        Elements items = doc.select("item");

        for (Element item : items) {
            Article article = new Article();
            article.title = getElementText(item, "title");
            article.link = getElementText(item, "link");
            article.pubDate = getElementTextOrDefault(item, "pubDate", "");
            article.author = getElementTextOrDefault(item, "author", "");
            article.description = getElementTextOrDefault(item, "description", "");
            articles.add(article);
        }

        return articles;
    }

    private String getElementText(Element parent, String tag) {
        Element element = parent.selectFirst(tag);
        if (element == null) {
            throw new IllegalStateException("Missing required tag: " + tag);
        }
        return element.text();
    }

    private String getElementTextOrDefault(Element parent, String tag, String defaultValue) {
        Element element = parent.selectFirst(tag);
        return element != null ? element.text() : defaultValue;
    }
}

