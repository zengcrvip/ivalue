package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.common.domain.iscene.*;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.core.rule.impl.StringNodeParse;
import com.axon.market.dao.mapper.iscene.IScenePilotMapper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.html.HTMLParagraphElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuan on 2017/2/8.
 */
@Component("scenePilotService")
public class ScenePilotService
{
    private static final Logger LOG = Logger.getLogger(ScenePilotService.class.getName());
    @Autowired
    @Qualifier("scenePilotDao")
    private IScenePilotMapper scenePilotDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 查询任务列表
     * @param type
     * @param name
     * @param offset
     * @param limit
     * @return
     */

    public Table getScenePilotList(String type,String name, int offset, int limit)
    {
        List<ScenePilotDomain> list = new LinkedList<ScenePilotDomain>();
        Integer count=0;
        //type==2表示场景导航 type==1表示全页面导航
        if(Integer.valueOf(type)==2)
        {
            list = scenePilotDao.getScenePilotList(name, offset, limit);
            count = scenePilotDao.getScenePilotListCount(name);
        }
        else{
            list = scenePilotDao.getFullPageList(name, offset, limit);
            count = scenePilotDao.getFullPageListCount(name);
        }
        return new Table(list, count);
    }

    /**
     * 根据id查询domain对象
     * @param id
     * @return
     */
    public ScenePilotDomain queryScenePilot(Integer id){
        ScenePilotDomain domain = scenePilotDao.queryScenePilotDomain(id);
        return domain;
    }


