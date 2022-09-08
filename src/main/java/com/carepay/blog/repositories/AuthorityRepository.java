package com.carepay.blog.repositories;

import com.carepay.blog.models.Authority;
import com.carepay.blog.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository  extends JpaRepository<Authority, Long> {
    Authority findDistinctByAuthority(Role authority);

}
