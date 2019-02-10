package kr.ac.skuniv.di.hangulstudy.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Juhyun on 2018-03-15.
 */

public class FinishWord extends AsyncTask<Void,Void,String> {
    String answer;
    String id;
    int day;
    String word;

    public FinishWord(String id, int day, String word){
        this.id = id;
        this.day = day;
        this.word = word;
    }

    @Override
    protected String doInBackground(Void... params) {
        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody = null;

        requestBody = new FormBody.Builder().add("id",id).add("day",String.valueOf(day)).add("word",word).build();
        Log.d("@@@word",""+word);
        Log.d("@@@day",""+day);
        Log.d("@@@id",""+id);
        Request request = new Request.Builder()
                .url("http://117.17.142.133:8080/skuniv/finishStudy")
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();
            Log.d("answer",answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }


}
