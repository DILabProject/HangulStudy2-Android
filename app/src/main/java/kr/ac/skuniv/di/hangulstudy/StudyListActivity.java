package kr.ac.skuniv.di.hangulstudy;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.VO.StudyListVO;
import kr.ac.skuniv.di.hangulstudy.http.BringStudyList;

public class StudyListActivity extends FragmentActivity {
    kr.ac.skuniv.di.hangulstudy.http.BringHangulInfo bringHangulInfo;
    String hangulinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_studylist);


        ListView listview = (ListView) findViewById(R.id.listview) ;
        final ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

//        BringStudyList bringStudyList = new BringStudyList("123");
//        try {
//            String result = bringStudyList.execute().get();
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//            StudyListVO[] studyListVOS = gson.fromJson(result, StudyListVO[].class);
//            for (StudyListVO studyListVO : studyListVOS) {
//                adapter.addItem(studyListVO);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        adapter.addItem("1일차","간다");
        adapter.addItem("2일차","하루");
        adapter.addItem("3일차","비행기");
        adapter.addItem("4일차","배");




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                bringHangulInfo = new kr.ac.skuniv.di.hangulstudy.http.BringHangulInfo(String.valueOf(i+1));
                try {
                    hangulinfo = bringHangulInfo.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

//                Log.d("aaa",hangulinfo+"<<<<<<<<<<<<");

                Intent intent = new Intent(StudyListActivity.this, StudyActivity.class);
                intent.putExtra("hangulinfo",hangulinfo);
                intent.putExtra("word",adapter.getItem(i).getWord());
                startActivity(intent);
            }
        });
    }
}
