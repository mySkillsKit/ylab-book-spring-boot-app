package com.edu.ulab.app.web.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserRequest {
    @NotBlank(message = "Invalid fullName: Must not contain only spaces")
    @Size(min = 2, max = 30, message = "Invalid fullName: Must be min 2 characters")
    private String fullName;

    @NotBlank(message = "Invalid title: Must not contain only spaces")
    @Size(min = 1, max = 30, message = "Invalid title: Must be min 1 characters")
    private String title;

    @Min(value = 1, message = "Invalid age: positive number, min 1 is required")
    @Max(value = 150, message = "Invalid age: positive number, max 150 is required")
    private int age;
}
