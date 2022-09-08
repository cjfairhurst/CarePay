package com.carepay.blog;

import com.carepay.blog.errors.ResourceNotFoundException;
import com.carepay.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests(
                (authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
                .httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and().csrf().disable()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
