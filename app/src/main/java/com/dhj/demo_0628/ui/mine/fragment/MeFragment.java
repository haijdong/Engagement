package com.dhj.demo_0628.ui.mine.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhj.demo_0628.R;
import com.dhj.demo_0628.common.FusionIntent;
import com.segi.view.imageview.CircleImageView;
import com.tommy.base.base.BaseFragment;


/**
 * Created by donghaijun on 2017/12/11.
 */

public class MeFragment extends BaseFragment {

    public static MeFragment newInstance(String id, String name) {
        MeFragment fragment = new MeFragment();
        Bundle data = new Bundle();
        data.putString(FusionIntent.EXTRA_DATA1, id);
        data.putString(FusionIntent.EXTRA_DATA2, name);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    protected void initViews() {
        mView = View.inflate(getActivity(), R.layout.fragment_me, null);

    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }





}
