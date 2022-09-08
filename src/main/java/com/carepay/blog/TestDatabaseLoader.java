package com.carepay.blog;

import com.carepay.blog.models.Authority;
import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.Role;
import com.carepay.blog.models.User;
import com.carepay.blog.repositories.AuthorityRepository;
import com.carepay.blog.repositories.BlogPostRepository;
import com.carepay.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestDatabaseLoader {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            loadAuthorities();
            loadUsers();
            loadBlogPosts();
        };
    }

    private void loadAuthorities() {
        final Authority admin = new Authority();
        admin.setAuthority(Role.ROLE_ADMIN);
        authorityRepository.save(admin);

        final Authority poster = new Authority();
        poster.setAuthority(Role.ROLE_POSTER);
        authorityRepository.save(poster);
    }

    private void loadUsers() {
        final User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setAuthorities(List.of(authorityRepository.findDistinctByAuthority(Role.ROLE_ADMIN)));
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        userRepository.save(admin);

        final User poster = new User();
        poster.setUsername("poster");
        poster.setPassword(passwordEncoder.encode("poster"));
        poster.setAuthorities(List.of(authorityRepository.findDistinctByAuthority(Role.ROLE_POSTER)));
        poster.setAccountNonExpired(true);
        poster.setAccountNonLocked(true);
        poster.setCredentialsNonExpired(true);
        poster.setEnabled(true);
        userRepository.save(poster);
    }

    private void loadBlogPosts() {
        final BlogPost adminBlogPost = new BlogPost();
        adminBlogPost.setUser(userRepository.findByUsername("admin").get());
        adminBlogPost.setTitle("Admin Title");
        adminBlogPost.setSubtitle("Admin Subtitle");
        adminBlogPost.setContent("Admin Content");
        adminBlogPost.setCreationTimestamp(LocalDateTime.now());
        blogPostRepository.save(adminBlogPost);

        final BlogPost posterBlogPost = new BlogPost();
        posterBlogPost.setUser(userRepository.findByUsername("poster").get());
        posterBlogPost.setTitle("Poster Title");
        posterBlogPost.setSubtitle("Poster Subtitle");
        posterBlogPost.setContent("Poster Content");
        posterBlogPost.setCreationTimestamp(LocalDateTime.now());
        blogPostRepository.save(posterBlogPost);
    }


}
