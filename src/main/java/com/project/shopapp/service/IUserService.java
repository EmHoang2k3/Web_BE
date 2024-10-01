package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.UserModel;
import com.project.shopapp.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface IUserService {
    UserModel createUser(UserDTO userDTO) throws Exception;

    String login (String phoneNumber, String password) throws Exception;

    Page<UserResponse> getAllUser(PageRequest pageRequest) ;

    UserModel getUserDetailsFormToken(String token) throws Exception;
}
