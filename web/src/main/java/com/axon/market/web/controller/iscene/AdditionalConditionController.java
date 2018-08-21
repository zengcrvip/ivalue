package com.axon.market.web.controller.iscene;


import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.AdditionalConditionDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.iscene.AdditionalConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2016/12/1.
 */
@Controller("additionalConditionController")
public class AdditionalConditionController
{

    @Autowired
    @Qualifier("additionalService")
    private AdditionalConditionService additionalService;

    /**
     * 获取导航指标数据
     * @param name
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "getAdditionalData.view" , method = RequestMethod.POST)
    @ResponseBody
    public Table<AdditionalConditionDomain> getGlobalSettingList(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "start") Integer offset,
                                                                 @RequestParam(value = "length") Integer limit)
    {   //如果前台传入name为空或null，统一设置name为null丢给给sql语句里面判断
        if (StringUtils.isEmpty(name))
        {
            name = null;
        }
        List<AdditionalConditionDomain> list = additionalService.queryAdditionalDomain(SearchConditionUtil.optimizeCondition(name), limit, offset);
        Table table = new Table(list, list.size());
        return table;
    }

    /**
     * 获取新增下拉
     * @return
     */
    @RequestMapping(value = "getAdditionalType.view" ,method =  RequestMethod.POST)
    @ResponseBody
    public SelectResult getAdditionalType()
    {
        String options = "<option value='1'>int</option><option value='2'>String</option>";
        return new SelectResult(options);
    }

    /**
     * 内部类作为返回对象包装message
     */
    private class SelectResult{
        public String message;

        public SelectResult(String message)
        {
            this.message = message;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }
    }

    /**
     * 新增或修改
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditionAdditional.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditAdditional(@RequestBody Map<String,String> param, HttpSession session)
    {
        if (param.get("id") == null)
        {   //数据没取到
            return new Operation(false, ReturnMessage.ERROR);
        }
        Integer id = Integer.parseInt(param.get("id"));
        String name = param.get("name");
        String desc = param.get("desc");
        Integer type = Integer.parseInt(param.get("type"));

//        UserDomain user = UserUtils.getLoginUser(session);
//        String userName = user.getName();
//        Integer userId = user.getId();
        //暂时写死数据
        String userName = "admin";
        Integer userId = 1;

        AdditionalConditionDomain ac = new AdditionalConditionDomain(id, name, desc, type, userName, userId, 0);

        if (id > 0)
        {   //修改
            if (additionalService.editAdditionalCondition(ac))
            {
                return new Operation(true, ReturnMessage.EDIT_SUCCESS);
            }
            else
            {
                return new Operation(false, ReturnMessage.EDIT_FAILED);
            }
        }
        else
        { //新增
            if (additionalService.addAdditionalDomain(ac))
            {
                return new Operation(true, ReturnMessage.ADD_SUCCESS);
            }
            else
            {
                return new Operation(false, ReturnMessage.ADD_FAILED);
            }
        }
    }

    /**
     * 删除
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteAdditional.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation delAdditionalCondition(@RequestBody Map<String , String> param,HttpSession session)
    {
//        UserDomain user = UserUtils.getLoginUser(session);
//        String userName=user.getName();
//        Integer userId=user.getId();
        //暂时写死数据
        String userName = "admin";
        Integer userId = 1;

        if (param.get("id") == null)
        {
            return new Operation(false, ReturnMessage.ERROR);
        }
        else
        {
            Integer id = Integer.parseInt(param.get("id"));
            if (additionalService.deleteAdditionCondition(id,userName,userId))
            {
                return new Operation(true, ReturnMessage.DELETE_SUCCESS);
            }
            else
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
        }
    }

}
