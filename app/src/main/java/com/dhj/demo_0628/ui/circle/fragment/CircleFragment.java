package com.dhj.demo_0628.ui.circle.fragment;

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

public class CircleFragment extends BaseFragment {

    public static CircleFragment newInstance(String id, String name) {
        CircleFragment fragment = new CircleFragment();
        Bundle data = new Bundle();
        data.putString(FusionIntent.EXTRA_DATA1, id);
        data.putString(FusionIntent.EXTRA_DATA2, name);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    protected void initViews() {
        mView = View.inflate(getActivity(), R.layout.fragment_circle, null);

    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }





}
