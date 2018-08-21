package com.axon.market.common.ftp.impl;

import com.axon.market.common.ftp.IFtpAction;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FTP上传文件
 * Created by chenyu on 2016/10/18.
 */
public class FtpUploadFileAction implements IFtpAction
{
    /**
     * 原始文件
     */
    private String srcFile;

    /**
     * 目标文件
     */
    private String dstFile;

    public FtpUploadFileAction(String srcFile, String dstFile)
    {
        this.srcFile = srcFile;
        this.dstFile = dstFile;
    }

    @Override
    public boolean action(FTPClient ftpClient) throws IOException
    {
        boolean result = true;
        FileInputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(new File(srcFile));
            String dstFilePath = dstFile.substring(0, dstFile.lastIndexOf("/") + 1);
            String dstFileName = dstFile.substring(dstFile.lastIndexOf("/") + 1, dstFile.length());
            int reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                result = false;
            }
            else
            {
                ftpClient.changeWorkingDirectory(dstFilePath);
                // 写文件
                ftpClient.storeFile(dstFileName, inputStream);
            }
        }
        finally
        {
            // 关闭流
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return result;
    }
}
