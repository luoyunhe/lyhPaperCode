package com.lyh.dapplockerclient;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImportLockFragment extends Fragment implements View.OnClickListener {

    private Button btn_cancel, btn_new;
    private EditText etImportInfo, etNewLock;


    public ImportLockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_import_lock, container, false);

        btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_new = v.findViewById(R.id.btn_new);
        etImportInfo = v.findViewById(R.id.import_info);
        etNewLock = v.findViewById(R.id.new_lock_name);

        btn_cancel.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
        String pubKey = sp.getString(Util.USER_PUB_KEY_KEY, "");
        String ethAddr = sp.getString(Util.USER_ETH_ADDR_KEY, "");
        if (pubKey.equals("")) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Util.KEY_ALGORITHM);
                keyPairGenerator.initialize(512);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                String pubStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                pubKey = pubStr;
                String priStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
                sp.edit().putString(Util.USER_PUB_KEY_KEY, pubStr)
                        .putString(Util.USER_PRI_KEY_KEY, priStr).apply();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                etImportInfo.setText("发生未知异常！");
                return v;
            }
        }
        String userInfoJson = sp.getString(Util.USER_INFO_KEY, "");
        UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
        ImportInfo importInfo = new ImportInfo();
        importInfo.setUserName(userInfo.getName());
        importInfo.setUserPubKey(pubKey);
        importInfo.setUserAddr(ethAddr);
        LockService service = RetrofitMgr.getInstance().createService(LockService.class);
        service.genImport().enqueue(new retrofit2.Callback<ImportResp>() {
            @Override
            public void onResponse(Call<ImportResp> call, Response<ImportResp> response) {
                if (response.body() == null || response.body().code != 0) {
                    Toast.makeText(getContext(), "服务异常", Toast.LENGTH_LONG).show();
                } else {
                    importInfo.setKey(response.body().key);
                    importInfo.setPasswd(response.body().passwd);
                    String importInfoJson = JSON.toJSONString(importInfo);
                    etImportInfo.setText(importInfoJson);
                }
            }

            @Override
            public void onFailure(Call<ImportResp> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                getActivity().finish();
                break;
            case R.id.btn_new:
                String lockName = etNewLock.getText().toString();
                String info = etImportInfo.getText().toString();
                ImportInfo importInfo = JSON.parseObject(info, ImportInfo.class);
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, info);
                clipboard.setPrimaryClip(clipData);

                LockInfo lockInfo = new LockInfo();
                lockInfo.setName(lockName);
                lockInfo.setImport(true);
                lockInfo.setContractAddr("");
                lockInfo.setImportTaskId(importInfo.getKey());

                GreenDaoManager.getInstance().getDaoSession().getLockInfoDao().insert(lockInfo);


                getActivity().finish();
                break;
            default:
                return;
        }
    }
}
