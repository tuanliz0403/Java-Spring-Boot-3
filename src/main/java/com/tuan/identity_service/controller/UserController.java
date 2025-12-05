package com.tuan.identity_service.controller;

import com.tuan.identity_service.dto.request.ApiResponse;
import com.tuan.identity_service.dto.request.UserCreationRequest;
import com.tuan.identity_service.dto.request.UserUpdateRequest;
import com.tuan.identity_service.entity.User;
import com.tuan.identity_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> response = new ApiResponse<>();

        response.setResult(userService.createUser(request));

        return response;
    }

    @GetMapping
    public List<User> getUser(){
        return userService.getUser();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUserById(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return userService.updateUserById(id, request);
    }

    @DeleteMapping
    public void clearAllUsers(){
        userService.clearAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id){
        userService.deleteUserById(id);
    }

}
