package com.synjones.huixinexiao.ui.card.fragment;

import android.os.Bundle;
import android.view.View;

import com.synjones.huixinexiao.R;
import com.synjones.huixinexiao.common.FusionIntent;
import com.tommy.base.base.BaseFragment;


/**
 * Created by donghaijun on 2017/12/11.
 */

public class FriendFragment extends BaseFragment {

    public static FriendFragment newInstance(String id, String name) {
        FriendFragment fragment = new FriendFragment();
        Bundle data = new Bundle();
        data.putString(FusionIntent.EXTRA_DATA1, id);
        data.putString(FusionIntent.EXTRA_DATA2, name);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    protected void initViews() {
        mView = View.inflate(getActivity(), R.layout.fragment_friend, null);

    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }





}
