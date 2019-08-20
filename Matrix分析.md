# Matrix
## Matrix
1. 通过构造者模式创建对象。  
2. 对象初始化的时候会添加所有的Plugin，并对每个Plugin进行初始化。  
3. AppActiveMatrixDelegate.INSTANCE.init(application);AppActiveMatrixDelegate单例对象进行初始化。  

### AppActiveMatrixDelegate
1. 初始化的时候注册了两个监听
```
        //监听内存不足的情况
        application.registerComponentCallbacks(controller);
        //监听activity生命周期
        application.registerActivityLifecycleCallbacks(controller);
```
2. 在activity的生命周期内
    1. onActivityStarted被监听，然后更新当前activity的名字，如果当前App不在前台，则通知所有监听器App将要在前台。
    2. onActivityStopped被监听，如果当前activity栈中没有activity存在（通过反射获取activity栈），则通知所有监听器App即进入后台。

3. 在内存低的时候并为TRIM_MEMORY_UI_HIDDEN情况下，通知所有监听器App在后台。

### MatrixHandlerThread
1. 里面包含了一个单独的HandlerThread、Handler和主线程的Handler。

### Plugin 所有插件的基类
1. 能够在plugin的各个生命周期的时候通知到自己的监听器。

# TracePlugin
## TracePlugin
1. 初始化的时候添加了四个具体的功能类
```
AnrTracer  ANR检测
FrameTracer   帧率检测
EvilMethodTracer  超时的方法检测
StartupTracer  
```

2. 两个核心类AppMethodBeat和UIThreadMonitor
### UIThreadMonitor
1. 


