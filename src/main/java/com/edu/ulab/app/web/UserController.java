package com.edu.ulab.app.web;

import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.facade.UserDataFacade;
import com.edu.ulab.app.web.constant.WebConstant;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.BaseWebResponse;
import com.edu.ulab.app.web.response.UserBookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.edu.ulab.app.web.constant.WebConstant.REQUEST_ID_PATTERN;
import static com.edu.ulab.app.web.constant.WebConstant.RQID;

@Slf4j
@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/user",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserDataFacade userDataFacade;

    public UserController(UserDataFacade userDataFacade) {
        this.userDataFacade = userDataFacade;
    }


    @Operation(summary = "Create user and books")
    @ApiResponse(responseCode = "200", description = "Create user and books successfully",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserBookResponse.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseWebResponse.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PostMapping(value = "/create")
    public UserBookResponse createUserWithBooks(@Valid @RequestBody UserBookRequest request,
                                                @RequestHeader(RQID) @Pattern(regexp = REQUEST_ID_PATTERN) final String requestId) {
        UserBookResponse response = userDataFacade.createUserWithBooks(request);
        log.info("Response with created user and his books: {}", response);
        return response;
    }


    @Operation(summary = "Update user and books by userId")
    @ApiResponse(responseCode = "200", description = "Update user and books successfully",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserBookResponse.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseWebResponse.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PutMapping(value = "/update/{userId}")
    public UserBookResponse updateUserWithBooks(@Valid @RequestBody UserBookRequest request,
                                                @PathVariable Long userId) {
        checkPositiveNumber(userId);
        UserBookResponse response = userDataFacade.updateUserWithBooks(request, userId);
        log.info("Response with updated user and his books: {}", response);
        return response;
    }


    @Operation(summary = "Get user and his books by userId")
    @ApiResponse(responseCode = "200", description = "Get user and his books successfully",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserBookResponse.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseWebResponse.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @GetMapping(value = "/get/{userId}")
    public UserBookResponse getUserWithBooks(@PathVariable Long userId) {
        checkPositiveNumber(userId);
        UserBookResponse response = userDataFacade.getUserWithBooks(userId);
        log.info("Response with user and his books: {}", response);
        return response;
    }

    @Operation(summary = "Delete user and his books by userId")
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseWebResponse.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @DeleteMapping(value = "/delete/{userId}")
    public void deleteUserWithBooks(@PathVariable Long userId) {
        checkPositiveNumber(userId);
        log.info("Delete user and his books:  userId {}", userId);
        userDataFacade.deleteUserWithBooks(userId);
    }

    private void checkPositiveNumber(Long userId) {
        if (userId <= 0L) {
            throw new NotFoundException("Invalid input UserId: "
                    + userId + " Please check id and try again");
        }
    }

}
