package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.*;
import com.veritas.TMServer.model.*;
import com.veritas.TMServer.security.TokenProvider;
import com.veritas.TMServer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


@Slf4j
@RestController
@RequestMapping("/system")
public class WebController {        //웹 전반적인 요청을 처리하는 컨트롤러
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
    private AndroidPushNotificationService androidPushNotificationsService;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private TokenProvider tokenProvider;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody WebDTO webDTO){      //회원가입
        try {
            if(webService.countManager() == 0) {
                // 요청을 이용해 저장할 사용자 만들기
                WebEntity web = WebEntity.builder()
                        .email(webDTO.getEmail())
                        .username(webDTO.getUsername())
                        .password(passwordEncoder.encode(webDTO.getPassword()))
                        .warningLevel(81)
                        .build();
                // 서비스를 이용해 리포지터리에 사용자 저장
                WebEntity registeredWeb = webService.create(web);
                WebDTO responseWebDTO = WebDTO.builder()
                        .email(registeredWeb.getEmail())
                        .id(registeredWeb.getId())
                        .username(registeredWeb.getUsername())
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
    public ResponseEntity<?> authenticate(@RequestBody WebDTO webDTO) {     //로그인
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
    public ResponseEntity<?> addDevice(@RequestBody DeviceDTO deviceDTO) {      //장치추가
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
    public void deleteDevice(@RequestBody DeviceDTO deviceDTO) {        //장치삭제
        String id = deviceDTO.getId();
        deviceService.delete(id);
    }


    @PostMapping("/searchuser")
    public ResponseEntity<?> searchUser(@RequestBody String search) {     //회원 검색

        search = search.replaceAll("\"", "");

        try {
            List<UserEntity> entities = userService.searchList(search);
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
    public ResponseEntity<?> searchContact(@RequestBody SearchDTO searchDTO) {      //날짜와 UUID로 접촉기록 검색
        String uuid = searchDTO.getUuid();
        String date = searchDTO.getDate();
        String date2 = searchDTO.getDate2();

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
    public ResponseEntity<?> setLevel(@RequestBody WebDTO webDTO) {     //자동알림을 보낼 위험도 설정

        try {
            int warningLevel = webDTO.getWarningLevel();
            if( warningLevel != 0 ) webService.setLevel(warningLevel);

            return ResponseEntity.ok().body(0);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<WebDTO> response = ResponseDTO.<WebDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/addinfected")
    public ResponseEntity<?> createInfected(@RequestBody InfectedDTO dto){      //확진자 추가
        try{
            log.info(String.valueOf(dto));
            InfectedEntity entity = InfectedDTO.infectedEntity(dto);
            entity.setId(null);
            entity.setManagerCheck(true);
            List<InfectedEntity> entities = infectedService.create(entity);
            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();

            riskService.firstCalculation(entity);      //확진자에 대한 접촉 연산

            userService.updateRisk(dto.getUuid(), 0);

            long risk = webService.getLevel();
            List<UserEntity> riskOverList = userService.findOverRisk(risk);

            UserEntity userEntity = userService.findByUuid(dto.getUuid());
            if(riskOverList.indexOf(userEntity) >= 0) riskOverList.remove(riskOverList.indexOf(userEntity));

            for(int i = 0; i < riskOverList.size(); i++) {
                riskService.notificate(riskOverList.get(i), "You are the " + riskOverList.get(i).getContactDegree() + " contact with COVID-19.",
                        "Your risk is " + riskOverList.get(i).getRisk() + "%.");
            }       //설정된 위험도를 초과한 접촉자들에게 알림을 보냄

            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/notificateindividual")
    public void notificateIndividual(@RequestBody UserDTO userDTO) {        //개인 알림 송신

        UserEntity userEntity = userService.findByUuid(userDTO.getUuid());
        riskService.notificate(userEntity, "You are in close COVID19 with a coronavirus patient.", "Your risk is " + userEntity.getRisk() + "%.");

    }

    @PostMapping("/userlist")
    public ResponseEntity<?> userList(@RequestBody int page){        //회원목록

        try {
            List<UserEntity> entities = userService.userList(page-1, 10);

            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping("/contactlist")
    public ResponseEntity<?> contactList(@RequestBody int page) {        //접촉목록
        try {
            List<ContactEntity> entities = contactService.contactList(page-1, 10);

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

    @GetMapping("/resetrisk")
    public int contactList() {
        userService.reset();
        return 1;
    }

    @GetMapping("/notice")
    public long count() {       //관리자가 확인하지 않은 확진자의 수
        try {
            return infectedService.managerNotice();
        } catch(Exception e) {
            return -1;
        }
    }

    @GetMapping("/noticelist")
    public ResponseEntity<?> noticeList() {     //확진자목록
        try {
            List<InfectedEntity> entities = infectedService.infectedList();

            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());

            Collections.reverse(dtos);

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
    public ResponseEntity<?> deviceList() {     //장치목록
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
    public int getLevel() {     //현재 설정된 위험도를 리턴
        try {
            return webService.getLevel();
        } catch(Exception e) {
            return -1;
        }
    }

    @GetMapping("/totaluser")       //사용자가 총 몇 명인지 리턴, 페이지네이션에 사용됨
    public int totalUser() {
        try {
            return Math.toIntExact(userService.count());
        } catch(Exception e) {
            return 0;
        }
    }

    @GetMapping("/totalcontact")        //접촉 수가 총 몇 개인지 리턴, 페이지네이션에 사용됨
    public int totalContact() {
        try {
            return Math.toIntExact(contactService.count());
        } catch(Exception e) {
            return 0;
        }
    }

    @PutMapping("/check")
    public ResponseEntity<?> check(@RequestBody InfectedDTO infectedDTO) {      //신규 확진자에 대한 확인
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

    @PutMapping("/modifyuser")      //사용자 정보 수정
    public ResponseEntity<Integer> contactList(@RequestBody UserDTO userDTO) {
        try {
            String uuid = userDTO.getUuid();
            log.info(String.valueOf(userDTO));
            UserEntity userEntity = userService.findByUuid(uuid);
            if(!userEntity.getEmail().equals(userDTO.getEmail())) {
                userService.updateEmail(uuid, userDTO.getEmail());
            }
            if(!("".equals(userDTO.getPassword()) || userDTO.getPassword().equals(null))) {
                userService.updatePassWord(uuid, passwordEncoder.encode(userDTO.getPassword()));
            }
            if(!userEntity.getUsername().equals(userDTO.getUsername())) {
                userService.updateUserName(uuid, userDTO.getUsername());
            }
            if(!userEntity.getSimpleAddress().equals(userDTO.getSimpleAddress())) {
                userService.updateSimpleAddress(uuid, userDTO.getSimpleAddress());
            }
            if(!userEntity.getDetailAddress().equals(userDTO.getDetailAddress())) {
                userService.updateDetailAddress(uuid, userDTO.getDetailAddress());
            }
            if(!userEntity.getPhoneNumber().equals(userDTO.getPhoneNumber())) {
                userService.updatePhoneNumber(uuid, userDTO.getPhoneNumber());
            }

            return ResponseEntity.ok().body(1);
        }  catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ContactDTO> response = ResponseDTO.<ContactDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(1);
        }
    }

//    public void firstCalculation(InfectedEntity infectedEntity) {       //1차 접촉자 판별 함수
//        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findFirstContactList(infectedEntity.getUuid(), infectedEntity.getEstimatedDate()));
//        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
//        ArrayList<String> duplicate = new ArrayList<String>();
//        userService.reset();        //모든 사용자의 위험도를 0으로 되돌려 이전 연산으로 기록된 위험도를 초기화
//
//        if (contactList.isEmpty()) return;
//
//        for (int i = 0; i < contactList.size(); ++i) {      //1차 접촉기록의 수만큼 반복연산
//
//            ContactEntity entity = contactList.get(i);
//
//            if (!duplicate.contains(entity.getContactTargetUuid())) {
//                duplicate.add(entity.getContactTargetUuid());
//                nextList.add(entity);       //서로다른 접촉이나 중복된 uuid일 경우 최초 접촉기록만을 nextList에 저장
//            }
//            this.riskCalculation(entity, 1, 100);       //해당 접촉기록에 대한 위험도 연산
//        }
//
//        for (int j = 0; j < nextList.size(); ++j) this.continuousCalculation(nextList.get(j), 2);       //nextList에 담긴 접촉 기록으로부터 2차 접촉 연산 수행
//    }
//
//    public void continuousCalculation(ContactEntity contactEntity, int contactDegree) {     //n차 접촉 연산 함수
//
//        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findContinuousContactList(contactEntity.getContactTargetUuid(), contactEntity.getDate(), contactEntity.getFirstTime()));
//        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
//        ArrayList<String> duplicate = new ArrayList<String>();
//
//        if (contactList.isEmpty()) return;
//
//        Float superRisk = userService.findRiskByUuid(contactEntity.getContactTargetUuid());
//
//        for (int i = 0; i < contactList.size(); ++i) {      //n차 접촉기록의 수만큼 반복연산
//            ContactEntity entity = contactList.get(i);
//
//            if(!duplicate.contains(entity.getContactTargetUuid())) {
//                duplicate.add(entity.getContactTargetUuid());
//                nextList.add(entity);       //서로다른 접촉이나 중복된 uuid일 경우 최초 접촉기록만을 nextList에 저장
//            }
//            this.riskCalculation(entity, contactDegree, superRisk);         //해당 접촉기록에 대한 위험도 연산
//        }
//
//        for (int j = 0; j < nextList.size(); ++j) {     //해당 함수의 연산이 2차 접촉이라면 3차로 변경후 3차접촉 연산, 3차 접촉이라면 종료
//            if (contactDegree == 2) contactDegree = 3;
//            else if (contactDegree == 3) break;
//
//            this.continuousCalculation(nextList.get(j), contactDegree);
//        }
//    }
//
//
//    public void riskCalculation(ContactEntity entity, int thisContactDegree, float superRisk) {     //위험도 계산 함수
//
//        String uuid = entity.getContactTargetUuid();
//        float contactTime = (float) entity.getContactTime() / 300;
//        Integer contactDegree = userService.findContactDegreeByUuid(uuid);
//
//        if (contactDegree == null) return;
//
//        float risk = userService.findRiskByUuid(uuid);
//        float halfRisk = superRisk * 1 / 2;       //피접촉자는 접촉자의 위험도 50%에서 시작
//
//        if (contactTime > 1) {
//            contactTime = 1;
//        }       //5분 이상 접촉 시 최대치 위험도 부여
//
//        float calculatedRisk = (halfRisk * contactTime) * 4 / 5 + halfRisk;     //피접촉자는 접촉자의 위험도로부터 최소 50% 최대 90% 까지 부여
//
//        if (contactDegree == 0 || contactDegree > thisContactDegree) {      //접촉차수는 모든 기록 중 가장 확진자로부터 근접한 차수로 기록
//            userService.updateContactDegree(uuid, thisContactDegree);
//        }
//
//        if (risk < calculatedRisk) userService.updateRisk(uuid, calculatedRisk);
//
//
//    }
//
//
//    public void notificate(UserEntity userEntity, String titleMessage, String bodyMessage) {        //FCM 알림을 전송하고, 그 내용을 DB에 저장
//
//        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(userEntity, titleMessage, bodyMessage);
//        HttpEntity<String> request = new HttpEntity<>(notifications);
//
//        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
//        CompletableFuture.allOf(pushNotification).join();
//
//        try{
//            String firebaseResponse = pushNotification.get();
//        }
//        catch (InterruptedException e){
//            log.debug("got interrupted!");
//        }
//        catch (ExecutionException e){
//            log.debug("execution error!");
//        }
//
//        LocalTime now = LocalTime.now();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        String formatedNow = now.format(formatter);
//
//        FCMEntity fcmEntity = new FCMEntity();
//
//        fcmEntity.setUuid(userEntity.getUuid());
//        fcmEntity.setDate(String.valueOf(LocalDate.now()));
//        fcmEntity.setTime(String.valueOf(now));
//        fcmEntity.setTitle(titleMessage);
//        fcmEntity.setBody(bodyMessage);
//        fcmEntity.setRisk(userEntity.getRisk());
//        fcmEntity.setContactDegree(userEntity.getContactDegree());
//        fcmService.create(fcmEntity);
//
//
//    }


}
