package com.edu.ulab.app.entity;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Book {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private long pageCount;

}
