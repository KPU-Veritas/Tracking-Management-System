package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ContactDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contact")
public class ContactController {    // 접촉기록 컨트롤러
    @Autowired
    private ContactService service;

    @PostMapping("/recordcontact")
    public ResponseEntity<?> createContact( // 접촉 기록을 요청 받는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody ContactDTO dto){
        try {
            ContactEntity entity = ContactDTO.contactEntity(dto);
            entity.setId(null);
            entity.setUuid(uuid);
            List<ContactEntity> entities = service.create(entity);
            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deletecontact")
    public ResponseEntity<?> deleteContact( // 접촉 기록을 지우는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody ContactDTO dto
    ){
        try{
            ContactEntity entity = ContactDTO.contactEntity(dto);
            entity.setUuid(uuid);
            List<ContactEntity> entities = service.delete(entity);
            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}

