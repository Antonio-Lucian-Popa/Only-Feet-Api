package com.asusoftware.only_feet_api.post.service;

import com.asusoftware.only_feet_api.media_file.model.MediaFile;
import com.asusoftware.only_feet_api.media_file.model.MediaLocationType;
import com.asusoftware.only_feet_api.media_file.model.dto.MediaFileDto;
import com.asusoftware.only_feet_api.media_file.service.MediaService;
import com.asusoftware.only_feet_api.post.model.Post;
import com.asusoftware.only_feet_api.post.model.PostVisibility;
import com.asusoftware.only_feet_api.post.model.dto.CreatePostDto;
import com.asusoftware.only_feet_api.post.model.dto.PostDto;
import com.asusoftware.only_feet_api.post.repository.PostRepository;
import com.asusoftware.only_feet_api.user.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MediaService mediaService;
    private final ModelMapper mapper;

    /**
     * Creează o postare nouă.
     */
    @Transactional
    public PostDto createPost(CreatePostDto dto, User creator, List<MultipartFile> files) {
        Post post = Post.builder()
                .creatorId(creator.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .visibility(dto.getVisibility())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        post = postRepository.save(post);

        List<MediaFile> filesSaved = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                // Salvăm fișierul media și asociem cu postarea
                MediaFile mediaFile = mediaService.saveFile(file, post.getId(), MediaLocationType.POST);
                filesSaved.add(mediaFile);
            }
        }

        PostDto savedPostDto = mapper.map(postRepository.save(post), PostDto.class);

        if(!filesSaved.isEmpty()) {
            List<MediaFileDto> mediaFileDtos = filesSaved.stream()
                    .map(mediaFile -> mapper.map(mediaFile, MediaFileDto.class))
                    .collect(Collectors.toList());
            savedPostDto.setMediaFiles(mediaFileDtos);

        }
        return savedPostDto;
    }

    /**
     * Găsește postările unui creator cu paginare (pentru profil).
     */
    public Page<PostDto> getPostsByCreator(UUID creatorId, boolean isSubscriber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = isSubscriber
                ? postRepository.findByCreatorId(creatorId, pageable)
                : postRepository.findByCreatorIdAndVisibility(creatorId, PostVisibility.PUBLIC, pageable);

        return posts.map(post -> mapper.map(post, PostDto.class));
    }

    /**
     * Găsește postările publice pentru feed (cu paginare).
     */
    public Page<PostDto> getAllPublicPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByVisibility(PostVisibility.PUBLIC, pageable);
        return posts.map(post -> mapper.map(post, PostDto.class));
    }

    /**
     * Găsește o postare după ID.
     */
    public PostDto getPostById(UUID postId, boolean isSubscriber, UUID creatorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Postarea nu există."));

        if (!post.getCreatorId().equals(creatorId)) {
            throw new RuntimeException("Postarea nu aparține creatorului respectiv.");
        }

        if (!isSubscriber && !"PUBLIC".equals(post.getVisibility())) {
            throw new RuntimeException("Conținut disponibil doar pentru abonați.");
        }

        return mapper.map(post, PostDto.class);
    }

    /**
     * Șterge o postare (doar dacă aparține creatorului).
     */
    public void deletePost(UUID postId, UUID requesterId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Postarea nu a fost găsită."));

        if (!post.getCreatorId().equals(requesterId)) {
            throw new RuntimeException("Nu ai permisiunea să ștergi această postare.");
        }

        postRepository.deleteById(postId);
    }
}

