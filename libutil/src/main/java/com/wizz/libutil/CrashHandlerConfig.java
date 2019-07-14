package com.wizz.libutil;

import android.os.Build;

/**
 * =====================================================================================
 * Summary:
 * File: CrashHandlerConfig.java
 *
 * @author xwy
 * @date 2019/7/14 14:07
 * =====================================================================================
 */
public final class CrashHandlerConfig {
     int targetTimes = 3;
     CrashHandler.CrashProcess crashProcess;

    private CrashHandlerConfig() {
    }

    private CrashHandlerConfig(int targetTimes, CrashHandler.CrashProcess crashProcess) {
        this.targetTimes = targetTimes;
        this.crashProcess = crashProcess;
    }

    public static final class Builder{
        private int targetTimes = 3;
        private CrashHandler.CrashProcess crashProcess;
        private CrashHandlerConfig config;

        public Builder(){
            config = new CrashHandlerConfig();
        }
        public Builder setTargetTimes(int targetTimes){
            this.targetTimes = targetTimes;
            return this;
        }
        public Builder setCrashProcess(CrashHandler.CrashProcess crashProcess){
            this.crashProcess = crashProcess;
            return this;
        }

        public CrashHandlerConfig build(){
            config.targetTimes = this.targetTimes;
            config.crashProcess = this.crashProcess;
            return config;
        }
    }
}
