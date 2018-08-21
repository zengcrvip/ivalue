package com.axon.market.common.monitor;

import java.io.File;

/**
 * Created by chenyu on 2016/5/31.
 */
public class FileEntry
{
    private File file;

    /**
     * 构造函数
     *
     * @param fileName 文件名
     */
    public FileEntry(String fileName)
    {
        file = new File(fileName);
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }
}
