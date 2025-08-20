package org.example.pat.security;

import io.jsonwebtoken.*;//Jwts, Claims, SignatureAlgorithm — для создания и парсинга токенов.
import io.jsonwebtoken.security.Keys;// для генерации ключа подписи
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

//отвечает за работу с jwt токенами,создание, валидация, извлечение информации
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    //Метод возвращает ключ подписи HMAC-SHA, созданный из строки jwtSecret.
    //Метод jwtSecret.getBytes() преобразует строку в байты (нужно для генерации Key).
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //создает токен на основе информации об аутентификации пользователя, дата создания и срок действия
    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Получаем роль из authentication (первый authority)
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER");

        return Jwts.builder()
                .setSubject(authentication.getName())//устанавливает информацию в токен(в моем случае email)
                .claim("userId", userDetails.getId())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)//Подписывает токен твоим ключом с алгоритмом HS512
                .compact();//Финализирует построение токена и возвращает его в виде строки.
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    //Парсит токен с помощью твоего ключа.
    //Получает его тело (Claims).
    //Возвращает subject — в нашем случае это email пользователя, переданный в generateToken.
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    //Jwts.parserBuilder() - создает парсер JWT
    //.setSigningKey() - устанавливает ключ для проверки подписи
    //.build() - завершает настройку парсера
    //.parseClaimsJws(token) - разбирает и проверяет токен:Проверяет подпись, Проверяет срок действия
    //.getBody() - получает "тело" токена (claims)
    //.getSubject() - извлекает subject (username/email пользователя)

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtException("JWT token is invalid or expired: " + ex.getMessage(), ex);
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);//Извлекает email из токена.
        // Извлекаем роль из токена (если она есть)
        String role = getRoleFromToken(token);

        //Создаёт объект Authentication для Spring Security с:
        //username — как principal (основной пользователь),
        //null — пароль (не нужен здесь),
        //List.of() —  список прав/ролей.
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(role))
                // Для ролей: roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                //List.of() // Пустой список authorities, если ролей нет
        );
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    //Смотрит на заголовок Authorization.
    //Проверяет, начинается ли он с "Bearer ".
    //Если да, отрезает "Bearer " и возвращает только токен.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
