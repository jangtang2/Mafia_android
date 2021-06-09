package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.chat.ChatAdapter;
import com.example.myapplication.chat.G;
import com.example.myapplication.chat.MessageItem;

import java.util.ArrayList;
import java.util.Calendar;

public class MainChatActivity extends AppCompatActivity {

        EditText et;
        ListView listView;

        ArrayList<MessageItem> messageItems=new ArrayList<>();
        ChatAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_chat);

            et=findViewById(R.id.et);
            listView=findViewById(R.id.listview);
            adapter=new ChatAdapter(messageItems,getLayoutInflater());
            listView.setAdapter(adapter);
    }

    public void clickSend(View view){//firebase DB에 저장할 값들(네임, 메세지, 시간)
            String nickName= "nacho";
            String message= et.getText().toString();

            //메세지 작성 시간 문자열로..
            Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
            String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

            //EditText에 있는 글씨 지우기
            et.setText("");

            //새로 추가된 데이터(값 : MessageItem객체) 가져오기
            MessageItem messageItem= new MessageItem();
            messageItem.setMessage(message);
            messageItem.setNickname(nickName);

            //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
            messageItems.add(messageItem);

            //리스트뷰를 갱신
            adapter.notifyDataSetChanged();
            listView.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동

            //소프트키패드를 안보이도록..
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

            //처음 시작할때 EditText가 다른 뷰들보다 우선시 되어 포커스를 받아 버림.
            //즉, 시작부터 소프트 키패드가 올라와 있음.

            //그게 싫으면...다른 뷰가 포커스를 가지도록
            //즉, EditText를 감싼 Layout에게 포커스를 가지도록 속성을 추가!![[XML에]
    };
}