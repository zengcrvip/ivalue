package com.axon.market.common.ftp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * FTP操作
 * Created by chenyu on 2016/10/18.
 */
@Service("ftpOperate")
public class FtpOperate
{
    private static final Logger LOG = Logger.getLogger(FtpOperate.class.getName());

    private static int sessionConnectTimeout = 10 * 1000;

    private FTPClient ftpClient = new FTPClient();

    public synchronized boolean doAction(IFtpAction ftpAction, String host, String user, String password, String port)
    {
        boolean result = false;
        try
        {
            // 连接FTP
            open(host, user, password, port);
            // 接口实现相应操作
            ftpClient.enterLocalPassiveMode();
            ftpClient.configure(new FTPClientConfig("com.axon.market.common.ftp.MyUnixFTPEntryParser"));
            result = ftpAction.action(ftpClient);
        }
        catch (Exception e)
        {
            LOG.error("Ftp doAction error. ", e);
        }
        finally
        {
            try
            {
                // 关闭连接
                close();
            }
            catch (IOException e)
            {
                LOG.error("FTP close error. ", e);
            }
        }
        return result;
    }

    /**
     * 打开FTP连接
     *
     * @param host     FTP服务器地址
     * @param user     登录FTP用户名称
     * @param password 登录FTP用户密码
     * @param port     登录FTP端口
     * @throws IOException
     */
    private void open(String host, String user, String password, String port) throws IOException
    {
        if (StringUtils.isEmpty(port))
        {
            port = "21";
        }
        ftpClient.connect(host, Integer.parseInt(port));
        ftpClient.login(user, password);
    }

    /**
     * 关闭FTP连接
     *
     * @throws IOException
     */
    private void close() throws IOException
    {
        if (null != ftpClient)
        {
            ftpClient.logout();
            if (ftpClient.isConnected())
            {
                ftpClient.disconnect();
            }
        }
    }
}
