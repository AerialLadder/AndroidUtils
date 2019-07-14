# AndroidUtils
android常用工具，所有提供的API均经过本人亲自测试后发布。


#### 1.CrashHandler
> 功能：在app连续Crash的时候能够清除app数据或者进行自定义的补救措施，恢复app的正常运行。  

使用方法：
```
1.在自定义的Application的onCreate()中初始化
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
    可以进行默认初始化，达到3次crash清除app数据，
    boolean result = FileUtil.deleteDirectory(mContext.getFilesDir().getAbsolutePath(), false) &
            FileUtil.deleteDirectory(mContext.getCacheDir().getAbsolutePath(), false) &
            FileUtil.deleteDirectory(mContext.getExternalCacheDir().getAbsolutePath(), false);
    也可以尽心自定义初始化，自定义crash次数和对应的操作。
```