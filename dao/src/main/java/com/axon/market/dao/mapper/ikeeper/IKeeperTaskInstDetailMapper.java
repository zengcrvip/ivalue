package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.TaskInstDetailDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 掌柜实例详情，具体客户执行详情
 * Created by zengcr on 2017/8/20.
 */
@Component("iKeeperTaskInstDetailDao")
public interface IKeeperTaskInstDetailMapper extends IMyBatisMapper{

    List<Map<String, Object>> queryTaskInstDetailByProductId(@Param("productId") String productId);


    /**
     * 单个新增炒店任务详情
     * @param taskInstDetailDomain
     */
    void insertTaskInstDetail(TaskInstDetailDomain taskInstDetailDomain);

    /**
     * 批量执行掌柜任务实例详情
     * @param taskInstDetailDomainList
     */
    void batchInsertTaskInstDetail(@Param("taskInstDetailDomainList") List<TaskInstDetailDomain> taskInstDetailDomainList);

}
