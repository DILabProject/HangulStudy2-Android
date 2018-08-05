package kr.ac.skuniv.di.hangulstudy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.VO.StudyListVO;
import kr.ac.skuniv.di.hangulstudy.http.BringHangulInfo;
import kr.ac.skuniv.di.hangulstudy.http.BringStudyList;

public class StudyListActivity extends FragmentActivity {
    BringHangulInfo bringHangulInfo;
    String hangulinfo;
    String savedId;
    ListViewAdapter adapter;
    int StudyActivityCode = 1000;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_studylist);

        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        savedId = loginInfo.getString("id","none");

        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        BringStudyList bringStudyList = new BringStudyList(savedId);
        try {
            String result = bringStudyList.execute().get();
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            StudyListVO[] studyListVOS = gson.fromJson(result, StudyListVO[].class);
            for (StudyListVO studyListVO : studyListVOS) {
                adapter.addItem(studyListVO);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                bringHangulInfo = new BringHangulInfo(adapter.getItem(i).getWord());
                try {
                    hangulinfo = bringHangulInfo.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(StudyListActivity.this, StudyActivity.class);
                intent.putExtra("hangulinfo",hangulinfo);
                intent.putExtra("word",adapter.getItem(i).getWord());
                intent.putExtra("day",adapter.getItem(i).getDay());
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == StudyActivityCode) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                BringStudyList bringStudyList = new BringStudyList(savedId);
                try {
                    String result = bringStudyList.execute().get();
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    adapter.listViewItemList.clear();
                    StudyListVO[] studyListVOS = gson.fromJson(result, StudyListVO[].class);
                    for (StudyListVO studyListVO : studyListVOS) {
                        adapter.addItem(studyListVO);
                    }

                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();
                    listview.invalidateViews();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
