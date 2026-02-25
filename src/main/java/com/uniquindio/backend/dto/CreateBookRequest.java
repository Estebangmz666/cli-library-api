package com.uniquindio.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private String genre;
    private Integer publishedYear;
    private BigDecimal price;
}
