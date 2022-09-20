package com.edu.ulab.app.web.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BookRequest {
    @NotBlank(message = "Invalid title: Must not contain only spaces")
    @Size(min = 1, max = 30, message = "Invalid title: Must be min 1 characters")
    private String title;

    @NotBlank(message = "Invalid author: Must not contain only spaces")
    @Size(min = 2, max = 30, message = "Invalid author: Must be min 2 characters")
    private String author;

    @Min(value = 0, message = "Invalid pageCount: positive number, min 0 is required")
    private long pageCount;
}
