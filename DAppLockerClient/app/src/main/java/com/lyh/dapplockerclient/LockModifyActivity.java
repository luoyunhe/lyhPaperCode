package com.lyh.dapplockerclient;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lyh.lockersc.Locker_sol_Locker;
import com.lyh.lockersc.Web3jUtil;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;

public class LockModifyActivity extends AppCompatActivity {

    private TextView name, contractAddr;
    private Boolean isRunning = false;

    private Boolean hasUserList = false;

    private RelativeLayout userListContainer;

    private AndroidTreeView treeView;

    private Credentials credentials = null;

    TreeNode userList;

    private Button btnDelLock, btnAddUser;
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
            Intent i = new Intent();
            i.putExtra("lock_info", info);
            setResult(-1, i);
            finish();
        });
        btnAddUser = findViewById(R.id.btn_add_user);
        if (!info.isImport()) {
            btnAddUser.setVisibility(View.VISIBLE);
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
            contractAddr.setText("未激活，点击我激活！");
            contractAddr.setOnClickListener(v -> {
                EditText input = new EditText(LockModifyActivity.this);
                 new AlertDialog.Builder(LockModifyActivity.this)
                         .setTitle("请输入激活序列！")
                         .setView(input)
                         .setPositiveButton("激活", (dialog, which) -> {
                             String str = input.getText().toString();
                             info.setContractAddr(str);
                             contractAddr.setText(str);
                             contractAddr.setClickable(false);
                         })
                         .setNegativeButton("取消", null).show();
            });
        } else {
            contractAddr.setText(info.getContractAddr());
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("lock_info", info);
        setResult(0, intent);
        super.onBackPressed();
    }
    class AddUserTask extends AsyncTask<Pair<String[], Credentials>, String, String> {


        @Override
        protected String doInBackground(Pair<String[], Credentials>... pairs) {
            String importInfoStr = pairs[0].first[0];
            String contractAddr = pairs[0].first[1];
            Credentials credentials = pairs[0].second;
            ImportInfo info = JSON.parseObject(importInfoStr, ImportInfo.class);
            HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
            Web3j web3 = Web3j.build(httpService);
            Locker_sol_Locker contract = Locker_sol_Locker.load(contractAddr, web3, credentials, new DefaultGasProvider());
            try {
                contract.addUser(info.getUserAddr(), info.getUserName(), info.getUserPubKey()).send();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
    class GetUserListTask extends AsyncTask<String, String, Tuple6<String, String, String, List<String>, String, String>> {

        @Override
        protected Tuple6<String, String, String, List<String>, String, String> doInBackground(String... pairs) {
            String contractAddr = info.getContractAddr();
            HttpService httpService =  new HttpService(Web3jUtil.INFURA_URL);
            Web3j web3 = Web3j.build(httpService);
            Locker_sol_Locker contract = Locker_sol_Locker.load(contractAddr, web3, credentials, new DefaultGasProvider());
            Tuple6<String, String, String, List<String>, String, String> res = null;
            try {
                res = contract.getUserInfo().send();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return res;
        }

        @Override
        protected void onPostExecute(Tuple6<String, String, String, List<String>, String, String> res) {
            super.onPostExecute(res);
            String names = res.getValue5();
            String pubKeys = res.getValue6();
            String[] nameArr = names.split("\n");
            String[] pubKeyArr = pubKeys.split("\n");
            for (int i = 0; i < nameArr.length; i++) {
                if (nameArr[i].equals("")) continue;
                TreeNode node = new TreeNode(new UserHolder.UserItem(nameArr[i], pubKeyArr[i]))
                        .setViewHolder(new UserHolder(LockModifyActivity.this));
                userList.addChild(node);
            }

            Log.d("res", res.getValue1());
            Log.d("res", res.getValue2());
            Log.d("res", res.getValue3());
            Log.d("res", res.getValue4().toString());
            Log.d("res", res.getValue5());
            Log.d("res", res.getValue6());
            isRunning = false;
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

class UserHolder extends TreeNode.BaseNodeViewHolder<UserHolder.UserItem> {
    public TextView tvName, tvPubKey;


    public UserHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, UserItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_user_node, null, false);
        tvName = view.findViewById(R.id.name);
        tvPubKey = view.findViewById(R.id.pub_key);

        tvName.setText(value.name);
        tvPubKey.setText(value.pubKey.substring(0, 25) + "...");
        return view;
    }


    public static class UserItem {
        public String name;
        public String pubKey;

        public UserItem(String name, String pubKey) {
            this.name = name;
            this.pubKey = pubKey;
        }
    }
}