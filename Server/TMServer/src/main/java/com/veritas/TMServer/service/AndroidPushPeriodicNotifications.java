package com.veritas.TMServer.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson(String fcmToken, float risk) throws JSONException {
        UserService userService = new UserService();

        JSONObject body = new JSONObject();

        JSONArray array = new JSONArray();

        array.put(fcmToken);

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","Warning!");
        notification.put("body","Your risk is"+risk+"%");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
