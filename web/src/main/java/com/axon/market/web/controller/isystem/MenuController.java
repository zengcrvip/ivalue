package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.MenuDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/22.
 */
@Controller("menuController")
public class MenuController
{
    @Autowired
    @Qualifier("menuService")
    private MenuService menuService;

    @RequestMapping(value = "queryMenusByPage.view")
    @ResponseBody
    public Table queryMenusByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String title = SearchConditionUtil.optimizeCondition(String.valueOf(param.get("title")));
        String parentId = String.valueOf(param.get("parentId"));
        Integer level = Integer.parseInt(param.get("level"));
        return menuService.queryMenusByPage(title, parentId, level, start, length);
    }

    @RequestMapping(value = "queryCurrentLevelMenu.view")
    @ResponseBody
    public List<MenuDomain> queryCurrentLevelMenu(@RequestBody Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer level = Integer.parseInt(param.get("level"));
        return menuService.queryCurrentLevelMenu(level);
    }

    @RequestMapping(value = "addOrEditMenu.view")
    @ResponseBody
    public Operation addOrEditMenu(@RequestBody MenuDomain menuDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return menuService.addOrEditMenu(menuDomain);
    }

    @RequestMapping(value = "deleteMenu.view")
    @ResponseBody
    public Operation deleteMenu(@RequestBody MenuDomain menuDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return menuService.deleteMenu(menuDomain.getId());
    }
}
