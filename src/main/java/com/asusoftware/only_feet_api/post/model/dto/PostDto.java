package com.asusoftware.only_feet_api.post.model.dto;

import com.asusoftware.only_feet_api.media_file.model.dto.MediaFileDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private UUID id;
    private UUID creatorId;
    private String title;
    private String description;
    private String visibility;
    private LocalDateTime createdAt;
    private List<MediaFileDto> mediaFiles;
}
