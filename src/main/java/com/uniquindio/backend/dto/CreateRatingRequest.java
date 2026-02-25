package com.uniquindio.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRatingRequest {
    private Long bookId;
    private Integer score; // 1 to 5
}
