package com.lyh.dapplockerclient;

import android.icu.text.AlphabeticIndex;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface LockService {
    @POST("/api/login")
    Call<LoginResp> login(@Body LoginReq req);

//    @Headers("Authorization: Authorization")
    @POST("/api/lock/salt")
    Call<SaltResp> getSalt(@Header("Authorization") String auth, @Body SaltReq req);

//    @Headers("Authorization: Authorization")
    @GET("/api/lock/randomstr")
    Call<RanStrResp> getRandomStr(@Header("Authorization") String auth, @Query("address") String address);

    @POST("/api/lock/import")
    Call<ImportResp> genImport();

    @PUT("/api/lock/import")
    Call<SetImportResp> setImport(@Body SetImportReq req);

    @GET("/api/lock/import")
    Call<GetImportResp> getImport(@Query("key") String key);

    @GET("/api/lock/record")
    Call<RecordResp> getRecord(@Query("address") String addr);
}
class RecordResp {
    public int code;
    public String msg;
    static class Record {
        public String name;
        public long timestamp;
    }
    public ArrayList<Record> records;
}


class GetImportResp {
    public int code;
    public String activateStr;
    public String msg;
}

class SetImportResp {
    public int code;
}
class SetImportReq {
    public String key;
    public String passwd;
    public String activateStr;
}
class ImportResp {
    public int code;
    public String key;
    public String passwd;
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

class SaltReq {
    public String contractAddr;
}

class SaltResp {
    public String salt;
    public int code;
}