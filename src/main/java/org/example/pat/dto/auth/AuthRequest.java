package org.example.pat.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank String email, @NotBlank String password) {
}
