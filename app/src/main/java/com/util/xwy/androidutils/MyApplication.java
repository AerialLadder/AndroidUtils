package com.util.xwy.androidutils;

import android.app.Application;

import com.wizz.libutil.CrashHandler;
import com.wizz.libutil.CrashHandlerConfig;

/**
 * =====================================================================================
 * Summary:
 * File: MyApplication.java
 *
 * @author xwy
 * @date 2019/7/13 10:53
 * =====================================================================================
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(new CrashHandlerConfig.Builder()
                .setTargetTimes(1).setCrashProcess(null)
                .build()).init(this);
    }

}
