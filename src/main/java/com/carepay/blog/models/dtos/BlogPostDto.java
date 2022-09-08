package com.carepay.blog.models.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BlogPostDto {

    private UUID uuid;

    /** Username of the blog post author **/
    private String username;

    private String title;
    private String subtitle;
    private String content;

    private LocalDateTime creationTimestamp;
    private LocalDateTime lastEditedTimestamp;

}
