package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.iscene.UrlBlacklistService;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.domain.isystem.UserDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by hale on 2016/12/5.
 */
@Controller("urlBlaclistController")
public class UrlBlaclistController
{

    @Autowired
    @Qualifier("urlBlacklistService")
    private UrlBlacklistService urlBlackListService;

    /**
     * 查询网址黑名单
     *
     * @param url    网址
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     */
    @RequestMapping(value = "queryUrlBlacklist.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryUrlBlacklist(@RequestParam(value = "url", required = false) String url, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "length", required = false) Integer length)
    {
        return urlBlackListService.queryUrlBlacklist(SearchConditionUtil.optimizeCondition(url), start, length);
    }

    /**
     * 新增或删除网址黑名单
     *
     * @param paras   type 类型 url 网址
     * @param session HttpSession对象
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "addOrDeleteUrlBlacklist.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrDeleteUrlBlacklist(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String type = String.valueOf(paras.get("type"));
        String url = String.valueOf(paras.get("url"));
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return urlBlackListService.addOrDeleteUrlBlacklist(type, url, userDomain.getId());
    }
}
