package com.axon.market.web.controller.iconsumption;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iconsumption.DixiaoTaskStatusEnum;
import com.axon.market.common.domain.iconsumption.DixiaoCodeDomain;
import com.axon.market.common.domain.iconsumption.DixiaoListDomain;
import com.axon.market.common.domain.iconsumption.DixiaoResultDomain;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iconsumption.DixiaoService;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuwen on 2017/7/21.
 */

@Controller("dixiaoController")
public class DixiaoController {
    private static final Logger LOG = Logger.getLogger(DixiaoController.class.getName());

    @Qualifier("dixiaoService")
    @Autowired
    private DixiaoService dixiaoService;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    /**
     * 低销波次统计结果展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryDixiaoResult.view", method = RequestMethod.POST)
    @ResponseBody
    public List<DixiaoResultDomain> queryDixiaoResult(@RequestBody Map<String, Object> paras, HttpSession session) {
        return dixiaoResultMapper.queryDixiaoResult(paras);
    }

    /**
     * 话+渠道编码查询结果分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryBusinessCodeByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<DixiaoCodeDomain> queryBusinessCodeByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        //炒店任务名称
        String businessname = paras.get("name")==null?"":SearchConditionUtil.optimizeCondition((String) (paras.get("name"))).trim();
        paras.put("name", businessname);

        if (paras.get("start")!=null && !"".equals(paras.get("start"))){
            paras.put("offset",Integer.valueOf((String)paras.get("start")));
        }
        if (paras.get("length")!=null && !"".equals(paras.get("length"))){
            paras.put("limit",Integer.valueOf((String)paras.get("length")));
        }

        //int items = dixiaoResultMapper.queryBusinessCodeTotal(paras);
        int items = dixiaoService.queryBusinessCodeTotal(paras);
        //List<DixiaoCodeDomain> list = dixiaoResultMapper.queryBusinessCodeByPage(paras);
        List<DixiaoCodeDomain> list = dixiaoService.queryBusinessCodeByPage(paras);
        return new Table(list, items);
    }

    /**
     * 合作伙伴渠道编码查询结果分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPartnerCodeByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<DixiaoCodeDomain> queryPartnerCodeByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        //炒店任务名称
        String businessname = paras.get("name")==null?"":SearchConditionUtil.optimizeCondition((String) (paras.get("name"))).trim();
        paras.put("name", businessname);

        if (paras.get("start")!=null && !"".equals(paras.get("start"))){
            paras.put("offset",Integer.valueOf((String)paras.get("start")));
        }
        if (paras.get("length")!=null && !"".equals(paras.get("length"))){
            paras.put("limit",Integer.valueOf((String)paras.get("length")));
        }

        int items = dixiaoResultMapper.queryPartnerCodeTotal(paras);
        List<DixiaoCodeDomain> list = dixiaoResultMapper.queryPartnerCodeByPage(paras);
        return new Table(list, items);
    }

    /**
     * 低消任务分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryDixiaoTaskByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<DixiaoTaskDomain> queryDixiaoTaskByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        String salename = paras.get("salename")==null?"":SearchConditionUtil.optimizeCondition((String) (paras.get("salename"))).trim();;
        paras.put("salename", salename);

        if (paras.get("start")!=null && !"".equals(paras.get("start"))){
            paras.put("offset",Integer.valueOf((String)paras.get("start")));
        }
        if (paras.get("length")!=null && !"".equals(paras.get("length"))){
            paras.put("limit",Integer.valueOf((String)paras.get("length")));
        }

        List<DixiaoTaskDomain> dixiaoTaskList = null;
        dixiaoTaskList = dixiaoResultMapper.queryDixiaoTaskByPage(paras);
        int itemCounts = 0;
        itemCounts = dixiaoResultMapper.queryDixiaoTaskTotal(paras);
        return new Table(dixiaoTaskList, itemCounts);
    }

    /**
     * 低消分配提醒任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "notifyToOperatorForDixiao.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult notifyToOperatorForDixiao(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return dixiaoService.reminder(userDomain);
    }


    /**
     * 低消档位线下分配
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "allocateDixiaoOffline.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult allocateDixiaoOffline(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return dixiaoService.allocateDixiaoOffline(paras, userDomain);
    }

    /**
     * 省级操作员低消档位线下分配
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "allocateDixiaoOfflineforProvince.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult allocateDixiaoOfflineforProvince(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.allocateDixiaoOfflineforProvince(paras);
    }

    /**
     * 低消档位线上分配
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "allocateDixiaoOnline.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult allocateDixiaoOnline(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.allocateDixiaoOnline(paras);
    }

    /**
     * 修改低消任务状态
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "modifyTaskStatus.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult modifyTaskStatus(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.modifyTaskStatus(paras);
    }

    /**
     * 线上团队分配
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "allocatePartner.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult allocatePartner(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.allocatePartner(paras);
    }

    /**
     * 推送文件给风雷
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "sendToFenglei.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult sendToFenglei(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.ModifyNotifyFtp(paras);
    }

    /**
     * 线下确认分配后一键推动话+
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "sendToVoiceplusOffline.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult sendToVoiceplusOffline(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        paras.put("status", DixiaoTaskStatusEnum.TASK_CHOOSE_FINISH_OFFLINE.getValue());
        return dixiaoService.modifyTaskStatus(paras);
        //return dixiaoService.sendToVoiceplusOffline(paras);
    }

    /**
     * 线上确认分配后一键推动话+
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "sendToVoiceplusOnline.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult sendToVoiceplusOnline(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        //未有分配的档位，不需要推送
        if (dixiaoResultMapper.queryToVoiceOnlineFtpTotal((Integer)paras.get("taskid"))==0){
            return new ServiceResult(-1,"当前未分配档位，请确认!");
        }

        //状态置位
        paras.put("status",DixiaoTaskStatusEnum.TASK_CHOOSE_FINISH_ONLINE.getValue());
        return dixiaoService.modifyTaskStatus(paras);
    }

    /**
    * 用户低销信息查询
    ** @param paras
    * @param session
    * @return
            */
    @RequestMapping(value = "queryDixiaoListByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<DixiaoListDomain> queryDixiaoListByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        List<DixiaoListDomain> list = dixiaoService.queryDixiaoListByPage(paras);
        Integer count = dixiaoService.queryDixiaoListTotal(paras);
        return new Table(list,count);
    }

