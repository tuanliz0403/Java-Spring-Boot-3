package com.tuan.identity_service.service;

import com.tuan.identity_service.dto.request.UserCreationRequest;
import com.tuan.identity_service.dto.request.UserUpdateRequest;
import com.tuan.identity_service.entity.User;
import com.tuan.identity_service.exception.AppException;
import com.tuan.identity_service.exception.ErrorCode;
import com.tuan.identity_service.mapper.UserMapper;
import com.tuan.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUser(){
        return userRepository.findAll();
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User updateUserById(String id, UserUpdateRequest request){
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        User user = getUserById(id);
        userMapper.updateUser(user, request);

        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public void clearAllUsers() {
        userRepository.deleteAll();
    }
}
