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

### LooperMonitor
这个类实现了MessageQueue.IdleHandler接口，用处是在当MessageQueue发现当前没有更多消息可以处理的时候, 则顺便干点别的事情。  
在LooperMonitor的queueIdle()方法中替换MainLooper中的Printer对象，通过添加的自定义Printer对象监听message的开始处理和结束处理并通知给自身的监听器。

### AppMethodBeat
1. 这个类替换了Activity中默认的Handler.Callback，在Handler处理Message的时候记录了各种时间参数。
2. 自身也能监听Message的处理。

### UIThreadMonitor
1. 在初始化的时候会向LooperMonitor注册自身为监听器，在Looper中开始处理message和处理结束的时候分别调用AppMethodBeat.i和AppMethodBeat.o。
2. 自身启动的时候会把自身作为一个Runnable根据状态添加到对应的反射的对象里。
3. run()方法里面会设置doFrameBegin状态值。
4. 在Message处理结束的监听中调用doFrameEnd()-->addFrameCallback()再次反射调用对应状态的对象执行自身的run方法。

### AnrTracer
ANR检测方法，在监听到Message开始处理的时候向Matrix维护的HandlerThread中添加一个延时任务，延时时间为5秒，任务执行的是上报一些当前Activity，进程相关的信息。当监听到Message处理结束的时候移除任务。


