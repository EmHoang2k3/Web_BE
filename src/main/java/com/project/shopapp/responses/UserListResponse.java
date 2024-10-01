package com.project.shopapp.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponse {
    private List<UserResponse> user;
    private int totalPage;
}
