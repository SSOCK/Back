package com.runningmate.backend.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageUploadResponse {
    @JsonProperty("Url")
    String url;
}
