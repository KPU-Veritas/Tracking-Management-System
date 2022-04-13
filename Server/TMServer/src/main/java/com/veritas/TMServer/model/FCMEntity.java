package com.veritas.TMServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "FCMTable")
public class FCMEntity {    // FCM 알림 기록 entity
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uuid;
    private String date;
    private String time;
    private String title;
    private String body;
    private float risk;
    private int contactDegree;
}