    /**
     * 低消任务配置 导出excel
     *
     * @param paras
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "exportDixiao.view", method = RequestMethod.POST)
    public void exportDixiao(@RequestParam  Map<String, Object> paras, HttpServletRequest request,
                             HttpServletResponse response, HttpSession session)
    {
        dixiaoService.exportDixiao(paras,request,response);
    }


    /**
     * 更新档位类型
     ** @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "updateDixiaoRankType.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateDixiaoRankType(@RequestBody Map<String, Object> paras, HttpSession session) {
        return dixiaoService.updateDixiaoRankType(paras);
    }

    /**
     * 查询低消统计信息
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryDixiaoStatistic.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryDixiaoStatistic(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dixiaoService.queryDixiaoStatistic(paras);
    }

    /**
     * 查询低消线上团队统计信息
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryDixiaoParnterStatistic.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryDixiaoParnterStatistic(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, Object>> bases = dixiaoService.queryDixiaoParnterStatistic(paras);
        Integer count = bases.size();
        //dixiaoService.queryDixiaoParnterStatisticTotal(paras);
        return new Table(bases,count);
    }

    /**
     * 根据taskid查询低消任务信息
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryDixiaoStatus.view", method = RequestMethod.POST)
    @ResponseBody
    public DixiaoTaskDomain queryDixiaoStatus(@RequestBody Map<String, Object> paras)
    {
        return dixiaoService.queryDixiaoTaskById(paras);
    }
}
