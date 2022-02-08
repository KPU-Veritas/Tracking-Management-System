package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ContactDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.dto.UserDTO;
import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.ContactService;
import com.veritas.TMServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system")
//@CrossOrigin(origins = "http://localhost:3000")     //CORS 규정 위반 에러 수정
public class ManagerController {
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/userlist")
    public ResponseEntity<?> userList(){

        try {
            // (1) 서비스 메서드의 userList메서드를 사용해 모든 유저 리스트 가져옴.
            List<UserEntity> entities = userService.userList();

            // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 UserDTO리스트로 변환한다.
            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            // (3) 변환된 UserDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

            // (4) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/contactlist")
    public ResponseEntity<?> contactList() {
        try {
            // (1) 서비스 메서드의 contactList메서드를 사용해 모든 유저 리스트 가져옴.
            List<ContactEntity> entities = contactService.contactList();

            // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 ContactDTO리스트로 변환한다.
            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());

            // (3) 변환된 ContactDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();

            // (4) ResponseDTO를 리턴한다.
            if (response != null) return ResponseEntity.ok().body(response);
            else return ResponseEntity.badRequest().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
