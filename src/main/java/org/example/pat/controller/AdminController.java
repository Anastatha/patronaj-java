package org.example.pat.controller;

import jakarta.validation.constraints.Email;
import org.example.pat.dto.ApiResult;
import org.example.pat.dto.auth.AuthRequest;
import org.example.pat.dto.auth.AuthResponse;
import org.example.pat.dto.curator.CreateCuratorInput;
import org.example.pat.dto.curator.CuratorResponse;
import org.example.pat.dto.curator.CuratorWithUserResponse;
import org.example.pat.dto.curator.UpdateCuratorInput;
import org.example.pat.entity.Curator;
import org.example.pat.security.CurrentUserId;
import org.example.pat.security.JwtTokenProvider;
import org.example.pat.service.ICuratorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ICuratorService curatorService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(ICuratorService curatorService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.curatorService = curatorService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create-curator")
    public ApiResult<CuratorResponse> createCurator(@RequestBody CreateCuratorInput input) {
        return new ApiResult.Success<>(CuratorResponse.fromEntity(curatorService.createCurator(input)));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ApiResult<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtTokenProvider.generateToken(authentication);
        return new ApiResult.Success<>(new AuthResponse(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CURATOR')")
    @GetMapping("/get-curator")
    public ApiResult<CuratorWithUserResponse> getCurator(@CurrentUserId Long id) {
        Curator curator = curatorService.getCurator(id);
        return new ApiResult.Success<>(CuratorWithUserResponse.fromEntity(curator));
    }

    @PatchMapping("/update-curator/{id}")
    public ApiResult<CuratorResponse> patchUpdate(@PathVariable Long id, @RequestBody UpdateCuratorInput input) {
        return new ApiResult.Success<>(CuratorResponse.fromEntity(curatorService.updateCurator(id, input)));
    }

    @PatchMapping("/delete-curator/{id}")
    public ApiResult<CuratorResponse> softDeleteCurator(@PathVariable Long id) {
        return new ApiResult.Success<>(CuratorResponse.fromEntity(curatorService.softDeleteCurator(id)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CURATOR')")
    @GetMapping("/get-curators")
    public ApiResult<List<CuratorResponse>> getCurators() {
        List<CuratorResponse> curators = curatorService.getAllCurator()
                .stream()
                .map(CuratorResponse::fromEntity)
                .toList();
        return new ApiResult.Success<>(curators);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CURATOR')")
    @GetMapping("/curator-by-email")
    public ApiResult<CuratorResponse> getCuratorByEmail(@RequestParam @Email String email) {
        return new ApiResult.Success<>(CuratorResponse.fromEntity(curatorService.getCuratorByEmail(email)));
    }
}
