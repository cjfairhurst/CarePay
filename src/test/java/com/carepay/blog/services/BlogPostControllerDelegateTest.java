package com.carepay.blog.services;

import com.carepay.blog.MockHelper;
import com.carepay.blog.errors.ResourceNotFoundException;
import com.carepay.blog.errors.UnauthorizedAccessException;
import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.User;
import com.carepay.blog.models.dtos.BlogPostDto;
import com.carepay.blog.repositories.BlogPostRepository;
import com.carepay.blog.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BlogPostControllerDelegateTest {

    @Mock
    private BlogPostRepository blogPostRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BlogPostControllerDelegate sut;

    @Test
    public void whenFindAllBlogPosts_thenAllBlogPostDtosAreReturned() {
        Mockito.when(blogPostRepository.findAllByOrderByCreationTimestampDesc())
                .thenReturn(MockHelper.createAllBlogPosts());

        final List<BlogPostDto> allBlogPosts = sut.findAllBlogPosts();

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findAllByOrderByCreationTimestampDesc();

        assertThat(allBlogPosts.size(), equalTo(2));
        assertThat(allBlogPosts.get(0).getUsername(), equalTo("admin"));
        assertThat(allBlogPosts.get(1).getUsername(), equalTo("poster"));
    }

    @Test
    public void whenFindAllBlogPostsByUser_thenAllBlogPostDtosByUserAreReturned() {
        Mockito.when(userRepository.findByUsername(Mockito.eq("admin")))
                .thenReturn(Optional.of(MockHelper.createAdminUser()));
        Mockito.when(blogPostRepository.findAllByUserOrderByCreationTimestampDesc(Mockito.any(User.class)))
                .thenReturn(List.of(MockHelper.createAdminBlogPost()));

        final List<BlogPostDto> allBlogPosts = sut.findAllBlogPostsForUser("admin");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(Mockito.eq("admin"));
        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findAllByUserOrderByCreationTimestampDesc(Mockito.any(User.class));

        assertThat(allBlogPosts.size(), equalTo(1));
        assertThat(allBlogPosts.get(0).getUsername(), equalTo("admin"));
    }

    @Test
    public void givenUserDoesNotExist_whenFindAllBlogPostsByUser_thenResourceNotFound() {
        Mockito.when(userRepository.findByUsername(Mockito.eq("admin")))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.findAllBlogPostsForUser("admin");
        });

        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(Mockito.eq("admin"));
    }

    @Test
    public void givenAdminUser_whenPostBlogPost_thenBlogPostIsPosted() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createAdminUser());

        sut.postBlogPost(BlogPostDto.builder().username("admin").build(), authentication);

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .save(Mockito.any(BlogPost.class));
    }

    @Test
    public void givenAdminUser_whenUpdateBlogPost_thenBlogPostIsUpdated() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createAdminUser());
        Mockito.when(blogPostRepository.findByUuid(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(MockHelper.createAdminBlogPost()));

        sut.updateBlogPost(BlogPostDto.builder()
                .uuid(UUID.randomUUID())
                .username("admin")
                .build(), authentication);

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findByUuid(Mockito.any(UUID.class));
        Mockito.verify(blogPostRepository, Mockito.times(1))
                .save(Mockito.any(BlogPost.class));
    }

    @Test
    public void givenAdminUser_andNonExistingBlogPost_whenUpdateBlogPost_thenResourceNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createAdminUser());
        Mockito.when(blogPostRepository.findByUuid(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.updateBlogPost(BlogPostDto.builder()
                    .uuid(UUID.randomUUID())
                    .username("admin")
                    .build(), authentication);
        });

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findByUuid(Mockito.any(UUID.class));
    }

    @Test
    public void givenAdminUser_whenDeleteBlogPost_thenBlogPostIsDeleted() {
        final BlogPost posterBlogPost = MockHelper.createPosterBlogPost();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createAdminUser());
        Mockito.when(blogPostRepository.findByUuid(Mockito.eq(posterBlogPost.getUuid())))
                .thenReturn(Optional.of(posterBlogPost));

        sut.deleteBlogPost(posterBlogPost.getUuid(), authentication);

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findByUuid(Mockito.eq(posterBlogPost.getUuid()));
        Mockito.verify(blogPostRepository, Mockito.times(1))
                .delete(Mockito.eq(posterBlogPost));
    }

    @Test
    public void givenPosterUser_andAdminBlogPost_whenUpdateBlogPost_thenUnauthorizedAccess() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createPosterUser());

        assertThrows(UnauthorizedAccessException.class, () -> {
            sut.updateBlogPost(BlogPostDto.builder()
                    .uuid(UUID.randomUUID())
                    .username("admin")
                    .build(), authentication);
        });
    }

    @Test
    public void givenPosterUser_andPosterBlogPost_whenDeleteBlogPost_thenBlogPostIsDeleted() {
        final BlogPost posterBlogPost = MockHelper.createPosterBlogPost();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(MockHelper.createPosterUser());
        Mockito.when(blogPostRepository.findByUuid(Mockito.eq(posterBlogPost.getUuid())))
                .thenReturn(Optional.of(posterBlogPost));

        sut.deleteBlogPost(posterBlogPost.getUuid(), authentication);

        Mockito.verify(blogPostRepository, Mockito.times(1))
                .findByUuid(Mockito.eq(posterBlogPost.getUuid()));
        Mockito.verify(blogPostRepository, Mockito.times(1))
                .delete(Mockito.eq(posterBlogPost));
    }

}