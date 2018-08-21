package com.axon.market.web.controller.iresource;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iresource.SmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/20.
 */
@Controller("smsContentController")
public class SmsContentController
{
    @Autowired
    @Qualifier("smsContentService")
    private SmsContentService smsContentService;

    /**
     * 列表查询
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "querySmsContentsByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table querySmsContentsByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        return smsContentService.querySmsContentsByPage(param);
    }

    /**
     * 新增/修改
     * @param smsContent
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditSmsContent.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditSmsContent(@RequestBody SmsContentDomain smsContent, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return smsContentService.addOrEditSmsContent(smsContent, userDomain);
    }

    /**
     * 删除
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteSmsContent.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteSmsContent(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String contentId = String.valueOf(params.get("id"));
        return smsContentService.deleteSmsContentById(Integer.valueOf(contentId), userDomain.getId());
    }



    @RequestMapping(value = "queryAllProductUnderCatalog.view", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDomain> queryAllProductUnderCatalog(HttpSession session)
    {
        return smsContentService.queryAllProductUnderCatalog();
    }

    /**
     * 
     * @param param 0：炒店业务， 1：场景业务  2：掌柜短信业务  3：掌柜话+业务' 
     * @return
     */
    @RequestMapping(value = "queryContentByBusinessType.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryContentByBusinessType(@RequestParam Map<String, String> param)
    {
        return smsContentService.queryContentByBusinessType(param);
    }
}
