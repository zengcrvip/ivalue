package com.axon.market.common.monitor;

/**
 * Created by chenyu on 2016/5/31.
 */
public class FileAlterationMonitor implements Runnable
{
    private FileAlterationObserver observer;

    private Thread thread;

    public FileAlterationMonitor(FileAlterationObserver observer)
    {
        // 观察者（观察文件变化）
        this.observer = observer;
        this.thread = new Thread(this);
    }

    public void start()
    {
        // 开始监控
        this.thread.start();
    }

    @Override
    public void run()
    {
        observer.checkAndNotify();
    }
}
