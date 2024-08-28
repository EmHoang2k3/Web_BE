package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.UserModel;

import java.util.List;


public interface IUserService {
    UserModel createUser(UserDTO userDTO) throws Exception;

    String login (String phoneNumber, String password) throws Exception;

    List<UserModel> getAllUser() ;

    UserModel getUserDetailsFormToken(String token) throws Exception;
}
