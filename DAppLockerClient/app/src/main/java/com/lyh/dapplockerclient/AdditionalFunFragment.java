package com.lyh.dapplockerclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AdditionalFunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditionalFunFragment extends Fragment {

    private RelativeLayout container;

    private AndroidTreeView tView;
    private ArrayList<LockInfo> newLockInfoList;
    private ArrayList<LockInfo> importLockInfoList;


    public AdditionalFunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AdditionalFunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdditionalFunFragment newInstance(String param1) {
        AdditionalFunFragment fragment = new AdditionalFunFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_additional_fun, c, false);

        container = v.findViewById(R.id.container);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        container.removeAllViews();

        LockInfoDao dao = GreenDaoManager.getInstance().getDaoSession().getLockInfoDao();
        List<LockInfo> lockInfoList = dao.queryBuilder().build().list();
        newLockInfoList = new ArrayList<>();
        importLockInfoList = new ArrayList<>();
        for (LockInfo i : lockInfoList) {
            if (i.isImport()) {
                importLockInfoList.add(i);
            } else {
                newLockInfoList.add(i);
            }
        }
        TreeNode root = TreeNode.root();

        TreeNode newLockItem = new TreeNode(new LockContainerHolder.LockContainerItem("新建的锁")).setViewHolder(new LockContainerHolder(getContext()));
//        TreeNode importLockItem = new TreeNode(new LockContainerHolder.LockContainerItem("导入的锁")).setViewHolder(new LockContainerHolder(getContext()));

        for (LockInfo i : newLockInfoList) {
            TreeNode node = new TreeNode(new LockHolder.LockItem(i.getName(), i.getContractAddr())).setViewHolder(new LockHolder(getContext()));
            newLockItem.addChild(node);
        }
//        for (LockInfo i : importLockInfoList) {
//            TreeNode node = new TreeNode(new LockHolder.LockItem(i.getName(), i.getContractAddr())).setViewHolder(new LockHolder(getContext()));
//            importLockItem.addChild(node);
//        }

        root.addChildren(newLockItem);

        tView = new AndroidTreeView(getContext(), root);
        tView.setDefaultNodeClickListener((node, value) -> {
            if (node.getLevel() != 2) {
                return;
            }
            LockInfo item;
            if (node.getParent().getId() == 1) {
                item = newLockInfoList.get(node.getId() - 1);
            } else {
                item = importLockInfoList.get(node.getId() - 1);
            }
            Intent intent = new Intent(getActivity(), LoggingActivity.class);

            intent.putExtra("lock_info", item);
            startActivity(intent);

        });
        container.addView(tView.getView());
    }
}
