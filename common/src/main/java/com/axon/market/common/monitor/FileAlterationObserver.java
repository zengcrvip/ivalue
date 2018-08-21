package com.axon.market.common.monitor;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.MessageFormat;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by chenyu on 2016/5/31.
 */
public class FileAlterationObserver
{
    private static final Logger LOG = Logger.getLogger(FileAlterationObserver.class.getName());

    private FileAlterationListener listener;

    private WatchService watchService;

    private Path path;

    /**
     * 构造函数，创建文件监控观察者
     *
     * @param directory 目录
     * @param listener  监听者
     */
    public FileAlterationObserver(String directory, FileAlterationListener listener)
    {
        try
        {
            this.watchService = FileSystems.getDefault().newWatchService();
            this.listener = listener;
            FileEntry fileEntry = new FileEntry(directory);
            path = Paths.get(fileEntry.getFile().getAbsolutePath());
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        }
        catch (IOException e)
        {
            LOG.error("NIO File Monitor Init WatchService Error.", e);
        }
    }

    /**
     * @param event
     * @param <T>
     * @return
     */
    private <T> WatchEvent<Path> cast(WatchEvent<?> event)
    {
        return (WatchEvent<Path>) event;
    }

    /**
     *
     */
    public void checkAndNotify()
    {
        while (true)
        {
            try
            {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents())
                {
                    // 文件监控类型（创建，更改，删除）
                    String kind = event.kind().name();
                    WatchEvent<Path> evt = cast(event);
                    Path child = path.resolve(evt.context());
                    // 监控文件实体类
                    FileEntry fileEntry = new FileEntry(MessageFormat.format("{0}{1}{2}", child.getParent(), File.separator, child.getFileName()));
                    doFile(fileEntry, kind);
                }
                if (!key.reset())
                {
                    break;
                }
            }
            catch (Exception e)
            {
                LOG.error("Monitor File Error.", e);
            }
        }
    }

    /**
     * 文件操作
     *
     * @param fileEntry 文件
     * @param kind      操作类型
     */
    private void doFile(FileEntry fileEntry, String kind)
    {
        switch (kind)
        {
            case "ENTRY_CREATE":
            {
                doCreate(fileEntry);
                break;
            }
            case "ENTRY_DELETE":
            {
                doDelete(fileEntry);
                break;
            }
            case "ENTRY_MODIFY":
            {
                doModify(fileEntry);
                break;
            }
            default:
            {
                break;
            }
        }
    }

    /**
     * 创建监控操作
     *
     * @param fileEntry 文件
     */
    private void doCreate(FileEntry fileEntry)
    {
        if (fileEntry.getFile().isDirectory())
        {
            listener.onDirectoryCreate(fileEntry);
        }
        else
        {
            listener.onFileCreate(fileEntry);
        }
    }

    /**
     * 删除监控操作
     *
     * @param fileEntry 文件
     */
    private void doDelete(FileEntry fileEntry)
    {
        if (fileEntry.getFile().isDirectory())
        {
            listener.onDirectoryDelete(fileEntry);
        }
        else
        {
            listener.onFileDelete(fileEntry);
        }
    }

    /**
     * 更改监控操作
     *
     * @param fileEntry 文件
     */
    private void doModify(FileEntry fileEntry)
    {
        //是否为文件目录
        if (fileEntry.getFile().isDirectory())
        {
            listener.onDirectoryChange(fileEntry);
        }
        else
        {
            listener.onFileChange(fileEntry);
        }
    }

}
