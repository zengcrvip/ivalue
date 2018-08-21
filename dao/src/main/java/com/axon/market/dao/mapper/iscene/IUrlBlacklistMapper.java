package com.axon.market.dao.mapper.iscene;

import com.axon.market.dao.base.IMyBatisMapper;
import com.axon.market.common.domain.iscene.BannedHostsDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hale on 2016/12/6.
 */

@Component("urlBlacklistDao")
public interface IUrlBlacklistMapper extends IMyBatisMapper
{
    List<BannedHostsDomain> queryUrlBlacklist(@Param(value = "url") String url, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer queryUrlBlacklistCount(@Param(value = "url") String url);

    Integer deleteUrlBlacklist(@Param(value = "url") String url);

    Integer addUrlBlacklist(@Param(value = "info") BannedHostsDomain bannedHostsDomain);
}
