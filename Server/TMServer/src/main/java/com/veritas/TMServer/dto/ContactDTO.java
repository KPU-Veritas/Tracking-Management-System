package com.veritas.TMServer.dto;

import com.veritas.TMServer.model.ContactEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDTO {
    private String id;
    private String contactTargetUuid;
    private String date;
    private String firstTime;
    private String lastTime;
    private boolean checked;

    public ContactDTO(final ContactEntity entity) {
        this.id = entity.getId();
        this.contactTargetUuid = entity.getContactTargetUuid();
        this.date = entity.getDate();
        this.firstTime = entity.getFirstTime();
        this.lastTime = entity.getLastTime();
        this.checked = isChecked();
    }
    public static ContactEntity toEntity(final ContactDTO dto) {
        return ContactEntity.builder()
                .id(dto.getId())
                .contactTargetUuid(dto.getContactTargetUuid())
                .date(dto.getDate())
                .firstTime(dto.getFirstTime())
                .lastTime(dto.getLastTime())
                .checked(dto.isChecked())
                .build();
    }
}
