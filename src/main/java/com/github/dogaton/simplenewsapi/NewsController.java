package com.github.dogaton.simplenewsapi;

import com.github.dogaton.simplenewsapi.record.Article;
import com.github.dogaton.simplenewsapi.record.News;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final WebClient webClient = WebClient.builder()
            .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
            .build();
    @Value("${gnews.api.api-version}")
    private String apiVersion;
    @Value("${gnews.api.host}")
    private String host;
    @Value("${gnews.api.scheme}")
    private String httpsScheme;
    @Value("${gnews.api.key}")
    private String gNewsApiKey;

    private static final String TOP_HEADLINES = "/top-headlines";
    private static final String SEARCH = "/search";

    @GetMapping("/articles")
    @ResponseBody
    public Flux<Article> getNewsArticles(@RequestParam(defaultValue = "10") int count) {
        return webClient.get()
                .uri(UriComponentsBuilder.newInstance()
                        .scheme(httpsScheme)
                        .host(host)
                        .path(apiVersion)
                        .path(TOP_HEADLINES)
                        .queryParam("max", count)
                        .queryParam("token", gNewsApiKey)
                        .build()
                        .toUri())
                .retrieve()
                .bodyToFlux(News.class)
                .flatMapIterable(News::articles);
    }

    @GetMapping("/articles/search")
    @ResponseBody
    public Flux<Article> getNewsArticlesByTitleOrAuthor(@RequestParam String title) {
        return webClient.get()
                .uri(UriComponentsBuilder.newInstance()
                        .scheme(httpsScheme)
                        .host(host)
                        .path(apiVersion)
                        .path(SEARCH)
                        .queryParam("q", title)
                        .queryParam("in", "title")
                        .queryParam("token", gNewsApiKey)
                        .build()
                        .toUri())
                .retrieve()
                .bodyToFlux(News.class)
                .flatMapIterable(News::articles);
    }
}
