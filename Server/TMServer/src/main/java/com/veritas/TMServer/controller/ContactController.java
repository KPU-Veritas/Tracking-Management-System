package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ContactDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("contact")
public class ContactController {
    @Autowired
    private ContactService service;
    @GetMapping
    public ResponseEntity<?> retrieveContactList(){
        String temporaryUserId = "temporary-user";
        List<ContactEntity> entities = service.retrieve(temporaryUserId);
        List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());
        ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody ContactDTO dto){
        try {
            String temporaryUserId = "temporary-user";

            ContactEntity entity = ContactDTO.toEntity(dto);
            entity.setId(-1);
            entity.setUuid(temporaryUserId);
            List<ContactEntity> entities = service.create(entity);
            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            String error =e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}

