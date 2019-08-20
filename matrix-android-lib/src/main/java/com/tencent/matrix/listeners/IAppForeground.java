package com.tencent.matrix.listeners;

public interface IAppForeground {

    /**
     * 是否在展示（前景）
     * @param isForeground
     */
    void onForeground(boolean isForeground);
}
