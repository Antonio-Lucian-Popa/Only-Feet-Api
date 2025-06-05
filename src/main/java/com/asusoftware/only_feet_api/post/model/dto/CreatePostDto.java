package com.asusoftware.only_feet_api.post.model.dto;

import com.asusoftware.only_feet_api.post.model.PostVisibility;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {
    private String title;
    private String description;
    private String mediaType;
    private PostVisibility visibility;
}
