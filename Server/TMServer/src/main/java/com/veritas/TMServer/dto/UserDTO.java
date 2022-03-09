package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.UserEntity;
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
    private String fcmToken;
    private float risk;
    private int contactDegree;

    public UserDTO(final UserEntity entity) {
        this.uuid = entity.getUuid();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.detailAddress = entity.getDetailAddress();
        this.fcmToken = entity.getFcmToken();
        this.risk = entity.getRisk();
        this.contactDegree = entity.getContactDegree();
    }

    public static UserEntity userEntity(final UserDTO dto) {
        return UserEntity.builder()
                .uuid(dto.getUuid())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .detailAddress(dto.getDetailAddress())
                .fcmToken(dto.getFcmToken())
                .risk(dto.getRisk())
                .contactDegree(dto.getContactDegree())
                .build();
    }
}
