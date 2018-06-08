package kr.ac.skuniv.di.hangulstudy;
//confirm

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssomai.android.scalablelayout.ScalableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import kr.ac.skuniv.di.hangulstudy.VO.HangulVO;
import kr.ac.skuniv.di.hangulstudy.VO.LetterVO;
import kr.ac.skuniv.di.hangulstudy.VO.PointVO;
import kr.ac.skuniv.di.hangulstudy.VO.WrongWordVO;
import kr.ac.skuniv.di.hangulstudy.http.SendWorngLetterInfo;
import kr.ac.skuniv.di.hangulstudy.sharedmemory.SharedMemory;


/**
 * Created by namgiwon on 2018. 1. 30..
 */

public class HangulFragment extends Fragment {
    DrawLine drawLine;
    RelativeLayout parentLayout;
    Gson gson;
    LinkedList<Path> paintStack;
    private int backgroundid = 1000;
    private int id = 1;
    private int blackBlockSize = 100; //글자 사이즈
    private int clearBlockSize = 200; //가이드라인 사이즈
    private int backgroundSize = 10;
    ArrayList<View> GuideLine = new ArrayList<View>();
    ArrayList<Integer> CheckIDList = new ArrayList<Integer>();  //한 획을 그리면서 지나간 view들의 id를 담는 ArrayList

    private WrongWordVO wrongWordVO;

    //************** 중요! ****************
    /********Json Array 구조 형식***********
     fullIfo  - {"word" : wordJsonArr, "stroke" : strokeJsonArr}
     wordJsonArr - [{"x1" : 1, "y1" : 1, "x2" : 2, "y2" : 2} , {"x1" : 1, "y1" : 1, "x2" : 2, "y2" : 2} ..... 여러개]
     strokeJsonArr - [{"stroke" : "2,1,1,2"},{"stroke" : "1,2,1,1"} ]
     *************************************/

    JsonObject fullInfo; // Word 정보, Stroke 정보,  Answer 정보를 담는 JsonObject;
    JsonArray wordJsonArr;   // 글자정보 ArrayList
    JsonArray strokeJsonArr; // 획수정보 ArrayList
    ScalableLayout word;
    ScalableLayout lastSL;
    String strokeArr[];

    HangulVO hangulVO;

    int index = 0;
    int strokeCount = 0;
    int wordCount = 0; // jsonArray에 몇글자가 들어왔는지 체크
    SharedMemory sharedMemory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paintStack = new LinkedList<Path>();
        sharedMemory = SharedMemory.getinstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hangul, null);
        gson = new Gson();
        parentLayout = view.findViewById(R.id.parent);
        parentLayout.setOnTouchListener(tListener);
        parentLayout.setOnDragListener(dListener);
        parentLayout.setClickable(false);

        ////// 글자 좌표정보 및 획수정보 를 번들(getArgument)로 받아옴

        hangulVO = gson.fromJson(getArguments().getString("hangulinfo"), HangulVO.class);
        lastSL = new ScalableLayout(getActivity(), backgroundSize * 100, backgroundSize * 100);


