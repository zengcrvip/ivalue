package com.axon.market.core.service.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.dao.mapper.itag.IRemoteServerMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.rmi.server.RemoteServer;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/4.
 */
@Component("remoteServerService")
public class RemoteServerService
{
    private static final Logger LOG = Logger.getLogger(RemoteServer.class.getName());

    @Autowired
    @Qualifier("remoteServerDao")
    private IRemoteServerMapper remoteServerDao;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    public static RemoteServerService getInstance()
    {
        return (RemoteServerService) SpringUtil.getSingletonBean("remoteServerService");
    }

    /**
     * @param host
     * @param user
     * @param password
     * @param port
     * @param connectType
     * @return
     */
    public RemoteServerDomain generateRemoteServerDomain(String host, String user, String password, String port, String connectType)
    {
        RemoteServerDomain remoteServerDomain = new RemoteServerDomain();
        remoteServerDomain.setServerIp(host);
        remoteServerDomain.setServerUser(user);
        remoteServerDomain.setPassword(password);
        remoteServerDomain.setPort(Integer.parseInt(port));
        remoteServerDomain.setConnectType(connectType);
        return remoteServerDomain;
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    public Table queryRemoteServersByPage(String serverName, Integer offset, Integer limit)
    {
        try
        {
            Integer count = remoteServerDao.queryAllRemoteServerCounts(serverName);
            List<RemoteServerDomain> list = remoteServerDao.queryRemoteServersByPage(serverName, offset, limit);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Remote Servers Error. ", e);
            return new Table();
        }
    }

    /**
     * @param serverId
     * @return
     */
    public RemoteServerDomain queryRemoteServerById(Integer serverId)
    {
        try
        {
            return remoteServerDao.queryRemoteServerById(serverId);
        }
        catch (Exception e)
        {
            LOG.error("Query Remote Server By Id Error. ", e);
            return null;
        }
    }

    /**
     * 新增/修改
     *
     * @param remoteServerDomain
     * @return
     */
    public Operation addOrEditRemoteServer(RemoteServerDomain remoteServerDomain, UserDomain userDomain)
    {
        try
        {
            if (remoteServerDomain.getPort() == null)
            {
                remoteServerDomain.setPort("sftp".equals(remoteServerDomain.getConnectType()) ? 22 : 21);
            }
            remoteServerDomain.setPassword(EncryptUtil.encryption(remoteServerDomain.getPassword(), "market"));
            Boolean result;
            String message;
            //新增
            if (remoteServerDomain.getId() == null || remoteServerDomain.getId() == 0)
            {
                remoteServerDomain.setCreateUser(userDomain.getId());
                remoteServerDomain.setCreateUserName(userDomain.getName());
                remoteServerDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = remoteServerDao.createRemoteServer(remoteServerDomain) == 1;
                message = result ? "新增远程服务器成功" : "新增远程服务器失败";
            }
            else
            {
                remoteServerDomain.setUpdateUser(userDomain.getId());
                remoteServerDomain.setUpdateTime(TimeUtil.formatDate(new Date()));
                result = remoteServerDao.updateRemoteServer(remoteServerDomain) == 1;
                message = result ? "更新远程服务器成功" : "更新远程服务器失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("addOrEditRemoteServer Error. ", e);
            return new Operation();
        }
    }

    /**
     * 删除
     *
     * @param id
     * @param userId
     * @return
     */
    public Operation deleteRemoteServer(Integer id, Integer userId)
    {
        try
        {
            Boolean result = remoteServerDao.deleteRemoteServer(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除远程服务器成功" : "删除远程服务器失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Remote Server Error. ", e);
            return new Operation();
        }
    }

    /**
     * @return
     */
    public List<Map<String, String>> queryAllRemoteServerIdAndNames()
    {
        try
        {
            return remoteServerDao.queryAllRemoteServerIdAndNames();
        }
        catch (Exception e)
        {
            LOG.error("Query Remote Server Names Error. ", e);
            return null;
        }
    }

    /**
     * @param remoteServerDomain
     * @return
     */
    public Operation testConnection(RemoteServerDomain remoteServerDomain)
    {
        try
        {
            Boolean result = fileOperateService.testConnection(remoteServerDomain);
            String message = result ? "连接服务器成功" : "连接服务器失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Test Remote Server Connection Error. ", e);
            return new Operation();
        }
    }
}
