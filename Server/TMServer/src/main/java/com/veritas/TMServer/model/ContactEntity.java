package com.veritas.TMServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Contact")
public class ContactEntity {    // 접촉 정보 Entity
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uuid;
    private String contactTargetUuid;
    private String date;
    private String firstTime;
    private String lastTime;
    @Builder.Default
    @Column(name = "contactTime")
    private int contactTime = 0;
    @Column(columnDefinition = "boolean default false")
    private boolean checked;
}
