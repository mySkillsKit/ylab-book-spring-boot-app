package com.edu.ulab.app.web.request;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class UserBookRequest {
    @Valid
    private UserRequest userRequest;
    @Valid
    private List<BookRequest> bookRequests;
}
