package kr.ac.skuniv.di.hangulstudy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.http.CheckLogin;

public class LoginActivity extends AppCompatActivity {

    private String input_id = " ";
    private String input_password = " ";
    private EditText id_edit, password_edit;
    private Button login_button;
    private Button signUp_button;
    private String result;
    private String saved_id;
    private String saved_stdNo;

    SharedPreferences loginInfo;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInfo = getSharedPreferences("info", Activity.MODE_PRIVATE);
        editor = loginInfo.edit();

        id_edit = (EditText)findViewById(R.id.editText_id);
        password_edit = (EditText)findViewById(R.id.editText_password);
        login_button = (Button)findViewById(R.id.button_login);
        signUp_button = (Button)findViewById(R.id.button_signUp);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_id = id_edit.getText().toString();
                input_password = password_edit.getText().toString();

                if(!TextUtils.isEmpty(input_id) && !TextUtils.isEmpty(input_password)) {
                    Intent intent = new Intent(LoginActivity.this, StudyListActivity.class);
                    Log.d("check", input_id+", " + input_password + "!!!");
                    startActivity(intent);


//                    CheckLogin checkLogin = new CheckLogin(input_id, input_password);
//                    try {
//                        result = checkLogin.execute().get();
//                        Log.d("check result", result);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(result.equals("0")){
//                        Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                    else if(result.equals("1")) {
//                        //SharedPreference에 값 저장
//                        editor.putString("id", input_id);
//                        editor.putString("password",input_password);
//                        editor.commit(); //완료한다.
//                        startActivity(intent);
//                        finish();
//                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
