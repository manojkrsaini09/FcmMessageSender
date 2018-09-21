package com.metacube.sageclarity.FirebaseTest;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@RestController
public class MainController {

    public static final String AUTH_KEY_FCM = "";
    public static final String API_URL_FCM = "";

    @Autowired
    private NotificationService notificationService;

   /* @PostConstruct
    public void initFireBaseApp(){
        FileInputStream refreshToken = null;
        try {
            refreshToken = new FileInputStream("path/to/refreshToken.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);

    }*/

    public void sendNotification(){
        String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = null;
        try {
            url = new URL(FMCurl);

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+authKey);
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject json = new JSONObject();
            json.put("to","/topics/all");
            JSONObject data = new JSONObject();
            data.put("message","");
            json.put("data", data);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/")
    public String welcomeApi(){
        return "Welcome";
    }


    @GetMapping("/testApi")
    public String testApi(@RequestParam(value = "deviceId", required = true) String deviceId,
                          @RequestParam(value = "currentCondition", required = true) String currentCondition){
        try {
            notificationService.sendCommonMessage(deviceId,currentCondition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Test";
    }
}
