package com.carepay.blog.services;

import com.carepay.blog.errors.ResourceNotFoundException;
import com.carepay.blog.errors.UnauthorizedAccessException;
import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.Role;
import com.carepay.blog.models.User;
import com.carepay.blog.models.dtos.BlogPostDto;
import com.carepay.blog.models.mappers.BlogPostMapper;
import com.carepay.blog.repositories.BlogPostRepository;
import com.carepay.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogPostControllerDelegate {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    public List<BlogPostDto> findAllBlogPosts() {
        final List<BlogPost> blogPosts = blogPostRepository.findAllByOrderByCreationTimestampDesc();
        return BlogPostMapper.toDtos(blogPosts);
    }

    public List<BlogPostDto> findAllBlogPostsForUser(final String username) {
        final Optional<User> userOptional = userRepository.findByUsername(username);
        final User user = userOptional.orElseThrow(ResourceNotFoundException::new);

        final List<BlogPost> blogPosts = blogPostRepository.findAllByUserOrderByCreationTimestampDesc(user);
        return BlogPostMapper.toDtos(blogPosts);
    }

    public void postBlogPost(final BlogPostDto blogPostDto, final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        final BlogPost blogPost = BlogPostMapper.fromDto(blogPostDto, user);

        blogPostRepository.save(blogPost);
    }

    public void updateBlogPost(final BlogPostDto blogPostDto, final Authentication authentication) {
        authenticateMatchingUserOrAdmin(blogPostDto.getUsername(), authentication);

        final Optional<BlogPost> blogPostOptional = blogPostRepository.findByUuid(blogPostDto.getUuid());
        final BlogPost blogPost = blogPostOptional.orElseThrow(ResourceNotFoundException::new);

        final BlogPost merged = BlogPostMapper.merge(blogPostDto, blogPost);
        blogPostRepository.save(merged);
    }

    public void deleteBlogPost(final UUID blogPostUuid, final Authentication authentication) {
        final Optional<BlogPost> blogPostOptional = blogPostRepository.findByUuid(blogPostUuid);
        final BlogPost blogPost = blogPostOptional.orElseThrow(ResourceNotFoundException::new);

        authenticateMatchingUserOrAdmin(blogPost.getUser().getUsername(), authentication);

        blogPostRepository.delete(blogPost);
    }

    private void authenticateMatchingUserOrAdmin(final String usernameToMatch, final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        if (isMatchingUser(usernameToMatch, user.getUsername()) || hasRole(user, Role.ROLE_ADMIN)) {
            return;
        }
        throw new UnauthorizedAccessException();
    }

    private boolean isMatchingUser(final String usernameToMatch, final String username) {
        return usernameToMatch.equals(username);
    }

    private boolean hasRole(User user, Role roleToCheck) {
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(roleToCheck.toString()));
    }

}
