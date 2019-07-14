package com.wizz.libutil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * =====================================================================================
 * Summary: 全局异常管理
 * File: CrashHandler.java
 *
 * @author xwy
 * @date 2019/7/13 10:45
 * =====================================================================================
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * context
     */
    private Context mContext;

    private String TAG = this.getClass().getSimpleName();

    private static CrashHandler mInstance;
    private static String PREFS_NAME = "crashTimesCache";
    private static String CRASH_TIMES_KEY = "crashTimes";

    private WeakReference<Activity> lastActivityCreated;

    private int targetTimes = 3;
    private CrashProcess crashProcess;

    private CrashHandler() {
    }
    private CrashHandler(CrashHandlerConfig config) {
        this.targetTimes = config.targetTimes;
        this.crashProcess = config.crashProcess;
    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized CrashHandler getInstance() {
        if (null == mInstance) {
            mInstance = new CrashHandler();
        }
        return mInstance;
    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized CrashHandler getInstance(final CrashHandlerConfig config) {
        if (null == mInstance) {
            mInstance = new CrashHandler(config);
        }
        return mInstance;
    }

    public void lastActivityManager(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                lastActivityCreated = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void init(Application context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
        lastActivityManager(context);

        //启动后10秒重置crash次数
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(CRASH_TIMES_KEY, 0);
                editor.commit();
            }
        }, 10 * 1000);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果自己没处理交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Log.e(TAG, "自己处理");
            //自己处理
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Activity activity = lastActivityCreated.get();
            if (null != activity) {
                activity.finish();
            }
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 处理异常
     *
     * @return 处理了该异常返回true, 否则false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        SharedPreferences sp = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        int crashTimes = sp.getInt(CRASH_TIMES_KEY, 0) + 1;
        editor.putInt(CRASH_TIMES_KEY, crashTimes);
        editor.commit();
        if (crashTimes >= targetTimes) {
            if (null != crashProcess){
                crashProcess.process();
            }else{
                crashProcess = new CrashProcess() {
                    @Override
                    public boolean process() {
                        return mInstance.deleFilesDir();
                    }
                };
                crashProcess.process();
            }
        }
        return true;
    }



    private boolean deleFilesDir() {
        boolean result = FileUtil.deleteDirectory(mContext.getFilesDir().getAbsolutePath(), false) &
                FileUtil.deleteDirectory(mContext.getCacheDir().getAbsolutePath(), false) &
                FileUtil.deleteDirectory(mContext.getExternalCacheDir().getAbsolutePath(), false);
        if (result)
            Log.e(TAG, "删除成功");
        return result;
    }

    public static interface CrashProcess{
        public boolean process();
    }
}
