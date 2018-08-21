package com.axon.market.core.service.ikeeper;
import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.icommon.RuleService;
import com.axon.market.dao.mapper.ikeeper.IKeeperMgrMapper;
import com.axon.market.dao.mapper.isystem.IUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yuanfei on 2017/4/25.
 */
@Service("keeperMgrService")
public class KeeperMgrService
{
    @Autowired
    @Qualifier("userDao")
    private IUserMapper userDao;

    @Autowired
    @Qualifier("keeperMgrDao")
    private IKeeperMgrMapper keeperMgrDao;

    @Autowired()
    @Qualifier("ruleService")
    private RuleService ruleService;

    /**
     * app类型 返回数据
     * @return
     */
    public List<Map<String,Object>> fetchActivities()
    {
        return keeperMgrDao.fetchActivities();
    }

    /**
     *
     * @param userDomain
     * @param startDate
     * @param endDate
     * @param type
     * @param orgCode
     * @param activId
     * @return
     */
    public Map<String,Object> fetchFee(UserDomain userDomain,String startDate,String endDate,String type,String orgCode,String activId)
    {
        Integer orgType = StringUtils.isNotEmpty(type)?Integer.valueOf(type):null;
        Integer activityId = StringUtils.isNotEmpty(activId) && !"-1".equals(activId)?Integer.valueOf(activId):null;
        Map<String,Object> result = keeperMgrDao.fetchFee(userDomain, dateTransform(startDate), dateTransform(endDate), orgType, orgCode, activityId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:00:00");
        result.put("shopKeeperTime",sdf.format(new Date()));
        return result;
    }

    /**
     *
     * @param userDomain
     * @return
     */
    public Map<String,Object> fetchAreaRank(UserDomain userDomain,String startDate,String endDate,String areaCode,String activId)
    {
        Map<String,Object> mapResult = new HashMap<String,Object>();
        Integer cityCode = StringUtils.isNotEmpty(areaCode) && !"-1".equals(areaCode)?Integer.valueOf(areaCode) : null;
        Integer activityId = StringUtils.isNotEmpty(activId) && !"-1".equals(activId)?Integer.valueOf(activId) : null;
        //UserDomain userDomain = userDao.queryUserByToken(token);
        //如果不是省级的，又没有areaCode的默认当前地区
        if (userDomain.getAreaCode() != 99999)
        {
            cityCode = userDomain.getAreaCode();
        }

        List<Map<String,Object>> list =  keeperMgrDao.fetchAreaRank(userDomain, dateTransform(startDate), dateTransform(endDate), activityId);
        for (int i=0;i<list.size();i++)
        {
            list.get(i).put("rank", i+1);

            //设置当前地市的排�?
            if (null != cityCode && cityCode.equals(Integer.valueOf(String.valueOf(list.get(i).get("areaCode")))))
            {
                mapResult.put("rank", i + 1);
            }
        }
        mapResult.put("dataList", list);

        return mapResult;
    }

    /**
     *
     * @param userDomain
     * @return
     */
    public Map<String,Object> fetchChannelRank(UserDomain userDomain,String startDate,String endDate,String areaCode,String channelCode, String activId)
    {
        Map<String,Object> mapResult = new HashMap<String,Object>();
        // UserDomain userDomain = userDao.queryUserByToken(token);
        Integer activityId = StringUtils.isNotEmpty(activId) && !"-1".equals(activId)?Integer.valueOf(activId):null;
        Integer cityCode = null;
        //如果当前用户是省级用户，那么根据"2".equals(orgType)判断是否选择了地市，如果选择了地市则根据地市code查询营业�?
        //如果当前用户为地市用户或者营业厅用户，则默认查询当前用户地州下的所有营业厅
        if (userDomain.getAreaCode() == 99999 && StringUtils.isNotEmpty(areaCode) && !"-1".equals(areaCode))
        {
            cityCode = Integer.valueOf(areaCode);
        }
        else if(userDomain.getAreaCode() != 99999)
        {
            cityCode = userDomain.getAreaCode();
        }

        List<Map<String,Object>> list =  keeperMgrDao.fetchChannelRank(userDomain, dateTransform(startDate), dateTransform(endDate),cityCode, activityId);
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        for (int i=0;i<list.size();i++)
        {
            list.get(i).put("rank", i+1);

            //如果查询类型中传入的�?3，则获取当前营业厅的排名并返回
            if (StringUtils.isNotEmpty(channelCode) && String.valueOf(list.get(i).get("channelCode")).equals(channelCode))
            {
                mapResult.put("rank", i + 1);
            }
        }

        if (StringUtils.isNotEmpty(channelCode) && null == mapResult.get("rank") )
        {
            mapResult.put("rank", -1);
        }

        //获取�?10营业�?
        if (list.size() >= 10)
        {
            data = list.subList(0,10);
        }
        else
        {
            data = list;
        }
        mapResult.put("dataList", data);
        return mapResult;
    }

    /**
     * 根据终端探索规则匹配人数
     * @param rules
     * @return
     */
    public Integer queryMatchUserCountByRule(String rules)
    {
        return ruleService.queryMatchRuleUserCountByKeeper(rules);
    }

    /**
     * 时间格式转换
     * @param dateStr
     * @return
     */
    private String dateTransform(String dateStr)
    {
        if (StringUtils.isEmpty(dateStr))
        {
            return TimeUtil.formatDateToYMDDevide(new Date());
        }
        return dateStr.replace("/","-");
    }
}
