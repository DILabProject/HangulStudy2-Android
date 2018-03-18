package kr.ac.skuniv.di.hangulstudy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.http.CheckLogin;
import kr.ac.skuniv.di.hangulstudy.sharedmemory.SharedMemory;

public class MainActivity extends FragmentActivity {
    private String result;
    private String savedId;
    private String savedPassword;
    Intent studylist;
    static boolean isStart;
    SharedMemory sharedMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sharedMemory = SharedMemory.getinstance();
        studylist = new Intent(this,LoginActivity.class);

        isStart = false;
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isStart==false){
                    isStart=true;

//                    //아이디가 저장되어 있다면 받아옴
//                    SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
//                    savedId = loginInfo.getString("id","none");
//                    savedPassword = loginInfo.getString("password","none");
//
//                    //자동 로그인 부분
//                    CheckLogin checkLogin = new CheckLogin(savedId, savedPassword);
//                    try {
//                        result = checkLogin.execute().get();
//                        Log.d("connect",result);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(result.equals("1")){
//                        Intent intent = new Intent(MainActivity.this, StudyListActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else if(result.equals("0")){
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }

                    startActivity(studylist);
                  finish();
                }
            }
        }, 3000);



    }
}
