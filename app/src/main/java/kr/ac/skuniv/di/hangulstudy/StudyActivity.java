package kr.ac.skuniv.di.hangulstudy;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import kr.ac.skuniv.di.hangulstudy.sharedmemory.SharedMemory;

/**
 * Created by namgiwon on 2018. 1. 31..
 */

public class StudyActivity extends FragmentActivity{
    SharedMemory sharedMemory;
    RelativeLayout hangulja;
    HangulFragment hangul;
    String str = "간다";
    private DrawLine drawLine = null; // 선그리기 뷰 객체
    Button reset;
    Button back;
    Button previous;
    Button next;
    String hangulinfo;

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
    String stroke = "2,1,1,2";
    String stroke1 = "1,2,1,1";

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_study);


        sharedMemory = SharedMemory.getinstance();

        reset = (Button) findViewById(R.id.study_reset);
        reset.setOnClickListener(bListener);
        back = (Button) findViewById(R.id.study_back);
        back.setOnClickListener(bListener);
        previous = (Button) findViewById(R.id.study_previous);
        previous.setOnClickListener(bListener);
        next = (Button) findViewById(R.id.study_next);
        next.setOnClickListener(bListener);
        preview = (TextView) findViewById(R.id.study_preview);
        pullstring = (TextView) findViewById(R.id.study_pullstring);
        pullstring.setText(str);
        hangulja = (RelativeLayout) findViewById(R.id.main_hangulja);
        hangulinfo = getIntent().getStringExtra("hangulinfo");

        // 번들생성
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 수
        bundle.putString("hangulinfo",hangulinfo);
        bundle.putSerializable("answer",stroke);
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
        preview.setText(String.valueOf(str.charAt(now)));

    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.study_reset:
                  hangul.resetPaint();
                    break;
                case R.id.study_back:    // 뒤로가기 버튼 즉, 가장 최신의 획을 지우는 버튼
                   hangul.backPaint();
                    break;
                case R.id.study_next:
                    if(now < str.length()-1){
                        now++;
                        preview.setText(String.valueOf(str.charAt(now)));
                        Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 수
                        bundle.putString("jsonarr",jsonarr1.toString());
                        bundle.putSerializable("answer",stroke1);
                        hangul = new HangulFragment();
                        hangul.setArguments(bundle);

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_hangulja, hangul);
                        fragmentTransaction.commitNow();
                        hangul.resetPaint();
                    }
                    break;
                case R.id.study_previous:
                    if(now != 0 ){
                        now--;
                        preview.setText(String.valueOf(str.charAt(now)));
                        Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 수
                        bundle.putString("jsonarr",jsonarr.toString());
                        bundle.putSerializable("answer",stroke);
                        hangul = new HangulFragment();
                        hangul.setArguments(bundle);
                        //fragment전환
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_hangulja, hangul);
                        fragmentTransaction.commitNow();
                        hangul.resetPaint();
                    }
                    break;
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


}
