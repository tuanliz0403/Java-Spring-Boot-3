package com.tuan.identity_service.service;

import com.tuan.identity_service.dto.request.AuthenticationRequest;
import com.tuan.identity_service.exception.AppException;
import com.tuan.identity_service.exception.ErrorCode;
import com.tuan.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository repository;

    public boolean authenticate(AuthenticationRequest request){
        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        return encoder.matches(request.getPassword(), user.getPassword());
    }
}
