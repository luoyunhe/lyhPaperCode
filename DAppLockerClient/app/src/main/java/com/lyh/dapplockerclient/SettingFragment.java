package com.lyh.dapplockerclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import jnr.ffi.annotations.In;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";



    private final String CLASSNAME = this.getClass().getName();

    private ArrayList<LinearLayout> linearLayouts = new ArrayList<>();

    private File walletDir = null;

    private Button btnLogout;

    private TextView toLogin;

    // TODO: Rename and change types of parameters
    private String mParam1;

//    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletDir = new File(getContext().getFilesDir().getPath() + "/MyWallet");
        if (!walletDir.exists()) {
            walletDir.mkdirs();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
        String userInfoJson = sp.getString(Util.USER_INFO_KEY, "");
        UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
        if (userInfo == null) {
            toLogin.setText("点击登陆");
            toLogin.setClickable(true);
        } else {
            toLogin.setText("你好！" + userInfo.getName());
            toLogin.setClickable(false);
            btnLogout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        linearLayouts.add((LinearLayout) v.findViewById(R.id.private_key_setting));
        linearLayouts.add((LinearLayout) v.findViewById(R.id.add_lock));
        linearLayouts.add((LinearLayout) v.findViewById(R.id.my_lock));
        btnLogout = v.findViewById(R.id.btn_logout);
        toLogin = v.findViewById(R.id.to_login);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
                sp.edit().putString(Util.USER_INFO_KEY, "").apply();
                toLogin.setText("点击登陆");
                toLogin.setClickable(true);
                btnLogout.setVisibility(View.INVISIBLE);
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        for (LinearLayout i : linearLayouts) {
            i.setOnClickListener(this);
        }
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.private_key_setting:
                Log.d(CLASSNAME, "click private key setting");
                dealClickPKS();
                break;
            case R.id.add_lock:
                Log.d(CLASSNAME, "click add lock");
                dealAddLock();
                break;
            case R.id.my_lock:
                Log.d(CLASSNAME, "click my lock");
                Intent intent = new Intent(getContext(), MyLockActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }
    }

    private void dealAddLock() {
        Intent intent = new Intent(getContext(), NewLockActivity.class);
        startActivity(intent);
    }

    private void dealClickPKS() {
        SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
        String walletFileName = sp.getString(Util.WALLET_FILE_NAME, "");
        if (walletFileName.equals("")) {
            doNewPKS();
        } else {
            doPKS(walletFileName);
        }
    }

    private void doPKS(final String walletFileName) {
        final EditText passwdET = new EditText(getContext());
        passwdET.setTransformationMethod(new PasswordTransformationMethod());
        new AlertDialog.Builder(getContext())
            .setTitle("请输入密码")
            .setView(passwdET)
            .setPositiveButton("确定", (dialog, which) -> {
                String passwd = passwdET.getText().toString();
                String fileName = Paths.get(getWalletDirString(), walletFileName).toString();
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
                SharedPreferences sp = getContext().getSharedPreferences("setting",
                        0);
                sp.edit().putString(Util.USER_ETH_ADDR_KEY, credentials.getAddress()).apply();
                Toast t = Toast.makeText(getContext(), "已经设置的私钥地址是：" + credentials.getAddress(), Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void doNewPKS() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View v = layoutInflater.inflate(R.layout.new_pk, null, false);
        final EditText pkET = v.findViewById(R.id.pk);
        final EditText passwdET = v.findViewById(R.id.passwd);
        AlertDialog.Builder builder = new  AlertDialog.Builder(getContext());
        builder.setTitle("设置私钥及密码");
        builder.setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String privateKey = pkET.getText().toString();
                String passwd = passwdET.getText().toString();
                Credentials credentials = Credentials.create(privateKey);
                ECKeyPair ecKeyPair = credentials.getEcKeyPair();
                String walletFileName = null;
                try {
                    walletFileName = WalletUtils.generateWalletFile(passwd, ecKeyPair, walletDir, false);
                } catch (CipherException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                SharedPreferences sp = getContext().getSharedPreferences("setting",
                        0);
                sp.edit().putString(Util.WALLET_FILE_NAME, walletFileName).apply();
                sp.edit().putString(Util.USER_ETH_ADDR_KEY, credentials.getAddress()).apply();
                Toast.makeText(getContext(), "添加成功！", Toast.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("取消", null).show();
    }

    private String getWalletDirString() {
        return walletDir.getAbsolutePath();
    }
}
