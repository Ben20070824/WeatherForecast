package com.example.weatherforecast.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherforecast.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText etAccount, etPassword, etPasswordEnsure;
    private CheckBox cbAgreement;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etPasswordEnsure = findViewById(R.id.et_password_ensure);
        cbAgreement = findViewById(R.id.cb_agreement);
        btnRegister = findViewById(R.id.btn_register);
    }
    private void initEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString();
                String passwordEnsure = etPasswordEnsure.getText().toString();

                // 1. 检查账户名是否为空
                if(account.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "请输入账户名", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. 检查密码是否为空
                if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. 检查两次密码是否一致
                if(!password.equals(passwordEnsure)){
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 4. 检查是否同意协议
                if(!cbAgreement.isChecked()){
                    Toast.makeText(RegisterActivity.this, "未同意用户协议", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 所有验证通过
                Toast.makeText(RegisterActivity.this, "注册成功！欢迎使用天气预报！", Toast.LENGTH_SHORT).show();
                saveRegistrationInfo();
                LoginActivity.startLoginActivity(RegisterActivity.this);
            }
        });
    }
    public static void startRegisterActivity(Context context){
        Intent intent=new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
    private void saveRegistrationInfo(){
        SharedPreferences userRegister = getSharedPreferences("user_register", MODE_PRIVATE);
        userRegister.edit().putString("user_account",etAccount.getText().toString())
                .putString("user_password",etPassword.getText().toString())
                .apply();
    }
}