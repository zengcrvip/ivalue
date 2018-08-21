package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.domain.ishop.BusinessHallPortraitDomain;
import com.axon.market.common.domain.ishop.ShopListDomain;
import com.axon.market.common.domain.ishop.ShopReviewDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.dao.mapper.ishop.IShopListMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

import com.axon.market.common.domain.isystem.UserDomain;

/**
 * 我的炒店service
 * Created by gloomysw on 2017/02/20.
 */
@Component("shopListService")
public class ShopListService
{
    @Autowired
    @Qualifier("shopListDao")
    private IShopListMapper iShopListMapper;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    public static ShopListService getInstance()
    {
        return (ShopListService) SpringUtil.getSingletonBean("shopListService");
    }

    private static Logger LOG = Logger.getLogger(ShopListService.class.getName());

    /**
     * 获取我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日11:02:16
     *
     * @return 我的炒店集合对象
     */
    public List<ShopListDomain> selectMyShopList()
    {
        return iShopListMapper.selectMyShopList();
    }

    /**
     * 新增我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:43:13
     *
     * @param model 我的炒店对象
     * @return 受影响行数
     */
    public long insertMyShopList(ShopListDomain model, Integer createUserId, String cityCode, String baseIdArray)
    {
        int number = iShopListMapper.insertMyShopList(model, createUserId);
        if (number > 0)
        {
            String businIdStr = MessageFormat.format("{0}", model.getBaseId());
            businIdStr = "," + businIdStr.replaceAll(",", "");
            iShopListMapper.updateUserBusinessHallIds(businIdStr, createUserId);

            UserDomain modelf = selectReviewUserPhoneByBaseId(createUserId, cityCode, String.valueOf(model.getBaseId()), baseIdArray);
            if (modelf != null)
            {
                //String message = MessageFormat.format("【智能营销】尊敬的{0}用户您好，炒店配置审批[{1}]待您审核，请登录江苏智能营销平台处理", modelf.getName(), model.getBaseName());
                String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(), modelf.getName(), model.getBaseName());
                sendSmsService.sendReminderNoticeSms(modelf.getTelephone(), message);
            }
            if (model.getStatus() == "1")
            {
                shopTaskService.shopAddEffectiveTask(Integer.valueOf(String.valueOf(model.getBaseId())), model.getCityCode());
            }

            return model.getBaseId();
        }
        return number;
    }

    /**
     * 修改我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:43:44
     *
     * @param model 我的炒店对象
     * @return 受影响行数
     */
    public int updateMyShopList(ShopListDomain model, Integer createUserId, String baseIdArray, String cityCode)
    {
        int rowsSult = iShopListMapper.updateMyShopList(model, createUserId, baseIdArray, cityCode);

        if (model.getStatus() == "1")
        {
            shopTaskService.shopAddEffectiveTask(Integer.valueOf(String.valueOf(model.getBaseId())), model.getCityCode());
        }

        UserDomain modelf = selectReviewUserPhoneByBaseId(createUserId, cityCode, String.valueOf(model.getBaseId()), baseIdArray);
        if (modelf != null)
        {
            //String message = MessageFormat.format("【智能营销】尊敬的{0}用户您好，炒店配置审批[{1}]待您审核，请登录江苏智能营销平台处理", modelf.getName(), model.getBaseName());
            String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(), modelf.getName(), model.getBaseName());
            sendSmsService.sendReminderNoticeSms(modelf.getTelephone(), message);
        }
        return rowsSult;
    }

    /**
     * 查询待审核店面审核人员手机号码
     * 创建人:邵炜
     * 创建时间:2017年3月11日17:37:26
     *
     * @param createUserId 创建人ID
     * @param cityCode     城市ID
     * @param baseId       店面主键
     * @return
     */
    public UserDomain selectReviewUserPhoneByBaseId(Integer createUserId, String cityCode, String baseId, String baseIdArray)
    {
        return iShopListMapper.selectReviewUserPhoneByBaseId(createUserId, cityCode, baseId, baseIdArray);
    }

    /**
     * @param cityCode
     * @return
     * @see IShopListMapper#queryBaseIdByCityCode(Integer)
     */
    public List<Integer> queryBaseIdByCityCode(Integer cityCode)
    {
        return iShopListMapper.queryBaseIdByCityCode(cityCode);
    }

    /**
     * 获取位置基站站点总数
     *
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @param baseArea 基站站点所属城市
     * @return 总数
     */
    public int selectPositionBaseTotal(@Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea, Integer createUserId, String baseIdArray, String cityCode,String busCoding)
    {
        return iShopListMapper.selectPositionBaseTotal(baseId, baseName, baseArea, createUserId, baseIdArray, cityCode, busCoding);
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
    public List<ShopListDomain> selectPositionBaseByPage(int offset, int limit, String baseId, String baseName, String baseArea, Integer createUserId, String baseIdArray, String cityCode,String busCoding)
    {
        return iShopListMapper.selectPositionBaseByPage(offset, limit, baseId, baseName, baseArea, createUserId, baseIdArray, cityCode, busCoding);
    }

    /**
     * 获取待审核位置基站站点总数
     *
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @return 总数
     */
    public int selectBaseInfoByPageStatusTotal(String baseId, String baseName, Integer reviewUserId)
    {
        return iShopListMapper.selectBaseInfoByPageStatusTotal(baseId, baseName, reviewUserId);
    }

    /**
     * 获取待审核位置基站站点列表
     *
     * @param offset   每次查询数量
     * @param limit    起始标记位
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @return 站点列表
     */
    public List<ShopListDomain> selectBaseInfoByPageStatus(int offset, int limit, String baseId, String baseName, Integer reviewUserId)
    {
        return iShopListMapper.selectBaseInfoByPageStatus(offset, limit, baseId, baseName, reviewUserId);
    }

    /**
     * 根据主键及审核人ID查询数据
     * 创建人:邵炜
     * 创建时间:2017年2月27日23:38:02
     *
     * @param baseId       主键
     * @param reviewUserId 审核人ID
     * @return 炒店表实体
     */
    public ShopReviewDomain selectShopByBaseId(String baseId, Integer reviewUserId)
    {
        return iShopListMapper.selectShopByBaseId(baseId, reviewUserId);
    }

    /**
     * 炒店审核
     * 创建人:邵炜
     * 创建时间:2017年2月22日22:13:14
     *
     * @param baseId 炒店表主键
     * @param status 状态
     * @return
     */
    public int updateShopStatusByBaseId(String baseId, String status, Integer reviewUserId, String updateReviewUserId, Integer updateReviewFrom, String cityCode,String baseInfoName)
    {
        String sendMessageMob="";
        if (status.equals("1")){
            sendMessageMob=iShopListMapper.selectPhoneByUserId(baseId);
        }
        int number = iShopListMapper.updateShopStatusByBaseId(baseId, status, reviewUserId, updateReviewUserId, updateReviewFrom);
        if (status.equals("1"))
        {
            String message = MessageFormat.format("【智能营销】尊敬的炒点管理员您好，炒店配置审批[{0}]已审核通过，请登录江苏智能营销平台查阅",baseInfoName);
            sendSmsService.sendReminderNoticeSms(sendMessageMob, message);
            shopTaskService.shopAddEffectiveTask(Integer.valueOf(baseId), Integer.valueOf(cityCode));
        }
        return number;
    }

    /**
     * 新增炒店审核说明
     * 创建人:邵炜
     * 创建时间:2017年2月22日22:37:33
     *
     * @param shopId      炒店外键
     * @param remark      说明
     * @param loginUserId 登录人主键
     * @return
     */
    public int insertShopAuditThrough(String shopId, String remark, Integer loginUserId)
    {
        return iShopListMapper.insertShopAuditThrough(shopId, remark, loginUserId);
    }

    /**
     * @param baseId
     * @return
     */
    public ShopListDomain queryBaseInfoById(String baseId)
    {
        return iShopListMapper.queryBaseInfoById(baseId);
    }

    // 根据用户主键查询用户号码
    // create by gloomy 2017-04-07 14:32:35
    // 用户主键
    // 用户手机号码
public String selectPhoneByUserId(String userId){return iShopListMapper.selectPhoneByUserId(userId);}

    /**
     * 根据编码查询营业厅画像
     * @param businessHallCoding
     * @return
     */
    public BusinessHallPortraitDomain queryBusinessHallPortraitById(String businessHallCoding)
    {
        try
        {
            return iShopListMapper.queryBusinessHallPortraitById(businessHallCoding);
        }
        catch (Exception e)
        {
            LOG.error("queryBusinessHallPortraitById error",e);
            return new BusinessHallPortraitDomain();
        }
    }
}
