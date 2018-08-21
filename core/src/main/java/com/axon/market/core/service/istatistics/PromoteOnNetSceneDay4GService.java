package com.axon.market.core.service.istatistics;


import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dtt on 2017/8/9.
 */
@Component("PromoteOnNetSceneDay4GService")
public class PromoteOnNetSceneDay4GService {
    private static final Logger LOG =Logger.getLogger(PromoteOnNetSceneDay4GService.class.getName());
    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;


    String queryPromoteOnNetSceneDay4GSql = "select \t\n" +
            "\ttimest,\n" +
            "\ttask_id,\n" +
            "\tscene_type,\n" +
            "\tscene_condition,\n" +
            "\tcatch_2guser_num,\n" +
            "\tcatch_user_num,\n" +
            "\tgift_user_num,\n"+
            "\tsend_user_num \n" +
            "\tfrom umid.rpt_promote_onnet_scene_day \n" ;

    /**
     * 分页获取4G促登网数据
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String,Object>> queryPromoteOnNetSceneDay4GByPage(String startTime,String endTime,Integer offset, Integer limit){
        String whereStr = " where 1=1 ";
        if(!StringUtils.isEmpty(startTime)){
            whereStr += " and timest >=  " + startTime;
        }else{

        }
        if(!StringUtils.isEmpty(endTime)){
            whereStr += " and timest <= " + endTime;
        }
        whereStr += " order by timest,task_id ";
        if(limit == 0){
            whereStr += " limit 20 offset 0 ";
        }else{
            whereStr +=" limit " + limit + " offset " + offset;
        }

        List<Map<String,Object>> dataList=new ArrayList<>();
        String finalSql = queryPromoteOnNetSceneDay4GSql + whereStr;
        LOG.info("查询4G促登网报表SQL:" + finalSql);
        try
        {
            dataList = greenPlumOperateService.query(finalSql);
        }
        catch (Exception e)
        {
            LOG.error("查询4G促登网报表异常：", e);
            return dataList;
        }
        return dataList;
    }

    /**
     * 计算4G促登网总量
     * @param startTime
     * @param endTime
     * @return
     */
    public int queryCount(String startTime,String endTime){
        String sql = " SELECT count(1) FROM umid.rpt_promote_onnet_scene_day  where 1=1 ";
        if(!StringUtils.isEmpty(startTime)){
            sql += " and timest >=  " + startTime;
        }
        if(!StringUtils.isEmpty(endTime)){
            sql += " and timest <= " + endTime;
        }
        int count = 0;
        try
        {
            count = greenPlumOperateService.queryRecordCount(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return count;
    }


}
