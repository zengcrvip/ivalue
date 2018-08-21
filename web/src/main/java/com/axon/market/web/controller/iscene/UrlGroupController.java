package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.iscene.UrlDomain;
import com.axon.market.common.domain.iscene.UrlGroupDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;

import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.iscene.UrlGroupService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * Created by DELL on 2016/12/23.
 */
@Controller("urlGroupController")
public class UrlGroupController
{
    @Autowired
    @Qualifier("urlGroupService")
    private UrlGroupService urlGroupService;

    private static final Logger LOG = Logger.getLogger(UrlGroupController.class.getName());


    /**
     * 获取url分类数据
     *
     * @param offset
     * @param limit
     * @param groupName//分类名称
     * @param urlName//网址名称
     * @param urlWord//网址
     * @return
     */
    @RequestMapping(value = "getUrlGroupList.view" , method = RequestMethod.POST)
    @ResponseBody
    public Table<UrlGroupDomain> getUrlGroupList(@RequestParam("start") Integer offset,
                                                 @RequestParam("length") Integer limit,
                                                 @RequestParam(value = "groupName", required = false) String groupName,
                                                 @RequestParam(value = "urlName", required = false) String urlName,
                                                 @RequestParam(value = "urlWord", required = false) String urlWord
    )
    {
        return urlGroupService.getUrlGroupList(offset, limit, SearchConditionUtil.optimizeCondition(groupName), SearchConditionUtil.optimizeCondition(urlName), SearchConditionUtil.optimizeCondition(urlWord));
    }

    /**
     * 获取url数据
     *
     * @param offset
     * @param limit
     * @param id
     * @return
     */
    @RequestMapping(value = "getUrlList.view" , method = RequestMethod.POST)
    @ResponseBody
    public Table<UrlDomain> getUrlList(@RequestParam("start") Integer offset,
                                       @RequestParam("length") Integer limit,
                                       @RequestParam(value = "id", required = false) Integer id
    )
    {
        return urlGroupService.queryUrlList(offset, limit, id);
    }

    /**
     * 文件上传
     * @param fileName//js处理后的文件名
     * @param orgFileName//原文件名
     * @param request
     * @return
     */
    @RequestMapping("uploadUrlFile.view")
    @ResponseBody
    public Operation uploadUrlFile(@RequestParam(value = "fname",required = false) String fileName,
                                   @RequestParam(value = "originalFile",required = false) String orgFileName,
                                   HttpServletRequest request)
    {
        if(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(orgFileName)){
            return new Operation(false,"文件传输异常");
        }
        return urlGroupService.uploadUrlFile(fileName,orgFileName,request);
    }


    /**
     * 上传文件入库
     *
     * @param fileName//文件名
     * @param request
     * @return
     */
    @RequestMapping(value = "addUrlState.view" , method =  RequestMethod.POST)
    @ResponseBody
    public Operation addUrlState(@RequestParam(value = "name", required = false) String fileName,
                                 HttpServletRequest request)
    {
        boolean isSuccess = false;
        String filePath = request.getSession().getServletContext().getRealPath(fileName);
        if(!StringUtils.isEmpty(filePath)){
            isSuccess = urlGroupService.batchUpload(filePath, fileName.split("-")[0], request.getSession());
        }
        return new Operation(isSuccess, "");
    }

    /**
     * 新增或修改网址列表名
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditUrlGroup.view" , method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditUrlGroup(@RequestBody Map<String, String> param, HttpSession session)
    {
        return urlGroupService.addOrEditUrlGroup(param, session);
    }

    /**
     * 删除网址群组
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteUrlGroup.view" , method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteUrlGroup(@RequestBody Map<String, String> param)
    {
        String urlGroupId = param.get("urlgpId");
        if (StringUtils.isEmpty(urlGroupId))
        {
            new Operation(false, ReturnMessage.ERROR);
        }
        Integer id = Integer.parseInt(urlGroupId);
        return urlGroupService.deleteUrlGroup(id);
    }

    /**
     * 删除单条数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteUrl.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteUrl(@RequestBody Map<String, String> param)
    {
        String id = param.get("Id");
        String groupId = param.get("GpId");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(groupId))
        {
            return new Operation(false, ReturnMessage.ERROR);
        }
        return urlGroupService.deleteUrl(Integer.parseInt(id), Integer.parseInt(groupId));
    }

    /**
     * 新增单条url数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "saveUrl.view" , method = RequestMethod.POST)
    @ResponseBody
    public Operation saveUrl(@RequestBody Map<String, String> param)
    {
        String id = param.get("id");//网址群组的id
        String url = param.get("url");//网址
        String name = param.get("name");//网址名
        if (StringUtils.isEmpty(id) || Integer.parseInt(id) < 0)
        {
            return new Operation(false, ReturnMessage.ERROR);
        }
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(name))
        {
            return new Operation(false, ReturnMessage.EMPTY_CONTENT);
        }
        UrlDomain urlDomain = new UrlDomain();
        urlDomain.setUrl(url);
        urlDomain.setName(name);
        urlDomain.setIsDelete(0);
        urlDomain.setUrlGroupId(Integer.parseInt(id));
        return urlGroupService.saveUrl(urlDomain, Integer.parseInt(id));
    }
}
