package com.synjones.huixinexiao.ui.school.activity;

import android.view.View;
import android.widget.TextView;

import com.synjones.huixinexiao.R;
import com.tommy.base.base.BaseActivity;

public class AnimActivity extends BaseActivity implements View.OnClickListener {

    private TextView anim;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_anim);

        anim = findViewById(R.id.anim);
    }

    @Override
    protected void initEvents() {
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.trans).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alpha:
                break;
            case R.id.scale:
                break;
            case R.id.rotate:

                break;
            case R.id.trans:

                break;
        }
    }
}
