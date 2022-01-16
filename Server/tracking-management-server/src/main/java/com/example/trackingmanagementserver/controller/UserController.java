package com.example.trackingmanagementserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trackingmanagementserver.dto.ResponseDTO;
import com.example.trackingmanagementserver.dto.UserDTO;
import com.example.trackingmanagementserver.model.UserEntity;
import com.example.trackingmanagementserver.security.TokenProvider;
import com.example.trackingmanagementserver.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		try {
			// ��û�� �̿��� ������ ����� �����
			UserEntity user = UserEntity.builder()
					.email(userDTO.getEmail())
					.username(userDTO.getUsername())
					.password(userDTO.getPassword())
					.phoneNumber(userDTO.getPhoneNumber())
					.simpleAddress(userDTO.getSimpleAddress())
					.detailAddress(userDTO.getDetailAddress())
					.build();
			// ���񽺸� �̿��� �������͸��� ����� ����
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
					.email(registeredUser.getEmail())
					.uuid(registeredUser.getUuid())
					.username(registeredUser.getUsername())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		}catch(Exception e) {
			// ����� ������ �׻� �ϳ��̹Ƿ� ����Ʈ�� ������ �ϴ� ResponseDTO�� ������� �ʰ� �׳� UserDTO ����
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
}
