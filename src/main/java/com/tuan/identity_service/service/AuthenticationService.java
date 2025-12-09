package com.tuan.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.tuan.identity_service.dto.request.AuthenticationRequest;
import com.tuan.identity_service.dto.request.response.AuthenticationResponse;
import com.tuan.identity_service.exception.AppException;
import com.tuan.identity_service.exception.ErrorCode;
import com.tuan.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository repository;
    protected static final String KEY_SIGNER = "ec5829f00fc62941fb76eeef22d7bce570f1df1e45b4fa26f11539ff81d8d40b";
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        boolean authenticated = encoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw new AppException(ErrorCode.WRONG_PASSWORD);
        return new AuthenticationResponse().builder().authenticated(true).token(generateToken(request.getUsername())).build();
    }

    private String generateToken(String username){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().
        subject(username).issuer("tuan.com").issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())).claim("Custom claim", "Custom")
        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(KEY_SIGNER.getBytes()));
            return jwsObject.serialize();

        }
        catch (JOSEException e){
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
