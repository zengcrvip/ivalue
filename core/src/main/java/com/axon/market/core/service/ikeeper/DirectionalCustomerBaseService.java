package com.axon.market.core.service.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ikeeper.KeeperMaintainUserDomain;
import com.axon.market.common.domain.ikeeper.UserMaintainDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.excel.ExcelReader;
import com.axon.market.core.service.iflow.OldCustomerService;
import com.axon.market.dao.mapper.ikeeper.IDirectionalCustomerMapper;
import com.sun.xml.bind.v2.TODO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangtt on 2017/8/10.
 */
@Service("directionalCustomerBaseService")
public class DirectionalCustomerBaseService
{
    private static Logger LOG = Logger.getLogger(DirectionalCustomerBaseService.class.getName());

    AxonEncryptUtil axonEncryptUtil = AxonEncryptUtil.getInstance();

    @Autowired
    @Qualifier("directionalCustomerDao")
    IDirectionalCustomerMapper directionalCustomerDao;


    /**
     * 查询用户维系关系数据
     *
     * @param paramMap
     * @return
     */
    public Table<Map<String, Object>> queryDirectonalCustomerBase(Map<String, Object> paramMap)
    {
        try
        {
            int count = directionalCustomerDao.queryDirectonalCustomerBaseCount(paramMap);
            List<Map<String, Object>> dataList = directionalCustomerDao.queryDirectonalCustomerBase(paramMap);
            List<Map<String, Object>> newDataList = new ArrayList<Map<String, Object>>();
            // 循环解码手机号
            for (Map<String, Object> map : dataList)
            {
                String decryptPhone = axonEncryptUtil.decrypt(String.valueOf(map.get("userPhone")));
                map.put("userPhone", decryptPhone.substring(2));
                newDataList.add(map);
            }
            return new Table<>(dataList, count);
        }
        catch (Exception e)
        {
            LOG.error("queryDirectonalCustomerBase error", e);
            return new Table<Map<String, Object>>();
        }
    }

    /**
     * 查询地市用户是否有管理权
     *
     * @param userId
     * @return
     */
    public boolean queryUserManageJurisdiction(int userId)
    {
        boolean isTrue = false;
        Integer i = directionalCustomerDao.queryUserManageJurisdiction(userId);
        if (i == null || i == 0)
        {
            return false;
        }
        else if (i == 1)
        {
            isTrue = true;
        }
        return isTrue;
    }


    /**
     * 根据用户手机号检查用户是否已经存在
     *
     * @param userEncryptPhone
     * @return
     */
    @Transactional
    public int checkUserIsExist(String userEncryptPhone, Integer updateUserId)
    {
        int result = 3;// 1:有重复并且已经失效 2:有重复但失效失败 3:无重复 4:操作异常
        try
        {
            Integer userId = directionalCustomerDao.checkUserIsExist(userEncryptPhone);
            if (userId != null)
            {
                int i = directionalCustomerDao.delExistUser(userId, updateUserId, "重复覆盖");
                result = i > 0 ? 1 : 2;
            }
        }
        catch (Exception e)
        {
            LOG.error("checkUserIsExist error", e);
            result = 4;
            return result;
        }
        return result;
    }


    /**
     * 创建用户维系关系
     *
     * @param paramMap
     * @return
     */
    public int createUserMaintain(Map<String, Object> paramMap)
    {
        int i = directionalCustomerDao.createUserMaintain(paramMap);
        return i;
    }

    /**
     * 查询用户维系关系明细
     *
     * @param userId
     * @return
     */
    public UserMaintainDomain queryUserMaintainDetail(Integer userId)
    {
        UserMaintainDomain domain = directionalCustomerDao.queryUserMaintainDetail(userId);
        if (null != domain)
        {
            return domain;
        }
        else
        {
            return null;
        }
    }

