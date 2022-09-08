package com.carepay.blog.models.mappers;

import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.User;
import com.carepay.blog.models.dtos.BlogPostDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlogPostMapper {

    public static List<BlogPostDto> toDtos(final List<BlogPost> blogPosts) {
        final List<BlogPostDto> dtos = new ArrayList<>();
        for (BlogPost blogPost : blogPosts) {
            dtos.add(BlogPostMapper.toDto(blogPost));
        }
        return dtos;
    }

    public static BlogPostDto toDto(final BlogPost blogPost) {
        return BlogPostDto.builder()
                .uuid(blogPost.getUuid())
                .username(blogPost.getUser().getUsername())
                .title(blogPost.getTitle())
                .subtitle(blogPost.getSubtitle())
                .content(blogPost.getContent())
                .creationTimestamp(blogPost.getCreationTimestamp())
                .lastEditedTimestamp(blogPost.getLastEditedTimestamp())
                .build();
    }

    public static BlogPost fromDto(final BlogPostDto blogPostDto, final User user) {
        final BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setSubtitle(blogPostDto.getSubtitle());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setCreationTimestamp(getOrCreateCreationTimestamp(blogPostDto));
        return blogPost;
    }

    public static BlogPost merge(final BlogPostDto blogPostDto, final BlogPost blogPost) {
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setSubtitle(blogPostDto.getSubtitle());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setLastEditedTimestamp(LocalDateTime.now());
        return blogPost;
    }

    private static LocalDateTime getOrCreateCreationTimestamp(final BlogPostDto blogPostDto) {
        final LocalDateTime creationTimestamp = blogPostDto.getCreationTimestamp();
        if (creationTimestamp == null) {
            return LocalDateTime.now();
        }
        return creationTimestamp;
    }

}
