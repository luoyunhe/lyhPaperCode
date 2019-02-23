package com.lyh.dapplockerclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        v.setClickable(false);
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (userName.equals("") || password.equals("")) {
            Toast.makeText(this, "账号、密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        LockService service = RetrofitMgr.getInstance().createService(LockService.class);
        LoginReq req = new LoginReq(userName, password);
        service.login(req).enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                LoginResp resp = response.body();
                if (resp == null || resp.code != 0) {
                    Toast.makeText(LoginActivity.this, "登录异常！", Toast.LENGTH_LONG).show();
                    v.setClickable(true);
                    return;
                }
                SharedPreferences sp = getSharedPreferences("setting", 0);
                UserInfo userInfo = new UserInfo();
                userInfo.setName(userName);
                sp.edit().putString(Util.USER_INFO_KEY, JSON.toJSONString(userInfo))
                        .putString(Util.USER_TOKEN_KEY, resp.token).commit();

                v.setClickable(true);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "登录异常！", Toast.LENGTH_LONG).show();
                v.setClickable(true);
                t.printStackTrace();
            }
        });

    }

}
