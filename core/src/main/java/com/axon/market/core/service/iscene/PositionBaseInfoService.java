package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.util.excel.ExcelReader;
import com.axon.market.dao.mapper.iscene.IPositionBaseMapper;
import com.axon.market.dao.mapper.ishop.IShopTaskMapper;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置场景位置点选择 service
 * Created by zengcr on 2016/12/6.
 */
@Component("positionBaseService")
public class PositionBaseInfoService
{
    @Qualifier("positionBaseDao")
    @Autowired
    private IPositionBaseMapper iPositionBaseMapper;

    @Qualifier("shopTaskDao")
    @Autowired
    private IShopTaskMapper iShopTaskMapper;

    //合并数据分隔符
    private final String SEPARATOR = ",";
    //基站类型IDs
    private final String BASETYPES = "2,3,4,5,6";
    //基站地市
    private final String AREAS = "南京,南通,宿迁,常州,徐州,扬州,无锡,泰州,淮安,盐城,苏州,连云港,镇江";
    //状态
    private final String STATUS = "0,1";

    /**
     * 获取位置基站站点总数
     *
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @param baseArea 基站站点所属城市
     * @return 总数
     */
    public int queryPositionBaseTotal(String baseId, String baseName, String baseArea)
    {
        return iPositionBaseMapper.queryPositionBaseTotal(baseId, baseName, baseArea);
    }

    /**
     * 获取位置基站站点列表
     *
     * @param offset   每次查询数量
     * @param limit    起始标记位
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @param baseArea 基站站点所属城市
     * @return 站点列表
     */
    public List<PositionBaseDomain> queryPositionBaseByPage(int offset, int limit, String baseId, String baseName, String baseArea)
    {
        return iPositionBaseMapper.queryPositionBaseByPage(offset, limit, baseId, baseName, baseArea);
    }

