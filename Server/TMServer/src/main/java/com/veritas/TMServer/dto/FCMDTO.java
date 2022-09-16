package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.FCMEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FCMDTO { // FCM 알림 기록 DTO
    private Long id;
    private String uuid;
    private String email;
    private String date;
    private String time;
    private String title;
    private String body;
    private float risk;
    private int contactDegree;

    public FCMDTO(final FCMEntity entity){
        this.id = entity.getId();
        this.uuid = entity.getUuid();
        this.date = entity.getDate();
        this.time = entity.getTime();
        this.title = entity.getTitle();
        this.body = entity.getBody();
        this.risk = entity.getRisk();
        this.contactDegree = entity.getContactDegree();
    }

    public static FCMEntity fcmEntity(final FCMDTO dto){
        return FCMEntity.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .date(dto.getDate())
                .time(dto.getTime())
                .title(dto.getTitle())
                .body(dto.getBody())
                .risk(dto.getRisk())
                .contactDegree(dto.getContactDegree())
                .build();
    }
}
