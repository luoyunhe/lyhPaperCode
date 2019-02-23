package com.lyh.dapplockerclient;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lyh.lockersc.Locker_sol_Locker;
import com.lyh.lockersc.Web3jUtil;

import org.json.JSONObject;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLReason;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;

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
        String userInfoStr = sp.getString(Util.USER_INFO_KEY, "");
        UserInfo info = JSON.parseObject(userInfoStr, UserInfo.class);

        String pubKeyStr = sp.getString(Util.USER_PUB_KEY_KEY, "");
        if (pubKeyStr.equals("")) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Util.KEY_ALGORITHM);
                keyPairGenerator.initialize(512);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                String pubStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                pubKeyStr = pubStr;
                String priStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
                sp.edit().putString(Util.USER_PUB_KEY_KEY, pubStr)
                        .putString(Util.USER_PRI_KEY_KEY, priStr).apply();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        final String pubKey = pubKeyStr;

        String newLockName = etNewLock.getText().toString();

        final EditText passwdET = new EditText(getContext());
        passwdET.setTransformationMethod(new PasswordTransformationMethod());
        new AlertDialog.Builder(getContext())
                .setTitle("请输入密码")
                .setView(passwdET)
                .setPositiveButton("确定", (dialog, which) -> {
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

                    String[] tmp = new String[3];
                    tmp[0] = newLockName;
                    tmp[1] = info.getName();
                    tmp[2] = pubKey;
                    Pair<Credentials, String[]> pair = new Pair<>(credentials, tmp);
                    new DeploySC().execute(pair);
                    Toast.makeText(getContext(), "合约已经在后台部署！", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

     private class DeploySC extends AsyncTask<Pair<Credentials, String[]>, String, Pair<Locker_sol_Locker, String>> {
        @Override
        protected Pair<Locker_sol_Locker, String> doInBackground(Pair<Credentials, String[]>... params) {
            Pair<Credentials, String[]> pair = params[0];
            Credentials credentials = pair.first;
            Pair<Locker_sol_Locker, String> result = null;
            try {
                HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
                Web3j web3 = Web3j.build(httpService);
                Locker_sol_Locker contract = Locker_sol_Locker.deploy(web3, credentials, new DefaultGasProvider(),
                        pair.second[2], pair.second[1]).send();
                result = new Pair<>(contract, pair.second[0]);
                web3.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Pair<Locker_sol_Locker, String> result) {
            super.onPostExecute(result);
            if (result == null) {
                new AlertDialog.Builder(getContext())
                        .setTitle("合约部署结果")
                        .setMessage("部署失败")
                        .setPositiveButton("确定", null)
                        .show();
                Toast.makeText(getContext(), "合约部署失败，请重试！", Toast.LENGTH_LONG).show();
            } else {
                Log.d("sc", "合约地址：" + result.first.getContractAddress());
                Toast.makeText(getContext(), result.first.getContractAddress(), Toast.LENGTH_LONG).show();

                LockInfo lockInfo = new LockInfo();
                lockInfo.setName(result.second);
                lockInfo.setContractAddr(result.first.getContractAddress());
                lockInfo.setImport(false);

                DaoSession daoSession =  GreenDaoManager.getInstance().getDaoSession();
                LockInfoDao lockInfoDao = daoSession.getLockInfoDao();
                lockInfoDao.insert(lockInfo);

                new AlertDialog.Builder(getContext())
                        .setTitle("合约部署结果")
                        .setMessage("部署成功！地址：" + result.first.getContractAddress())
                        .setPositiveButton("确定", null)
                        .show();
            }
        }
    }
}
