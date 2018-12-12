package com.synjones.huixinexiao;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.synjones.huixinexiao.ui.circle.fragment.CircleFragment;
import com.synjones.huixinexiao.ui.card.fragment.FriendFragment;
import com.synjones.huixinexiao.ui.home.fragment.HomeFragment;
import com.synjones.huixinexiao.ui.mine.fragment.MeFragment;
import com.tommy.base.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    public static final String fragment1Tag = "fragment1";
    public static final String fragment2Tag = "fragment2";
//    public static final String fragment3Tag = "fragment3";
    public static final String fragment4Tag = "fragment4";
    public static final String fragment5Tag = "fragment5";
    public static final String fragment6Tag = "fragment6";
    private Fragment fragment1, fragment2, fragment3, fragment4, fragment5, fragment6;
    public RadioGroup mRadioGroup;
    private FragmentManager mFragmentManager;
    private FragmentTransaction ft;
    private FragmentManager fm;


    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);





        mRadioGroup = (RadioGroup) findViewById(R.id.rg_group);
        mFragmentManager = getSupportFragmentManager();
        fm = getSupportFragmentManager();
        fragment1 = fm.findFragmentByTag(fragment1Tag);
        fragment2 = fm.findFragmentByTag(fragment2Tag);
//        fragment3 = fm.findFragmentByTag(fragment3Tag);
        fragment4 = fm.findFragmentByTag(fragment4Tag);
        fragment5 = fm.findFragmentByTag(fragment5Tag);
        fragment6 = fm.findFragmentByTag(fragment6Tag);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ft = fm.beginTransaction();

                if (fragment1 != null) {
                    ft.hide(fragment1);
                }
                if (fragment2 != null) {
                    ft.hide(fragment2);
                }
                if (fragment3 != null) {
                    ft.hide(fragment3);
                }
                if (fragment4 != null) {
                    ft.hide(fragment4);
                }
                if (fragment5 != null) {
                    ft.hide(fragment5);
                }
                if (fragment6 != null) {
                    ft.hide(fragment6);
                }

                switch (checkedId) {
                    case R.id.rb_home:
                        if (fragment1 == null) {
                            fragment1 = new HomeFragment();
                            ft.add(R.id.fl_container, fragment1, fragment1Tag);
                        } else {
                            ft.show(fragment1);
                        }
                        break;
                    case R.id.rb_found:
                        if (fragment2 == null) {
                            fragment2 = new CircleFragment();
                            ft.add(R.id.fl_container, fragment2, fragment2Tag);
                        } else {
                            ft.show(fragment2);
                        }
                        break;
                    case R.id.rb_love:
//                        if (fragment3 == null) {
//                            fragment3 = new OtherFragment();
//                            ft.add(R.id.fl_container, fragment3,
//                                    fragment3Tag);
//                        } else {
//                            ft.show(fragment3);
//                        }




                        break;
                    case R.id.rb_message:
                        if (fragment4 == null) {
                            fragment4 = new FriendFragment();
                            ft.add(R.id.fl_container, fragment4, fragment4Tag);
                        } else {
                            ft.show(fragment4);
                        }
                        break;
                    case R.id.rb_mine:
                        if (fragment5 == null) {
                            fragment5 = new MeFragment();
                            ft.add(R.id.fl_container, fragment5, fragment5Tag);
                        } else {
                            ft.show(fragment5);
                        }
                        break;
                    default:
                        break;
                }
                ft.commit();
            }
        });
        mRadioGroup.check(R.id.rb_home);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }


    // 点击两次退出程序
    private static final long EXIT_TIME = 2000;
    private long mLastRunBackgroundTime;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            long curTime = System.currentTimeMillis();
            if (mLastRunBackgroundTime == 0) {
                mLastRunBackgroundTime = curTime;
                show(R.string.exit_system);
            } else {
                if (curTime - mLastRunBackgroundTime > EXIT_TIME) {
                    show(R.string.exit_system);
                    mLastRunBackgroundTime = curTime;
                } else {
                    finish();
                }
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
