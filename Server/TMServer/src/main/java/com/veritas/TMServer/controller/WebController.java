package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.ContactDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.dto.UserDTO;
import com.veritas.TMServer.dto.WebDTO;
import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.model.WebEntity;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.ContactService;
import com.veritas.TMServer.service.UserService;
import com.veritas.TMServer.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system")
public class WebController {
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private WebService webService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody WebDTO webDTO){
    log.info("1번구간");
        try {
            // 요청을 이용해 저장할 사용자 만들기
            WebEntity web = WebEntity.builder()
                    .email(webDTO.getEmail())
                    .username(webDTO.getUsername())
                    .password(passwordEncoder.encode(webDTO.getPassword()))
                    .notice(webDTO.getNotice())
                    .warningLevel(webDTO.getWarningLevel())
                    .build();
            // 서비스를 이용해 리포지터리에 사용자 저장
            WebEntity registeredWeb = webService.create(web);
            WebDTO responseWebDTO = WebDTO.builder()
                    .email(registeredWeb.getEmail())
                    .id(registeredWeb.getId())
                    .username(registeredWeb.getUsername())
                    .notice(0)
                    .warningLevel(0)
                    .build();
            log.info("2번구간");
            return ResponseEntity.ok().body(responseWebDTO);
        }catch(Exception e) {
            // 사용자 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            log.info("3번구간");
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody WebDTO webDTO) {
        WebEntity web = webService.getByCredentials(webDTO.getEmail(), webDTO.getPassword(), passwordEncoder);

        if (web != null) {
            final String token = tokenProvider.createWebToken(web);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(web.getUsername())
                    .uuid(web.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
