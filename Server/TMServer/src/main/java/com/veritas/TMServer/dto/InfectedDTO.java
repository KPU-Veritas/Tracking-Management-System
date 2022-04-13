package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.InfectedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfectedDTO {  // 감염 정보 DTO
    private Long id;
    private String uuid;
    private String judgmentDate;
    private String estimatedDate;
    private String detailSituation;
    private boolean managerCheck;

    public InfectedDTO(final InfectedEntity entity){
        this.id = entity.getId();
        this.uuid = entity.getUuid();
        this.judgmentDate = entity.getJudgmentDate();
        this.estimatedDate = entity.getEstimatedDate();
        this.detailSituation = entity.getDetailSituation();
        this.managerCheck = entity.isManagerCheck();
    }

    public static InfectedEntity infectedEntity(final InfectedDTO dto){
        return InfectedEntity.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .judgmentDate(dto.getJudgmentDate())
                .estimatedDate(dto.getEstimatedDate())
                .detailSituation(dto.getDetailSituation())
                .managerCheck(dto.isManagerCheck())
                .build();
    }
}
