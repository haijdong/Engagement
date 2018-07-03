package com.tommy.base;


import android.content.Intent;



import java.io.File;
import java.io.IOException;

import cn.segi.framework.application.BaseApplication;
import cn.segi.framework.consts.FrameworkConst;

public class MyApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    protected void createDir() {
        super.createDir();
        //nomedia文件
        File nomediaFile = new File(FrameworkConst.FILE_PATH + File.separator + FrameworkConst.NOMEDIA);
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void uncaughtException(Thread thread, Throwable ex) {
        Intent in = new Intent(Intent.ACTION_MAIN);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        System.exit(0);
    }

    @Override
    public void terminateApplication() {
        super.terminateApplication();
    }

}
