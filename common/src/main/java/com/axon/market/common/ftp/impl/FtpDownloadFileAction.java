package com.axon.market.common.ftp.impl;

import com.axon.market.common.ftp.IFtpAction;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FTP下载文件
 * Created by chenyu on 2016/10/26.
 */
public class FtpDownloadFileAction implements IFtpAction
{
    /**
     * 原始文件
     */
    private String srcFile;

    /**
     * 目标文件
     */
    private String dstFile;

    public FtpDownloadFileAction(String srcFile, String dstFile)
    {
        this.srcFile = srcFile;
        this.dstFile = dstFile;
    }

    @Override
    public boolean action(FTPClient ftpClient) throws IOException
    {
        boolean result = true;
        OutputStream outputStream = null;

        try
        {
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                result = false;
            }
            else
            {
                File localFile = new File(dstFile);
                // 本地文件输出流

                outputStream = new FileOutputStream(localFile);
                // 下载文件
                ftpClient.retrieveFile(srcFile, outputStream);
            }
        }
        finally
        {
            // 关闭流，释放资源
            if (outputStream != null)
            {
                outputStream.close();
            }
        }
        return result;
    }
}
