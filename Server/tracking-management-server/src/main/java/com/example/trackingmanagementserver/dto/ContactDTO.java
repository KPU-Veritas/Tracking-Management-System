// 접촉 정보 아이템을 생성, 수정, 삭제
package com.example.trackingmanagementserver.dto;

import com.example.trackingmanagementserver.model.ContactEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDTO {
	private int id;
	private String contactTargetUuid;
	private String dateTime;
	
	public ContactDTO(final ContactEntity entity) {
		this.id = entity.getId();
		this.contactTargetUuid = entity.getContactTargetUuid();
		this.dateTime = entity.getDateTime();
	}
	public static ContactEntity toEntity(final ContactDTO dto) {
		return ContactEntity.builder()
				.id(dto.getId())
				.contactTargetUuid(dto.getContactTargetUuid())
				.dateTime(dto.getDateTime())
				.build();
	}
}
