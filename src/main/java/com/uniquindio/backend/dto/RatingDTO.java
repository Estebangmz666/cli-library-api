package com.uniquindio.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
