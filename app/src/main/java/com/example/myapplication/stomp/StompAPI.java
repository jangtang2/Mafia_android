package com.example.myapplication.stomp;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.dynamicanimation.animation.SpringAnimation;

import com.example.myapplication.retrofit.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class StompAPI {

    private StompClient mStompClient;

    String BASE_URL = "ws://ec2-13-125-225-233.ap-northeast-2.compute.amazonaws.com:8080/ws/websocket";


    String TAG = "TAG";
    boolean isUnexpectedClosed;

    private List<StompHeader> headerList = new ArrayList<>();
    private List<User> userList;

    private Listener listener;
    private JobListener jobListener;

    @SuppressLint("CheckResult")
    public void initStomp() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BASE_URL);
        mStompClient.connect();

    }

    @SuppressLint("CheckResult")
    public void onConnected(String nickname) {

        mStompClient.topic("/topic/public").subscribe(topicMessage -> {
            Gson gson = new Gson();
            //List<User> list = gson.fromJson(topicMessage.getPayload(), new TypeToken<List<User>>(){}.getType());

            if(listener!=null) {
                listener.onConnected();
            }
        });

        mStompClient.topic("/user/queue/role").subscribe(message -> {
           Log.d("ROLE", message.getPayload());
           if(jobListener!=null) {
               jobListener.onJobReceived();
           }
        });

        JsonObject obj = new JsonObject();
        obj.addProperty("sender", nickname);
        obj.addProperty("type", "JOIN");

        mStompClient.send("/app/chat.addUser", obj.toString()).subscribe();
        mStompClient.send("/app/role", nickname).subscribe();
    }

    public void sendMessage(User user, String msg) {

        JsonObject obj = new JsonObject();
        obj.addProperty("sender", "a");
        obj.addProperty("content", msg);
        obj.addProperty("type", "CHAT");
        mStompClient.send("/app/chat.sendMessage", obj.toString()).subscribe();

    }

    public void onDisconnect() {
        mStompClient.disconnect();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onConnected();
    }

    public interface JobListener {
        void onJobReceived();
    }

}