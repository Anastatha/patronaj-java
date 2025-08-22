package org.example.pat.controller;

import io.micrometer.common.lang.Nullable;
import org.example.pat.dto.ApiResult;
import org.example.pat.dto.user.*;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Label;
import org.example.pat.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("permitAll()")
    @GetMapping("/get-user/{id}")
    public ApiResult<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/get-users")
    public ApiResult<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete-user/{id}")
    public ApiResult<UserResponse> softDelete(@PathVariable Long id) {
        User user = userService.softDeleteUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/get-users/{curatorId}")
    public ApiResult<List<UserResponse>> getUsersByCurator(@PathVariable Long curatorId) {
        List<UserResponse> users = userService.getUsersByCuratorId(curatorId)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/get-users-by-labels")
    public ApiResult<List<UserResponse>> getUsersByLabel(@RequestBody LabelsInput request) {
        List<UserResponse> users = userService.getUsersByLabel(request.labels())
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/get-users-by-okved")
    public ApiResult<List<UserResponse>> getUsersByOkved(@RequestBody OkvedInput request) {
        List<UserResponse> users = userService.getUsersByOkved(request.okved())
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/get-users-by-labels-or-okved")
    public ApiResult<List<UserResponse>> getUsersByLabelOrOkved(
            @RequestBody LabelsOrOkvedsInput request
    ) {
        List<UserResponse> users = userService.getUsersByLabelOrOkved(request.okved(), request.labels())
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/update-user/{id}")
    public ApiResult<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserInput input
    ) {
        User updated = userService.updateUser(id, input);
        return new ApiResult.Success<>(UserResponse.fromEntity(updated));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/get-users-between")
    public ApiResult<List<UserResponse>> getUsersBetween(
            @RequestParam @Nullable String from,
            @RequestParam @Nullable String to
    ) {
        List<UserResponse> users = userService.getUsersBetween(from, to)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }
}
