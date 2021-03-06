package kr.ac.skuniv.di.hangulstudy.http;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import kr.ac.skuniv.di.hangulstudy.VO.UserVO;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Juhyun on 2018-03-15.
 */

public class SendSignUpInfo extends AsyncTask<Void,Void,String> {
    String answer;
    String userVO;
    UserVO userInfo;

    public SendSignUpInfo(UserVO userInfo){
        this.userInfo = userInfo;;
    }

    @Override
    protected String doInBackground(Void... params) {
        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody = null;

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        userVO = gson.toJson(userInfo);

        requestBody = new FormBody.Builder().add("userVO",userVO).build();
        Request request = new Request.Builder()
                .url("http://117.17.142.133:8080/skuniv/signUp")
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

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