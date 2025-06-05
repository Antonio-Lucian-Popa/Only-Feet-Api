package com.asusoftware.only_feet_api.media_file.repository;

import com.asusoftware.only_feet_api.media_file.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, UUID> {
    List<MediaFile> findByPostId(UUID postId);
    Optional<MediaFile> findFirstByPostIdIsNullAndFileUrlContaining(UUID userId);
}