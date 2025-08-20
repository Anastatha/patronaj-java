package org.example.pat.security;

import org.example.pat.entity.Curator;
import org.example.pat.repository.CuratorRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;//интерфейс, который описывает пользователя для Spring Security.
import org.springframework.security.core.userdetails.UserDetailsService;//интерфейс от Spring Security, который обязывает нас реализовать метод loadUserByUsername().
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

//Когда пользователь пытается залогиниться, Spring Security вызывает этот класс, чтобы:
//Найти пользователя в базе данных по email (или username).
//Получить его пароль.
//Получить его роли/права (если есть).
//Вернуть объект, который Spring Security сможет использовать для проверки пароля и авторизации.
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CuratorRepository curatorRepository;

    public CustomUserDetailsService(CuratorRepository curatorRepository) {
        this.curatorRepository = curatorRepository;
    }

    //Возвращает UserDetails — объект, который Spring Security будет использовать для проверки пароля.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Curator user = curatorRepository.getCuratorByEmail(email);

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
