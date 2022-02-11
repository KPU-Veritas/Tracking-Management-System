package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.WebEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebDTO {
    private String token;
    private String id;
    private String username;
    private String email;
    private String password;
    private int notice;
    private int warningLevel;

    public WebDTO(final WebEntity entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
    }

    public static WebEntity webEntity(final WebDTO dto) {
        return WebEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .notice(dto.getNotice())
                .warningLevel(dto.getWarningLevel())
                .build();
    }
}
