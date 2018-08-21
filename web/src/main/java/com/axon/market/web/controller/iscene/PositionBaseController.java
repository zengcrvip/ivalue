package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.iscene.PositionBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * 位置场景基站站点管理
 * Created by zengcr on 2016/12/1.
 */
@Controller("positionBaseController")
public class PositionBaseController
{

    private static final Logger LOG = Logger.getLogger(PositionBaseController.class.getName());

    @Qualifier("positionBaseService")
    @Autowired
    private PositionBaseInfoService positionBaseService;

    /**
     * 导出EXECL数据字段
     */
    private static String[] fields = new String[]{"baseId", "baseName", "locationType", "cityName", "businessHallCode", "lng", "lat", "radius", "address", "addDate", "status"};
    private static final Logger log = Logger.getLogger(PositionBaseController.class);

    /**
     * 位置场景基站配置分页展示
     * @param paras 基站ID，基站名称，所属地区
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionBaseByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<PositionBaseDomain> queryPositionBaseByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String)paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //基站ＩＤ
        String baseId = (String) (paras.get("baseId"));
        //基站名称
        String baseName = (String) (paras.get("baseName"));
        //所属地市
        String baseArea = (String) (paras.get("baseArea"));
        Integer itemCounts = 0;

        List<PositionBaseDomain> positionBaseDomainList = null;
        itemCounts = positionBaseService.queryPositionBaseTotal(baseId, baseName, baseArea);
        positionBaseDomainList = positionBaseService.queryPositionBaseByPage(curPageIndex, pageSize, baseId, baseName, baseArea);

        result.put("itemCounts", itemCounts);
        result.put("items", positionBaseDomainList);
        return new Table(positionBaseDomainList,itemCounts);
    }

    /**
     * 新建或修改位置场景基站
     * 当ID不存在时新增，存在时修改
     * @param positionBaseDomain 基站对象实体
     * @param session
     * @return
     */
    @RequestMapping(value = "createOrUpdatePositionBase.view")
    @ResponseBody
    public ServiceResult createOrUpdatePositionBase(@RequestBody PositionBaseDomain positionBaseDomain, HttpSession session)
    {
        return positionBaseService.createOrUpdatePositionBase(positionBaseDomain);
    }

    /**
     * 根据ID查询位置基站
     * @param paras 主键ID
     * @param session
     * @return
     */
    @RequestMapping(value="queryPositionBaseById.view")
    @ResponseBody
    public Map<String, Object>  queryPositionBaseById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer baseId = Integer.valueOf(String.valueOf(paras.get("baseId")));
        PositionBaseDomain positionBaseDomain = positionBaseService.queryPositionBaseById(baseId);
        result.put("positionBaseDomain", positionBaseDomain);
        return result;
    }
    /**
     * 根据ID查询删除营业厅信息
     * @param paras 主键ID
     * @param session
     * @return
     */
    @RequestMapping(value = "delPositionBaseById.view")
    @ResponseBody
    public List<Map<String, Object>>  delPositionBaseById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Integer baseId = Integer.valueOf(String.valueOf(paras.get("baseId")));
        Map<String,Object> name=positionBaseService.delUserPositionBaseById(baseId);
        List<Map<String, Object>> result = positionBaseService.delTaskPositionBaseById(baseId);
        result.add(0,name);
        return result;


    }

    /**
     * 根据ID删除位置场景基站
     * @param paras 主键ID
     * @param session
     * @return
     */
    @RequestMapping(value = "deletePositionBaseById.view")
    @ResponseBody
    public ServiceResult deletePositionBaseById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Integer baseId = Integer.valueOf(String.valueOf(paras.get("baseId")));
        String input=String.valueOf(paras.get("input"));
        String userId=String.valueOf(paras.get("userId"));
        ServiceResult result = new ServiceResult();
        try
        {
            positionBaseService.deletePositionBaseById(baseId,input,userId);
        }
        catch (Exception e)
        {
            LOG.error("失效营业厅失败",e);
            result.setRetValue(-1);
            result.setDesc("数据库删除操作异常");
        }
        return result;
    }

    /**
     * 位置场景基站EXECl导出
     * @param param
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getBaseDataDown.view", method = RequestMethod.POST)
    public void getPositionReportDown(@RequestParam Map<String, Object> param, HttpServletRequest request,
                                 HttpServletResponse response,HttpSession session)
    {
        String areaName = (String) param.get("areaName");
        String baseId = (String) (param.get("baseId"));
        String baseName = (String) (param.get("baseName"));
        String areaCode = (String) (param.get("areaCode"));
        String buscoding = (String) (param.get("buscoding"));

        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer createUserId = userDomain.getId();
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }

        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        datas = positionBaseService.queryPositionBase(baseId, baseName, areaCode,createUserId,baseIdArray,cityCode,buscoding);

        String tableName = "基站明细-" + areaName;
        List<ExcelRowEntity> excelDataList = getExcelData(datas);
        ExportUtils.getInstance().exportData(tableName, excelDataList, request, response);
    }

    /**
     * 私有方法，生成execl表格对象
     * @param dataList 源数据
     * @return
     */
    private List<ExcelRowEntity> getExcelData(List<Map<String, Object>> dataList)
    {
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();

        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "位置点ID"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "位置点名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "位置点类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "所属城市"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "经度"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "纬度"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "半径(米)"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "地址"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "更新时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "状态"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();
                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                for (int j = 0; j < fields.length; j++)
                {
                    String value = "";
                    if (null != rowData.get(fields[j]))
                    {
                        value = "" + rowData.get(fields[j]);
                    }
                    cellEntityList.add(new ExcelCellEntity(1, 1, value));
                }
                result.add(excelRow);
            }
        }

        return result;
    }

    /**
     * 批量导入基站数据
     * @param request
     * @return
     */
    @RequestMapping("batchImportBaseInfo.view")
    @ResponseBody
    public Map<String,Object> batchImportBaseInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("file");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        Long fileId = new Date().getTime();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", file.getOriginalFilename());
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("taskType", "位置场景基站导入");
        fileInfo.put("createUser", userDomain == null ? "admin": userDomain.getName());
        fileInfo.put("createDate", new Date());
        ServiceResult returnResult = null;
        Map<String,Object> result = new HashMap<String,Object>();
        try
        {
            returnResult =  positionBaseService.storeFile(fileInfo, file.getInputStream());
            result.put("retValue",returnResult.getRetValue());
            result.put("desc",returnResult.getDesc());
            result.put("fileId",fileId);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            result.put("retValue", "-1");
            result.put("desc",e.getMessage());
        }
        return result;
    }


    /**
     * 批量导入基站点列表展示
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionBaseImport.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryPositionBaseImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        Long fileId = (Long)paras.get("fileId");
        Integer itemCounts = 0;

        List<Map<String,Object>> positionBaseImportList = null;
        itemCounts = positionBaseService.queryPositionBaseImportTotal(fileId);
        positionBaseImportList = positionBaseService.queryPositionBaseImport((curPage - 1) * countsPerPage, countsPerPage,fileId);

        result.put("itemCounts", itemCounts);
        result.put("items", positionBaseImportList);
        return result;
    }

    /**
     * 临时保存导入基站数据
     * @param session
     * @return
     */
    @RequestMapping(value = "createPositionBaseImport.view")
    @ResponseBody
    public ServiceResult createPositionBaseImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return positionBaseService.createPositionBaseImport(paras);
    }
}
