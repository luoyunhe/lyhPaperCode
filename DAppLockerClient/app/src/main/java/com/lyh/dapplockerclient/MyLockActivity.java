package com.lyh.dapplockerclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

public class MyLockActivity extends AppCompatActivity {

    private RelativeLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lock);
        container = findViewById(R.id.container);
        SharedPreferences sp = getSharedPreferences("setting", 0);
        String lockInfoListStr = sp.getString(Util.LOCK_INFO_KEY, "[]");
        List<LockInfo> lockInfoList = JSON.parseArray(lockInfoListStr, LockInfo.class);
        ArrayList<LockInfo> newLockInfoList = new ArrayList<>();
        ArrayList<LockInfo> importLockInfoList = new ArrayList<>();
        for (LockInfo i : lockInfoList) {
            if (i.isImport()) {
                importLockInfoList.add(i);
            } else {
                newLockInfoList.add(i);
            }
        }
        TreeNode root = TreeNode.root();

        TreeNode newLockItem = new TreeNode(new LockContainerHolder.LockContainerItem("新建的锁")).setViewHolder(new LockContainerHolder(this));
        TreeNode importLockItem = new TreeNode(new LockContainerHolder.LockContainerItem("导入的锁")).setViewHolder(new LockContainerHolder(this));

        for (LockInfo i : newLockInfoList) {
            TreeNode node = new TreeNode(new LockHolder.LockItem(i.getName(), i.getContractAddr())).setViewHolder(new LockHolder(this));
            newLockItem.addChild(node);
        }
        for (LockInfo i : importLockInfoList) {
            TreeNode node = new TreeNode(new LockHolder.LockItem(i.getName(), i.getContractAddr())).setViewHolder(new LockHolder(this));
            importLockItem.addChild(node);
        }




        root.addChildren(newLockItem, importLockItem);




        AndroidTreeView tView = new AndroidTreeView(this, root);
        container.addView(tView.getView());
        EditText et = new EditText(this);


    }
}
class LockContainerHolder extends TreeNode.BaseNodeViewHolder<LockContainerHolder.LockContainerItem> {
    private TextView tvValue;
    private TextView tvSign;

    public LockContainerHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, LockContainerItem value) {
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

    public static class LockContainerItem {
        public String text;

        public LockContainerItem(String text) {
            this.text = text;
        }
    }
}

class LockHolder extends TreeNode.BaseNodeViewHolder<LockHolder.LockItem> {
    private TextView tvName, tvContractAddr;
    private TextView tvSign;


    public LockHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, LockItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_lock_node, null, false);
        tvName = view.findViewById(R.id.name);
        tvContractAddr = view.findViewById(R.id.contract_addr);
        tvSign = view.findViewById(R.id.node_sign);
        tvName.setText(value.name);
        tvContractAddr.setText(value.contractAddr.equals("") ? "未激活" : value.contractAddr);
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

    public static class LockItem {
        public String name;
        public String contractAddr;

        public LockItem(String name, String contractAddr) {
            this.name = name;
            this.contractAddr = contractAddr;
        }
    }
}

