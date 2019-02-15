package com.lyh.dapplockerclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.lyh.lockersc.Locker_sol_Locker;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.File;

public class SplashActivity extends Activity {
    private Handler handler = new Handler();
    private boolean isStartMainActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Util.walletDir = new File(getFilesDir().getPath() + "/MyWallet");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startMainActivity();

            }
        }, 1500);
//        new MyTask().execute("111");
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    private void startMainActivity() {
        if (isStartMainActivity == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            isStartMainActivity = true;
        }
    }
//    class MyTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... p) {
//            Web3j web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/fc331766f3c8401d862db11cb785fc07"));
////        Credentials credentials =
//            Log.d("dddddddddddd", web3.toString());
//            Log.d("dddddddddddd", web3.toString());
//            Log.d("dddddddddddd", web3.toString());
//            Log.d("dddddddddddd", web3.toString());
//            Log.d("dddddddddddd", web3.toString());
//            org.web3j.crypto.Credentials credentials = org.web3j.crypto.Credentials.create("2903BB92A51AAFEAE5B936AEC758257F04D8432AFBB4C2F1609B10C1B86C847E");
//            Locker_sol_Locker contract = null;
//            try {
//                contract = Locker_sol_Locker.deploy(web3, credentials,
//                        new DefaultGasProvider(), "pubKey", "lyh").send();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (contract != null) {
//                Log.d("ddddddd", contract.getContractAddress());
//            }
//            return "";
//        }
//    }
}
