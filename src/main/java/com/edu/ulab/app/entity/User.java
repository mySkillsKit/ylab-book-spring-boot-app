package com.edu.ulab.app.entity;

import lombok.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    private Long id;
    private String fullName;
    private String title;
    private int age;
}
