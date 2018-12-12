package com.synjones.huixinexiao.ui.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.synjones.huixinexiao.R;
import com.synjones.huixinexiao.common.FusionIntent;
import com.tommy.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by donghaijun on 2017/12/11.
 */

public class HomeFragment extends BaseFragment {


    public static HomeFragment newInstance(String id, String name) {
        HomeFragment fragment = new HomeFragment();
        Bundle data = new Bundle();
        data.putString(FusionIntent.EXTRA_DATA1, id);
        data.putString(FusionIntent.EXTRA_DATA2, name);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    protected void initViews() {
        mView = View.inflate(getActivity(), R.layout.fragment_home, null);
        ((TextView)findViewById(R.id.text)).setText("majeshi\nshazi");
        ((TextView)findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "点击了菜单:" + "majeshi\nshazi", Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }





}
