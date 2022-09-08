package com.carepay.blog.models;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "authority_id"),
            inverseJoinColumns = @JoinColumn(name ="user_id")
    )
    private Collection<Authority> authorities;

}