    /**
     * 新建或修改位置场景基站
     * 当站点ID存在时修改，不存在时新增
     *
     * @param positionBaseDomain 基站站点对象实体
     * @return
     */
    public ServiceResult createOrUpdatePositionBase(PositionBaseDomain positionBaseDomain)
    {
        ServiceResult result = new ServiceResult();
        //新增或修改
        int flag = 0;
        Integer id = positionBaseDomain.getBaseId();
        if (id == null)
        {
            flag = iPositionBaseMapper.createPositionBase(positionBaseDomain);
        }
        else
        {
            flag = iPositionBaseMapper.updatePositionBase(positionBaseDomain);
        }
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("位置场景基站数据库操作异常");
        }
        return result;
    }

    /**
     * 根据主键ID查询位置基站
     *
     * @param baseId 基站站点
     * @return
     */
    public PositionBaseDomain queryPositionBaseById(Integer baseId)
    {
        PositionBaseDomain positionBaseDomain = iPositionBaseMapper.queryPositionBaseById(baseId);
        return positionBaseDomain;
    }
    /**
     * 根据ID查询删除指定营业厅信息(对应任务ID，对应任务名称)
     * @param baseId 基站站点
     * @return
     */
    public List<Map<String,Object>>  delTaskPositionBaseById(Integer baseId)
    {
        return iPositionBaseMapper.delTaskPositionBase(baseId);
    }
    /**
     * 根据ID查询营业厅信息(对应管理员)
     * @param baseId 基站站点
     * @return
     */
    public Map<String,Object>  delUserPositionBaseById(Integer baseId)
    {
        return iPositionBaseMapper.delUserPositionBase(baseId);
    }


    /**
     * 根据ID删除位置场景
     * 根据ID备注删除原因，执行者，操作时间
     * @param input 删除原因
     * @param userId 执行者
     * @param baseId 基站站点
     * @return
     */
    @Transactional
    public void deletePositionBaseById(int baseId,String input,String userId )
    {
         iPositionBaseMapper.updPositionBase(input,userId,baseId);
         iPositionBaseMapper.deletePositionBaseById(baseId);
         iShopTaskMapper.deletePositionBaseTaskById(baseId);
    }

    /**
     * 查询基站详情
     *
     * @param baseId   站点ID
     * @param baseName 站点名称
     * @return
     */
    public List<Map<String, Object>> queryPositionBase(String baseId, String baseName, String baseArea, Integer createUserId, String baseIdArray, String cityCode,String buscoding)
    {
        return iPositionBaseMapper.queryPositionBase(baseId, baseName, baseArea, createUserId, baseIdArray, cityCode,buscoding);
    }

    /**
     * 文件保存
     *
     * @param fileInfo 源文件
     * @param is       输入流
     * @return
     */
    @Transactional
    public ServiceResult storeFile(Map<String, Object> fileInfo, InputStream is)
    {
        ServiceResult result = new ServiceResult();
        List<String[]> fileData = null;
        String fileId = "" + fileInfo.get("fileId");

        int rowNo = 0;
        try
        {
            fileData = readXlsFile(is, 2);
            if (fileData.size() == 0)
            {
                return new ServiceResult(-1, "数据行数为0");
            }
            else
            {
                for (String[] row : fileData)
                {
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    String rowData = joinRowdata(row);
                    rowMap.put("fileId", fileId);
                    rowMap.put("rowNo", rowNo++);
                    rowMap.put("rowData", rowData);
                    rowMap.put("status", "success");
                    rowMap.put("result", "导入完成");
                    if (row.length == 9)
                    {
                        //校验名称
                        if (row[0] == null || "".equals(row[0]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "基站名称不能为空");
                        }
                        //校验基站类型ID
                        if (row[1] == null || "".equals(row[1]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "基站类型ID不能为空");
                        }
                        else
                        {
                            if (BASETYPES.indexOf(row[1]) == -1)
                            {
                                rowMap.put("status", "error");
                                rowMap.put("result", "基站类型ID只能从列表中选择");
                            }
                        }
                        //校验基站地市
                        if (row[2] == null || "".equals(row[2]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "所属地市不能为空");
                        }
                        else
                        {
                            if (AREAS.indexOf(row[2]) == -1)
                            {
                                rowMap.put("status", "error");
                                rowMap.put("result", "所属地市只能从列表中选择");
                            }
                        }
                        //校验经度
                        if (row[4] == null || "".equals(row[4]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "经度不能为空");
                        }
                        //校验纬度
                        if (row[5] == null || "".equals(row[5]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "纬度不能为空");
                        }
                        //校验半径
                        if (row[6] == null || "".equals(row[6]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "半径不能为空");
                        }
                        //校验状态
                        if (row[8] == null || "".equals(row[8]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "状态不能为空");
                        }
                        else
                        {
                            if (STATUS.indexOf(row[8]) == -1)
                            {
                                rowMap.put("status", "error");
                                rowMap.put("result", "状态只能从列表中选择");
                            }
                        }
                    }
                    else
                    {
                        rowMap.put("status", "error");
                        rowMap.put("result", "不符合模板规定列数");
                    }
                    iPositionBaseMapper.insertRow(rowMap);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fileInfo.put("status", "error");
            fileInfo.put("result", "存储失败");
            return new ServiceResult(-1, e.getMessage());
        }
        fileInfo.put("status", "YBC");
        fileInfo.put("result", "已存储");
        iPositionBaseMapper.insertFile(fileInfo);
        return new ServiceResult(0, "存储文件成功, 总行数:" + rowNo);
    }

    /**
     * 私有方法
     * 读取EXCEL数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param is
     * @param startRow
     * @return
     * @throws Exception
     */
    private List<String[]> readXlsFile(InputStream is, int startRow) throws Exception
    {
        List<String[]> list = new ArrayList<String[]>();
        ExcelReader excel = new ExcelReader(is);
        int rowNum = excel.getRowNums() - startRow;
        int count = 0;
        for (int i = startRow; count < rowNum; i++)
        {
            String[] row = excel.readRow(i);
            if (row != null)
            {
                list.add(row);
                count++;
            }
        }
        return list;
    }


    /**
     * 连接行数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param rowdata
     * @return
     */
    private String joinRowdata(String[] rowdata)
    {
        StringBuffer buff = new StringBuffer();
        for (String item : rowdata)
        {
            if (item == null || "".equals(item))
            {
                buff.append(SEPARATOR).append("NULL");
            }
            else
            {
                String value = item.replace("'", "''");
                buff.append(SEPARATOR).append("'").append(value).append("'");
            }
        }
        if (rowdata.length > 0)
        {
            return buff.substring(SEPARATOR.length());
        }
        else
        {
            return null;
        }
    }

    /**
     * 查询位置场景基站导入数据总数
     *
     * @param fileId 文件ID
     * @return
     */
    public int queryPositionBaseImportTotal(Long fileId)
    {
        return iPositionBaseMapper.queryPositionBaseImportTotal(fileId);
    }

    /**
     * 查询位置场景基站分页导入数据展示列表
     *
     * @param offset 每次查询数量
     * @param limit  起始标记位
     * @param fileId 文件ＩＤ
     * @return
     */
    public List<Map<String, Object>> queryPositionBaseImport(int offset, int limit, Long fileId)
    {
        List<Map<String, Object>> list = iPositionBaseMapper.queryPositionBaseImport(offset, limit, fileId);
        for (Map<String, Object> row : list)
        {
            String rowData = (String) row.get("data");
            if (rowData != null)
            {
                String[] values = rowData.split(SEPARATOR);
                for (int i = 0; i < values.length; i++)
                {
                    if (!"NULL".equals(values[i]))
                    {
                        int length = values[i].length();
                        String value = values[i].substring(1, length - 1);
                        row.put("COL" + i, value);
                    }
                }
            }
        }
        return list;
    }

    /**
     * * @param paras 文件ＩＤ
     * 保存导入基站数据
     *
     * @return
     */
    public ServiceResult createPositionBaseImport(Map<String, Object> paras)
    {
        Long fileId = (Long) paras.get("fileId");
        ServiceResult result = new ServiceResult();
        List<Map<String, Object>> dataList = iPositionBaseMapper.queryPositionBaseImportAll(fileId);
        if (dataList.size() > 0)
        {
            for (Map<String, Object> row : dataList)
            {
                String rowData = (String) row.get("data");
                if (rowData != null)
                {
                    String[] values = rowData.split(SEPARATOR);
                    for (int i = 0; i < values.length; i++)
                    {
                        if (!"NULL".equals(values[i]))
                        {
                            int length = values[i].length();
                            String value = values[i].substring(1, length - 1);
                            row.put("COL" + i, value);
                            if (i == 1)
                            {
                                addBaseType(value, row);
                            }
                            if (i == 2)
                            {
                                row.put("areaCode", getBaseAreaCode().get(value));
                            }
                        }
                    }
                }
            }
        }
        int flag = iPositionBaseMapper.createPositionBaseImport(dataList);
        if (flag < 1)
        {
            result.setRetValue(-1);
            result.setDesc("位置场景基站数据库操作异常");
        }
        return result;
    }

    /**
     * 添加基站类型
     *
     * @param value 基站分类
     * @param row   目标容器
     */
    private void addBaseType(String value, Map<String, Object> row)
    {
        switch (value)
        {
            case "1":
            {
                row.put("baseType", "自营厅");
                break;
            }
            case "2":
            {
                row.put("baseType", "合作厅");
                break;
            }
            case "3":
            {
                row.put("baseType", "临促点");
                break;
            }
            case "4":
            {
                row.put("baseType", "第三方");
                break;
            }
            case "5":
            {
                row.put("baseType", "公共设施（机场，车站）");
                break;
            }
            case "6":
            {
                row.put("baseType", "其它");
                break;
            }
        }
    }

    /**
     * 获取地区编码
     *
     * @return
     */
    private Map<String, String> getBaseAreaCode()
    {
        Map<String, String> areaMap = new HashMap<String, String>();
        areaMap.put("南京", "25");
        areaMap.put("南通", "513");
        areaMap.put("宿迁", "527");
        areaMap.put("常州", "519");
        areaMap.put("徐州", "516");
        areaMap.put("扬州", "514");
        areaMap.put("无锡", "510");
        areaMap.put("泰州", "523");
        areaMap.put("淮安", "517");
        areaMap.put("盐城", "515");
        areaMap.put("苏州", "512");
        areaMap.put("连云港", "518");
        areaMap.put("镇江", "511");
        return areaMap;
    }
}