    /**
     * 更新用户维系关系
     *
     * @param paraMap
     * @return
     */
    public ServiceResult updateUserMaintain(Map<String, Object> paraMap)
    {
        int i;
        try
        {
            i = directionalCustomerDao.updateUserMaintain(paraMap);
        }
        catch (Exception e)
        {
            LOG.error("updateUserMaintain error", e);
            return new ServiceResult(-1, "更新用户维系关系异常");
        }
        return i > 0 ? new ServiceResult() : new ServiceResult(-1, "更新失败");
    }

    /**
     * 删除用户维系关系
     *
     * @param userId
     * @return
     */
    public ServiceResult deleteUserMaintain(String userId)
    {
        int i;
        try
        {
            i = directionalCustomerDao.deleteUserMaintain(userId);
        }
        catch (Exception e)
        {
            LOG.error("deleteUserMaintain error", e);
            return new ServiceResult(-1, "删除用户维系关系异常");
        }
        return i > 0 ? new ServiceResult() : new ServiceResult(-1, "删除失败");
    }

    /**
     * 获取当前登录用户的手机号和掌柜信息
     *
     * @param loginUserId
     * @return
     */
    public Map<String, Object> queryCurrentKeeperUser(Integer loginUserId)
    {
        Map<String, Object> map = directionalCustomerDao.queryCurrentKeeperUser(loginUserId);
        return map;
    }

    /**
     * @param maintainUserId
     * @return
     */
    public List<Map<String, String>> queryUserCustPhoneNumberList(Integer maintainUserId)
    {
        return directionalCustomerDao.queryUserCustPhoneNumberList(maintainUserId);
    }


