package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.ScenesDomain;
import com.axon.market.common.domain.iscene.StatsDomain;
import com.axon.market.dao.mapper.iscene.ITaskStatisticsMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuan on 2017/4/6.
 */
@Component("taskStatisticsService")
public class TaskStatisticsService
{
    private static final Logger LOG = Logger.getLogger(TaskStatisticsService.class.getName());
    @Autowired
    @Qualifier("taskStatisticsDao")
    private ITaskStatisticsMapper taskStatisticsDao;

    /**
     * 查询导航任务统计列表
     *
     * @param taskName 任务名称
     * @param startTime 开始时间
     * @param endTime  结束时间
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table getTaskStatisticsList(String taskName,String startTime,String endTime,int type, int offset, int limit)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date sDate = sdf.parse(startTime);
            Date eDate = sdf.parse(endTime);
            List<StatsDomain> list = taskStatisticsDao.getStatsList(taskName,sDate,eDate,type, offset, limit);
            Integer count = taskStatisticsDao.getStatsListCount(taskName,sDate,eDate,type);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            return new Table(null, 0);
        }
    }
}
