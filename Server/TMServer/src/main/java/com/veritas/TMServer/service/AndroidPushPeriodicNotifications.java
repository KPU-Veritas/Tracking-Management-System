package com.veritas.TMServer.service;

import com.veritas.TMServer.dto.FCMDTO;
import com.veritas.TMServer.model.FCMEntity;
import com.veritas.TMServer.model.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson(UserEntity userEntity, String titleMessage,String bodyMessage) throws JSONException {

        JSONObject body = new JSONObject();

        JSONArray array = new JSONArray();

        array.put(userEntity.getFcmToken());

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();

        notification.put("title", titleMessage);
        notification.put("body", bodyMessage);

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
