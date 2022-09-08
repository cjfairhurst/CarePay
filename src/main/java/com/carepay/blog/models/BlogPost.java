package com.carepay.blog.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class BlogPost {

    @Id
    @GeneratedValue
    private Long id;

    @Type(type="org.hibernate.type.UUIDCharType")
    private final UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String subtitle;
    private String content;

    private LocalDateTime creationTimestamp;
    private LocalDateTime lastEditedTimestamp;

}