//        //////////////////
//        JsonObject object = hangulVO.getWord().get(wordCount).getAsJsonObject();
//        String key = object.get("key").getAsString();
//        JsonArray array = object.get("value").getAsJsonArray();
//        MakeWord((JsonArray) array);
//        //////////////////


        MakeWord((JsonArray) hangulVO.getWord().get(wordCount));


        //워드 체크
        Log.d("checkWord", hangulVO.getWord().get(wordCount).toString());

        parentLayout.addView(lastSL);
        return view;
    }

    /*********
     터치리스너
     ********/
    View.OnTouchListener tListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            Rect rect = new Rect();
            parentLayout.getGlobalVisibleRect(rect);

            if ((int) view.getId() < 2000) {         //10 이란 숫자는 그려진 최대 id값
                x = x + location[0] - rect.left;         // 해당 아이디의 절대 좌표를 계산 하기 위하여 좌표에 뷰의 왼쪽마진값을 더한다
                y = y + location[1] - rect.top;          // 해당 아이디의 절대 좌표를 계산 하기 위하여 좌표에 뷰의 위쪽마진 값을 더한다
            }
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    sharedMemory.getDrawLine().oldX = x;
                    sharedMemory.getDrawLine().oldY = y;
                    sharedMemory.getDrawLine().path.reset();
                    sharedMemory.getDrawLine().path.moveTo(x, y);

                    ClipData.Item item = new ClipData.Item(
                            (CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData("a", mimeTypes, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            null);
                    view.startDrag(data, // data to be dragged
                            shadowBuilder, // drag shadow
                            view, // 드래그 드랍할  Vew
                            0 // 필요없은 플래그
                    );
                    return false;
                }
                case MotionEvent.ACTION_MOVE: {
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    return true;
                }
            }
            return true;
        }
    };


    /*********
     드래그리스너
     ********/
    View.OnDragListener dListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            float x = dragEvent.getX();
            float y = dragEvent.getY();
            int[] location = new int[2]; // 절대좌표 구하기
            view.getLocationOnScreen(location);
            Rect l = new Rect();
            parentLayout.getGlobalVisibleRect(l);

            if ((int) view.getId() < 2000) {         //10 이란 숫자는 그려진 최대 id값
                x = x + location[0] - l.left;                // 해당 아이디의 절대 좌표를 계산 하기 위하여 좌표에 뷰의 왼쪽마진값을 더한다
                y = y + location[1] - l.top;           // 해당 아이디의 절대 좌표를 계산 하기 위하여 좌표에 뷰의 위쪽마진 값을 더한다
            }

            switch (dragEvent.getAction()) {
                // 이미지를 드래그 시작될때

                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                // 드래그한 이미지를 옮길려는 지역으로 들어왔을때

                case DragEvent.ACTION_DRAG_ENTERED:
                    CheckIDList.add(view.getId());
                    return false;

                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d("aa", String.valueOf(dragEvent.getX()));
                    Log.d("ab", String.valueOf(x));
                    Log.d("ac", String.valueOf(l.left));
                    //포인트가 이동될때 마다 두 좌표(이전에눌렀던 좌표와 현재 이동한 좌료)간의 간격을 구한다.
                    float dx = Math.abs(x - sharedMemory.getDrawLine().oldX);
                    float dy = Math.abs(y - sharedMemory.getDrawLine().oldY);
                    //두 좌표간의 간격이 4px이상이면 (가로든, 세로든) 그리기 bitmap에 선을 그린 다.
                    if (dx >= 4 || dy >= 4) {
                        //path에 좌표의 이동 상황을 넣는다. 이전 좌표에서 신규 좌표로..
                        //lineTo를 쓸수 있지만.. 좀더 부드럽게 보이기 위해서 quadTo를 사용함.
                        sharedMemory.getDrawLine().path.quadTo(sharedMemory.getDrawLine().oldX, sharedMemory.getDrawLine().oldY, x, y);
                        //포인터의 마지막 위치값을 기억한다.
                        sharedMemory.getDrawLine().oldX = x;
                        sharedMemory.getDrawLine().oldY = y;
                        //그리기 bitmap에 path를 따라서 선을 그린다.
                        sharedMemory.getDrawLine().canvas.drawPath(sharedMemory.getDrawLine().path, sharedMemory.getDrawLine().paint);
                    }
                    //화면을 갱신시킴... 이 함수가 호출 되면 onDraw 함수가 실행됨.
                    sharedMemory.getDrawLine().invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    paintStack.push(sharedMemory.getDrawLine().path);
                    sharedMemory.getDrawLine().path = new Path();
                    boolean isCollect = true; // 획 그리기 성공 체크
                    for (int i = 0; i < CheckIDList.size(); i++) {
                        if (CheckIDList.get(i) > 100 && CheckIDList.get(i) < 2000) {
                            isCollect = false;
                        }
                    }

                    //터치 후 정답 처리 부분.
                    if (!isCollect) {
                        Log.d("wordCount", String.valueOf(wordCount));
                        Log.d("index", String.valueOf(index));
                        Fail("123",hangulVO.getStroke().get(wordCount).get(index-1).getLetter());
                    }
                    else if (isCollect) {
                        Collect();
                    }

                    CheckIDList.clear();
                    return true;
            }
            return true;
        }
    };

    /*********
     정답처리 메소드
     ********/
    public void Collect() {
        Log.d("index",String.valueOf(hangulVO.getStroke().size()));

        if (index != hangulVO.getStroke().get(wordCount).size()) {
            PaintGuideLine((JsonArray) hangulVO.getWord().get(wordCount), word);
        } else {
            Log.d("wordCount", wordCount+"");
            if (wordCount != hangulVO.getWord().size() - 1) { //한글자의 마지막 획수까지 완성시킨경우



                index = 0;
                strokeCount = 0;
                wordCount++;
                Log.d("wordCount2", wordCount+"");
                resetPaint();
                MakeWord((JsonArray) hangulVO.getWord().get(wordCount));


                //preview를 바꾸어 주는 부분  BroadCast로 메시지를 보냄
                final Intent broadintent = new Intent("updatepreview");
                final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
                broadintent.putExtra("wordindex",wordCount);
                broadcastManager.sendBroadcast(broadintent);

                //한글자를 완성시켰을 경우 틀린 자음,모음 횟수 통신부분

            } else {
                Log.d("finish", "done!!!!!!!!!");
                //하루차 글자를 모두 완성했을경우
                final Intent broadintent = new Intent("finishword");
                final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
                broadintent.putExtra("isfinish","finish");
                broadcastManager.sendBroadcast(broadintent);
            }
        }
    }

    /*********
     실패처리 메소드
     ********/
    public void Fail(String id,String letter) {

        Log.d("testID",id);
        Log.d("testLetter",letter);
        Toast.makeText(getContext(), "다시그려보세요", Toast.LENGTH_LONG).show();
        backPaint();

        SendWorngLetterInfo sendWorngLetterInfo = new SendWorngLetterInfo(id, letter );
        sendWorngLetterInfo.execute();
    }

    /*********
     한글자에대한 좌표정보를 받아 글자를 만든 ScalableLayout을 반환하는 메소드
     즉, 한 글자를 클리어할때마다 이 메소드를 호출하면 글자를 바꿀수 있게 한것이다.
     lastSl 을 비우고 새글자를 덮어쓴다
     ********/
    public void MakeWord(JsonArray jsonarr) {
        //strokeArr = hangulVO.getStroke().get(wordCount).split(",");
//        Log.d("testSplit1", strokeArr[0]);
//        Log.d("testSplit2", strokeArr[1]);
//        Log.d("testSplit3", strokeArr[2]);
//        Log.d("testSplit4", strokeArr[3]);
        ScalableLayout sl = new ScalableLayout(getActivity(), backgroundSize * 100, backgroundSize * 100);
        PaintBackground(sl);
        PaintWord(jsonarr, sl);
        PaintGuideLine(jsonarr, sl);
        word = sl;
        lastSL.removeAllViews();
        lastSL.addView(sl, 0, 0, backgroundSize * 100, backgroundSize * 100);
    }


    /*********
     가이드라인 그리는 메소드
     ********/
