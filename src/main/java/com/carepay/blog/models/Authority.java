package com.carepay.blog.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }

}
