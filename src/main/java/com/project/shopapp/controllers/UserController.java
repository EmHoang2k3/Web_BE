package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.UserModel;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
import com.project.shopapp.responses.UserListResponse;
import com.project.shopapp.responses.UserResponse;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.service.UserService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService iUserService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (
            @Valid @RequestBody
            UserDTO userDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                                .build()
                );
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .build());
            }
             UserModel user = iUserService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                            .user(user)
                            .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (
            @Valid @RequestBody
            UserLoginDTO userLoginDTO

    ){
        //Kiem tra thong tin dang nhap va sinh toke
        try {
          String token = iUserService.login(
                  userLoginDTO.getPhoneNumber(),
                  userLoginDTO.getPassword());

            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED,e.getMessage()))
                            .build());
        }
        //Tra ve token trong response
    }

    @GetMapping("")
    public ResponseEntity<UserListResponse> getAllUser (
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){
        PageRequest pageRequest = PageRequest.of(page -1,limit, Sort.by("id").ascending());
        Page userPage = iUserService.getAllUser(pageRequest);
        int totalPage = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();
        return ResponseEntity.ok(UserListResponse.builder()
                        .user(users)
                        .totalPage(totalPage)
                        .build());
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String token){
        try{
            String extractedToken = token.substring(7);
            UserModel user = iUserService.getUserDetailsFormToken(extractedToken);
            return ResponseEntity.ok(UserResponse.formUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
