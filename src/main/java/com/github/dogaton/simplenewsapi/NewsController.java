package com.github.dogaton.simplenewsapi;

import com.github.dogaton.simplenewsapi.record.Article;
import com.github.dogaton.simplenewsapi.record.News;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private static final String TOKEN = "token";

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
                        .queryParam(TOKEN, gNewsApiKey)
                        .build()
                        .toUri())
                .retrieve()
                .bodyToFlux(News.class)
                .flatMapIterable(News::articles);
    }

    @GetMapping("/articles/search")
    @ResponseBody
    public Mono<ResponseEntity<Flux<Article>>> getNewsArticlesByTitle(
            @RequestParam String keyword,
            @RequestParam(required = false) String title) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(httpsScheme)
                .host(host)
                .path(apiVersion)
                .path(SEARCH);

        if (title != null) {
            uriBuilder.queryParam("q", title);
            uriBuilder.queryParam("in", title);
        }

        return webClient.get()
                .uri(uriBuilder
                        .queryParam("q", keyword)
                        .queryParam(TOKEN, gNewsApiKey)
                        .build()
                        .toUri())
                .retrieve()
                .bodyToFlux(News.class)
                .flatMapIterable(News::articles)
                .collectList()
                .flatMap(articles -> {
                    if (articles.isEmpty()) {
                        return Mono.just(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.ok().body(Flux.fromIterable(articles)));
                    }
                });
    }
}