//    public void PaintGuideLine(JsonArray jsonarr, ScalableLayout sl) {  //정답 가이드라인 나오는곳
//        if (GuideLine.size() != 0) {
//            Log.d("ff", "remove guideline");
//            removeGuidLine();
//        }
//        for (int i = strokeCount; i < strokeCount + Integer.parseInt(strokeArr[index]); i++) {
//            JsonObject jsonobj = jsonarr.get(i).getAsJsonObject();
//            PointVO point = gson.fromJson(jsonobj, PointVO.class);
//            String direct = PaintDirect(point);  // 그려야할 방향이 가로인지 세로인지 리턴값 = vertical or horizontal or round
//            for (int j = 0; ; j++) {
//                ImageView iv1 = new ImageView(getActivity());
//                int ivTOP = 0;
//                int ivLEFT = 0;
//                iv1.setId(id);
//                iv1.setClickable(true);
//                id++;
//                iv1.setOnTouchListener(tListener);
//                iv1.setOnDragListener(dListener);
//                iv1.setBackgroundResource(R.drawable.b1); // 이미지뷰 이미지지정 : 투명블럭(글자의 정답체크를 위한 투명 이미지)
//
//                GuideLine.add(iv1); // arraylist 에 추가
//                //글자블럭 param 설정
//                if (direct.equals("horizontal")) {
//                    ivLEFT = point.getX1() + j * blackBlockSize;
//                    ivTOP = point.getY1();
//                } else if (direct.equals("vertical")) {
//                    ivLEFT = point.getX1();
//                    ivTOP = point.getY1() + j * blackBlockSize;
//                }
////                sl.addView(iv,ivLEFT,ivTOP,blackBlockSize,blackBlockSize);
//                sl.addView(iv1, ivLEFT - (clearBlockSize - blackBlockSize) / 2, ivTOP - (clearBlockSize - blackBlockSize) / 2, clearBlockSize, clearBlockSize);
//                if (direct.equals("horizontal")) {
//                    if (j == (point.getX2() - point.getX1()) / blackBlockSize) break;
//                } else if (direct.equals("vertical")) {
//                    if (j == ((point.getY2() - point.getY1()) / blackBlockSize)) break;
//                }
//            }
//        }
//        strokeCount += Integer.parseInt(strokeArr[index]);
//        index++;
//        id = 0;
//
//    }


    public void PaintGuideLine(JsonArray jsonarr, ScalableLayout sl) {  //정답 가이드라인 나오는곳
        if (GuideLine.size() != 0) {
            Log.d("ff", "remove guideline");
            removeGuidLine();
        }
        for (int i = strokeCount; i < strokeCount + hangulVO.getStroke().get(wordCount).get(index).getStrokeNum(); i++) {
            JsonObject jsonobj = jsonarr.get(i).getAsJsonObject();
            PointVO point = gson.fromJson(jsonobj, PointVO.class);
            String direct = PaintDirect(point);  // 그려야할 방향이 가로인지 세로인지 리턴값 = vertical or horizontal or round
            for (int j = 0; ; j++) {
                ImageView iv1 = new ImageView(getActivity());
                int ivTOP = 0;
                int ivLEFT = 0;
                iv1.setId(id);
                iv1.setClickable(true);
                id++;
                iv1.setOnTouchListener(tListener);
                iv1.setOnDragListener(dListener);
                iv1.setBackgroundResource(R.drawable.b1); // 이미지뷰 이미지지정 : 투명블럭(글자의 정답체크를 위한 투명 이미지)

                GuideLine.add(iv1); // arraylist 에 추가
                //글자블럭 param 설정
                if (direct.equals("horizontal")) {
                    ivLEFT = point.getX1() + j * blackBlockSize;
                    ivTOP = point.getY1();
                } else if (direct.equals("vertical")) {
                    ivLEFT = point.getX1();
                    ivTOP = point.getY1() + j * blackBlockSize;
                }else if (direct.equals("leftDiagonal")){
                    ivLEFT = point.getX1() + j * blackBlockSize ;
                    ivTOP = point.getY1() - j * blackBlockSize;
                } else if (direct.equals("rightDiagonal")){
                    ivLEFT = point.getX1() + j * blackBlockSize ;
                    ivTOP = point.getY1() + j * blackBlockSize;
                }
//                sl.addView(iv,ivLEFT,ivTOP,blackBlockSize,blackBlockSize);
                sl.addView(iv1, ivLEFT - (clearBlockSize - blackBlockSize) / 2, ivTOP - (clearBlockSize - blackBlockSize) / 2, clearBlockSize, clearBlockSize);
                if (direct.equals("horizontal")) {
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize) break;
                } else if (direct.equals("vertical")) {
                    if (j == ((point.getY2() - point.getY1()) / blackBlockSize)) break;
                } else if (direct.equals("leftDiagonal")){
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize) break;
                }else if (direct.equals("rightDiagonal")){
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize) break;
                }
            }
        }
