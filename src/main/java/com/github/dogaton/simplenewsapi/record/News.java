package com.github.dogaton.simplenewsapi.record;

import java.util.List;

public record News(
        int totalArticles,
        List<Article> articles
) {}