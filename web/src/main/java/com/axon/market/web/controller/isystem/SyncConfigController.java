package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.isystem.SyncConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * Created by xuan on 2017/4/24.
 */
@Controller("syncConfigController")
public class SyncConfigController
{
    @Autowired
    @Qualifier("syncConfigService")
    private SyncConfigService syncConfigService;

    /**
     * 列表查询
     * @param param
     * @return
     */
    @RequestMapping(value = "querySyncConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Table querySyncConfig(@RequestParam Map<String, String> param)
    {
        return syncConfigService.querySyncConfig(param);
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditSync.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditSync(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Operation operation = syncConfigService.addOrEditSync(paras);
        return operation;
    }

    /**
     * 删除
     *
     * @param paras id 模型id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteSync.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteSync(@RequestBody Map<String, Object> paras)
    {
        Operation operation = syncConfigService.deleteSync(paras);
        return operation;
    }

    /**
     * 同步
     * @return
     */
    @RequestMapping(value = "syncConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation syncConfig()
    {
//        //判断点击同步当前时间是否是4的倍数
//        Date d = new Date();
//        int hours = d.getHours();
//        if(hours/4==0)
//        {
//            Operation operation = syncConfigService.syncConfig();
//            return operation;
//        }
//        else
//        {
//
//            return new Operation(false, "当前时间小时数不是4的倍数，不能执行同步！");
//        }
        Operation operation = syncConfigService.syncConfig();
        return operation;
    }

    /**
     * 列表查询
     * @return
     */
    @RequestMapping(value = "querySyncById.view", method = RequestMethod.POST)
    @ResponseBody
    public Table querySyncById(@RequestBody Map<String, Object> paras)
    {
        return syncConfigService.querySyncById(paras);
    }
}
