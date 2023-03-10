package com.github.dogaton.simplenewsapi.record;

import java.time.LocalDateTime;

public record Article(
        String title,
        String description,
        String content,
        String url,
        String image,
        LocalDateTime publishedAt,
        Source source
) {}