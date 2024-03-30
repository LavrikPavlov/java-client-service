package ru.kazan.clientservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDetail implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String client) throws UsernameNotFoundException {

        UserProfile user = userProfileRepository.findByClientId(UUID.fromString(client))
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));

        return new User(user.getClient().getId().toString(), user.getPassword(),
                mapRoleAuthority(user.getRole()));

    }

    private Collection<? extends GrantedAuthority> mapRoleAuthority(RoleEnum role){
        return Set.of(new SimpleGrantedAuthority(role.toString()));
    }
}
