package com.asusoftware.only_feet_api.media_file.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileDto {
    private UUID id;
    private UUID postId;
    private String fileUrl;
    private String mimeType;
}
