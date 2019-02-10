package kr.ac.skuniv.di.hangulstudy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.VO.StudyListVO;
import kr.ac.skuniv.di.hangulstudy.http.BringHangulInfo;

public class StudyListDialog extends Dialog {

    private List<StudyListVO> studyListItem = new ArrayList<StudyListVO>();
    private ListView StudyWordListview;
    private ListViewAdapter adapter;
    private String result;
    private String hangulinfo;
    private android.app.Activity act;

    public StudyListDialog(@NonNull final Context context, final android.app.Activity act, String studyList) {
        super(context);

        this.act = act;
        result = studyList;

        Initialize();

        StudyWordListview = (ListView) findViewById(R.id.study_listview);
        adapter = new ListViewAdapter();
        StudyWordListview.setAdapter(adapter);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        StudyListVO[] studyListVOS = gson.fromJson(result, StudyListVO[].class);
        for (StudyListVO studyListVO : studyListVOS) {
            adapter.addItem(studyListVO);
        }

        // 클릭 시 한글 프레그먼트로 이동
        StudyWordListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BringHangulInfo bringHangulInfo = new BringHangulInfo(adapter.getItem(i).getWord());
                try {
                    hangulinfo = bringHangulInfo.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getContext(), StudyActivity.class);
                intent.putExtra("hangulinfo", hangulinfo);
                intent.putExtra("word", adapter.getItem(i).getWord());
                intent.putExtra("day", adapter.getItem(i).getDay());
                context.startActivity(intent);
                dismiss();
                act.finish();
            }
        });
    }

    private void Initialize() {
        //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //다이얼로그의 배경을 투명으로 만듭니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //다이얼로그에서 사용할 레이아웃입니다.
        setContentView(R.layout.studylist_dialog);
    }


}
