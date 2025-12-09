package com.tuan.identity_service.controller;

import com.tuan.identity_service.dto.request.ApiResponse;
import com.tuan.identity_service.dto.request.AuthenticationRequest;
import com.tuan.identity_service.dto.request.IntrospectRequest;
import com.tuan.identity_service.dto.request.response.AuthenticationResponse;
import com.tuan.identity_service.dto.request.response.IntrospectResponse;
import com.tuan.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.authenticate(request)).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request)).build();
    }
}
