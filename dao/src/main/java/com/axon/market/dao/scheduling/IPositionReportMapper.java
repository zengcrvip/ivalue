package com.axon.market.dao.scheduling;

import com.axon.market.dao.base.IMyBatisMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 位置场景统计DAO
 * Created by zengcr on 2016/12/12.
 */
@Service("positionReportDao")
public interface IPositionReportMapper  extends IMyBatisMapper
{
    /**
     * 查询位置场景统计总数
     * @param paras 任务名称，基站地区，开始时间，结束时间
     * @return
     */
    int queryPositionReportTotal(Map<String, Object> paras);
    /**
     * 查询位置场景统计分页列表
     * @param paras 任务名称，基站地区，开始时间，结束时间
     * @return
     */
    List<Map<String,Object>> queryPositionReortByPage(Map<String, Object> paras);
    /**
     *查询位置场景统计列表
     * @param paras
     * @return
     */
    List<Map<String,Object>> queryPositionReport(Map<String, Object> paras);
}
