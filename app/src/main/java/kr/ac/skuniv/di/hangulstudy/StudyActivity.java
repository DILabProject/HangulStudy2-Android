package kr.ac.skuniv.di.hangulstudy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedList;

import kr.ac.skuniv.di.hangulstudy.http.FinishWord;
import kr.ac.skuniv.di.hangulstudy.sharedmemory.SharedMemory;

/**
 * Created by namgiwon on 2018. 1. 31..
 */

public class StudyActivity extends FragmentActivity{
    SharedMemory sharedMemory;
    RelativeLayout hangulja;
    HangulFragment hangul;
    String word ;
    int day;
    String isFinish;
    int wordindex; // 글자 인덱스 (프래그먼트로 부터 index를 받아서 저장)
    private DrawLine drawLine = null; // 선그리기 뷰 객체
    Button reset;
    Button back;
    Button previous;
    Button next;
    String hangulinfo;
    String id;

    int now=0;
    TextView preview;
    TextView pullstring;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    JsonArray jsonarr;
    JsonArray jsonarr1;
    JsonArray jsonarr2;
    JsonObject jsonobj;
    JsonObject jsonobj1;
    JsonObject jsonobj2;
    JsonArray strokearr;
    JsonObject fullInfo = new JsonObject();
    JsonObject strokeObj = new JsonObject();
    JsonObject strokeObj1 = new JsonObject();
//    String stroke = "2,1,1,2";
//    String stroke1 = "1,2,1,1";

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_study);
        word = getIntent().getStringExtra("word");
        day = getIntent().getIntExtra("day",0);
        sharedMemory = SharedMemory.getinstance();
        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        id = loginInfo.getString("id","none");



        preview = (TextView) findViewById(R.id.study_preview);
        pullstring = (TextView) findViewById(R.id.study_pullstring);
        pullstring.setText(word);
        hangulja = (RelativeLayout) findViewById(R.id.main_hangulja);


        hangulinfo = getIntent().getStringExtra("hangulinfo");

        //"ㅇ" TEST
//        hangulinfo = "{\"word\":[[{\"x1\":\"500\",\"x2\":\"200\",\"y1\":\"300\",\"y2\":\"300\"},{\"x1\":\"200\",\"x2\":\"200\",\"y1\":\"300\",\"y2\":\"600\"},{\"x1\":\"200\",\"x2\":\"500\",\"y1\":\"600\",\"y2\":\"600\"},{\"x1\":\"500\",\"x2\":\"500\",\"y1\":\"600\",\"y2\":\"300\"},{\"x1\":\"700\",\"x2\":\"700\",\"y1\":\"200\",\"y2\":\"800\"}],[{\"x1\":\"200\",\"x2\":\"200\",\"y1\":\"300\",\"y2\":\"600\"},{\"x1\":\"200\",\"x2\":\"500\",\"y1\":\"300\",\"y2\":\"300\"},{\"x1\":\"500\",\"x2\":\"500\",\"y1\":\"300\",\"y2\":\"600\"},{\"x1\":\"200\",\"x2\":\"500\",\"y1\":\"600\",\"y2\":\"600\"},{\"x1\":\"700\",\"x2\":\"700\",\"y1\":\"200\",\"y2\":\"800\"},{\"x1\":\"700\",\"x2\":\"800\",\"y1\":\"400\",\"y2\":\"400\"}]],\"stroke\":[[{\"letter\":\"ㅇ\",\"strokeNum\":4},{\"letter\":\"ㅣ\",\"strokeNum\":1}],[{\"letter\":\"ㅁ\",\"strokeNum\":1},{\"letter\":\"ㅁ\",\"strokeNum\":2},{\"letter\":\"ㅁ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1}]]}";

        //이마
//        hangulinfo = "{\"word\":[[{\"x1\":\"800\",\"x2\":\"300\",\"y1\":\"500\",\"y2\":\"500\"},{\"x1\":\"300\",\"x2\":\"300\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"300\",\"x2\":\"800\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"1000\",\"y2\":\"500\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"}],[{\"x1\":\"300\",\"x2\":\"300\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"300\",\"x2\":\"800\",\"y1\":\"500\",\"y2\":\"500\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"300\",\"x2\":\"800\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"},{\"x1\":\"1200\",\"x2\":\"1400\",\"y1\":\"700\",\"y2\":\"700\"}]],\"stroke\":[[{\"letter\":\"ㅇ\",\"strokeNum\":4},{\"letter\":\"ㅣ\",\"strokeNum\":1}],[{\"letter\":\"ㅁ\",\"strokeNum\":1},{\"letter\":\"ㅁ\",\"strokeNum\":2},{\"letter\":\"ㅁ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1}]]}";

        //우비
