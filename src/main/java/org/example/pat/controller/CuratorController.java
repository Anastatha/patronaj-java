package org.example.pat.controller;

import org.example.pat.dto.ApiResult;
import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.dto.user.UpdateUserInput;
import org.example.pat.dto.user.UserResponse;
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

    // Создать пользователя
    @PreAuthorize("permitAll()")
    @PostMapping("/create-user/{curatorId}")
    public ApiResult<UserResponse> createUser(
            @PathVariable Long curatorId,
            @RequestBody CreateUserInput input) {
        return new ApiResult.Success<>(userService.createUser(curatorId, input));
    }

    // Получить пользователя по id
    @PreAuthorize("permitAll()")
    @GetMapping("/get-user/{id}")
    public ApiResult<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    // Получить всех пользователей
    @PreAuthorize("permitAll()")
    @GetMapping("/get-users")
    public ApiResult<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    // Мягкое удаление пользователя
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("'/delete-user/{id}")
    public ApiResult<UserResponse> softDelete(@PathVariable Long id) {
        User user = userService.softDeleteUser(id);
        return new ApiResult.Success<>(UserResponse.fromEntity(user));
    }

    // Пользователи по id куратора
    @PreAuthorize("permitAll()")
    @GetMapping("/get-users/{curatorId}")
    public ApiResult<List<UserResponse>> getUsersByCurator(@PathVariable Long curatorId) {
        List<UserResponse> users = userService.getUsersByCuratorId(curatorId)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

//    public static class LabelsRequest {
//        private List<Label> labels;
//
//        public List<Label> getLabels() { return labels; }
//        public void setLabels(List<Label> labels) { this.labels = labels; }
//    }
    // Поиск пользователей по labels
//    @PreAuthorize("permitAll()")
//    @PostMapping("/get-users-by-labels")
//    public ApiResult<List<UserResponse>> getUsersByLabel(@RequestBody LabelsRequest request) {
//        System.out.println(request.getLabels());
//        List<UserResponse> users = userService.getUsersByLabel(request.getLabels())
//                .stream()
//                .map(UserResponse::fromEntity)
//                .toList();
//        return new ApiResult.Success<>(users);
//    }

    // Поиск пользователей по ОКВЭД
    @PreAuthorize("permitAll()")
    @PostMapping("/get-users-by-okved")
    public ApiResult<List<UserResponse>> getUsersByOkved(@RequestBody String okved) {
        System.out.println(okved);
        List<UserResponse> users = userService.getUsersByOkved(okved)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }


    // Поиск пользователей по ОКВЭД или labels
    @PreAuthorize("permitAll()")
    @PostMapping("/get-users/by-labels-or-okved")
    public ApiResult<List<UserResponse>> getUsersByLabelOrOkved(
            @RequestParam(required = false) List<String> okved,
            @RequestBody(required = false) List<Label> labels
    ) {
        List<UserResponse> users = userService.getUsersByLabelOrOkved(okved, labels)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }

    // Обновить пользователя
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-user/{id}")
    public ApiResult<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserInput input
    ) {
        User updated = userService.updateUser(id, input);
        return new ApiResult.Success<>(UserResponse.fromEntity(updated));
    }

    // Поиск пользователей по дате обновления
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-users-between")
    public ApiResult<List<UserResponse>> getUsersBetween(
            @RequestParam String from,
            @RequestParam String to
    ) {
        List<UserResponse> users = userService.getUsersBetween(from, to)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(users);
    }
}
