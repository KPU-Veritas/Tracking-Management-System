package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.dto.UserDTO;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {   // 사용자의 정보 컨트롤러
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){    // 회원가입 할 때 사용하는 url
        try {
            // 요청을 이용해 저장할 사용자 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
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
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){    // 로그인 할 때 사용하는 url
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if(user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
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

    @PutMapping("/addFcmToken")
    public ResponseEntity<?> addFcmToekn(@RequestBody UserDTO userDTO){ // 사용자의 fcm token 정보 갱신 요청 시 필요한 url
        userService.updateFcmToken(userDTO.getUuid(), userDTO.getFcmToken());
        String msg = "fcmToken saved";
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(Collections.singletonList(msg)).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getRisk")
    public ResponseEntity<?> getRisk(@AuthenticationPrincipal String uuid){ // 사용자가 자신의 위험도를 확인할 때 사용하는 url
        float risk = userService.findRiskByUuid(uuid);
        ResponseDTO<Float> response = ResponseDTO.<Float>builder().data(Collections.singletonList(risk)).build();
        return ResponseEntity.ok().body(response);
    }

}
////@GetMapping("/fcmlist")
//    public ResponseEntity<?> fcmList(@AuthenticationPrincipal String uuid){ // 사용자가 받은 알림 목록을 요청 할때 사용하는 url
//        List<FCMEntity> entities = service.retrieve(uuid);
//        List<FCMDTO> dtos = entities.stream().map(FCMDTO::new).collect(Collectors.toList());
//        ResponseDTO<FCMDTO> response = ResponseDTO.<FCMDTO>builder().data(dtos).build();
//        return ResponseEntity.ok(response);
//
//    }//