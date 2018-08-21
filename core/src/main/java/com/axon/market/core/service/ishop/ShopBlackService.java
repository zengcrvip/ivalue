package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.iscene.MarketAreaDomain;
import com.axon.market.common.domain.ishop.ShopBlackDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.dao.mapper.ishop.IShopBlackMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * 炒店黑名单服务类
 * Created by zengcr on 2017/4/12.
 */
@Service("shopBlackService")
public class ShopBlackService
{
    private static final Logger LOG = Logger.getLogger(ShopBlackService.class.getName());

    @Autowired
    @Qualifier("shopBlackDAO")
    private IShopBlackMapper iShopBlackMapper;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

    public static ShopBlackService getInstance()
    {
        return (ShopBlackService) SpringUtil.getSingletonBean("shopBlackService");
    }

    private AxonEncryptUtil axonEncryptUtil = AxonEncryptUtil.getInstance();
    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private static final String columnSeparator = "|";
    private byte[] lineByte = {0x0A};
    private String lineSeparator = new String(lineByte);

    /**
     * 获取炒店黑名单总数
     *
     * @param paras phone 号码
     * @return 总数
     */
    public int queryShopBlackPhoneTotal(Map<String, Object> paras)
    {
        return iShopBlackMapper.queryShopBlackPhoneTotal(paras);
    }

    /**
     * 分页查询炒店黑名单
     *
     * @param paras offset 起始标记位,limit 限制大小,phone 号码
     * @return
     */
    public List<ShopBlackDomain> queryShopBlackPhoneByPage(Map<String, Object> paras)
    {
        return iShopBlackMapper.queryShopBlackPhoneByPage(paras);
    }

    /**
     * 查询场景位置号段地区树结构
     *
     * @param areaIds 已选中的地区ID
     * @return
     */
    public String queryShopBlackBaseAreas(String areaIds, String businessCodes)
    {
        try
        {
            List<MarketAreaDomain> marketAreas = iShopBlackMapper.queryShopBlackBaseAreas(areaIds, businessCodes);
            return JsonUtil.objectToString(marketAreas);
        }
        catch (JsonProcessingException e)
        {
        }
        return null;
    }

    /**
     * @param paras
     * @return
     */
    public List<Map<String, String>> queryShopBlockBases(Map<String, Object> paras)
    {
        return iShopBlackMapper.queryShopBlockBases(paras);
    }

    /**
     * 新增炒店用户黑名单
     *
     * @param shopBlackDomain
     * @return
     */
    public Operation createShopUserBlackList(ShopBlackDomain shopBlackDomain)
    {
        Boolean result = true;
        Integer resultCount = 0;
        try
        {
            if (iShopBlackMapper.queryShopUserBlackListByPhone(shopBlackDomain.getPhone()) > 0)
            {
                return new Operation(false, "不能重复添加相同的手机号码");
            }

            if (!"".equals(shopBlackDomain.getHideArea()))    //省级、地市级管理员
            {
                result = iShopBlackMapper.createShopUserBlackList(shopBlackDomain) == 1;
                String message = result ? "新增成功" : "新增失败";
                return new Operation(result, message);
            }
            else    // 营业厅管理员
            {
                String[] hideBasesArray = shopBlackDomain.getHideBases().split(",");
                for (String itemHideBase : hideBasesArray)
                {
                    resultCount += insertShopUserBlackList(shopBlackDomain.getPhone(), shopBlackDomain.getHideStartTime(), shopBlackDomain.getHideEndTime(), shopBlackDomain.getHideArea(), itemHideBase, shopBlackDomain.getCreateUser());
                }
                result = resultCount > 0;
                String message = result ? "新增成功" : "新增失败";
                return new Operation(result, message);
            }
        }
        catch (Exception e)
        {
            LOG.error("createShopUserBlackList error." + e);
            return new Operation();
        }
    }

    /**
     * 批量导入 炒店用户黑名单
     *
     * @param paras
     * @return
     */
    public Operation createMoreShopUserBlackList(Map<String, Object> paras)
    {
        String dbFileId = String.valueOf(paras.get("dbFileId"));
        String hideStartTime = String.valueOf(paras.get("hideStartTime"));
        String hideEndTime = String.valueOf(paras.get("hideEndTime"));
        String hideBases = String.valueOf(paras.get("hideBases"));
        String hideArea = String.valueOf(paras.get("hideArea"));
        Integer createUser = Integer.valueOf(String.valueOf(paras.get("createUser")));
        Integer resultCount = 0;

        List<Map<String, Object>> phoneList = iShopBlackMapper.queryShopUserBlackListPhoneByFileId(dbFileId);
        for (Map<String, Object> item : phoneList)
        {
            if (!"".equals(hideArea))    //省级、地市级管理员
            {
                resultCount += insertShopUserBlackList(String.valueOf(item.get("phone")), hideStartTime, hideEndTime, hideArea, hideBases, createUser);
            }
            else    // 营业厅管理员
            {
                String[] hideBasesArray = hideBases.split(",");
                for (String itemHideBase : hideBasesArray)
                {
                    resultCount += insertShopUserBlackList(String.valueOf(item.get("phone")), hideStartTime, hideEndTime, hideArea, itemHideBase, createUser);
                }
            }
        }

        Boolean result = resultCount > 0;
        String message = result ? "新增成功" : "新增失败";
        return new Operation(result, message);
    }

