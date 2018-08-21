package com.axon.market.web.controller.iscene;


import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.LocationDomain;
import com.axon.market.common.domain.iscene.LocationGroupDomain;


import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.core.service.iscene.LocationGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/1/3.
 */
@Controller("locationGroupController")
public class LocationGroupController
{
    @Autowired
    @Qualifier("locationGroupService")
    private LocationGroupService localGroupService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    private String templeFile;//临时文件

    /**
     * 查询地区群组列表
     *
     * @param offset
     * @param limit
     * @param groupName
     * @return
     */
    @RequestMapping(value = "getLocationGroupList.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Table<LocationGroupDomain> getLocalGroup(@RequestParam("start") Integer offset,
                                                    @RequestParam("length") Integer limit,
                                                    @RequestParam(value = "groupName", required = false) String groupName)
    {
        if (StringUtils.isEmpty(groupName))
        {
            //如果区域名称为空，统一设置为null交给sql处理
            groupName = null;
        }
        return localGroupService.getLocalGroup(offset, limit, SearchConditionUtil.optimizeCondition(groupName));
    }

    /**
     * 新增或修改地区群组名
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditLocationGroup.view" ,method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditLocationGroup(@RequestBody Map<String, String> param, HttpSession session)
    {
        String id = param.get("Id");
        String name = param.get("Name");
        if (StringUtils.isEmpty(name))
        {
            return new Operation(false, ReturnMessage.EMPTY_CONTENT);
        }
        if (StringUtils.isEmpty(id))
        {
            //新增
            return localGroupService.addLocationGroup(name, session);
        }
        else
        {
            //修改
            return localGroupService.editLocationGroup(Integer.parseInt(id), name);
        }
    }

    /**
     * 获取区域列表数据
     *
     * @param offset
     * @param limit
     * @param tableName
     * @return
     */
    @RequestMapping(value = "getLocationList.view" , method = RequestMethod.POST)
    @ResponseBody
    public Table<LocationDomain> getLocationList(@RequestParam("start") Integer offset,
                                                 @RequestParam("length") Integer limit,
                                                 @RequestParam(value = "tableName", required = false) String tableName)
    {
        if (StringUtils.isEmpty(tableName))
        {
            List<LocationDomain> list = new ArrayList<LocationDomain>();
            return new Table<LocationDomain>(list, 0);
        }
        return localGroupService.getLocationList(offset, limit, tableName);
    }

    /**
     * 删除区域数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteLocation.view" , method =  RequestMethod.POST)
    @ResponseBody
    public Operation delLocation(@RequestBody Map<String, String> param)
    {
        String id = param.get("pid");
        String tableName = param.get("tableName");
        if (StringUtils.isEmpty(id) || Integer.parseInt(id) <= 0)//判断id是否有效
        {
            return new Operation(false, ReturnMessage.DELETE_FAILED);
        }
        else
        {
            return localGroupService.delLocation(Integer.parseInt(id), tableName);
        }
    }

    /**
     * 上传文件
     *
     * @param fName
     * @param request
     * @return
     */
    @RequestMapping("upLoadLocationFile.view")
    @ResponseBody
    public Object upLoadData(@RequestParam(value = "fName", required = false) String fName,
                             HttpServletRequest request)
    {
        try
        {
            File file = fileUploadService.fileUpload(request, "temple.cvs");
            templeFile = file.getPath();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new Operation(false, org.apache.commons.lang.StringUtils.EMPTY);
        }
        return new Operation(true, org.apache.commons.lang.StringUtils.EMPTY);
    }

    private boolean isHead = true;//全局的定义默认有标题行

    /**
     * 上传文件到库
     *
     * @param uploadName
     * @param session
     * @return
     */
    @RequestMapping(value = "addsLocation.view" ,method =  RequestMethod.POST)
    @ResponseBody
    public Operation addsLocation(@RequestParam(value = "name", required = false) String uploadName,
                                  HttpSession session)
    {
//        boolean head = false;
//        //通过标记位判断是否是头部
//        if (isHead)
//        {
//            head = isHead;
//            isHead = false;
//        }
        return localGroupService.addsLocation(templeFile, isHead, uploadName, session);
    }

    /**
     * 删除地区群组
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteLocationGroup.view" ,method =  RequestMethod.POST)
    @ResponseBody
    public Operation delLocalGroup(@RequestBody Map<String, String> param)
    {
        String id = param.get("pid");
        if (StringUtils.isEmpty(id) || Integer.parseInt(id) <= 0)
        {
            return new Operation(false, ReturnMessage.ERROR);
        }
        return localGroupService.delLocalGroup(Integer.parseInt(id));
    }


}
