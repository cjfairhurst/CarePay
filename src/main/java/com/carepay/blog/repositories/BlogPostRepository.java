package com.carepay.blog.repositories;

import com.carepay.blog.models.BlogPost;
import com.carepay.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findById(Long id);

    Optional<BlogPost> findByUuid(UUID uuid);

    List<BlogPost> findAllByUserOrderByCreationTimestampDesc(User user);

    List<BlogPost> findAllByOrderByCreationTimestampDesc();

}