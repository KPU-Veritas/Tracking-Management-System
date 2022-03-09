package com.veritas.TMServer.controller;

import com.veritas.TMServer.service.AndroidPushNotificationService;
import com.veritas.TMServer.service.AndroidPushPeriodicNotifications;
import com.veritas.TMServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/fcm")
public class NotificationController {
    @Autowired
    AndroidPushNotificationService androidPushNotificationsService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/send")
    public @ResponseBody ResponseEntity<String> send() throws JSONException, InterruptedException{
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(userService.findFcmTokenList());

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        }
        catch (InterruptedException e){
            log.debug("got interrupted!");
            throw new InterruptedException();
        }
        catch (ExecutionException e){
            log.debug("execution error!");
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}