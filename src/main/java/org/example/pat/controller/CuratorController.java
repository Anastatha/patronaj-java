package org.example.pat.controller;

import io.micrometer.common.lang.Nullable;
import org.example.pat.dto.ApiResult;
import org.example.pat.dto.user.*;
import org.example.pat.entity.User;
import org.example.pat.security.CurrentUserId;
import org.example.pat.service.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curator")
@PreAuthorize("hasRole('CURATOR')")
public class CuratorController {
    private final IUserService userService;

    public CuratorController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ApiResult<UserResponse> createUser(
            @CurrentUserId Long curatorId,
            @RequestBody CreateUserInput input) {
        return new ApiResult.Success<>(UserResponse.fromEntity(userService.createUser(curatorId, input)));
    }

    @GetMapping("/get-user/{id}")
    public ApiResult<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    @GetMapping("/get-users")
    public ApiResult<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @DeleteMapping("/delete-user/{id}")
    public ApiResult<UserResponse> softDelete(@PathVariable Long id) {
        User user = userService.softDeleteUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    @GetMapping("/get-users-by-curator")
    public ApiResult<List<UserResponse>> getUsersByCurator(@CurrentUserId Long curatorId) {
        List<UserResponse> users = userService.getUsersByCuratorId(curatorId)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PostMapping("/get-users-by-labels")
    public ApiResult<List<UserResponse>> getUsersByLabel(@RequestBody LabelsInput request) {
        List<UserResponse> users = userService.getUsersByLabel(request.labels())
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    @PostMapping("/get-users-by-okved")
    public ApiResult<List<UserResponse>> getUsersByOkved(@RequestBody OkvedInput request) {
        List<UserResponse> users = userService.getUsersByOkved(request.okved())
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

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

    @PutMapping("/update-user/{id}")
    public ApiResult<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserInput input
    ) {
        User updated = userService.updateUser(id, input);
        return new ApiResult.Success<>(UserResponse.fromEntity(updated));
    }

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
