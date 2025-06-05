package com.asusoftware.only_feet_api.media_file.service;

import com.asusoftware.only_feet_api.media_file.model.MediaFile;
import com.asusoftware.only_feet_api.media_file.model.MediaLocationType;
import com.asusoftware.only_feet_api.media_file.model.dto.MediaFileDto;
import com.asusoftware.only_feet_api.media_file.repository.MediaFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaFileRepository mediaFileRepository;
    private final ModelMapper mapper;

    @Value("${media.upload.dir}")
    private String uploadDir;

    @Value("${media.public.url.prefix}")
    private String publicUrlPrefix;

    @PostConstruct
    public void init() throws IOException {
        for (String folder : List.of("posts", "users")) {
            Path path = Paths.get(uploadDir, folder);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        }
    }

    /**
     * Salvează fișierul pentru o postare sau user (în folderul corespunzător).
     */
    public MediaFile saveFile(MultipartFile file, UUID targetId, MediaLocationType locationType) {
        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String uniqueName = UUID.randomUUID() + "." + extension;

            String subfolder = switch (locationType) {
                case POST -> "posts";
                case USER -> "users";
            };

            Path filePath = Paths.get(uploadDir, subfolder, uniqueName);
            Files.write(filePath, file.getBytes());

            String publicUrl = publicUrlPrefix + subfolder + "/" + uniqueName;

            MediaFile mediaFile = MediaFile.builder()
                    .postId(locationType == MediaLocationType.POST ? targetId : null)
                    .fileUrl(publicUrl)
                    .mimeType(file.getContentType())
                    .createdAt(LocalDateTime.now())
                    .build();

            return mediaFileRepository.save(mediaFile);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la salvarea fișierului: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public List<MediaFileDto> getMediaForPost(UUID postId) {
        return mediaFileRepository.findByPostId(postId).stream().map(
                mediaFile -> mapper.map(mediaFile, MediaFileDto.class)
        ).toList();
    }

    public Optional<MediaFile> getUserProfileMedia(UUID userId) {
        return mediaFileRepository.findFirstByPostIdIsNullAndFileUrlContaining(userId);
    }


}
