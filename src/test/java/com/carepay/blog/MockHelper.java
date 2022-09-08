package com.carepay.blog;

import com.carepay.blog.models.Authority;
import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.Role;
import com.carepay.blog.models.User;

import java.time.LocalDateTime;
import java.util.List;

public class MockHelper {

    public static Authority createAdminAuthority() {
        final Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_ADMIN);
        return authority;
    }

    public static Authority createPosterAuthority() {
        final Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_POSTER);
        return authority;
    }

    public static User createAdminUser() {
        final User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setAuthorities(List.of(createAdminAuthority()));
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        return admin;
    }

    public static User createPosterUser() {
        final User poster = new User();
        poster.setUsername("poster");
        poster.setPassword("poster");
        poster.setAuthorities(List.of(createPosterAuthority()));
        poster.setAccountNonExpired(true);
        poster.setAccountNonLocked(true);
        poster.setCredentialsNonExpired(true);
        poster.setEnabled(true);
        return poster;
    }

    public static List<BlogPost> createAllBlogPosts() {
        return List.of(createAdminBlogPost(), createPosterBlogPost());
    }

    public static BlogPost createAdminBlogPost() {
        final BlogPost adminBlogPost = new BlogPost();
        adminBlogPost.setUser(createAdminUser());
        adminBlogPost.setTitle("Admin Title");
        adminBlogPost.setSubtitle("Admin Subtitle");
        adminBlogPost.setContent("Admin Content");
        adminBlogPost.setCreationTimestamp(LocalDateTime.now());
        return adminBlogPost;
    }

    public static BlogPost createPosterBlogPost() {
        final BlogPost posterBlogPost = new BlogPost();
        posterBlogPost.setUser(createPosterUser());
        posterBlogPost.setTitle("Poster Title");
        posterBlogPost.setSubtitle("Poster Subtitle");
        posterBlogPost.setContent("Poster Content");
        posterBlogPost.setCreationTimestamp(LocalDateTime.now());
        return posterBlogPost;
    }
}