//        hangulinfo = "{\"word\":[[{\"x1\":\"1100\",\"x2\":\"500\",\"y1\":\"300\",\"y2\":\"300\"},{\"x1\":\"500\",\"x2\":\"500\",\"y1\":\"300\",\"y2\":\"700\"},{\"x1\":\"500\",\"x2\":\"1100\",\"y1\":\"700\",\"y2\":\"700\"},{\"x1\":\"1100\",\"x2\":\"1100\",\"y1\":\"700\",\"y2\":\"300\"},{\"x1\":\"300\",\"x2\":\"1300\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"1000\",\"y2\":\"1400\"}],[{\"x1\":\"300\",\"x2\":\"300\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"300\",\"x2\":\"800\",\"y1\":\"700\",\"y2\":\"700\"},{\"x1\":\"300\",\"x2\":\"800\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"}]],\"stroke\":[[{\"letter\":\"ㅇ\",\"strokeNum\":4},{\"letter\":\"ㅜ\",\"strokeNum\":1},{\"letter\":\"ㅜ\",\"strokeNum\":1}],[{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅣ\",\"strokeNum\":1}]]}";

        //당구
//        hangulinfo = "{\"word\":[[{\"x1\":\"400\",\"x2\":\"800\",\"y1\":\"400\",\"y2\":\"400\"},{\"x1\":\"400\",\"x2\":\"400\",\"y1\":\"400\",\"y2\":\"700\"},{\"x1\":\"400\",\"x2\":\"800\",\"y1\":\"700\",\"y2\":\"700\"},{\"x1\":\"1100\",\"x2\":\"1100\",\"y1\":\"300\",\"y2\":\"800\"},{\"x1\":\"1100\",\"x2\":\"1300\",\"y1\":\"500\",\"y2\":\"500\"},{\"x1\":\"1000\",\"x2\":\"600\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"600\",\"x2\":\"600\",\"y1\":\"1000\",\"y2\":\"1300\"},{\"x1\":\"600\",\"x2\":\"1000\",\"y1\":\"1300\",\"y2\":\"1300\"},{\"x1\":\"1000\",\"x2\":\"1000\",\"y1\":\"1300\",\"y2\":\"1000\"}],[{\"x1\":\"500\",\"x2\":\"1100\",\"y1\":\"300\",\"y2\":\"300\"},{\"x1\":\"1100\",\"x2\":\"1100\",\"y1\":\"300\",\"y2\":\"700\"},{\"x1\":\"300\",\"x2\":\"1300\",\"y1\":\"900\",\"y2\":\"900\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"900\",\"y2\":\"1300\"}]],\"stroke\":[[{\"letter\":\"ㄷ\",\"strokeNum\":1},{\"letter\":\"ㄷ\",\"strokeNum\":2},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅇ\",\"strokeNum\":4}],[{\"letter\":\"ㄱ\",\"strokeNum\":2},{\"letter\":\"ㅜ\",\"strokeNum\":1},{\"letter\":\"ㅜ\",\"strokeNum\":1}]]}";

        //나비
//        hangulinfo = "{\"word\":[[{\"x1\":\"400\",\"x2\":\"400\",\"y1\":\"400\",\"y2\":\"1000\"},{\"x1\":\"400\",\"x2\":\"900\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"},{\"x1\":\"1200\",\"x2\":\"1400\",\"y1\":\"700\",\"y2\":\"700\"}],[{\"x1\":\"400\",\"x2\":\"400\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"900\",\"x2\":\"900\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"400\",\"x2\":\"900\",\"y1\":\"700\",\"y2\":\"700\"},{\"x1\":\"400\",\"x2\":\"900\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"}]],\"stroke\":[[{\"letter\":\"ㄴ\",\"strokeNum\":2},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1}],[{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅂ\",\"strokeNum\":1},{\"letter\":\"ㅣ\",\"strokeNum\":1}]]}";

        //칫솔
