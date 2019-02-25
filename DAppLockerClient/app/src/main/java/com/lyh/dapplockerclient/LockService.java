package com.lyh.dapplockerclient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LockService {
    @POST("/api/login")
    Call<LoginResp> login(@Body LoginReq req);


    @POST("/api/lock")
    Call<LockResp> addLock(@Body LockReq req);

    @GET("/api/lock/randomstr")
    Call<RanStrResp> getRandomStr(@Query("address") String address);
}
class RanStrResp {
    public int code;
    public String ranStr;
}

class LoginReq {
    public String  userName;
    public String password;

    public LoginReq(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
class LoginResp {
    public int code;
    public String token;
    public String msg;
}

class LockReq {
    public String contractAddr;
}

class LockResp {
    public String salt;
    public int code;
}