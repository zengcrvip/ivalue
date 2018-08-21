package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.iscene.PositionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置场景统计
 * Created by zengcr on 2016/12/12.
 */
@Controller("positionReportController")
public class PositionReportController
{
    @Autowired
    @Qualifier("positionReportService")
    private PositionReportService positionReportService;
    //导出EXECL数据字段
    private static String[] fields = new String[]{"cdate", "taskName", "businessType", "baseName", "locationType", "areaName", "sendNum", "receiveNum", "flag"};
    private static Map<String,String> businessMap = new HashMap<String,String>(){
        {
            put("2","临时摊点（校园）");
            put("3","临时摊点（集客）");
            put("4","临时摊点（公众）");
        }
    };

    /**
     * 位置场景统计分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionReportByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryPositionReportByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        paras.put("offset", curPageIndex);
        paras.put("limit", pageSize);

        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        paras.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());
        paras.put("taskName", SearchConditionUtil.optimizeCondition(null != paras.get("taskName")?String.valueOf(paras.get("taskName")):""));

        int itemCounts = 0;
        List<Map<String, Object>> positionSceneList = null;
        itemCounts = positionReportService.queryPositionReportTotal(paras);
        positionSceneList = positionReportService.queryPositionSceneByPage(paras);
        result.put("itemCounts", itemCounts);
        result.put("items", positionSceneList);
        return new Table(positionSceneList, itemCounts);
    }

    /**
     * 位置场景基站导出
     *
     * @param param
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getPositionReportDown.view", method = RequestMethod.POST)
    public void getPositionReportDown(@RequestParam Map<String, Object> param, HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        param.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        param.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        String areaName = (String) param.get("areaName");
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        datas = positionReportService.queryPositionReport(param);

        String tableName = "场景任务统计-" + areaName;
        List<ExcelRowEntity> excelDataList = getExcelData(datas);
        ExportUtils.getInstance().exportData(tableName, excelDataList, request, response);
    }

    /**
     * 私有方法，生成EXECl导出对象
     *
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
        cellEntityList1.add(new ExcelCellEntity(1, 1, "日期"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "任务名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "业务类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "地市"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信发送人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信到达人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信到达率"));

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
                        String data = ""+rowData.get(fields[j]);
                        if(j == 2 && ("2".equals(data) || "3".equals(data) || "4".equals(data))){
                            value = "" + businessMap.get(data);
                        }else{
                            value = "" + rowData.get(fields[j]);
                        }

                    }
                    cellEntityList.add(new ExcelCellEntity(1, 1, value));
                }
                result.add(excelRow);
            }
        }

        return result;
    }


    /**
     * 获取位置场景统计 接口
     *
     * @param paras
     * @param token
     * @return
     */
    @RequestMapping(value = "queryPositionReport", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryPositionReport(@RequestBody Map<String, Object> paras, @RequestHeader("token") String token)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!"1*_ds(+W>".equals(token))  // 未通过验证
        {
            result.put("ResultCode", "00001");
            result.put("Message", "failed to pass the authentication");
            return result;
        }

        paras.put("offset", -1);
        paras.put("limit", -1);
        paras.put("areaCode", 99999);
        paras.put("businessCodes", "");


        List<Map<String, Object>> positionSceneList = positionReportService.queryPositionSceneByPage(paras);
        if (positionSceneList == null)
        {
            result.put("ResultCode", "00002");
            result.put("Message", "queryPositionScene error");
            return result;
        }

        try
        {
            result.put("ResultCode", "00000");
            result.put("Message", JsonUtil.objectToString(positionSceneList));
            return result;
        }
        catch (Exception e)
        {
            result.put("ResultCode", "00003");
            result.put("Message", "json parse error");
            return result;
        }
    }
}
