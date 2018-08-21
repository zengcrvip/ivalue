package com.axon.market.common.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Linux操作
 * Created by chenyu on 2016/8/17.
 */
@Service("jschOperate")
public class JschOperate
{
    private static final Logger LOG = Logger.getLogger(JschOperate.class.getName());

    private static int sessionConnectTimeout = 30 * 1000;

    private JSch jSch = new JSch();

    /**
     * @param jschAction 实现接口
     * @param host       Linux服务器
     * @param user       Linux服务器用户名
     * @param password   Linux服务器用户密码
     * @param port       Linux服务器端口
     * @return
     */
    public synchronized boolean doAction(IJschAction jschAction, String host, String user, String password, String port)
    {
        //String result = new String();
        boolean result = true;
        Session session = null;

        try
        {
            session = openSession(host, user, password, port);

            result = jschAction.action(session).equals("success") ? true : false ;
        }
        catch (Exception e)
        {
            LOG.error("JSch doAction error. ", e);
        }
        finally
        {
            closeSession(session);
        }

        return result;
    }

    /**
     * 打开Linux连接
     *
     * @param host     Linux服务器
     * @param user     Linux服务器用户
     * @param password Linux服务器用户密码
     * @param port     Linux服务器端口
     * @return
     * @throws JSchException
     */
    private Session openSession(String host, String user, String password, String port) throws JSchException
    {
        if (StringUtils.isEmpty(port))
        {
            port = "22";
        }

        Session session = jSch.getSession(user, host, Integer.parseInt(port));
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(sessionConnectTimeout);

        return session;
    }

    /**
     * 关闭Linux连接
     *
     * @param session
     */
    private void closeSession(Session session)
    {
        if (session != null)
        {
            session.disconnect();
        }
    }
}
