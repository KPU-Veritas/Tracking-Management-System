package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.dto.UserDTO;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")     //CORS 규정 위반 에러 수정
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try {
            // 요청을 이용해 저장할 사용자 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .simpleAddress(userDTO.getSimpleAddress())
                    .detailAddress(userDTO.getDetailAddress())
                    .build();
            // 서비스를 이용해 리포지터리에 사용자 저장
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .uuid(registeredUser.getUuid())
                    .username(registeredUser.getUsername())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e) {
            // 사용자 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());

        if(user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .uuid(user.getUuid())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }


    }

    @GetMapping("/userlist")
    public ResponseEntity<?> userList(/*@RequestBody UserDTO userDTO*/){
        //UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());

        // (1) 서비스 메서드의 userList메서드를 사용해 모든 유저 리스트 가져옴.
        List<UserEntity> entities = userService.userList();

        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 UserDTO리스트로 변환한다.
        List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

        // (3) 변환된 UserDTO리스트를 이용해ResponseDTO를 초기화한다.
        ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

        // (4) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);

    }
}
