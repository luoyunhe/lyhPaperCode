package com.lyh.dapplockerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoggingActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);
        lv = findViewById(R.id.lv_logging);
        Intent intent = getIntent();
        LockInfo info = intent.getParcelableExtra("lock_info");
        String addr = info.getContractAddr();
        LockService service = RetrofitMgr.getInstance().createService(LockService.class);
        service.getRecord(addr).enqueue(new Callback<RecordResp>() {
            @Override
            public void onResponse(Call<RecordResp> call, Response<RecordResp> response) {
               RecordResp resp = response.body();
                if (resp == null || resp.code != 0) {
                    Toast.makeText(LoggingActivity.this, "get result error", Toast.LENGTH_LONG);
                    return;
                }
                Log.d("record", resp.toString());
                List<String> listString = new ArrayList<>(2);
                for (int i = resp.records.size() - 1; i >= 0; i--) {
                    listString.add(String.format("  %s 于 %s 开锁", resp.records.get(i).name,
                            stampToDate(resp.records.get(i).timestamp)));
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(LoggingActivity.this,
                        android.R.layout.preference_category, android.R.id.title, listString);
                lv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<RecordResp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public static String stampToDate(long timestamp){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date((timestamp + 8 * 60 * 60)* 1000);
        res = simpleDateFormat.format(date);
        return res;
    }
}
