package com.axon.market.common.jsch.impl;

import com.axon.market.common.jsch.IJschAction;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 获取服务器文件目录
 * Created by chenyu on 2016/8/17.
 */
public class GetServerFileJschAction implements IJschAction
{
    private int chanelOpenTimeOut = 30 * 1000;

    /**
     * 需要获取的文件路径
     */
    private String path;

    public GetServerFileJschAction(String path)
    {
        this.path = path;
    }

    @Override
    public String action(Session session) throws IOException, JSchException
    {
        BufferedReader reader = null;
        ChannelExec channel = null;
        StringBuffer result = new StringBuffer();
        try
        {
            // Linux ls -l 指令
            String command = "ls -l " + path;
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.connect(chanelOpenTimeOut);
            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String buf;
            while ((buf = reader.readLine()) != null)
            {
                result.append(buf).append("\n");
            }
        }
        finally
        {
            // 关闭资源
            if (reader != null)
            {
                reader.close();
            }
            if (channel != null)
            {
                channel.disconnect();
            }
        }
        return result.toString();
    }
}
