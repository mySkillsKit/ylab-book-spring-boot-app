package com.edu.ulab.app.web.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BookRequest {
    @NotNull(message = "Invalid title: not Null")
    @NotEmpty(message = "Invalid title: not Empty")
    @NotBlank(message = "Invalid title: Must not contain only spaces")
    @Size(min = 1, max = 30, message = "Invalid title: Must be min 1 characters")
    private String title;

    @NotNull(message = "Invalid author: not Null")
    @NotEmpty(message = "Invalid author: not Empty")
    @NotBlank(message = "Invalid author: Must not contain only spaces")
    @Size(min = 2, max = 30, message = "Invalid author: Must be min 2 characters")
    private String author;

    @NotNull(message = "Invalid pageCount: not Null")
    @Min(value = 0, message = "Invalid pageCount: positive number, min 0 is required")
    private long pageCount;
}
