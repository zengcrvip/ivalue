package com.axon.market.web.controller.icommon;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.VerifyCodeUtil;
import com.axon.market.common.domain.icommon.MenuDomain;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import com.axon.market.web.constants.UserConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/4.
 */
@Controller("systemController")
public class SystemController
{
    @Autowired
    @Qualifier("marketingTasksService")
    private MarketingTasksService marketingTasksService;

    @RequestMapping(value = "index.view")
    public ModelAndView index()
    {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "queryAllMenus.view", method = RequestMethod.POST)
    @ResponseBody
    public List<MenuDomain> queryAllMenus(@RequestBody Map<String, Object> params, HttpSession session)
    {
        return (List<MenuDomain>) session.getAttribute(UserConstants.SESSION_MENU);
    }

    @RequestMapping(value = "{relativePath}/{htmlName}.requestHtml", method = RequestMethod.GET)
    public ModelAndView loadSegmentHtml(@PathVariable String relativePath, @PathVariable String htmlName, HttpSession session)
    {
        String url = "pages/";
        if (!"pages".equals(relativePath))
        {
            url += relativePath + "/";
        }
        return new ModelAndView(url + htmlName);
    }

    @RequestMapping(value = "getVerifyCode")
    @ResponseBody
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        VerifyCodeUtil.outputCaptcha(request, response);
    }

    /**
     * 获取系统省份信息
     *
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "querySystemConfig", method = RequestMethod.POST)
    @ResponseBody
    public Operation querySystemConfig(@RequestBody Map<String, Object> params, HttpSession session)
    {
        String province = SystemConfigBean.getInstance().getProvince();
        String provinceText = getProvinceText(province);
        Boolean result = StringUtils.isNotEmpty(provinceText);
        String msg = result ? provinceText : "";
        return new Operation(result, msg);
    }

    @RequestMapping(value = "queryUserDistribution.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> queryUserDistribution(@RequestBody Map<String, Object> params, HttpSession session)
    {
        return marketingTasksService.queryMarketingUserDistribution();
    }

    @RequestMapping(value = "queryMyTodoTask.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMyTodoTask(@RequestParam Map<String, Object> params, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        String type = String.valueOf(params.get("type"));
        return marketingTasksService.queryMyTodoTask(type, loginUser);
    }

    @RequestMapping(value = "queryMyTodoTaskColumn.view", method = RequestMethod.POST)
    @ResponseBody
    public List<String> queryMyTodoTaskColumn(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        return marketingTasksService.queryMyTodoTaskColumn(loginUser.getId());
    }

    /**
     * 获取省份对应的中文
     *
     * @param province
     * @return
     */
    private String getProvinceText(String province)
    {
        switch (province)
        {
            case "JS":
                return "江苏";
            case "GX":
                return "广西";
            case "SH":
                return "上海";
            case "GD":
                return "广东";
            case "SD":
                return "山东";
            case "FJ":
                return "福建";
            case "HUNAN":
                return "湖南";
            case "HAINAN":
                return "海南";
            case "LN":
                return "辽宁";
            case "SC":
                return "四川";
            case "GZ":
                return "贵州";
            case "AH":
                return "安徽";
            case "NMG":
                return "内蒙古";
            default:
                return "";
        }
    }
}
