package com.asusoftware.only_feet_api.media_file.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media_file")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private UUID postId; // Refers to Post.id

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    private String mimeType;
    private LocalDateTime createdAt;
}
