package com.epam.image.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class Image {
    private Integer id;
    private String objectPath;
    private String objectSize;
    private LocalDateTime timeAdded;
    private LocalDateTime timeUpdated;
    private List<String> labels;
    private ImageStatus status;
}