package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.SmsSendConfigDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.SmsSendConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/24.
 */
@Controller
public class SmsSendController
{
    @Autowired
    @Qualifier("smsSendConfigService")
    private SmsSendConfigService smsSendConfigService;

    @RequestMapping(value = "getSPNum", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSPNum()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> list = smsSendConfigService.getSPNum();
        result.put("retCode", "0");
        result.put("extraMsg", "查询成功");
        result.put("configs", list);
        return result;
    }

    @RequestMapping(value = "queryAllEffectiveAccessNumbers.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> queryAllEffectiveAccessNumbers(HttpSession session)
    {
        return smsSendConfigService.queryAllEffectiveAccessNumbers();
    }

    /**
     * 查询短信发送配置
     *
     * @param accessNumber 接入号
     * @param start        从第几页开始
     * @param length       获取几条数据
     * @return Table       列表统一返回对象
     */
    @RequestMapping(value = "querySmsSendConfigList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table querySmsSendConfigList(@RequestParam(value = "accessNumber", required = false) String accessNumber, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "length", required = false) Integer length)
    {
        return smsSendConfigService.querySmsSendConfigList(SearchConditionUtil.optimizeCondition(accessNumber), start, length);
    }

    /**
     * 新增或修改 短信发送配置
     *
     * @param smsSendConfigDomain 实体对象
     * @param session             HttpSession对象
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "addOrEditSmsSendConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditSmsSendConfig(@RequestBody SmsSendConfigDomain smsSendConfigDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return smsSendConfigService.addOrEditSmsSendConfig(smsSendConfigDomain, userDomain);
    }

    /**
     * 删除 短信发送配置
     *
     * @param params id 主键
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteSmsSendConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteSmsSendConfig(@RequestBody Map<String, Object> params)
    {
        String id = String.valueOf(params.get("id"));
        return smsSendConfigService.deleteSmsSendConfig(Integer.valueOf(id));
    }

}
