package com.lyh.dapplockerclient;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyh.lockersc.Locker_sol_Locker;
import com.lyh.lockersc.Web3jUtil;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LockModifyActivity extends AppCompatActivity {

    private TextView name, contractAddr;
    private Boolean isRunning = false;

    private Boolean hasUserList = false;

    private RelativeLayout userListContainer;

    private AndroidTreeView treeView;

    private Credentials credentials = null;

    TreeNode userList;

    private Button btnDelLock, btnAddUser, btnBindLock;
    LockInfo info;
    void getUserList() {
        isRunning = true;
        new GetUserListTask().execute("");
        Toast.makeText(this, "加载中", Toast.LENGTH_LONG).show();
        hasUserList = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_modify);
        Intent intent = getIntent();
        info = intent.getParcelableExtra("lock_info");
        userListContainer = findViewById(R.id.user_list_container);
        if (!info.isImport()) {
            TreeNode root = TreeNode.root();
            userList = new TreeNode(new UserContainerHolder.Item("用户列表"))
                    .setViewHolder(new UserContainerHolder(this));
            root.addChild(userList);
            treeView = new AndroidTreeView(this, root);
            treeView.setDefaultNodeClickListener((node, value) -> {
               if (node.getLevel() == 1) {
                   if (node.isSelected()){
                       return;
                   }
                   if (hasUserList) return;
                   if (credentials == null) {
                       final EditText passwdET = new EditText(this);
                       passwdET.setTransformationMethod(new PasswordTransformationMethod());
                       new android.app.AlertDialog.Builder(this)
                               .setTitle("请输入密码")
                               .setView(passwdET)
                               .setPositiveButton("确定", (dialog, which) -> {
                                   SharedPreferences sp = getSharedPreferences("setting", 0);
                                   String walletFileName = sp.getString(Util.WALLET_FILE_NAME, "");
                                   String passwd = passwdET.getText().toString();
                                   String fileName = Paths.get(Util.getWalletDirString(), walletFileName).toString();
                                   Credentials c = null;
                                   try {
                                       c = WalletUtils.loadCredentials(passwd, fileName);
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   } catch (CipherException e) {
                                       e.printStackTrace();
                                       Toast t = Toast.makeText(this, "密码错误，请重新尝试！", Toast.LENGTH_LONG);
                                       t.setGravity(Gravity.CENTER, 0, 0);
                                       t.show();
                                       return;
                                   }
                                    credentials = c;
                                    getUserList();
                               })
                               .setNegativeButton("取消", null)
                               .show();

                   } else {
                       getUserList();
                   }
               }
            });
            userListContainer.addView(treeView.getView());

        }
        name = findViewById(R.id.lock_name);
        btnDelLock = findViewById(R.id.btn_delete_lock);
        btnDelLock.setOnClickListener(v -> {
            new AlertDialog.Builder(LockModifyActivity.this)
                    .setTitle("警告")
                    .setMessage("是否确认删除？")
                    .setPositiveButton("是", (dialog, which) -> {
                        LockInfoDao dao = GreenDaoManager.getInstance().getDaoSession().getLockInfoDao();
                        dao.deleteByKey(info.getId());
                        finish();
                    })
                    .setNegativeButton("否", null).show();
        });

        btnBindLock = findViewById(R.id.btn_bind_lock);
        if (info.isImport()) {
            btnBindLock.setVisibility(View.INVISIBLE);
        }
        btnBindLock.setOnClickListener((v) -> {
            SharedPreferences sp = getSharedPreferences("setting", 0);
            String token = "Bearer " + sp.getString(Util.USER_TOKEN_KEY, "");
            LockService service = RetrofitMgr.getInstance().createService(LockService.class);
            SaltReq req = new SaltReq();
            req.contractAddr = info.getContractAddr();
            service.getSalt(token, req).enqueue(new Callback<SaltResp>() {
                @Override
                public void onResponse(Call<SaltResp> call, Response<SaltResp> response) {
                    SaltResp resp = response.body();
                    if (resp == null || resp.code != 0) {
                        Toast.makeText(LockModifyActivity.this, "服务异常", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject object = new JSONObject();
                    object.put("salt", resp.salt);
                    object.put("addr", info.getContractAddr());
                    Bitmap bm = Util.createQRCodeBitmap(object.toJSONString(), 600, 600,
                            "utf-8", "H", "1",
                            Color.BLACK, Color.WHITE);
                    ImageView iv = new ImageView(LockModifyActivity.this);
                    iv.setImageBitmap(bm);
                    new AlertDialog.Builder(LockModifyActivity.this)
                            .setTitle("绑定设备")
                            .setView(iv)
                            .setPositiveButton("确定", null).show();

                }

                @Override
                public void onFailure(Call<SaltResp> call, Throwable t) {

                }
            });
        });
        btnAddUser = findViewById(R.id.btn_add_user);
        if (!info.isImport()) {
            btnAddUser.setVisibility(View.VISIBLE);
        } else {
            btnAddUser.setVisibility(View.INVISIBLE);
        }
        btnAddUser.setOnClickListener(v -> {
            LayoutInflater layoutInflater = LayoutInflater.from(LockModifyActivity.this);
            View view = layoutInflater.inflate(R.layout.layout_add_user, null, false);
            final EditText input = view.findViewById(R.id.serial);
            final EditText etPasswd = view.findViewById(R.id.passwd);

            new AlertDialog.Builder(LockModifyActivity.this)
                    .setTitle("请输入用户序列和密码！")
                    .setView(view)
                    .setPositiveButton("添加", (dialog, which) -> {
                        if (isRunning) {
                            Toast.makeText(LockModifyActivity.this, "其它合约在运行，稍后再试！",
                                    Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            isRunning = true;
                            String [] strArr = new String[2];
                            strArr[0] = input.getText().toString();
                            strArr[1] = info.getContractAddr();
                            Credentials credentials = null;
                            SharedPreferences sp = getSharedPreferences("setting", 0);
                            String walletFileName = sp.getString(Util.WALLET_FILE_NAME, "");
                            String fileName = Paths.get(Util.getWalletDirString(), walletFileName).toString();
                            try {
                                String passwd = etPasswd.getText().toString();
                                credentials = WalletUtils.loadCredentials(passwd, fileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            } catch (CipherException e) {
                                e.printStackTrace();
                                Toast t = Toast.makeText(LockModifyActivity.this,
                                        "密码错误，请重新尝试！", Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                                return;
                            }
                            Pair<String[], Credentials> pair = new Pair<>(strArr, credentials);
                            new AddUserTask().execute(pair);
                            Toast.makeText(LockModifyActivity.this,
                                    "合约正在后台运行", Toast.LENGTH_LONG).show();
                        }

                    })
                    .setNegativeButton("取消", null).show();
        });

        contractAddr = findViewById(R.id.lock_contract_addr  );
        name.setText(info.getName());
        if (info.getContractAddr().equals("")) {
            contractAddr.setText("未激活");
//            contractAddr.setOnClickListener(v -> {
//                EditText input = new EditText(LockModifyActivity.this);
//                 new AlertDialog.Builder(LockModifyActivity.this)
//                         .setTitle("请输入激活序列！")
//                         .setView(input)
//                         .setPositiveButton("激活", (dialog, which) -> {
//                             String str = input.getText().toString();
//                             info.setContractAddr(str);
//                             LockInfoDao dao = GreenDaoManager.getInstance().getDaoSession().getLockInfoDao();
//                             dao.update(info);
//                             contractAddr.setText(str);
//                             contractAddr.setClickable(false);
//                         })
//                         .setNegativeButton("取消", null).show();
//            });
        } else {
            contractAddr.setText(info.getContractAddr());
        }


    }

    class AddUserTask extends AsyncTask<Pair<String[], Credentials>, String, String> {


        @Override
        protected String doInBackground(Pair<String[], Credentials>... pairs) {
            String importInfoStr = pairs[0].first[0];
            String contractAddr = pairs[0].first[1];
            Credentials credentials = pairs[0].second;
            ImportInfo info = null;
            try {
                info = JSON.parseObject(importInfoStr, ImportInfo.class);
            }
            catch ( Exception e ) {
                return "error";
            }
            HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
            Web3j web3 = Web3j.build(httpService);
            Locker_sol_Locker contract = Locker_sol_Locker.load(contractAddr, web3, credentials, new DefaultGasProvider());
            try {
                contract.addUser(info.getUserAddr(), info.getUserName(), info.getUserPubKey()).send();
                String key = info.getKey();
                String passwd = info.getPasswd();
                LockService service = RetrofitMgr.getInstance().createService(LockService.class);
                SetImportReq req = new SetImportReq();
                req.activateStr = contractAddr;
                req.key = info.getKey();
                req.passwd = info.getPasswd();
                service.setImport(req).enqueue(new Callback<SetImportResp>() {
                    @Override
                    public void onResponse(Call<SetImportResp> call, Response<SetImportResp> response) {
                       Log.d("set import", JSON.toJSONString(response.body()));
                    }

                    @Override
                    public void onFailure(Call<SetImportResp> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && s.equals("error")) {
                isRunning = false;
                new AlertDialog.Builder(LockModifyActivity.this)
                        .setTitle("error")
                        .setMessage("导入序列不合法")
                        .show();
                return;
            }
            isRunning = false;
            new AlertDialog.Builder(LockModifyActivity.this)
                    .setTitle("请复制激活序列给该用户！")
                    .setMessage(info.getContractAddr())
                    .setPositiveButton("复制", (dialog, which) -> {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(null, info.getContractAddr());
                        clipboard.setPrimaryClip(clipData);

                    })
                    .show();

        }
    }
    class GetUserListTask extends AsyncTask<String, String, Tuple3<List<String>, String, String>> {

        @Override
        protected Tuple3<List<String>, String, String> doInBackground(String... pairs) {
            String contractAddr = info.getContractAddr();
            HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
            Web3j web3 = Web3j.build(httpService);
            Locker_sol_Locker contract = Locker_sol_Locker.load(contractAddr, web3, credentials, new DefaultGasProvider());
            Tuple3<List<String>, String, String> res = null;
            try {
                res = contract.getUserInfo().send();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return res;
        }

        @Override
        protected void onPostExecute(Tuple3<List<String>, String, String> res) {
            super.onPostExecute(res);
            List<String> addrs = res.getValue1();
            String names = res.getValue2();
            String pubKeys = res.getValue3();
            String[] nameArr = names.split("\n");
            String[] pubKeyArr = pubKeys.split("\n");
            for (int i = 1; i < nameArr.length; i++) {
                TreeNode node = new TreeNode(new UserItem(nameArr[i], pubKeyArr[i], addrs.get(i - 1)))
                        .setViewHolder(LockModifyActivity.this.new UserHolder(LockModifyActivity.this));
                userList.addChild(node);
            }

            Log.d("res1", res.getValue1().toString());
            Log.d("res2", res.getValue2());
            Log.d("res3", res.getValue3());
            Log.d("res4", "res4");
            userList.getViewHolder().toggle(false);
            userList.getViewHolder().toggle(true);
            isRunning = false;
        }
    }
    class DelUserTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String userAddr = params[0];
            HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
            Web3j web3 = Web3j.build(httpService);
            Locker_sol_Locker contract = Locker_sol_Locker.load(info.getContractAddr(),
                    web3, credentials, new DefaultGasProvider());
            try {
                contract.delUser(userAddr).send();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    class UserHolder extends TreeNode.BaseNodeViewHolder<UserItem> {
        public TextView tvName, tvPubKey, tvSign;
        private Context context;


        public UserHolder(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public View createNodeView(final TreeNode node, UserItem value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_user_node, null, false);
            tvName = view.findViewById(R.id.name);
            tvPubKey = view.findViewById(R.id.pub_key);
            tvSign = view.findViewById(R.id.node_del);

            tvSign.setOnClickListener((v) -> {
                new AlertDialog.Builder(context)
                        .setTitle("警告")
                        .setMessage("是否删除")
                        .setPositiveButton("确定", ((dialog, which) -> {
                            String userAddr = value.address;
                            new DelUserTask().execute(userAddr);
                            treeView.removeNode(node);
                        }))
                        .setNegativeButton("取消", null).show();

            });

            tvName.setText(value.name);
            tvPubKey.setText(value.pubKey.substring(0, 25) + "...");
            return view;
        }
    }
    public static class UserItem {
        public String name;
        public String pubKey;
        public String address;

        public UserItem(String name, String pubKey, String address) {
            this.name = name;
            this.pubKey = pubKey;
            this.address = address;
        }
    }
}
class UserContainerHolder extends TreeNode.BaseNodeViewHolder<UserContainerHolder.Item> {
    private TextView tvValue;
    private TextView tvSign;

    public UserContainerHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, Item value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_lock_container_node, null, false);
        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        tvSign = view.findViewById(R.id.node_sign);

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (active) {
            tvSign.setText("-");
        } else {
            tvSign.setText("+");
        }
    }

    public static class Item {
        public String text;

        public Item(String text) {
            this.text = text;
        }
    }
}

