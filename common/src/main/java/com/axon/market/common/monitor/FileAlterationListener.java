package com.axon.market.common.monitor;

/**
 * Created by chenyu on 2016/5/31.
 */
public abstract class FileAlterationListener
{
    /**
     * 判断是否为需要监控的文件
     *
     * @param fileEntry
     * @return
     */
    abstract boolean accept(FileEntry fileEntry);

    /**
     * 文件夹创建监控
     *
     * @param fileEntry
     */
    abstract void directoryCreateAction(FileEntry fileEntry);

    /**
     * 文件夹更改监控
     *
     * @param fileEntry
     */
    abstract void directoryChangeAction(FileEntry fileEntry);


    /**
     * 文件夹删除监控
     *
     * @param fileEntry
     */
    abstract void directoryDeleteAction(FileEntry fileEntry);

    /**
     * 文件创建监控
     *
     * @param fileEntry
     */
    abstract void fileCreateAction(FileEntry fileEntry);

    /**
     * 文件更改监控
     *
     * @param fileEntry
     */
    abstract void fileChangeAction(FileEntry fileEntry);

    /**
     * 文件删除监控
     *
     * @param fileEntry
     */
    abstract void fileDeleteAction(FileEntry fileEntry);

    /**
     * @param fileEntry
     */
    public void onDirectoryCreate(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            directoryCreateAction(fileEntry);
        }
    }

    /**
     * @param fileEntry
     */
    public void onDirectoryChange(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            directoryChangeAction(fileEntry);
        }
    }

    /**
     * @param fileEntry
     */
    public void onDirectoryDelete(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            directoryDeleteAction(fileEntry);
        }
    }

    /**
     * @param fileEntry
     */
    public void onFileCreate(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            fileCreateAction(fileEntry);
        }
    }

    /**
     * @param fileEntry
     */
    public void onFileChange(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            fileChangeAction(fileEntry);
        }
    }

    /**
     * @param fileEntry
     */
    public void onFileDelete(FileEntry fileEntry)
    {
        if (accept(fileEntry))
        {
            fileDeleteAction(fileEntry);
        }
    }
}
