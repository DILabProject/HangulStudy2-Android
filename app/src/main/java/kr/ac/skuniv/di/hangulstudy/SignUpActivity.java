package kr.ac.skuniv.di.hangulstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import kr.ac.skuniv.di.hangulstudy.VO.UserVO;
import kr.ac.skuniv.di.hangulstudy.http.SendSignUpInfo;

public class SignUpActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private EditText name;
    private Button register;
    private Spinner age;
    private RadioGroup genderGroup;
    private RadioButton selectedGender;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        id = (EditText) findViewById(R.id.idText);
        password = (EditText) findViewById(R.id.passwordText);
        name = (EditText) findViewById(R.id.nameText);
        register = (Button)findViewById(R.id.registerButton);
        age = (Spinner)findViewById(R.id.ageSpinner);
        genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        adapter = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_dropdown_item);
        age.setAdapter(adapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserVO userVO = new UserVO();
                int selectedId = genderGroup.getCheckedRadioButtonId();
                selectedGender = (RadioButton)findViewById(selectedId);

                userVO.setGender(selectedGender.getText().toString());
                Log.d("checkGender", userVO.getGender());
                userVO.setId(id.getText().toString());
                userVO.setPassword(password.getText().toString());
                userVO.setName(name.getText().toString());
                userVO.setAge(age.getSelectedItem().toString());

                if(!TextUtils.isEmpty(userVO.getId()) && !TextUtils.isEmpty(userVO.getPassword()) && !TextUtils.isEmpty(userVO.getName()) && !TextUtils.isEmpty(userVO.getAge()) && !TextUtils.isEmpty(userVO.getGender())){
                    SendSignUpInfo sendSignUpInfo = new SendSignUpInfo(userVO);
                    sendSignUpInfo.execute();
                    finish();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "내용을 모두 입력하세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
