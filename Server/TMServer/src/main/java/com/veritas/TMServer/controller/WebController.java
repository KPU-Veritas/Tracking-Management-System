package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.*;
import com.veritas.TMServer.model.*;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private InfectedService infectedService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TokenProvider tokenProvider;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody WebDTO webDTO){
        try {
            if(webService.countManager() == 0) {
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
                return ResponseEntity.ok().body(responseWebDTO);
            }
            else {
                ResponseDTO responseDTO = ResponseDTO.builder().error("Already exists!").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }catch(Exception e) {
            // 사용자 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
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

    @PostMapping("/adddevice")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDTO deviceDTO) {
        try {
            DeviceEntity device = DeviceEntity.builder()
                    .place(deviceDTO.getPlace())
                    .build();

            DeviceEntity registeredDevice = deviceService.create(device);
            DeviceDTO response = DeviceDTO.builder()
                    .id(registeredDevice.getId())
                    .place(registeredDevice.getPlace())
                    .build();

            return ResponseEntity.ok().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DeviceDTO> response = ResponseDTO.<DeviceDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/deletedevice")
    public void deleteDevice(@RequestBody DeviceDTO deviceDTO) {
        String id = deviceDTO.getId();
        log.info(id);
        deviceService.delete(id);
    }


    @PostMapping("/searchuser")
    public ResponseEntity<?> searchUser(@RequestBody String name) {
        name = name.replaceAll("\"", "");
        try {
            List<UserEntity> entities = userService.searchList(name);

            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping("/searchcontact")
    public ResponseEntity<?> searchContact(@RequestBody SearchDTO searchDTO) {
        String uuid = searchDTO.getUuid();
        String date = searchDTO.getDate();
        String date2 = searchDTO.getDate2();
        log.info(uuid);
        log.info(date);
        log.info(date2);

        try {
            List<ContactEntity> entities = contactService.searchList(uuid,date,date2);

            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());

            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping("/setlevel")
    public ResponseEntity<?> setLevel(@RequestBody WebDTO webDTO) {

        try {
            int warningLevel = webDTO.getWarningLevel();
            log.info(String.valueOf(warningLevel));
            if( warningLevel != 0 ) webService.setLevel(warningLevel);

            return ResponseEntity.ok().body(0);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<WebDTO> response = ResponseDTO.<WebDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/userlist")
    public ResponseEntity<?> userList(){

        try {
            List<UserEntity> entities = userService.userList();

            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

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
            List<ContactEntity> entities = contactService.contactList();

            List<ContactDTO> dtos = entities.stream().map(ContactDTO::new).collect(Collectors.toList());

            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().data(dtos).build();

            if (response != null) return ResponseEntity.ok().body(response);
            else return ResponseEntity.badRequest().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/notice")
    public long count() {
        try {
            return infectedService.managerNotice();
        } catch(Exception e) {
            return -1;
        }
    }

    @GetMapping("/noticelist")
    public ResponseEntity<?> noticeList() {
        try {
            List<InfectedEntity> entities = infectedService.infectedList();

            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());

            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();

            if (response != null) return ResponseEntity.ok().body(response);
            else return ResponseEntity.badRequest().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/devicelist")
    public ResponseEntity<?> deviceList() {
        try {
            List<DeviceEntity> entities = deviceService.deviceList();

            List<DeviceDTO> dtos = entities.stream().map(DeviceDTO::new).collect(Collectors.toList());

            ResponseDTO<DeviceDTO> response = ResponseDTO.<DeviceDTO>builder().data(dtos).build();

            if (response != null) return ResponseEntity.ok().body(response);
            else return ResponseEntity.badRequest().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }



    @GetMapping("/getlevel")
    public int getLevel() {
        try {
            return webService.getLevel();
        } catch(Exception e) {
            return -1;
        }
    }

    @PutMapping("/check")
    public ResponseEntity<?> check(@RequestBody InfectedDTO infectedDTO) {
        try {
            Long id = infectedDTO.getId();
            boolean managerCheck = infectedDTO.isManagerCheck();
            infectedService.updateCheck(id.toString(), managerCheck);
            return null;
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    public void test() {

        List<InfectedEntity> lst = infectedService.retrieve("001");
        this.firstCalculation(lst.get(0));

    }

    public void firstCalculation(InfectedEntity infectedEntity) {
        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findFirstContactList(infectedEntity.getUuid(), infectedEntity.getEstimatedDate()));
        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
        ArrayList<String> duplicate = new ArrayList<String>();

        for (int i = 0; i < contactList.size(); ++i) {
            ContactEntity entity = contactList.get(i);
            if(!duplicate.contains(entity.getContactTargetUuid())) {
                duplicate.add(entity.getContactTargetUuid());
                nextList.add(entity);
            }
            this.riskCalculation(entity, 1, 100);
            log.info("do risk uuid : " + entity.getContactTargetUuid() + "  contactDegree :" + 1);
        }

        for (int j = 0; j < nextList.size(); ++j) this.continuousCalculation(nextList.get(j), 2);
    }

    public void continuousCalculation(ContactEntity contactEntity, int contactDegree) {
        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findContinuousContactList(contactEntity.getContactTargetUuid(), contactEntity.getDate(), contactEntity.getFirstTime()));
        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
        ArrayList<String> duplicate = new ArrayList<String>();

        float superRisk = userService.findRiskByUuid(contactEntity.getContactTargetUuid());

        for (int i = 0; i < contactList.size(); ++i) {
            ContactEntity entity = contactList.get(i);
            if(!duplicate.contains(entity.getContactTargetUuid())) {
                duplicate.add(entity.getContactTargetUuid());
                nextList.add(entity);
            }
            this.riskCalculation(entity, contactDegree, superRisk);
            log.info("do risk uuid : " + entity.getContactTargetUuid() + "  contactDegree :" + contactDegree);
        }

        for (int j = 0; j < nextList.size(); ++j) {
            if (contactDegree == 2) contactDegree = 3;
            else if (contactDegree == 3) break;

            this.continuousCalculation(nextList.get(j), contactDegree);
        }
    }


    public void riskCalculation(ContactEntity entity, int thisContactDegree, float superRisk) {
        String uuid = entity.getContactTargetUuid();
        int contactDegree  = userService.findByUuid(uuid).getContactDegree();
        float risk = userService.findRiskByUuid(uuid);
        float calculatedRisk = superRisk * 1/2;

        if(contactDegree == 0 ||  contactDegree > thisContactDegree) {
            userService.updateContactDegree(uuid, thisContactDegree);
        }

        if(risk < calculatedRisk) userService.updateRisk(uuid, calculatedRisk);

    }

}
