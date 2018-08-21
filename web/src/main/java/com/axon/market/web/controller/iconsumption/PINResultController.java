package com.axon.market.web.controller.iconsumption;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.iconsumption.*;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.dao.mapper.iconsumption.IPINResultMapper;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.domain.isystem.UserDomain;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.axon.market.common.util.AxonEncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhuwen on 2017/6/28.
 */
@Controller("pinresultController")
public class PINResultController {
    private static final Logger LOG = Logger.getLogger(PINResultController.class.getName());

    @Qualifier("PINResultDao")
    @Autowired
    private IPINResultMapper iPINResultMapper;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    private static Map<String,String> areaMap = new HashMap<String,String>(){
        {
            put("25", "南京市");
            put("510", "无锡市");
            put("511", "镇江市");
            put("512", "苏州市");
            put("513", "南通市");
            put("514", "扬州市");
            put("515", "盐城市");
            put("516", "徐州市");
            put("517", "淮安市");
            put("518", "连云港市");
            put("519", "常州市");
            put("523", "泰州市");
            put("527", "宿迁市");
        }
    };

    /**
     * 低销波次统计结果分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPINResultByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryPINResultByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //产品名称
        String rankname = SearchConditionUtil.optimizeCondition((String) (paras.get("rankname"))).trim();
        //地区编码
        String area = (String) (paras.get("area"));
        //波次号
        String batchno = (String)paras.get("batchno");
        //日期编码
        String monthcode = (String) (paras.get("monthcode"));
        //档位类型
        String rankType = (String)paras.get("rankType");

        Integer itemCounts = 0;

        parasMap.put("rankname", rankname);
        parasMap.put("area", area);
        parasMap.put("batchno", batchno);
        parasMap.put("monthcode", monthcode);
        parasMap.put("rankType", rankType);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);

        List<Map<String, Object>> resultList = null;

        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999) {
            //省级
            itemCounts = iPINResultMapper.queryPINResultTotal(parasMap);
            resultList = iPINResultMapper.queryPINResultByPage(parasMap);
        } else {
            //非省级
            itemCounts = iPINResultMapper.queryPINRankAreaTotal(parasMap);
            resultList = iPINResultMapper.queryPINRankAreaByPage(parasMap);
        }

        return new Table(resultList, itemCounts);
    }

    /**
     * 低销波次统计结果分页展示2
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPINResultByPage2.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryPINResultByPage2(@RequestBody Map<String, Object> paras, HttpSession session) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //产品名称
        String rankname = SearchConditionUtil.optimizeCondition((String) (paras.get("rankname"))).trim();
        //地区编码
        String area = (String) (paras.get("area"));
        //波次号
        String batchno = (String)paras.get("batchno");
        //日期编码
        String monthcode = (String) (paras.get("monthcode"));
        //档位类型
        String rankType = (String)paras.get("rankType");

        Integer itemCounts = 0;

        parasMap.put("rankname", rankname);
        parasMap.put("area", area);
        parasMap.put("batchno", batchno);
        parasMap.put("monthcode", monthcode);
        parasMap.put("rankType", rankType);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);

        List<Map<String, Object>> resultList = null;

        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999) {
            //省级
            itemCounts = iPINResultMapper.queryPINResultTotal(parasMap);
            resultList = iPINResultMapper.queryPINResultByPage(parasMap);
        } else {
            //非省级
            itemCounts = iPINResultMapper.queryPINRankAreaTotal(parasMap);
            resultList = iPINResultMapper.queryPINRankAreaByPage(parasMap);
        }

        return new Table(resultList, itemCounts);
    }


    /**
     * 低销波次统计结果分页展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPINResultMatchTotal.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryPINResultMatchTotal(@RequestBody Map<String, Object> paras, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> parasMap = new HashMap<String, Object>();
        //产品名称
        String rankname = SearchConditionUtil.optimizeCondition((String) (paras.get("rankname"))).trim();
        //地区编码
        String area = (String) (paras.get("area"));
        //波次号
        String batchno = (String)paras.get("batchno");
        //日期编码
        String monthcode = (String) (paras.get("monthcode"));
        //档位类型
        String rankType = (String) (paras.get("rankType"));

        Integer matchCounts = 0;

        parasMap.put("rankname", rankname);
        parasMap.put("area", area);
        parasMap.put("batchno", batchno);
        parasMap.put("monthcode", monthcode);
        parasMap.put("rankType", rankType);

        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (1 == userDomain.getUserType()) {
            //省级
            matchCounts = iPINResultMapper.queryPINResultMatchTotal(parasMap);
        } else if (2 == userDomain.getUserType()) {
            //市级
            matchCounts = iPINResultMapper.queryPINRankAreaMatchTotal(parasMap);
        }
        result.put("matchcount", matchCounts);

        return result;
    }

    /**
     * 低销批次号
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryLatesBatchByMonth.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> queryLatesBatchByMonth(@RequestBody Map<String, Object> paras) {
        List<Map<String, Object>> result = iPINResultMapper.queryLatesBatchByMonth(paras);
        return result;
    }

    /**
     * 查询分配情况
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryAllocated.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryAllocated(@RequestBody Map<String, Object> paras, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> parasMap = new HashMap<String, Object>();
        //地区编码
        String area = (String) (paras.get("area"));
        //产品类型
        String ranktype = (String) (paras.get("ranktype"));
        //波次号
        String batchno = (String) (paras.get("batchno"));
        parasMap.put("area", area);
        parasMap.put("ranktype", ranktype);
        parasMap.put("batchno", batchno);

        List<Map<String, Object>> listAllocatedProd = iPINResultMapper.queryAllocatedPINPro(parasMap);
        result.put("allocated", listAllocatedProd);
        List<Map<String, Object>> listUnallocatedProd = iPINResultMapper.queryUnallocatedPINPro(parasMap);
        result.put("unallocated", listUnallocatedProd);
        return result;
    }

    /**
     * 档位分配
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "allocatedRank.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult allocatedRank(@RequestBody Map<String, Object> paras, HttpSession session) {
        ServiceResult result = new ServiceResult();
        String addedstr = (String) (paras.get("addedlist"));
        String deletedstr = (String) (paras.get("deletedlist"));

        //如果没有分配，直接返回
        if ((addedstr == null || "".equals(addedstr)) && (deletedstr == null && "".equals(deletedstr))) {
            result.setDesc("没有分配，请确认");
            return result;
        }

        //新增分配
        if (addedstr != null && !"".equals(addedstr)) {
            iPINResultMapper.insertPINRankArea(paras);
        }

        //删除已分配
        if (deletedstr != null && !"".equals(deletedstr)) {
            iPINResultMapper.deletePINRankAreaBylist(paras);
        }

        return result;
    }

    /**
     * 用户低销信息查询
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPINDetailByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<PINfileDomain> queryPINDetailByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //低销档位ID
        String rankid = paras.get("rankid") == null? "" : (String)paras.get("rankid");
        //地区编码
        String area = paras.get("area") == null? "" : (String)paras.get("area");
        //手机号
        String phone = paras.get("phone") == null? "" : (String)paras.get("phone");
        String encryptphone = axonEncrypt.encrypt(phone);

        parasMap.put("rankid", rankid);
        parasMap.put("phone", encryptphone);
        parasMap.put("area", area);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);

        String totalsql = totalDetailSql(parasMap);
        String detailsql = detailSql(parasMap);
        List<PINfileDomain> resultList = new ArrayList<PINfileDomain>();
        Integer itemCounts = greenPlumOperateService.queryRecordCount(totalsql);

        greenPlumOperateService.query(detailsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                PINfileDomain fileDomain = new PINfileDomain();
                fileDomain.setSaleid(resultSet.getString("sale_id"));
                fileDomain.setBoid(resultSet.getString("sale_boid_id"));
                fileDomain.setAimsubid(resultSet.getString("aim_sub_id"));
                fileDomain.setUserid(resultSet.getString("user_id"));
                fileDomain.setCustid(resultSet.getString("cust_id"));
                fileDomain.setPhone(axonEncrypt.decryptWithoutCountrycode(resultSet.getString("phone")));
                fileDomain.setName(resultSet.getString("name"));
                fileDomain.setSex(resultSet.getString("sex"));
                fileDomain.setArea(resultSet.getString("area_code"));
                fileDomain.setSysname(resultSet.getString("sys_name"));
                fileDomain.setMainProductName(resultSet.getString("main_prod_name"));
                fileDomain.setMainProductCode(resultSet.getString("main_prod_code"));
                fileDomain.setNettime(resultSet.getString("net_time"));
                fileDomain.setPinProductId(resultSet.getString("pin_prod_id"));
                fileDomain.setPinChargeId(resultSet.getString("pin_charge_id"));
                fileDomain.setPinLevel(resultSet.getString("pin_level"));
                fileDomain.setLllARPU(resultSet.getString("lll_arpu"));
                fileDomain.setLlARPU(resultSet.getString("ll_arpu"));
                fileDomain.setlARPU(resultSet.getString("l_arpu"));
                fileDomain.setaARPU(resultSet.getString("a_arpu"));
                fileDomain.setaVolume(resultSet.getString("a_volume"));
                fileDomain.setaVoice(resultSet.getString("a_voice"));
                fileDomain.setIsRoam(resultSet.getString("is_roam"));
                fileDomain.setTerminal(resultSet.getString("terminal"));

                resultList.add(fileDomain);
            }

        }, 0);

        return new Table(resultList, itemCounts);
    }

    /**
     * 炒店任务配置 导出excel
     *
     * @param paras
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "exportDixiaonoused.view", method = RequestMethod.POST)
    public void exportDixiaonoused(@RequestParam  Map<String, Object> paras, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("rankid", paras.get("rankid") == null? "" : paras.get("rankid"));
        parasMap.put("area", paras.get("area") == null ? "" : paras.get("area"));
        parasMap.put("boid", paras.get("boid") == null ? "" : paras.get("boid"));
        parasMap.put("sysname", paras.get("sysname") == null ? "" : paras.get("sysname"));
        parasMap.put("ranktype", paras.get("ranktype") == null ? "" : paras.get("ranktype"));

        //拼接sql
        String downloadsql = downloadSql(parasMap);
        List<PINfileDomain> fileList = new ArrayList<PINfileDomain>();
        greenPlumOperateService.query(downloadsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                PINfileDomain fileDomain = new PINfileDomain();
                fileDomain.setSaleid(resultSet.getString("sale_id"));
                fileDomain.setBoid(resultSet.getString("sale_boid_id"));
                fileDomain.setAimsubid(resultSet.getString("aim_sub_id"));
                fileDomain.setUserid(resultSet.getString("user_id"));
                fileDomain.setCustid(resultSet.getString("cust_id"));
                fileDomain.setPhone(axonEncrypt.decryptWithoutCountrycode(resultSet.getString("phone")));
                fileDomain.setName(resultSet.getString("name"));
                fileDomain.setSex(resultSet.getString("sex"));
                fileDomain.setArea(resultSet.getString("area_code"));
                fileDomain.setSysname(resultSet.getString("sys_name"));
                fileDomain.setMainProductName(resultSet.getString("main_prod_name"));
                fileDomain.setMainProductCode(resultSet.getString("main_prod_code"));
                fileDomain.setNettime(resultSet.getString("net_time"));
                fileDomain.setPinProductId(resultSet.getString("pin_prod_id"));
                fileDomain.setPinChargeId(resultSet.getString("pin_charge_id"));
                fileDomain.setPinLevel(resultSet.getString("pin_level"));
                fileDomain.setLllARPU(resultSet.getString("lll_arpu"));
                fileDomain.setLlARPU(resultSet.getString("ll_arpu"));
                fileDomain.setlARPU(resultSet.getString("l_arpu"));
                fileDomain.setaARPU(resultSet.getString("a_arpu"));
                fileDomain.setaVolume(resultSet.getString("a_volume"));
                fileDomain.setaVoice(resultSet.getString("a_voice"));
                fileDomain.setIsRoam(resultSet.getString("is_roam"));
                fileDomain.setTerminal(resultSet.getString("terminal"));
                fileDomain.setRankType(resultSet.getString("level_type"));
                fileList.add(fileDomain);
            }
        }, 0);

        String fileName = "低销用户分配" + "-" + parasMap.get("sysname") + "-" + areaMap.get(""+parasMap.get("area")) + "-" + parasMap.get("ranktype") + "-" + parasMap.get("rankid") + "-" + TimeUtil.formatDateToYMDHMS(new Date());
        List<ExcelRowEntity> excelDataList = getdixiaoExcelData(fileList);
        ExportUtils.getInstance().exportData(fileName, excelDataList, request, response);
    }

    public String totalDetailSql(Map<String, Object> parasMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from model.pin_file ");

        sql.append("where 1=1 ");
        if (!"".equals(String.valueOf(parasMap.get("rankid")))) {
            sql.append("and pin_level = '").append(String.valueOf(parasMap.get("rankid"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("phone")))) {
            sql.append("and phone = '").append(String.valueOf(parasMap.get("phone"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("area"))) && !"99999".equals(String.valueOf(parasMap.get("area")))) {
            sql.append("and area_code = '").append(String.valueOf(parasMap.get("area"))).append("' ");
        }

        return sql.toString();
    }

    public String detailSql(Map<String, Object> parasMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("select sale_id, sale_boid_id, aim_sub_id, user_id, cust_id, phone, name,sex, area_code, sys_name, main_prod_name, main_prod_code, net_time, pin_prod_id, pin_charge_id, pin_level, lll_arpu, ll_arpu, l_arpu, a_arpu, a_volume, a_voice, is_roam, terminal from model.pin_file ");

        sql.append("where 1=1 ");
        if (!"".equals(String.valueOf(parasMap.get("rankid")))) {
            sql.append("and pin_level = '").append(String.valueOf(parasMap.get("rankid"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("phone")))) {
            sql.append("and phone = '").append(String.valueOf(parasMap.get("phone"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("area"))) && !"99999".equals(String.valueOf(parasMap.get("area")))) {
            sql.append("and area_code = '").append(String.valueOf(parasMap.get("area"))).append("' ");
        }

        sql.append("limit ").append(String.valueOf(parasMap.get("limit"))).append(" offset ").append(String.valueOf(parasMap.get("offset")));
        return sql.toString();
    }

    public String downloadSql(Map<String, Object> parasMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("select sale_id, sale_boid_id, aim_sub_id, user_id, cust_id, phone, name,sex, area_code, sys_name, main_prod_name, main_prod_code, net_time, pin_prod_id, pin_charge_id, pin_level, lll_arpu, ll_arpu, l_arpu, a_arpu, a_volume, a_voice, is_roam, terminal,level_type from model.pin_file ");
        sql.append("where 1=1 ");
        if (!"".equals(String.valueOf(parasMap.get("rankid")))) {
            sql.append("and pin_level = '").append(String.valueOf(parasMap.get("rankid"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("boid")))) {
            sql.append("and sale_boid_id = '").append(String.valueOf(parasMap.get("boid"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("area"))) && !"99999".equals(String.valueOf(parasMap.get("area")))) {
            sql.append("and area_code = '").append(String.valueOf(parasMap.get("area"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("sysname")))) {
            sql.append("and sys_name = '").append(String.valueOf(parasMap.get("sysname"))).append("' ");
        }
        if (!"".equals(String.valueOf(parasMap.get("ranktype")))) {
            sql.append("and level_type = '").append(String.valueOf(parasMap.get("ranktype"))).append("' ");
        }

        return sql.toString();
    }

    private List<ExcelRowEntity> getdixiaoExcelData(List<PINfileDomain> dataList)
    {
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();

        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "活动编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "波次编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "目标客户群编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "用户编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "客户编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "手机号"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "姓名"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "性别"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "区号"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "系统名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "主产品名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "主产品代码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "入网时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "低销产品id"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "低销资费id"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "低销档位"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上上上月ARPU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上上月ARPU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上月ARPU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月ARPU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月消耗总流量"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月消耗语音"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "是否长市漫"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "终端"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "档位类型"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                PINfileDomain fileDomain = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();

                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getSaleid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getBoid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getAimsubid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getUserid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getCustid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, axonEncrypt.decryptWithoutCountrycode(fileDomain.getPhone())));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getName()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getSex()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getArea()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getSysname()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getMainProductName()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getMainProductCode()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getNettime()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getPinProductId()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getPinChargeId()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getPinLevel()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getLllARPU()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getLlARPU()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getlARPU()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getaARPU()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getaVolume()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getaVoice()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getIsRoam()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getTerminal()));
                cellEntityList.add(new ExcelCellEntity(1, 1, fileDomain.getRankType()));

                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                result.add(excelRow);
            }
        }

        return result;
    }
}