//        hangulinfo = "{\"word\":[[{\"x1\":\"750\",\"x2\":\"750\",\"y1\":\"180\",\"y2\":\"380\"},{\"x1\":\"450\",\"x2\":\"1000\",\"y1\":\"380\",\"y2\":\"380\"},{\"x1\":\"900\",\"x2\":\"500\",\"y1\":\"400\",\"y2\":\"800\"},{\"x1\":\"800\",\"x2\":\"1000\",\"y1\":\"600\",\"y2\":\"800\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"200\",\"y2\":\"800\"},{\"x1\":\"800\",\"x2\":\"400\",\"y1\":\"900\",\"y2\":\"1300\"},{\"x1\":\"800\",\"x2\":\"1100\",\"y1\":\"1000\",\"y2\":\"1300\"}],[{\"x1\":\"900\",\"x2\":\"500\",\"y1\":\"100\",\"y2\":\"500\"},{\"x1\":\"800\",\"x2\":\"1000\",\"y1\":\"300\",\"y2\":\"500\"},{\"x1\":\"800\",\"x2\":\"800\",\"y1\":\"600\",\"y2\":\"700\"},{\"x1\":\"300\",\"x2\":\"1200\",\"y1\":\"700\",\"y2\":\"700\"},{\"x1\":\"500\",\"x2\":\"1100\",\"y1\":\"900\",\"y2\":\"900\"},{\"x1\":\"1100\",\"x2\":\"1100\",\"y1\":\"900\",\"y2\":\"1100\"},{\"x1\":\"500\",\"x2\":\"1100\",\"y1\":\"1100\",\"y2\":\"1100\"},{\"x1\":\"500\",\"x2\":\"500\",\"y1\":\"1100\",\"y2\":\"1300\"},{\"x1\":\"500\",\"x2\":\"1100\",\"y1\":\"1300\",\"y2\":\"1300\"}]],\"stroke\":[[{\"letter\":\"ㅊ\",\"strokeNum\":1},{\"letter\":\"ㅊ\",\"strokeNum\":2},{\"letter\":\"ㅊ\",\"strokeNum\":1},{\"letter\":\"ㅣ\",\"strokeNum\":1},{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅅ\",\"strokeNum\":1}],[{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅗ\",\"strokeNum\":1},{\"letter\":\"ㅗ\",\"strokeNum\":1},{\"letter\":\"ㄹ\",\"strokeNum\":2},{\"letter\":\"ㄹ\",\"strokeNum\":1},{\"letter\":\"ㄹ\",\"strokeNum\":2}]]}";

        //식사
