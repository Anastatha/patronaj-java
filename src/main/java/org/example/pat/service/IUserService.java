package org.example.pat.service;

import io.micrometer.common.lang.Nullable;
import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.dto.user.UpdateUserInput;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Label;

import java.util.List;

public interface IUserService {
    User createUser(Long curatorId, CreateUserInput createUserInput);
    User getUser(Long id);
    List<User> getAllUsers();
    User softDeleteUser(Long id);
    List<User> getUsersByCuratorId(Long curatorId);
    List<User> getUsersByLabel(List<Label> labels);
    List<User> getUsersByOkved(String okved);
    List<User> getUsersByLabelOrOkved(@Nullable List<String> okved, @Nullable List<Label> labels);
    User updateUser(Long id, UpdateUserInput input);
    List<User> getUsersBetween(@Nullable String from, @Nullable String to);
}
