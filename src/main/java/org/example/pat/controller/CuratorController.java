package org.example.pat.controller;

import org.example.pat.dto.ApiResult;
import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.dto.user.UserResponse;
import org.example.pat.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curator")
public class CuratorController {

    private final UserService userService;

    public CuratorController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/create-user/{curatorId}")
    public ApiResult<UserResponse> createUser(
            @PathVariable Long curatorId,
            @RequestBody CreateUserInput input) {
        return new ApiResult.Success<>(userService.createUser(curatorId, input));
    }

}