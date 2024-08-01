package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserService {
    UserModel createUser(UserDTO userDTO);

    String login (String phoneNumber,String password);
}
