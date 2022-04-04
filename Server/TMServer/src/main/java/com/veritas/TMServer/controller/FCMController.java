package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.FCMDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.model.FCMEntity;
import com.veritas.TMServer.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fcm")
public class FCMController {
    @Autowired
    private FCMService service;

    @GetMapping("/fcmlist")
    public ResponseEntity<?> fcmList(@AuthenticationPrincipal String uuid){
        List<FCMEntity> entities = service.retrieve(uuid);
        List<FCMDTO> dtos = entities.stream().map(FCMDTO::new).collect(Collectors.toList());
        ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().data(dtos).build();
        return ResponseEntity.ok(response);

    }

    @PostMapping("/saveFCM")
    public ResponseEntity<?> createFCM(
            @AuthenticationPrincipal String uuid,
            @RequestBody FCMDTO dto){
        try{
            FCMEntity entity = FCMDTO.fcmEntity(dto);
            entity.setId(null);
            entity.setUuid(uuid);
            List<FCMEntity> entities = service.create(entity);
            List<FCMDTO> dtos = entities.stream().map(FCMDTO::new).collect(Collectors.toList());
            ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deleteFCM")
    public ResponseEntity<?> deleteFCM(
            @AuthenticationPrincipal String uuid,
            @RequestBody FCMDTO dto) {
        try {
            FCMEntity entity = FCMDTO.fcmEntity(dto);
            entity.setUuid(uuid);
            List<FCMEntity> entities = service.delete(entity);
            List<FCMDTO> dtos = entities.stream().map(FCMDTO::new).collect(Collectors.toList());
            ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
