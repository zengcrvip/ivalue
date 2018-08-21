package com.axon.market.web.controller.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.greenplum.GreenPlumMetaDataService;
import com.axon.market.common.domain.greenplum.TableColumnDomain;
import com.axon.market.core.service.itag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/25.
 */
@Controller("tagController")
public class TagController
{
    @Autowired()
    @Qualifier("tagService")
    private TagService tagService;

    @Autowired()
    @Qualifier("greenPlumMetaDataService")
    private GreenPlumMetaDataService greenPlumMetaDataService;

    @RequestMapping(value = "querySchemas.view")
    @ResponseBody
    public List<String> getSchemas(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return greenPlumMetaDataService.getSchemas();
    }

    @RequestMapping(value = "queryTableAndViews.view")
    @ResponseBody
    public List<String> getTableAndViews(@RequestBody Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String schema = param.get("schema");
        return greenPlumMetaDataService.getTableAndViews(schema);
    }

    @RequestMapping(value = "queryTableColumnInfo.view")
    @ResponseBody
    public List<TableColumnDomain> getTableColumnInfo(@RequestBody Map<String, String> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String schema = String.valueOf(paras.get("schema"));
        String tableName = String.valueOf(paras.get("tableName"));
        return greenPlumMetaDataService.getTableColumnInfo(schema, tableName);
    }

    @RequestMapping(value = "queryAllTagsByPage.view")
    @ResponseBody
    public Table queryAllTagsByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String searchContent = param.get("searchContent");
        return tagService.queryTagsByPage(searchContent, start, length);
    }

    @RequestMapping(value = "addOrEditTag.view")
    @ResponseBody
    public Operation addOrEditTag(@RequestBody TagDomain tagDomain, HttpSession session) throws IOException
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return tagService.addOrEditTag(tagDomain, userDomain);
    }

    @RequestMapping(value = "deleteTag.view")
    @ResponseBody
    public Operation deleteTag(@RequestBody TagDomain tagDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return tagService.deleteTag(tagDomain.getId(), userDomain.getId());
    }

    @RequestMapping(value = "queryAllTagSchemaAndNames.view")
    @ResponseBody
    public List<Map<String, String>> queryAllTagSchemaAndNames(@RequestBody TagDomain tagDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return tagService.queryAllTagSchemaAndNames();
    }

    @RequestMapping(value = "loadTagDataFromImportFile.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation loadTagDataFromImportFile(HttpServletRequest request)
    {
        Integer id = Integer.parseInt(request.getParameter("id"));
        return tagService.loadTagDataFromImportFile(request, id);
    }
}
