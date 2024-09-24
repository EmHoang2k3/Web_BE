package com.project.shopapp.service;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
import com.project.shopapp.models.RoleModel;
import com.project.shopapp.models.UserModel;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authentication;
    @Override
    public UserModel createUser(UserDTO userDTO) throws Exception{
        //register user
        String phoneNumber = userDTO.getPhoneNumber();
        //Kiểm tra xem sđt này đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        RoleModel role = roleRepository.findById(userDTO.getRole())
                .orElseThrow(()-> new DataIntegrityViolationException("Role not found"));
        if(role.getName().equals(RoleModel.ADMIN)){
            throw new PermissionDenyException("You cannot register an admin account");
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

        newUser.setRole(role);
        //Kiểm tra nếu có account id thì không yêu cầu password
        if(userDTO.getFacebookAccountId()==0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception{
        Optional<UserModel> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phone number and password ");
        }
        //return optionalUser.get();
        UserModel existingUser = optionalUser.get();
        //Check password
        if(existingUser.getFacebookAccountId()==0
                && existingUser.getGoogleAccountId()==0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                 throw new BadCredentialsException("Wrong phone number of password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,password,
                existingUser.getAuthorities()
        );
        //authenticate with Java String security
        authentication.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public List<UserModel> getAllUser() {
     return userRepository.findAll();
    }

    @Override
    public UserModel getUserDetailsFormToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<UserModel> user = userRepository.findByPhoneNumber(phoneNumber);

        if(user.isPresent()){
            return  user.get();
        }else {
            throw new Exception("User not found");
        }
    }


}
