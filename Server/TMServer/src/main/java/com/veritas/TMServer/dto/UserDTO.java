package com.veritas.TMServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String uuid;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String simpleAddress;
    private String detailAddress;
}