    /**
     * 批量新增黑名单用户
     * @param shopBlackDomainList
     * @return
     */
    @Transactional
    public void batchHandleShopBlackUser(List<ShopBlackDomain> shopBlackDomainList, List<String> blackUserPhoneList)
    {
        //删除已经存在的号码
        iShopBlackMapper.deleteShopBlackUserBatch(StringUtils.join(blackUserPhoneList, ","));

        //增加需要插入的号码
        iShopBlackMapper.insertShopBlackUserBatch(shopBlackDomainList);
    }

    /**
     * 删除炒店用户黑名单
     *
     * @param paras
     * @return
     */
    public Operation deleteShopUserBlackList(Map<String, Object> paras)
    {
        String phone = String.valueOf(paras.get("phone"));
        if (phone == null || "".equals(phone))
        {
            return new Operation(false, "未找到该数据，请稍后重试");
        }
        try
        {
            Boolean result = iShopBlackMapper.deleteShopUserBlackList(paras) >= 1;
            String msg = result ? "删除成功" : "删除失败";
            return new Operation(result, msg);
        }
        catch (Exception e)
        {
            LOG.error("deleteShopUserBlackList error:" + e);
            return new Operation();
        }
    }

    /**
     * 删除炒店用户黑名单
     *
     * @param paras
     * @return
     */
    public Operation deleteAllShopUserBlackList(Map<String, Object> paras)
    {
        try
        {
            Boolean result = iShopBlackMapper.deleteAllShopUserBlackList(paras) >= 1;
            String msg = result ? "删除成功" : "删除失败";
            return new Operation(result, msg);
        }
        catch (Exception e)
        {
            LOG.error("deleteAllShopUserBlackList error:" + e);
            return new Operation();
        }
    }

    /**
     * 新增炒店用户黑名单公共方法
     *
     * @param phone      手机号
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param area       地区
     * @param base       营业厅编码
     * @param createUser 创建人
     * @return
     */
    private Integer insertShopUserBlackList(String phone, String startTime, String endTime, String area, String base, Integer createUser)
    {
        try
        {
            ShopBlackDomain shopBlackDomain = new ShopBlackDomain();
            shopBlackDomain.setPhone(phone);
            shopBlackDomain.setHideStartTime(startTime);
            shopBlackDomain.setHideEndTime(endTime);
            shopBlackDomain.setHideArea(area);
            shopBlackDomain.setHideBases(base);
            shopBlackDomain.setCreateUser(createUser);
            return iShopBlackMapper.createShopUserBlackList(shopBlackDomain);
        }
        catch (Exception e)
        {
            LOG.error("insertShopUserBlackList error." + e);
            return 0;
        }
    }

    /**
     * 刷新黑名单进GP
     *
     * @return
     */
    public ServiceResult flushShopBlack()
    {
        //1、查询炒店黑名单号码
        Map<String, Object> paras = new HashMap<String, Object>();
        List<ShopBlackDomain> blackList = iShopBlackMapper.queryShopBlackPhone(paras);
        //2、生成文件
        List<String> lineList = new ArrayList<String>();
        for (ShopBlackDomain shopBlackDomain : blackList)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(axonEncryptUtil.encrypt(String.valueOf(shopBlackDomain.getPhone()))).append(columnSeparator)
                    .append(shopBlackDomain.getHideStartTime()).append(columnSeparator)
                    .append(shopBlackDomain.getHideEndTime()).append(columnSeparator)
                    .append(shopBlackDomain.getHideArea()).append(columnSeparator)
                    .append(shopBlackDomain.getHideBases()).append(columnSeparator)
                    .append(shopBlackDomain.getCreateUser());
            lineList.add(buffer.toString());
        }
        if (CollectionUtils.isEmpty(lineList))
        {
            lineList.add("");
        }
        //3、删除原始数据
        greenPlumOperateService.update("truncate shop.shop_black_user");
        //4、入库GP
        ServiceResult result = upLoadGPTable(lineList);
        return result;
    }

    /**
     * 生成文件入库GP表
     *
     * @param lineList
     */
    private ServiceResult upLoadGPTable(List<String> lineList)
    {
        LOG.info("start upLoad shopBlack to GP Table");
        ServiceResult result = new ServiceResult();
        File directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
        if (!directory.exists())
        {
            directory.mkdir();
        }
        String targetFileName = directory.getPath() + File.separator + "@shop.shop_black_user";
        File file = null;
        LineIterator iterator = null;
        try
        {
            file = new File(targetFileName);
            FileUtils.writeStringToFile(file, StringUtils.join(lineList, lineSeparator), "GBK");
            File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
            iterator = FileUtils.lineIterator(file);
            operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, null);
        }
        catch (Exception e)
        {
            LOG.error("upLoad shopBlack File to GP Error. ", e);
            result.setRetValue(-1);
            result.setDesc("黑名单启用失败，请联系客服处理！");
        }
        finally
        {
            try
            {
                if (file != null)
                {
                    FileUtils.deleteQuietly(file);
                }
                if (directory != null)
                {
                    FileUtils.deleteDirectory(directory);
                }
            }
            catch (Exception e)
            {
                LOG.error("Delete Directory Error. ", e);
            }
        }
        return result;
    }
}
