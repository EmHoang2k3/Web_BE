package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.RoleModel;
import com.project.shopapp.models.UserModel;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserModel createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        //Kiểm tra xem sđt này đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        //convert từ userDto -> userModel
        UserModel newUser = UserModel.builder()
                .fullName(userDTO.getFullName())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBrith())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        RoleModel role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataIntegrityViolationException("Role not found"));
        newUser.setRole(role);
        //Kiểm tra nếu có account id thì không yêu cầu password
        if(userDTO.getFacebookAccountId()==0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            //String encodedPassword = passwordEncoder.encode(password);
           // newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}
