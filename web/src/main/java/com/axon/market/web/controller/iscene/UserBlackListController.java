package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.UserBlackListDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.iscene.UserBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wangtt on 2017/3/6.
 */
@Controller("userBlackListController")
public class UserBlackListController
{
    @Autowired
    @Qualifier("userBlackListService")
    private UserBlackListService userBlackListService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil encryptUtil;

    /**
     * 查询导航用户黑名单
     *
     * @param offset
     * @param limit
     * @param mob
     * @return
     */
    @RequestMapping(value = "getUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<UserBlackListDomain> getUserBlackList(@RequestParam("start") Integer offset,
                                                       @RequestParam("length") Integer limit,
                                                       @RequestParam(value = "mob", required = false) String mob)
    {
        if (StringUtils.isEmpty(mob))
        {
            //加载全部
            return userBlackListService.queryUserBlackList(offset, limit, null);
        }
        else
        {
            //根据手机号查询
            //先对手机号码进行加密
            String mobile = encryptUtil.encrypt(SearchConditionUtil.optimizeCondition(mob));
            return userBlackListService.queryUserBlackList(offset, limit, SearchConditionUtil.optimizeCondition(mobile));
        }
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "addUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addUserBlackList(@RequestBody Map<String, String> params)
    {
        return userBlackListService.addUserBlackList(params);
    }

    /**
     * 查询任务列表
     *
     * @param offset
     * @param limit
     * @param taskName
     * @return
     */
    @RequestMapping(value = "getTaskListForUserBlack.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryTaskList(@RequestParam("start") Integer offset,
                               @RequestParam("length") Integer limit,
                               @RequestParam(value = "taskName", required = false) String taskName)
    {

        if (StringUtils.isEmpty(taskName))
        {
            taskName = null;
        }
        return userBlackListService.queryTaskForUserBlackList(offset, limit, taskName);
    }

    /**
     * 删除用户黑名单
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteUserBlackList.view",method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteUserBlackList(@RequestBody Map<String,Integer> param){
        Integer id = param.get("id");
        if(id == null){
            return new Operation(false, ReturnMessage.ERROR);
        }
        return userBlackListService.deleteUserBlackList(id);
    }





}
