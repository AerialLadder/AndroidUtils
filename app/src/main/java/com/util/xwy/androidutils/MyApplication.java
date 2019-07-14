package com.util.xwy.androidutils;

import android.app.Application;

import com.util.crash.CrashHandler;
import com.util.crash.CrashHandlerConfig;

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
        CrashHandler.getInstance().init(this);

        /*CrashHandler.getInstance(new CrashHandlerConfig.Builder()
                .setTargetTimes(1)//预定的crash次数进行处理
                .setCrashProcess(new CrashHandler.CrashProcess() {
                    @Override
                    public boolean process() {
                        //达到预定的crash次数触发的操作
                        return true;
                    }
                })
                .build()).init(this);*/
    }
}
