package com.asusoftware.only_feet_api.media_file.controller;

import com.asusoftware.only_feet_api.media_file.model.MediaFile;
import com.asusoftware.only_feet_api.media_file.model.MediaLocationType;
import com.asusoftware.only_feet_api.media_file.model.dto.MediaFileDto;
import com.asusoftware.only_feet_api.media_file.service.MediaService;
import com.asusoftware.only_feet_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final UserService userService;

    /**
     * Upload fișier pentru o postare (poze sau video).
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<MediaFile> uploadPostMedia(
            @PathVariable UUID postId,
            @RequestParam("file") MultipartFile file
    ) {
        MediaFile mediaFile = mediaService.saveFile(file, postId, MediaLocationType.POST);
        return ResponseEntity.ok(mediaFile);
    }

    /**
     * Upload poză de profil pentru utilizatorul curent.
     */
    @PostMapping("/user")
    public ResponseEntity<MediaFile> uploadUserProfilePic(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        MediaFile mediaFile = mediaService.saveFile(file, keycloakId, MediaLocationType.USER);
        return ResponseEntity.ok(mediaFile);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<MediaFileDto>> getMediaForPost(@PathVariable UUID postId) {
        List<MediaFileDto> mediaFiles = mediaService.getMediaForPost(postId);
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/user")
    public ResponseEntity<MediaFile> getUserProfilePic(@AuthenticationPrincipal Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal); // dacă ai helper, sau altfel cauți în DB

        return mediaService.getUserProfileMedia(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}

