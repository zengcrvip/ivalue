package com.axon.market.dao.mapper.ishop;

import com.axon.market.dao.base.IMyBatisMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 地市任务汇总统计dao
 * Created by zengcr on 2016/12/13.
 */
@Service("shopTaskSummaryDao")
public interface IShopTaskSummaryMapper extends IMyBatisMapper
{
    /**
     * 查询查询地市任务汇总统计日报
     * @param paras 时间
     * @return
     */
    List<Map<String,Object>> queryPositionSummaryByDay(Map<String, Object> paras);
    /**
     * 查询查询地市任务汇总统计月报
     * @param paras 时间
     * @return
     */
    List<Map<String,Object>> queryPositionSummaryByMonth(Map<String, Object> paras);
}
