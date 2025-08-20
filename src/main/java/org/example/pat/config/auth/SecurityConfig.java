package org.example.pat.config.auth;

import org.example.pat.security.CustomUserDetailsService;
import org.example.pat.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity// включает Spring Security.
@EnableMethodSecurity // Важно для работы @PreAuthorize
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Включаем CORS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )//Говорим Spring'у: не храни сессии (поскольку у нас Stateless-приложение). Вся информация о пользователе содержится в токене.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login").permitAll()//разрешено всем
                        .requestMatchers("/curator/**").permitAll()
                        // Эндпоинты только для ADMIN
                        .requestMatchers(
                                "/admin/create-curator",
                                "/admin/delete-curator/**",
                                "/admin/update-curator/**"
                        ).hasRole("ADMIN")
                        // Эндпоинты для ADMIN и CURATOR
                        .requestMatchers(
                                "/admin/get-curator",
                                "/admin/get-curators",
                                "/admin/curator-by-email"
                        ).hasAnyRole("ADMIN", "CURATOR")
                        .anyRequest().authenticated()//все остальное требует токена
                )
                .addFilterBefore(//подключения фильтра
                        new JwtTokenFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    //Мы явно указываем, что хотим использовать свой CustomUserDetailsService для загрузки пользователей из базы.
    //Это используется при логине: логин → находим пользователя → сверяем пароль → создаём токен.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://your-frontend.com"));//поменять
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
