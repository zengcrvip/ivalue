package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.iscene.GlobalSettingDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.core.service.iscene.GlobalSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1.
 */
@Controller("globalSettingController")
public class GlobalSettingController
{

    @Autowired(required = true)
    @Qualifier("globalSettingService")
    private GlobalSettingService globalSettingService;


    /**
     * 获取全局设置数据
     *
     * @return
     */
    @RequestMapping(value = "getGlobalSettingList.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Table<GlobalSettingDomain> getGlobalSettingList()
    {
        List<GlobalSettingDomain> data = globalSettingService.queryGlobalSettings();
        Table table = new Table(data, data.size());
        return table;
    }

    /**
     * 修改全局设置
     * @param param
     * @return
     */
    @RequestMapping(value = "editGlobalSetting.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation editGlobalSetting(@RequestBody Map<String, String> param)
    {
        if (param.get("id") == null)
        {
            //没有取到值
            return new Operation(false, ReturnMessage.ERROR);
        }
        else
        {
            //取到值
            Integer id = Integer.parseInt(param.get("id"));
            Integer num = Integer.parseInt(param.get("number"));
            if (globalSettingService.updateGlobalSettings(id, num))
            {
                //修改成功
                return new Operation(true, ReturnMessage.EDIT_SUCCESS);
            }
            else
            {
                //修改失败
                return new Operation(false, ReturnMessage.EDIT_FAILED);
            }
        }
    }
}
