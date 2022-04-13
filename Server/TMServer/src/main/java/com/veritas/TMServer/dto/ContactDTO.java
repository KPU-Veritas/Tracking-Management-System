package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.ContactEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDTO {   // 접촉 기록 DTO
    private Long id;
    private String uuid;
    private String contactTargetUuid;
    private String date;
    private String firstTime;
    private String lastTime;
    private int contactTime;
    private boolean checked;

    public ContactDTO(final ContactEntity entity) {
        this.id = entity.getId();
        this.uuid = entity.getUuid();
        this.contactTargetUuid = entity.getContactTargetUuid();
        this.date = entity.getDate();
        this.firstTime = entity.getFirstTime();
        this.lastTime = entity.getLastTime();
        this.contactTime = entity.getContactTime();
        this.checked = isChecked();
    }
    public static ContactEntity contactEntity(final ContactDTO dto) {
        return ContactEntity.builder()
                .id(dto.getId())
                .contactTargetUuid(dto.getContactTargetUuid())
                .date(dto.getDate())
                .firstTime(dto.getFirstTime())
                .lastTime(dto.getLastTime())
                .contactTime(dto.getContactTime())
                .checked(dto.isChecked())
                .build();
    }
}
