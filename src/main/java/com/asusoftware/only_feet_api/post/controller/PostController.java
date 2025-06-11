package com.asusoftware.only_feet_api.post.controller;

import com.asusoftware.only_feet_api.post.model.PostVisibility;
import com.asusoftware.only_feet_api.post.model.dto.CreatePostDto;
import com.asusoftware.only_feet_api.post.model.dto.PostDto;
import com.asusoftware.only_feet_api.post.service.PostService;
import com.asusoftware.only_feet_api.user.model.User;
import com.asusoftware.only_feet_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    /**
     * Creează o postare nouă (doar creatorii).
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CREATOR')")
    public PostDto createPost( @RequestParam("title") String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam("visibility") PostVisibility visibility,
                               @RequestParam(value = "files", required = false) List<MultipartFile> files,
                              @AuthenticationPrincipal Jwt principal) {
        User user = userService.getCurrentUserEntity(principal);
        // Creezi manual DTO-ul
        CreatePostDto dto = CreatePostDto.builder()
                .title(title)
                .description(description)
                .visibility(visibility)
                .build();
        return postService.createPost(dto, user, files);
    }

    /**
     * Obține toate postările publice (feed global).
     */
    @GetMapping("/public")
    public Page<PostDto> getPublicPosts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPublicPosts(page, size);
    }

    /**
     * Obține toate postările unui creator (vizitator sau abonat).
     */
    @GetMapping("/creator/{creatorId}")
    public Page<PostDto> getPostsByCreator(@PathVariable UUID creatorId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @AuthenticationPrincipal Jwt principal) {
        boolean isSubscriber = false;
        if (principal != null) {
            // TODO: verificare abonament real
            isSubscriber = true;
        }
        return postService.getPostsByCreator(creatorId, isSubscriber, page, size);
    }

    /**
     * Obține o postare individuală (verifică abonamentul).
     */
    @GetMapping("/{postId}")
    public PostDto getPostById(@PathVariable UUID postId,
                               @RequestParam UUID creatorId,
                               @AuthenticationPrincipal Jwt principal) {
        boolean isSubscriber = false;
        if (principal != null) {
            // TODO: verificare abonament real
            isSubscriber = true;
        }
        return postService.getPostById(postId, isSubscriber, creatorId);
    }

    /**
     * Șterge o postare (doar creatorul).
     */
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAuthority('CREATOR')")
    public void deletePost(@PathVariable UUID postId,
                           @AuthenticationPrincipal Jwt principal) {
        User user = userService.getCurrentUserEntity(principal);
        postService.deletePost(postId, user.getId());
    }
}
