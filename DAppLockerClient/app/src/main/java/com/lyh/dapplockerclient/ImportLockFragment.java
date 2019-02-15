package com.lyh.dapplockerclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImportLockFragment extends Fragment implements View.OnClickListener {

    private Button btn_cancel, btn_new;


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
                break;
            default:
                return;
        }
    }
}
