package com.veritas.TMServer.service;

import com.veritas.TMServer.model.*;
import com.veritas.TMServer.persistence.WebRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class RiskService {
    @Autowired
    private WebRepository webRepository;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    private AndroidPushNotificationService androidPushNotificationService;
    @Autowired
    private FCMService fcmService;

    public void firstCalculation(InfectedEntity infectedEntity) {       //1차 접촉자 판별 함수
        log.info("FirstCaculation Start!");
        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findFirstContactList(infectedEntity.getUuid(), infectedEntity.getEstimatedDate()));
        log.info("here   " + infectedEntity);
        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
        ArrayList<String> duplicate = new ArrayList<String>();
        userService.reset();        //모든 사용자의 위험도를 0으로 되돌려 이전 연산으로 기록된 위험도를 초기화

        if (contactList.isEmpty()) return;

        for (int i = 0; i < contactList.size(); ++i) {      //1차 접촉기록의 수만큼 반복연산

            ContactEntity entity = contactList.get(i);

            if (!duplicate.contains(entity.getContactTargetUuid())) {
                duplicate.add(entity.getContactTargetUuid());
                nextList.add(entity);       //서로다른 접촉이나 중복된 uuid일 경우 최초 접촉기록만을 nextList에 저장
            }
            this.riskCalculation(entity, 1, 100);       //해당 접촉기록에 대한 위험도 연산
        }

        for (int j = 0; j < nextList.size(); ++j) this.continuousCalculation(nextList.get(j), 2);       //nextList에 담긴 접촉 기록으로부터 2차 접촉 연산 수행
        log.info("AllCacluation Done!");
    }

    public void continuousCalculation(ContactEntity contactEntity, int contactDegree) {     //n차 접촉 연산 함수
        log.info("ContinuousCaculation Start! About UUID : " + contactEntity.getContactTargetUuid() + " / ContactDegree : " + contactDegree);
        ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>(contactService.findContinuousContactList(contactEntity.getContactTargetUuid(), contactEntity.getDate(), contactEntity.getFirstTime()));
        ArrayList<ContactEntity> nextList = new ArrayList<ContactEntity>();
        ArrayList<String> duplicate = new ArrayList<String>();

        if (contactList.isEmpty()) return;

        Float superRisk = userService.findRiskByUuid(contactEntity.getContactTargetUuid());

        for (int i = 0; i < contactList.size(); ++i) {      //n차 접촉기록의 수만큼 반복연산
            ContactEntity entity = contactList.get(i);

            if(!duplicate.contains(entity.getContactTargetUuid())) {
                duplicate.add(entity.getContactTargetUuid());
                nextList.add(entity);       //서로다른 접촉이나 중복된 uuid일 경우 최초 접촉기록만을 nextList에 저장
            }
            this.riskCalculation(entity, contactDegree, superRisk);         //해당 접촉기록에 대한 위험도 연산
        }

        for (int j = 0; j < nextList.size(); ++j) {     //해당 함수의 연산이 2차 접촉이라면 3차로 변경후 3차접촉 연산, 3차 접촉이라면 종료
            if (contactDegree == 2) contactDegree = 3;
            else if (contactDegree == 3) break;

            this.continuousCalculation(nextList.get(j), contactDegree);
        }
    }


    public void riskCalculation(ContactEntity entity, int thisContactDegree, float superRisk) {     //위험도 계산 함수
        String uuid = entity.getContactTargetUuid();
        log.info("RiskCaculation Start! About UUID : " + uuid);
        float contactTime = (float) entity.getContactTime() / 300;
        Integer contactDegree = userService.findContactDegreeByUuid(uuid);

        if (contactDegree == null) return;

        float risk = userService.findRiskByUuid(uuid);
        float halfRisk = superRisk * 1 / 2;       //피접촉자는 접촉자의 위험도 50%에서 시작

        if (contactTime > 1) {
            contactTime = 1;
        }       //5분 이상 접촉 시 최대치 위험도 부여

        float calculatedRisk = (halfRisk * contactTime) * 4 / 5 + halfRisk;     //피접촉자는 접촉자의 위험도로부터 최소 50% 최대 90% 까지 부여

        if (contactDegree == 0 || contactDegree > thisContactDegree) {      //접촉차수는 모든 기록 중 가장 확진자로부터 근접한 차수로 기록
            userService.updateContactDegree(uuid, thisContactDegree);
        }

        if (risk < calculatedRisk) userService.updateRisk(uuid, calculatedRisk);

        log.info("RiskCaculation Done!!");
    }


    public void notificate(UserEntity userEntity, String titleMessage, String bodyMessage) {        //FCM 알림을 전송하고, 그 내용을 DB에 저장

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(userEntity, titleMessage, bodyMessage);
        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
        }
        catch (InterruptedException e){
            log.debug("got interrupted!");
        }
        catch (ExecutionException e){
            log.debug("execution error!");
        }

        LocalTime now = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formatedNow = now.format(formatter);

        FCMEntity fcmEntity = FCMEntity.builder()
                .uuid(userEntity.getUuid())
                .date(String.valueOf(LocalDate.now()))
                .time(String.valueOf(now))
                .title(titleMessage)
                .body(bodyMessage)
                .risk(userEntity.getRisk())
                .contactDegree(userEntity.getContactDegree())
                .build();

        fcmService.create(fcmEntity);

    }


}
