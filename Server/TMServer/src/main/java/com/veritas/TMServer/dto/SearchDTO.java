package com.veritas.TMServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchDTO {
    private Long id;
    private String uuid;
    private String contactTargetUuid;
    private String date;
    private String date2;
    private String firstTime;
    private String lastTime;
    private String username;
}
