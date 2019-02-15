package com.lyh.dapplockerclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etUserName, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        btnLogin = findViewById(R.id.btn_login);
        etUserName = findViewById(R.id.user_name);
        etPassword = findViewById(R.id.password);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (userName.equals("") || password.equals("")) {
            Toast.makeText(this, "账号、密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sp = getSharedPreferences("setting", 0);
        sp.edit().putString("user_info", "lyh").commit();

        finish();
    }

}
