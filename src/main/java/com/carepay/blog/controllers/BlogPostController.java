package com.carepay.blog.controllers;

import com.carepay.blog.models.dtos.BlogPostDto;
import com.carepay.blog.services.BlogPostControllerDelegate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/blogs", produces = "application/json")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostControllerDelegate blogPostControllerDelegate;

    @GetMapping
    public List<BlogPostDto> findAll() {
        return blogPostControllerDelegate.findAllBlogPosts();
    }

    @GetMapping("/{username}")
    public List<BlogPostDto> findAllForUser(@PathVariable final String username) {
        return blogPostControllerDelegate.findAllBlogPostsForUser(username);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    public void postBlogPost(@RequestBody final BlogPostDto blogPostDto,
                             final Authentication authentication) {
        blogPostControllerDelegate.postBlogPost(blogPostDto, authentication);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    public void updateBlogPost(@RequestBody final BlogPostDto blogPostDto,
                               final Authentication authentication) {
        blogPostControllerDelegate.updateBlogPost(blogPostDto, authentication);
    }

    @DeleteMapping("/{blogPostUuid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    public void deleteBlogPost(@PathVariable final UUID blogPostUuid,
                               final Authentication authentication) {
        blogPostControllerDelegate.deleteBlogPost(blogPostUuid, authentication);
    }

}
