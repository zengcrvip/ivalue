package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iStatistics.MessageMarketingDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.istatistics.MessageMarketingResultService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/4/17.
 */
@Controller("messageMarketingResult")
public class MessageMarketingResultController
{
    @Autowired
    @Qualifier("messageMarketingResultService")
    MessageMarketingResultService mmrService;

    private static final Logger LOG = Logger.getLogger(MessageMarketingResultController.class.getName());

    private static String[] fields = new String[]{"timest", "activiteName", "target_usernum", "send_num", "send_succ_percent", "recv_succ_usernum", "recv_succ_percent", "feedback_usernum",
            "feedback_percent", "feedback_usercnt", "product_order_cnt", "product_order_user", "product_order_succ_percent"};

    private NumberFormat numberFormat = NumberFormat.getPercentInstance();

    /**
     * 查询gpdb中的结果数据
     *
     * @return
     */
    @RequestMapping(value = "queryMessageMarketingResult.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<MessageMarketingDomain> queryDataFromGreenPlum(@RequestParam(value = "marketName", required = false) String marketName,
                                                                @RequestParam(value = "dateTime", required = false) String dateTime,
                                                                @RequestParam(value = "endTime", required = false) String endTime,
                                                                @RequestParam("start") Integer offset,
                                                                @RequestParam("length") Integer limit)
    {
        Table table = new Table();
        try
        {
            //对前台客户端输入的特殊字符进行处理
            String name =  SearchConditionUtil.optimizeConditionForGP(marketName);
            Integer count = mmrService.queryCount(name, dateTime, endTime);
            List list = mmrService.queryDataFromGP(name, offset, limit, dateTime, endTime);
            table = new Table<MessageMarketingDomain>(list, count);

        }
        catch (Exception e)
        {
            LOG.error("查询GP数据库异常: ", e);
            return table;
        }
        return table;
    }


    /**
     * 导出短信营销统计
     *
     * @param param
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "downloadMessageMarketingResult.view", method = RequestMethod.POST)
    public void getPositionReportDown(@RequestParam Map<String, Object> param, HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        param.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        param.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());
        String marketName = (String) param.get("marketingName");
        String startTime = (String) param.get("dateTime");
        String endTime = (String) param.get("endTime");
        List<MessageMarketingDomain> datas = new ArrayList<MessageMarketingDomain>();
        try
        {
            datas = mmrService.queryDataFromGP(marketName, null, null, startTime, endTime);//导出查询所有数据，设置分页为null
        }
        catch (Exception e)
        {
            LOG.error("短信营销统计导出表格失败：" + e.toString());
        }
        String tableName = startTime + "至" + endTime + "短信营销统计报表";
        List<Map<String, Object>> datalist = new ArrayList<>();
        numberFormat.setMaximumFractionDigits(2);//设置小数点之后保留2位小数
        for (MessageMarketingDomain md : datas)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("timest", md.getTimest());
            map.put("activiteName", md.getActiviteName());
            map.put("target_usernum", md.getTarget_usernum());
            map.put("send_num", md.getSend_num());
            //发送成功率
            map.put("send_succ_percent", numberFormat.format((float) md.getSend_num() == 0 ? 0.00 : (float) md.getSend_succ_usernum() / (float) md.getSend_num()));
            map.put("recv_succ_usernum", md.getRecv_succ_usernum());
            //到达成功率
            map.put("recv_succ_percent", numberFormat.format((float) md.getSend_succ_usernum() == 0 ? 0.00 : (float) md.getRecv_succ_usernum() / (float) md.getSend_succ_usernum()));
            map.put("feedback_usernum", md.getFeedback_usernum());
            //反馈率
            map.put("feedback_percent", numberFormat.format((float) md.getSend_succ_usernum() == 0 ? 0.00 : (float) md.getFeedback_usernum() / (float) md.getSend_succ_usernum()));
            map.put("feedback_usercnt", md.getFeedback_usercnt());
            map.put("product_order_cnt", md.getProduct_order_cnt());
            map.put("product_order_user", md.getProduct_order_user());
            //订购成功率
            map.put("product_order_succ_percent", numberFormat.format((float) md.getProduct_order_user() == 0 ? 0.00 : (float) md.getProduct_ordersucc_user() / (float) md.getProduct_order_user()));
            datalist.add(map);
        }
        List<ExcelRowEntity> excelDataList = getExcelData(datalist);
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
        cellEntityList1.add(new ExcelCellEntity(1, 1, "统计时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营销活动名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "任务目标人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "发送人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "发送成功率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "到达人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "到达率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "反馈人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "反馈率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "反馈次数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "订购笔数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "订购人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "订购成功率"));

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

}