//        strokeCount += Integer.parseInt(strokeArr[index]);


        strokeCount += hangulVO.getStroke().get(wordCount).get(index).getStrokeNum();
        index++;
        id = 0;
    }


    /*********
     이전 가이드라인을 지우는 메소드
     ********/
    public void removeGuidLine() {
        for (int i = 0; i < GuideLine.size(); i++) {
            word.removeView(GuideLine.get(i));
        }
        GuideLine.clear();
    }

    /*********
     정답처리를 위한 배경타일을 그리는 메소드
     ********/
    public void PaintBackground(ScalableLayout sl) { // 정답처리를 위한 글자밖의 타일들 그리기
        for (int i = 0; i < backgroundSize; i++) {
            for (int j = 0; j < backgroundSize; j++) {
                ImageView backgroundTile = new ImageView(getActivity());
                int ivTOP = 0;
                int ivLEFT = 0;
                backgroundTile.setId(backgroundid);
                backgroundid++;
//                iv.setOnTouchListener(tListener);
                backgroundTile.setOnDragListener(dListener);
                backgroundTile.setBackgroundResource(R.drawable.a1); //이미지뷰 이미지지정 :  글자블럭

                //글자블럭 param 설정
                ivLEFT = i * blackBlockSize;
                ivTOP = j * blackBlockSize;
                sl.addView(backgroundTile, ivLEFT, ivTOP, blackBlockSize, blackBlockSize);
            }
        }
    }

    /*********
     글자를 그리는 메소드
     ********/
    public void PaintWord(JsonArray jsonarr, ScalableLayout sl) {

        for (int i = 0; i < jsonarr.size(); i++) {
            JsonObject jsonobj = jsonarr.get(i).getAsJsonObject();
            PointVO point = gson.fromJson(jsonobj, PointVO.class);
            String direct = PaintDirect(point);  // 그려야할 방향이 가로인지 세로인지 리턴값 = vertical or horizontal or round
            for (int j = 0; ; j++) {
                Log.d("gg", "generate view");
                ImageView iv = new ImageView(getActivity());
                int ivTOP = 0;
                int ivLEFT = 0;
                iv.setBackgroundResource(R.drawable.b); //이미지뷰 이미지지정 :  글자블럭
                //글자블럭 param 설정
                if (direct.equals("horizontal")) {
                    ivLEFT = point.getX1() + j * blackBlockSize;
                    ivTOP = point.getY1();
                } else if (direct.equals("vertical")) {
                    ivLEFT = point.getX1();
                    ivTOP = point.getY1() + j * blackBlockSize;
                } else if (direct.equals("leftDiagonal")){
                    iv.setRotation(45);
                    ivLEFT = point.getX1() + j * blackBlockSize/2 ;
                    ivTOP = point.getY1() - j * blackBlockSize/2;
                } else if (direct.equals("rightDiagonal")){
                    iv.setRotation(45);
                    ivLEFT = point.getX1() + j * blackBlockSize/2 ;
                    ivTOP = point.getY1() + j * blackBlockSize/2;
                }
                sl.addView(iv, ivLEFT, ivTOP, blackBlockSize, blackBlockSize);
//                sl.addView(iv1,ivLEFT-(clearBlockSize-blackBlockSize)/2,ivTOP-(clearBlockSize-blackBlockSize)/2,clearBlockSize,clearBlockSize);
                if (direct.equals("horizontal")) {
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize) break;
                } else if (direct.equals("vertical")) {
                    if (j == ((point.getY2() - point.getY1()) / blackBlockSize)) break;
                } else if (direct.equals("leftDiagonal")){
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize *2) break;
                }else if (direct.equals("rightDiagonal")){
                    if (j == (point.getX2() - point.getX1()) / blackBlockSize *2 ) break;
                }
            }
        }
    }

    public void resetPaint() {
        sharedMemory.getDrawLine().canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        paintStack = new LinkedList<Path>();
        sharedMemory.getDrawLine().invalidate();
    }

    public void backPaint() {
        sharedMemory.getDrawLine().canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if (paintStack.size() > 0) paintStack.pop();
        if (paintStack.size() > 0) {
            for (int i = 0; i < paintStack.size(); i++) {
                sharedMemory.getDrawLine().canvas.drawPath(paintStack.get(i), sharedMemory.getDrawLine().paint);
            }
        }
        sharedMemory.getDrawLine().invalidate();
    }

    public String PaintDirect(PointVO point) {
        if (point.getX1() < point.getX2() && point.getY1() == point.getY2())
            return "horizontal";
        else if (point.getX1() == point.getX2() && point.getY1() < point.getY2())
            return "vertical";
        else if (point.getX1() < point.getX2() && point.getY1() > point.getY2())
            return "leftDiagonal";
        else if (point.getX1() < point.getX2() && point.getY1() < point.getY2())
            return "rightDiagonal";
        else return "round";
    }


}
