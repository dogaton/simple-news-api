package com.github.dogaton.simplenewsapi;

import com.github.dogaton.simplenewsapi.record.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenNewsCountIsCalledWithFive_ThenReturnFiveArticles() {
        int count = 5;
        webTestClient.get().uri("/news/articles?count={count}", count)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(count)
                .consumeWith(response -> {
                    List<Article> articles = response.getResponseBody();
                    assertNotNull(articles);
                    assertEquals(count, articles.size());
                    for (Article article : articles) {
                        assertNotNull(article.title());
                        assertNotNull(article.description());
                        assertNotNull(article.url());
                    }
                });
    }

    @Test
    void whenNewsCountIsCalledWithNoRequest_ThenReturnDefaultValueTenArticles() {
        int size = 10;
        webTestClient.get().uri("/news/articles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(size)
                .consumeWith(response -> {
                    List<Article> articles = response.getResponseBody();
                    assertNotNull(articles);
                    assertEquals(size, articles.size());
                    for (Article article : articles) {
                        assertNotNull(article.title());
                        assertNotNull(article.description());
                        assertNotNull(article.url());
                    }
                });
    }

}