package com.asusoftware.only_feet_api.post.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId; // Refers to User.id

    private String title;
    private String description;

    @Column(name = "media_type", nullable = false)
    private String mediaType; // IMAGE or VIDEO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostVisibility visibility; // PUBLIC or SUBSCRIBERS

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