    /**
     * 文件保存到临时库里面
     *
     * @return
     */
    @Transactional
    public ServiceResult importTempleFile(Map<String, Object> fileInfo, InputStream is, boolean isCanManage, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        String fileId = String.valueOf(fileInfo.get("fileId"));

        List<String[]> fileDate = new ArrayList<String[]>();
        try
        {
            fileDate = readXlsFile(is, 2);
            if (fileDate.size() > 5000)
            {
                return new ServiceResult(-1, "您导入的文件过大");
            }
            int rowNo = 0;
            int successRowNo = 0;
            int failureRowNo = 0;
            if (fileDate.size() == 0)
            {
                return new ServiceResult(-1, "数据行数为0");
            }
            else
            {
                for (String[] row : fileDate)
                {
                    // row[0]   用户姓名（必填）
                    // row[1]   用户手机（必填）
                    // row[2]   用户编码（必填）
                    // row[3]   用户地区编码（必填）
                    // row[4]   维系员工姓名
                    // row[5]   维系员工手机号（必填）
                    // row[6]   用户微信
                    // row[7]   用户QQ
                    // row[8]   用户旺旺
                    // row[9]   用户微博
                    if (rowNo > 5000)
                    {
                        break;
                    }
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    rowMap.put("fileId", fileId);
                    rowMap.put("rowNo", rowNo++);
                    rowMap.put("status", "success");
                    rowMap.put("result", "新增成功");
                    StringBuffer stringBuffer = new StringBuffer("");
                    // 总的校验一下格式
                    if (!checkRowFormat(row))
                    {
                        rowMap.put("status", "error");
//                        rowMap.put("result", "格式错误");
                        stringBuffer.append("格式错误");
                    }
                    else
                    {
                        row[1] = axonEncryptUtil.encrypt(row[1]);
                        // 校验维系员工
                        // （1）判断上传人是否有管理权，若只是地市末梢人员则只能导入自己维系的用户
                        if (isCanManage)
                        {
                            // 有管理权
                            // 校验上传的维系人员号码是否在掌柜维系员工中
                            List<String> allMaintainUser = directionalCustomerDao.queryAllMaintainUser();
                            if (!allMaintainUser.contains(String.valueOf(row[5])))
                            {
                                // 不在范围内
                                rowMap.put("status", "error");
                                stringBuffer.append("不在维系员工范围内");
//                                rowMap.put("result", "不在维系员工范围内");
                            }
                            // 校验导入的地区是否是当前管理员的地区，若是省级管理员则不限制
                            if (userDomain.getAreaCode() != 99999 && !String.valueOf(row[3]).equals(String.valueOf(userDomain.getAreaCode())))
                            {
                                // 地市管理员导入其他地区的数据
                                rowMap.put("status", "error");
                                stringBuffer.append(",地市编码不符");
//                                rowMap.put("result", "地市编码不符");
                            }
                        }
                        else
                        {
                            // 无管理权，只是末梢人员
                            String maintainPhone = axonEncryptUtil.decrypt(String.valueOf(userDomain.getTelephone())).substring(2);
                            if (!maintainPhone.equals(String.valueOf(row[5])))
                            {
                                rowMap.put("status", "error");
                                stringBuffer.append("末梢员工错误导入其他维系员工");
//                                rowMap.put("result", "末梢员工错误导入其他维系员工");
                            }
                            if(!String.valueOf(row[3]).equals(String.valueOf(userDomain.getAreaCode()))){
                                rowMap.put("status", "error");
                                stringBuffer.append(",地市编码不符");
//                                rowMap.put("result", "地市编码不符");
                            }
                        }
                    }
                    if ("error".equals(rowMap.get("status")))
                    {
                        // 错误数据入表
                        String dataStr = StringUtils.join(row, ",");
                        rowMap.put("rowData", dataStr);
                        rowMap.put("result",stringBuffer.toString());
                        directionalCustomerDao.importFailedDirectionCustomer(rowMap);
                        failureRowNo++;
                    }
                    else
                    {
                        // 组装数据
                        KeeperMaintainUserDomain domain = new KeeperMaintainUserDomain(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8],row[9]);
                        rowMap.put("rowData", domain);
                        directionalCustomerDao.importDirectionCustomerToTemple(rowMap);
                        successRowNo++;
                    }
                }
                fileInfo.put("status", "success");
                fileInfo.put("result", "存储成功");
                directionalCustomerDao.insertFileMessage(fileInfo);
                return new ServiceResult(0, "存储成功，共存入" + rowNo + "条,其中成功" + successRowNo + "条，失败" + failureRowNo + "条。");
            }
        }
        catch (Exception e)
        {
            LOG.error("importTempleFile error", e);
            fileInfo.put("status", "error");
            fileInfo.put("result", "存储失败");
            return new ServiceResult(-1, "导入异常");
        }

    }


    /**
     * 最终保存导入数据进目标表
     *
     * @param fileId
     * @return
     */

    public ServiceResult saveDirectionalCustomer(String fileId, Integer userId)
    {
        // 合进去之前先覆盖已经存在的数据
        // 查询出所有的已经重复的数据
        try
        {
            String ids = directionalCustomerDao.queryIsExist(fileId);
            if (!StringUtils.isEmpty(ids))
            {
                // 覆盖这些数据
                directionalCustomerDao.coverIsExistData(ids, userId);
            }
            // 最终导入数据进目标表
            directionalCustomerDao.saveDirectionCustomer(fileId, userId);
        }
        catch (Exception e)
        {
            LOG.error("saveDirectionalCustomer", e);
            return new ServiceResult(-1, "保存异常");
        }
        return new ServiceResult();
    }


    /**
     * 私有方法校验导入格式
     *
     * @param row
     * @return
     */
    private boolean checkRowFormat(String[] row)
    {
        if (row.length != 10 || checkIsEmpty(row) || !isNumber(row[2]) || !isPhoneFormat(StringUtils.trim(row[1])) || !isPhoneFormat(StringUtils.trim(row[5])))
        {
            return false;
        }
        return true;

    }

    /**
     * 私有方法判断字段是否为空
     * @param row
     * @return
     */
    private boolean checkIsEmpty(String[] row)
    {
        for(String str:row){
            if(StringUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 私有方法匹配是否是数字
     * @param param
     * @return
     */
    private boolean isNumber(String param){
        boolean isNumber = false;
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(param);
        boolean b = m.matches();
        if (b)
        {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * 私有方法校验手机格式
     *
     * @param param
     * @return
     */
    private static boolean isPhoneFormat(String param)
    {
        boolean isPhone = false;
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(param);
        boolean b = m.matches();
        if (b)
        {
            isPhone = true;
        }
        return isPhone;
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

}
