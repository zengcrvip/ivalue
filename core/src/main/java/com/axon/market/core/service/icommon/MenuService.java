package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.MenuResult;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.MenuDomain;
import com.axon.market.dao.mapper.icommon.IMenuMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by Administrator on 2016/11/14.
 */
@Service("menuService")
public class MenuService
{
    private static final Logger LOG = Logger.getLogger(MenuService.class.getName());

    @Autowired
    @Qualifier("menuDao")
    private IMenuMapper menuDao;

    private Map<Integer, String> levelMap = new HashMap<Integer, String>()
    {
        {
            put(2, "1");
            put(4, "2");
            put(6, "3");
            put(8, "4");
        }
    };

    public List<MenuDomain> getMenus(Integer userId, HttpSession session)
    {
        List<MenuDomain> menus = menuDao.queryMenusByUser(userId);
        //用于控制菜单跳转的权限控制
        MenuDomain menuStructureObj = getMenuStructure(menus, null, 0);
        session.setAttribute("M_PERMISSION", getMenuUrlList(menus));
        return menuStructureObj.getChildrenMenu();
    }

    /**
     * 获取所有菜单的url集合
     *
     * @param menus
     * @return
     */
    private List<String> getMenuUrlList(List<MenuDomain> menus)
    {
        List<String> menuUrl = new ArrayList<String>();
        for (MenuDomain menu : menus)
        {
            //如果url为空，说明是目录菜单，无需关注
            String url = menu.getUrl();
            if (StringUtils.isNotEmpty(url))
            {
                int startIndex = url.lastIndexOf("/") == -1 ? 0 : url.lastIndexOf("/") + 1;
                if (url.indexOf(".requestHtml") >= 0)
                {
                    menuUrl.add(url.substring(startIndex, url.indexOf(".requestHtml")));
                }
            }
        }
        return menuUrl;
    }

    /**
     * @param allMenus
     * @param parentMenu
     * @param level
     * @return
     */
    private MenuDomain getMenuStructure(List<MenuDomain> allMenus, MenuDomain parentMenu, int level)
    {
        if (parentMenu == null)
        {
            parentMenu = new MenuDomain();
            parentMenu.setId(0);
            level = 0;
        }

        for (MenuDomain menu : allMenus)
        {
            if (parentMenu.getId().equals(menu.getParentId()))
            {
                menu.setLocation(StringUtils.isEmpty(parentMenu.getLocation()) ? menu.getTitle() :
                        parentMenu.getLocation() + " > " + menu.getTitle());
                menu.setLevel(level + 1);
                parentMenu.getChildrenMenu().add(getMenuStructure(allMenus, menu, level + 1));

            }
        }
        parentMenu.getChildrenMenu().sort(new Comparator<MenuDomain>()
        {
            @Override
            public int compare(MenuDomain o1, MenuDomain o2)
            {
                return o1.getSortNo() - o2.getSortNo();
            }
        });

        return parentMenu;
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    public Table queryMenusByPage(String title, String parentId, Integer level, Integer offset, Integer limit)
    {
        try
        {
            Integer count = menuDao.queryAllMenuCounts(title, parentId, level);
            List<Map<String, String>> list = menuDao.queryMenusByPage(title, parentId, level, offset, limit);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Menus Error. ", e);
            return new Table();
        }
    }

    /**
     * 获取当前级别下栏目
     *
     * @param level
     * @return
     */
    public List<MenuDomain> queryCurrentLevelMenu(Integer level)
    {
        if (level == 0)
        {
            MenuDomain menuDomain = new MenuDomain();
            menuDomain.setId(0);
            menuDomain.setParentId(0);
            menuDomain.setLevel(1);
            menuDomain.setTitle("父级栏目");
            return new ArrayList<MenuDomain>()
            {{
                add(menuDomain);
            }};
        }
        return menuDao.queryMenusByLevel(level);
    }

    /**
     * 新增/修改栏目
     *
     * @param menuDomain
     * @return
     */
    public Operation addOrEditMenu(MenuDomain menuDomain)
    {
        // 判断标签名称是否和数据库中存在的标签名称重复
        if (isMenuExisted(menuDomain))
        {
            return new Operation(false, "栏目名称重复");
        }
        try
        {
            Boolean result;
            String message;
            // 新增
            if (menuDomain.getId() == null || menuDomain.getId() == 0)
            {
                if (!updateResetSort(menuDomain.getParentId()))
                {
                    return new Operation();
                }
                int sortNo = queryMaxSortNumberByParentId(menuDomain.getParentId());
                menuDomain.setSortNo(sortNo + 1);
                result = menuDao.createMenu(menuDomain) == 1;
                message = result ? "新增栏目成功" : "新增栏目失败";
            }
            else
            {
                if (!updateResetSort(menuDomain.getParentId()))
                {
                    return new Operation();
                }

                int sortNo = queryMaxSortNumberByParentId(menuDomain.getParentId());
                menuDomain.setSortNo(sortNo + 1);
                result = menuDao.updateMenu(menuDomain) == 1;
                message = result ? "更新栏目成功" : "更新栏目失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Menu Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param id
     * @return
     */
    public Operation deleteMenu(Integer id)
    {
        try
        {
            Boolean result = menuDao.deleteMenu(id) == 1;
            String message = result ? "删除栏目成功" : "删除栏目失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("delete Menu Error. ", e);
            return new Operation();
        }
    }

    public Boolean isMenuExisted(MenuDomain menuDomain)
    {
        MenuDomain oldMenuDomain = menuDao.queryMenuByTitle(menuDomain.getTitle());
        if (oldMenuDomain == null)
        {
            return false;
        }
        return !oldMenuDomain.getId().equals(menuDomain.getId());
    }

    /**
     * @param parentId
     * @return
     */
    private boolean updateResetSort(Integer parentId)
    {
        List<MenuResult> list = menuDao.querySortNumberByParentId(parentId);
        Integer listLength = list.size();
        Integer result = 0;
        for (MenuResult item : list)
        {
            result += menuDao.updateResetSort(item.getId(), item.getSort());
        }

        if (result == listLength)
        {
            return true;
        }
        return false;
    }

    /**
     * @param parentId
     * @return
     */
    private Integer queryMaxSortNumberByParentId(Integer parentId)
    {
        try
        {
            return menuDao.queryMaxSortNumberByParentId(parentId);
        }
        catch (Exception e)
        {
            LOG.error("queryMaxSortNumberByParentId Error:" + e);
            return 0;
        }
    }
}
