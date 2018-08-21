package com.axon.market.common.jsch.impl;

import com.axon.market.common.jsch.IJschAction;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

/**
 * Created by chenyu on 2016/8/17.
 */
public class SftpJschAction implements IJschAction
{
    private static int chanelOpenTimeOut = 30 * 1000;

    /**
     * 操作类型（上传，下载，删除）
     */
    private String type;

    /**
     * 本地文件
     */
    private String srcFile;

    /**
     * 目标文件
     */
    private String dstFile;

    public SftpJschAction(String type, String srcFile, String dstFile)
    {
        this.type = type;
        this.srcFile = srcFile;
        this.dstFile = dstFile;
    }

    @Override
    public String action(Session session) throws IOException, JSchException, SftpException
    {
        ChannelSftp channel = null;
        try
        {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(chanelOpenTimeOut);
            switch (type)
            {
                case "GET":
                {
                    channel.get(srcFile, dstFile);
                    break;
                }
                case "PUT":
                {
                    channel.put(srcFile, dstFile);
                    break;
                }
                case "DELETE":
                {
                    channel.rm(dstFile);
                    break;
                }
            }
        }
        catch(Exception e){
            return "failed";
        }
        finally
        {
            // 关闭资源
            if (channel != null)
            {
                channel.disconnect();
            }
        }
        return "success";
    }
}
