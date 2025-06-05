package com.asusoftware.only_feet_api.post.repository;

import com.asusoftware.only_feet_api.post.model.Post;
import com.asusoftware.only_feet_api.post.model.PostVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    // Pentru profil creator (abonat sau nu)
    Page<Post> findByCreatorId(UUID creatorId, Pageable pageable);
    Page<Post> findByCreatorIdAndVisibility(UUID creatorId, PostVisibility visibility, Pageable pageable);

    // Pentru feed public
    Page<Post> findByVisibility(PostVisibility visibility, Pageable pageable);

    // Dacă mai ai nevoie de metode fără paginare:
    List<Post> findByCreatorId(UUID creatorId);
    List<Post> findByCreatorIdAndVisibility(UUID creatorId, PostVisibility visibility);
}
