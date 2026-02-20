package com.example.weatherforecast.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherforecast.main.MainActivity;
import com.example.weatherforecast.R;

public class LoginActivity extends AppCompatActivity {
    //声明控件
    private EditText etAccount, etPassword;
    private CheckBox cbRememberPassword, cbAutomaticLogin;
    private TextView tvNoAccount;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initView();
        initClick();
        initEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //初始化控件
    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        cbRememberPassword = findViewById(R.id.cb_remember_password);
        cbAutomaticLogin = findViewById(R.id.cb_automatic_login);
        tvNoAccount = findViewById(R.id.tv_no_account);
        btnLogin = findViewById(R.id.btn_login);
    }
    private void initClick() {
        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startRegisterActivity(LoginActivity.this);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userRegister = getSharedPreferences("user_register", MODE_PRIVATE);
                if(userRegister!=null){
                    String account = userRegister.getString("user_account", "");
                    String password = userRegister.getString("user_password", "");
                    if (etAccount.getText().toString().equals(account)) {
                        if (etPassword.getText().toString().equals(password)) {
                            saveLoginState();
                            MainActivity.startMainActivity(LoginActivity.this);
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this,"请先注册账号",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initEvent() {
        updateLoginStateFromSP();
    }
    private void saveLoginState(){
        SharedPreferences loginState = getSharedPreferences("login_state", MODE_PRIVATE);
        loginState.edit().putBoolean("remember_password",cbRememberPassword.isChecked())
                .putBoolean("automatic_login",cbAutomaticLogin.isChecked())
                .apply();
    }
    private void updateLoginStateFromSP(){
        SharedPreferences loginState = getSharedPreferences("login_state", MODE_PRIVATE);
        cbRememberPassword.setChecked(loginState.getBoolean("remember_password",false));
        cbAutomaticLogin.setChecked(loginState.getBoolean("automatic_login",false));
        SharedPreferences userRegister = getSharedPreferences("user_register", MODE_PRIVATE);
        String userAccount = userRegister.getString("user_account", "");
        String userPassword = userRegister.getString("user_password", "");
        if(cbRememberPassword.isChecked()){
            etAccount.setText(userAccount);
            etPassword.setText(userPassword);

        }
        if(cbAutomaticLogin.isChecked()){
            Toast.makeText(LoginActivity.this,"自动登录中",Toast.LENGTH_SHORT).show();
            btnLogin.callOnClick();
        }
    }
    public static void startLoginActivity(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}