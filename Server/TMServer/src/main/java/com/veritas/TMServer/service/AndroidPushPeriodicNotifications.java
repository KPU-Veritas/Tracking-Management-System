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
    public static String PeriodicNotificationJson(List<String> fcmTokenList) throws JSONException {
        UserService userService = new UserService();

        LocalDate localDate = LocalDate.now();

                //String sampleData[] = {"cVezOzGaRYekOSZIy64peg:APA91bGSr_vEcAKD077V2WRrZLHrYrLWBoxagk0FCSTPFdHh7u2cm678Kxg9sqNTjKLXGTG5ErJ5z662jpE8ILwQOxE3h287yTe2EuJqC2OygclIyyBoh0cKmll1V8L0ocaVZzwYECHG"};

        JSONObject body = new JSONObject();


        JSONArray array = new JSONArray();

        for(int i=0; i<fcmTokenList.size(); i++) {
            array.put(fcmTokenList.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","hello!");
        notification.put("body","Today is "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
