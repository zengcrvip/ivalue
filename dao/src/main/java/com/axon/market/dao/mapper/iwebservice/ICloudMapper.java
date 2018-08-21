package com.axon.market.dao.mapper.iwebservice;

import com.axon.market.common.domain.webservice.AimSub;
import com.axon.market.common.domain.webservice.OperationIn;
import com.axon.market.common.domain.webservice.OperationStatus;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanfei on 2017/5/27.
 */
@Component("cloudDao")
public interface ICloudMapper extends IMyBatisMapper
{
    /**
     * 活动信息入库
     * @param operationIn
     * @return
     */
    int insertActiveData(@Param(value = "info") OperationIn operationIn);

    /**
     * 活动客户群组信息入库
     * @param aimSubs
     * @param saleId
     * @return
     */
    int insertActiveAimData(@Param(value = "list") List<AimSub> aimSubs,@Param(value = "saleId") String saleId);

    /**
     * 关联产品信息入库
     * @param aimSubs
     * @param saleId
     * @return
     */
    int insertActiveProduct(@Param(value = "list") List<AimSub> aimSubs,@Param(value = "saleId") String saleId);

    /**
     * 更新状态，回收任务
     * @param operationStatus
     * @return
     */
    int changeMarketingInfoStatus(@Param(value = "operationStatus") OperationStatus operationStatus);


    /**
     * 根据波次ID查询任务ID
     * @param operationStatus
     * @return
     */
    List<String> queryTaskIdByBoidId(@Param(value = "operationStatus") OperationStatus operationStatus);


}
