package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.InfectedDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.model.InfectedEntity;
import com.veritas.TMServer.service.InfectedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/infected")
public class InfectedController {   // 감염 정보 컨트롤러
    @Autowired
    private InfectedService service;
    @PostMapping("/addinfected")
    public ResponseEntity<?> createInfected(    // 감염 정보를 등록할 때 사용하는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody InfectedDTO dto){
        try{
            InfectedEntity entity = InfectedDTO.infectedEntity(dto);
            entity.setId(null);
            entity.setUuid(uuid);
            List<InfectedEntity> entities = service.create(entity);
            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deleteinfected")
    public ResponseEntity<?> deleteInfected(    // 감염 정보를 지울 때 사용하는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody InfectedDTO dto
    ){
        try{
            InfectedEntity entity = InfectedDTO.infectedEntity(dto);
            entity.setUuid(uuid);
            List<InfectedEntity> entities = service.delete(entity);
            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
