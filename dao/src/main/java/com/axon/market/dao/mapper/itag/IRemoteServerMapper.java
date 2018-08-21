package com.axon.market.dao.mapper.itag;

import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * Created by yangyang on 2016/1/27.
 */
@Component("remoteServerDao")
public interface IRemoteServerMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryAllRemoteServerCounts(@Param(value = "serverName") String serverName);

    /**
     * @return
     */
    List<Map<String, String>> queryAllRemoteServerIdAndNames();

    /**
     * @param offset
     * @param maxRecord
     * @return
     */
    List<RemoteServerDomain> queryRemoteServersByPage(@Param(value = "serverName") String serverName, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer maxRecord);

    /**
     * @param id
     * @return
     */
    RemoteServerDomain queryRemoteServerById(@Param(value = "id") Integer id);

    /**
     * @param remoteServerDomain
     * @return
     */
    Integer createRemoteServer(@Param(value = "info") RemoteServerDomain remoteServerDomain);

    /**
     * @param remoteServerDomain
     * @return
     */
    Integer updateRemoteServer(@Param(value = "info") RemoteServerDomain remoteServerDomain);

    /**
     * @param id
     * @param userId
     * @param time
     * @return
     */
    Integer deleteRemoteServer(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);
}