    /**
     * @param scenePilotDomain
     * @return
     */
    public Operation addOrEditScenePilot(ScenePilotDomain scenePilotDomain)
    {
        try
        {
            Boolean result;
            String message;
            //新增
            if (scenePilotDomain.getId() <= 0)
            {
                result = scenePilotDao.createScenePilot(scenePilotDomain) == 1;
                message = result ? "新增场景任务成功" : "新增场景任务失败";
            }
            else
            {
                result = scenePilotDao.updateScenePilot(scenePilotDomain) == 1;
                message = result ? "更新场景任务成功" : "更新场景任务失败";
                sendMessage(scenePilotDomain);
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Scene Pilot Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param scenePilotDomain
     * @return
     */
    public Operation deleteScenePilot(ScenePilotDomain scenePilotDomain)
    {
        try
        {
            //删除前先判断是否新增任务
            Integer value=scenePilotDao.getTaskJobsCount(scenePilotDomain.getId());
            if(value>0)
            {
                return new Operation(false, "任务已被设置,不能删除!");
            }
            Boolean result = scenePilotDao.deleteScenePilot(scenePilotDomain.getId()) == 1;
            String message = result ? "删除场景任务成功" : "删除场景任务失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Scene Pilot Error. ", e);
            return new Operation();
        }
    }

    /**
     * 查询网址分类
     *
     * @return
     */

    public List<UrlGroupDomain> getUrlGroup()
    {
        try
        {
            List<UrlGroupDomain> list = scenePilotDao.getUrlGroup();

            return list;
        }
        catch (Exception ex)
        {
            LOG.error("getUrlGroup ", ex);
            return null;
        }
    }

    /**
     * 查询地理位置
     *
     * @return
     */
    public List<LocationGroupDomain> getLocationGroup()
    {
        try
        {
            List<LocationGroupDomain> list = scenePilotDao.getLocationGroup();

            return list;
        }
        catch (Exception ex)
        {
            LOG.error("getUrlGroup ", ex);
            return null;
        }
    }

    /**
     * 获取场景url
     *
     * @return
     */
    public List<ScenesDomain> getSceneUrl()
    {
        try
        {
            List<ScenesDomain> list = scenePilotDao.getSceneUrl();

            return list;
        }
        catch (Exception ex)
        {
            LOG.error("getUrlGroup ", ex);
            return null;
        }
    }

    public List<ExtStopCondConfig> getAdditionalList(String name)
    {
        try
        {
            List<ExtStopCondConfig> list = scenePilotDao.getAdditionalList(name);

            return list;
        }
        catch (Exception ex)
        {
            LOG.error("getUrlGroup ", ex);
            return null;
        }
    }

    /**
     * 发送消息
     *
     */
    public String sendMessage(ScenePilotDomain model)
    {
        String rest = null;
        try
        {
            List<MarketJobDomain> list = scenePilotDao.selectTaskJobs(String.valueOf(model.getId()));
            if(list.size()>0)
            {
                //发送消息
                for (Integer i=0;i<list.size();i++)
                {
                    Map<String, Object> table = new Hashtable<String, Object>();
                    table.put("messageType", 51);
                    table.put("taskId",list.get(i).getId());
                    table.put("blockMode",model.getBlockMode());
 /*
                处理urlGroup 数据格式
             */
                    List<String> urlGroupIds = Arrays.asList(model.getUrlGroupIds().split(","));
//              table.put("urlGroup", scenePilotDomain.getUrlGroupIds());//list<String>
                    table.put("urlGroup", urlGroupIds);

            /*
                处理targetUsers 数据格式
             */
                    String users=list.get(0).getMarketSegmentIds()==null?"":list.get(0).getMarketSegmentIds();
                    List<String> targetUserIds = Arrays.asList(users.split(","));
//              table.put("targetUsers",marketJobDomain.getMarketSegmentIds());//list<String>
                    table.put("targetUsers", targetUserIds);

            /*
                处理locationGroup 数据格式
             */
                    if(StringUtils.isEmpty(model.getLocationGroupIds())){
                        table.put("targetLocationGroups", new ArrayList<>());
                    }else{
                        String[] str = model.getLocationGroupIds().split(",");
                        int array[] = new int[str.length];
                        for (int a = 0; a < str.length; a++)
                        {
                            array[a] = Integer.parseInt(str[a]);
                        }
                        List locationGroupIds = Arrays.asList(array);
                        table.put("targetLocationGroups", locationGroupIds);
                    }
                    table.put("pilotInterval",model.getIntervarTime());//投放次数
                    table.put("pilotType",model.getPilotType());
                    table.put("taskPriority",list.get(i).getScenePilotSort());//排序
                    table.put("sid",model.getSceneIds());
                    table.put("pilotUrl",model.getPilotUrl());
                    table.put("taskStm", translateDate(list.get(i).getStartTime()));
                    table.put("taskEtm", translateDate(list.get(i).getEndTime()));
                    table.put("onLineTm",translateHour(model.getOnLineTm()));
                    table.put("offLineTm",translateHour(model.getOffLineTm()));
                    table.put("extStopCond",model.getExtStopCond() == null ? "" : model.getExtStopCond());
                    table.put("audit",1);

                    // 数据组装结束===================
                    Gson gs = new Gson();
                    HttpUtil http = HttpUtil.getInstance();
                    // TODO 调接口发送信息，待调试
                    String jsonStr = gs.toJson(table);
//            String jStr = URLEncoder.encode(jsonStr,"UTF-8");
                    Map<String,String> map = new HashMap<>();
//            map.put("message",jStr);
                    map.put("message", jsonStr);
                    String result = http.sendHttpPost(interfaceBean.getScenePilotUrl(),map);
                    Map m = gs.fromJson(result, Map.class);
                    if(m.get("resultCode").equals("00000")){
                        rest = "success";
                    }else{
                        rest = "failure";
                    }


//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Hashtable hash = new Hashtable();
//                    hash.put("messageType",51);
//                    hash.put("taskId",list.get(i).getId());
//                    hash.put("blockMode",model.getBlockMode());
//                    hash.put("urlGroup", listUrl);
//                    hash.put("targetLocationGroups",listLocation);
//                    hash.put("targetUsers",listUser);
//                    hash.put("pilotInterval",model.getIntervarTime());//投放次数
//                    hash.put("pilotType",model.getPilotType());
//                    hash.put("taskPriority",list.get(i).getScenePilotSort());//排序
//                    hash.put("sid",model.getSceneIds());
//                    hash.put("pilotUrl",model.getPilotUrl());
//                    hash.put("taskStm",simpleDateFormat.parse(model.getOnLineTm()));
//                    hash.put("taskEtm",simpleDateFormat.parse(model.getOffLineTm()));
//                    hash.put("onLineTm",simpleDateFormat.parse(list.get(i).getStartTime()));
//                    hash.put("offLineTm", simpleDateFormat.parse(list.get(i).getEndTime()));
//                    hash.put("extStopCond",model.getExtStopCond());
//                    hash.put("audit",1);
//
//                    HttpUtil http = HttpUtil.getInstance();
//                    String result = http.sendHttpPost(interfaceBean.getScenePilotUrl(),hash);

                }
            }
        }
        catch (Exception ex)
        {
            rest ="sendMessageError";
            LOG.error("sendMessage error:", ex);
        }
        return rest;
    }

    /**
     * 日期转换
     *
     * @param dateTime
     * @return
     */
    public Long translateDate(String dateTime) throws ParseException
    {
//        Date dt = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = fmt.parse(dateTime);
        long dTime = d.getTime() / 1000L;
//        Long start = System.currentTimeMillis() / 1000L;//当前时间的unix时间戳
        LOG.info("taskStart/EndTime"+dTime);
        return dTime;
    }

    public Long translateHour(String dateTime) throws ParseException
    {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        Date d = fmt.parse(dateTime);
        Long dTime = d.getTime();
        Long res = (dTime - (fmt.parse("00:00").getTime())) / 1000L;
        LOG.info("taskStart/EndHour"+res);
        return res;
    }
}
