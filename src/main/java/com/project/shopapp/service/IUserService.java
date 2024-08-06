package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.UserModel;


public interface IUserService {
    UserModel createUser(UserDTO userDTO) throws Exception;

    String login (String phoneNumber, String password) throws Exception;
}
