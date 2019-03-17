package com.lyh.dapplockerclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.WalletUtils.generateWalletFile;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    final private String ClASSNAME = this.getClass().getSimpleName();

    private ListView lvLock;





    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lvLock = view.findViewById(R.id.lv_lock);
//        TextView tvHeader = new TextView(getContext());
//        tvHeader.setText("点击获取开锁二维码");
//        lvLock.addHeaderView(tvHeader);

        List<Map<String, String>> listMaps= new ArrayList<>();
        Map<String, String> map=new HashMap<String, String>();
        map.put("first", "第一句");
        map.put("second", "第二句");
        listMaps.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), listMaps,
                android.R.layout.simple_expandable_list_item_2, new String[]{"first", "second"},
                new int[]{android.R.id.text1, android.R.id.text2});
        lvLock.setAdapter(simpleAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LockInfoDao dao = GreenDaoManager.getInstance().getDaoSession().getLockInfoDao();
        List<LockInfo> lockInfoList = dao.queryBuilder().build().list();
        List<Map<String, String>> listMaps= new ArrayList<>();
        for (LockInfo info : lockInfoList) {
            if (info.isImport() && info.getContractAddr().equals("")) {
                String key = info.getImportTaskId();
                LockService service = RetrofitMgr.getInstance().createService(LockService.class);
                service.getImport(key).enqueue(new Callback<GetImportResp>() {
                    @Override
                    public void onResponse(Call<GetImportResp> call, Response<GetImportResp> response) {
                        GetImportResp resp = response.body();
                        if (resp == null || resp.code != 0) {
                            Toast.makeText(getContext(), "get result error", Toast.LENGTH_LONG);
                            return;
                        }
                        info.setContractAddr(resp.activateStr);
                        dao.update(info);
                    }

                    @Override
                    public void onFailure(Call<GetImportResp> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            if (info.isImport() && !info.getContractAddr().equals("") || !info.isImport()) {
                Map<String, String> map = new HashMap<>();
                map.put("first", info.getName());
                map.put("second", info.getContractAddr());
                listMaps.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listMaps,
                android.R.layout.simple_expandable_list_item_2, new String[]{"first", "second"},
                new int[]{android.R.id.text1, android.R.id.text2});
        lvLock.setAdapter(adapter);
        lvLock.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = view.findViewById(android.R.id.text2);
        String address = tv.getText().toString();
        SharedPreferences sp = getContext().getSharedPreferences("setting", 0);
        String priKey = sp.getString(Util.USER_PRI_KEY_KEY, "");
        String pubKey = sp.getString(Util.USER_PUB_KEY_KEY, "");
        String userAddr = sp.getString(Util.USER_ETH_ADDR_KEY, "");
        String token = "Bearer " + sp.getString(Util.USER_TOKEN_KEY, "");

        LockService service = RetrofitMgr.getInstance().createService(LockService.class);
        service.getRandomStr(token, address).enqueue(new Callback<RanStrResp>() {
            @Override
            public void onResponse(Call<RanStrResp> call, Response<RanStrResp> response) {
                RanStrResp resp = response.body();
                if (resp == null || resp.code != 0) {
                    Toast.makeText(getContext(), "服务异常", Toast.LENGTH_LONG).show();
                    return;
                }
                String randomStr = resp.ranStr;
                byte[] key = Base64.getDecoder().decode(priKey);
                Log.d("RSA pri:", priKey);
                String sign = "";
                try {
                    byte[] signByte = Util.encryptByPrivateKey(randomStr.getBytes("utf-8"), key);
                    sign = Base64.getEncoder().encodeToString(signByte);
                    Log.d("RSA sign", sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();
                object.put("sign", sign);
                object.put("addr", userAddr);
                object.put("cmd", "open");
                String jsonStr = object.toJSONString();
                Bitmap bm = Util.createQRCodeBitmap(jsonStr, 600, 600,
                        "utf-8", "H", "1",
                        Color.BLACK, Color.WHITE);
                ImageView iv = new ImageView(getContext());
                iv.setImageBitmap(bm);
                new AlertDialog.Builder(getContext())
                        .setTitle("扫码开锁")
                        .setView(iv)
                        .setPositiveButton("确定", null).show();
            }

            @Override
            public void onFailure(Call<RanStrResp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "服务异常", Toast.LENGTH_LONG).show();

            }
        });
    }
}
