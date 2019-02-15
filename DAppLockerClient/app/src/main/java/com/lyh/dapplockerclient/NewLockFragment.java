package com.lyh.dapplockerclient;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.lockersc.Locker_sol_Locker;
import com.lyh.lockersc.Web3jUtil;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.cert.CRLReason;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewLockFragment extends Fragment implements View.OnClickListener {

    private Button btn_new;
    private Button btn_cancel;
    private EditText etNewLock;

    public NewLockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_lock, container, false);

        btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_new = v.findViewById(R.id.btn_new);
        etNewLock = v.findViewById(R.id.new_lock_name);

        btn_cancel.setOnClickListener(this);
        btn_new.setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                getActivity().finish();
                break;
            case R.id.btn_new:
                dealNewLock();
                break;
            default:
                return;
        }
    }

    private void dealNewLock() {
        SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
        final String walletFileName = sp.getString(Util.WALLET_FILE_NAME, "");
        if (walletFileName.equals("")) {
            Toast.makeText(getContext(), "未设置私钥！", Toast.LENGTH_LONG).show();
            return;
        }

        String newLockName = etNewLock.getText().toString();

        final EditText passwdET = new EditText(getContext());
        passwdET.setTransformationMethod(new PasswordTransformationMethod());
        new AlertDialog.Builder(getContext())
                .setTitle("请输入密码")
                .setView(passwdET)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String passwd = passwdET.getText().toString();
                        String fileName = Paths.get(Util.getWalletDirString(), walletFileName).toString();
                        Credentials credentials = null;
                        try {
                            credentials = WalletUtils.loadCredentials(passwd, fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (CipherException e) {
                            e.printStackTrace();
                            Toast t = Toast.makeText(getContext(), "密码错误，请重新尝试！", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            return;
                        }
                        new DeploySC().execute(credentials);
                        Toast.makeText(getContext(), "合约已经在后台部署！", Toast.LENGTH_LONG).show();
                    }


                })
                .setNegativeButton("取消", null)
                .show();
    }


     private class DeploySC extends AsyncTask<Credentials, String, Locker_sol_Locker> {
        @Override
        protected Locker_sol_Locker doInBackground(Credentials... params) {
            Credentials credentials = params[0];
            Locker_sol_Locker result = null;
            try {
                HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
                Web3j web3 = Web3j.build(httpService);
                result = Locker_sol_Locker.deploy(web3, credentials, new DefaultGasProvider(),
                        "pubKey", "lyh").send();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Locker_sol_Locker result) {
            super.onPostExecute(result);
            if (result == null) {
                new AlertDialog.Builder(getContext())
                        .setTitle("合约部署结果")
                        .setMessage("部署失败")
                        .setPositiveButton("确定", null)
                        .show();
                Toast.makeText(getContext(), "合约部署失败，请重试！", Toast.LENGTH_LONG).show();
            } else {
                Log.d("sc", "合约地址：" + result.getContractAddress());
                Toast.makeText(getContext(), result.getContractAddress(), Toast.LENGTH_LONG).show();
                SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
                sp.edit().putString("sc_addr", result.getContractAddress()).apply();

                TextView tv = new TextView(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle("合约部署结果")
                        .setMessage("部署成功！地址：" + result.getContractAddress())
                        .setPositiveButton("确定", null)
                        .show();
            }
        }
    }
}
