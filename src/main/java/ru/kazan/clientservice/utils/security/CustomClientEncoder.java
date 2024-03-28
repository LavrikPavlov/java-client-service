package ru.kazan.clientservice.utils.security;



import org.springframework.security.crypto.password.PasswordEncoder;


public class CustomClientEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }

}