//        hangulinfo = "{\"word\":[[{\"x1\":\"800\",\"x2\":\"400\",\"y1\":\"300\",\"y2\":\"700\"},{\"x1\":\"700\",\"x2\":\"900\",\"y1\":\"500\",\"y2\":\"700\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"200\",\"y2\":\"800\"},{\"x1\":\"500\",\"x2\":\"1200\",\"y1\":\"1000\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"1000\",\"y2\":\"1300\"}],[{\"x1\":\"800\",\"x2\":\"300\",\"y1\":\"500\",\"y2\":\"1000\"},{\"x1\":\"600\",\"x2\":\"900\",\"y1\":\"700\",\"y2\":\"1000\"},{\"x1\":\"1200\",\"x2\":\"1200\",\"y1\":\"300\",\"y2\":\"1300\"},{\"x1\":\"1200\",\"x2\":\"1400\",\"y1\":\"700\",\"y2\":\"700\"}]],\"stroke\":[[{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅣ\",\"strokeNum\":1},{\"letter\":\"ㄱ\",\"strokeNum\":2}],[{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅅ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1},{\"letter\":\"ㅏ\",\"strokeNum\":1}]]}";


        // 번들생성
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 수
        bundle.putString("hangulinfo",hangulinfo);
        bundle.putString("word",word);
        //fragment들 생성
        hangul = new HangulFragment();
        hangul.setArguments(bundle);
        //fragment 관리자 선언
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //fragment전환
        fragmentTransaction.replace(R.id.main_hangulja, hangul);
        fragmentTransaction.commitNow();

        //=======================인텐트 받은 글자 파싱
        preview.setText(String.valueOf(word.charAt(now)));




        //메세지를 받기위한 브로드캐스트 설정
        LocalBroadcastManager.getInstance(this).registerReceiver(wordindexReceiver,
                new IntentFilter("updatepreview"));

        //메세지를 받기위한 브로드캐스트 설정
        LocalBroadcastManager.getInstance(this).registerReceiver(isfinishReceiver,
                new IntentFilter("finishword"));

    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){

//                case R.id.study_next:
//                    if(now < word.length()-1){
//                        now++;
//                        preview.setText(String.valueOf(word.charAt(now)));
//                        Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 수
//                        bundle.putString("jsonarr",jsonarr1.toString());
//                        hangul = new HangulFragment();
//                        hangul.setArguments(bundle);
//
//                        fragmentManager = getSupportFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.main_hangulja, hangul);
//                        fragmentTransaction.commitNow();
//                        hangul.resetPaint();
//                    }
//                    break;
//                case R.id.study_previous:
//                    if(now != 0 ){
//                        now--;
//                        preview.setText(String.valueOf(word.charAt(now)));
//                        Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 수
//                        bundle.putString("jsonarr",jsonarr.toString());
//                        hangul = new HangulFragment();
//                        hangul.setArguments(bundle);
//                        //fragment전환
//                        fragmentManager = getSupportFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.main_hangulja, hangul);
//                        fragmentTransaction.commitNow();
//                        hangul.resetPaint();
//                    }
//                    break;
            }
        }
    };

    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        //hasFocus : 앱이 화면에 보여졌을때 true로 설정되어 호출됨.
        //만약 그리기 뷰 전역변수에 값이 없을경우 전역변수를 초기화 시킴.
        if(hasFocus && drawLine == null)
        {
            //그리기 뷰가 보여질(나타날) 레이아웃 찾기..
            if(hangulja != null) //그리기 뷰가 보여질 레이아웃이 있으면...
            {
                //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
                Rect rect = new Rect(0, 0,
                        hangulja.getMeasuredWidth(), hangulja.getMeasuredHeight());
                //그리기 뷰 초기화..
                drawLine = new DrawLine(  this, rect);
                sharedMemory.setDrawLine(drawLine);
                //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
                hangulja.addView(drawLine);
            }
            if(drawLine != null) drawLine.setLineColor(Color.BLACK);
        }
        super.onWindowFocusChanged(hasFocus);
    }

//
//    private void renewDrawLine(){
//        hangulja = (RelativeLayout) findViewById(R.id.main_hangulja);
//        hangulja.removeView(drawLine);
//        //그리기 뷰가 보여질(나타날) 레이아웃 찾기..
//        if(hangulja != null) //그리기 뷰가 보여질 레이아웃이 있으면...
//        {
//            //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
//            Rect rect = new Rect(0, 0,
//                    hangulja.getMeasuredWidth(), hangulja.getMeasuredHeight());
//            //그리기 뷰 초기화..
//            drawLine = new DrawLine(  this, rect);
//            sharedMemory.setDrawLine(drawLine);
//            //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
//            hangulja.addView(drawLine);
//        }
//        if(drawLine != null) drawLine.setLineColor(Color.BLACK);
//        drawLine.setLineColor(Color.BLACK);
//        drawLine.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        drawLine.invalidate();
//    }


    BroadcastReceiver wordindexReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             wordindex = intent.getIntExtra("wordindex",-1);
//            ChatItem chatitem = gson.fromJson(receivemessage,ChatItem.class);
            if(wordindex >= 0 )
            {
                //what you want to do
                // preview 를 프래그먼트로  부터 브로드캐스트리시버로 받아서 preview update
                preview.setText(String.valueOf(word.charAt(wordindex)));
            }
        }
    };


    BroadcastReceiver isfinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isFinish = intent.getStringExtra("isfinish");
            if(isFinish.equals("finish"))
            {
                Log.d("@@@finish","test");
                //what you want to do
                // preview 를 프래그먼트로 부터 브로드캐스트리시버로 받아서 preview update
                FinishWord fw = new FinishWord(id,day,word);
                fw.execute();
                Intent intent1 = new Intent(StudyActivity.this,StudyListActivity.class);
                startActivity(intent1);
                finish();
                Log.d("@@@finish","test2");
            }
        }
    };

}
