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
@Table(name = "Infected")
public class InfectedEntity {   // 감염 정보 entity
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uuid;
    private String judgmentDate;
    private String estimatedDate;
    private String detailSituation;
    @Column(columnDefinition = "boolean default false")
    private boolean managerCheck;
}
