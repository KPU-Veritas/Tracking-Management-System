package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.DeviceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private String id;
    private String place;

    public DeviceDTO(final DeviceEntity entity) {
        this.id = entity.getId();
        this.place = entity.getPlace();
    }

    public static DeviceEntity deviceEntity(final DeviceDTO dto) {
        return DeviceEntity.builder()
                .id(dto.getId())
                .place(dto.getPlace())
                .build();
    }
}
