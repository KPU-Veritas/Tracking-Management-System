package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.InfectedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfectedDTO {
    private String id;
    private String uuid;
    private String judgmentDate;
    private String estimatedDate;
    private String detailSituation;

    public InfectedDTO(final InfectedEntity entity){
        this.id = entity.getId();
        this.uuid = entity.getUuid();
        this.judgmentDate = entity.getJudgmentDate();
        this.estimatedDate = entity.getEstimatedDate();
        this.detailSituation = entity.getDetailSituation();
    }

    public static InfectedEntity infectedEntity(final InfectedDTO dto){
        return InfectedEntity.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .judgmentDate(dto.getJudgmentDate())
                .estimatedDate(dto.getEstimatedDate())
                .detailSituation(dto.getDetailSituation())
                .build();
    }
}
