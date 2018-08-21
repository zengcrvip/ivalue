package com.axon.market.dao.mapper.ishop;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.domain.iscene.MarketAreaDomain;
import com.axon.market.common.domain.ishop.ShopBlackDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zengcr on 2017/4/12.
 */
@Component("shopBlackDAO")
public interface IShopBlackMapper extends IMyBatisMapper
{
    /**
     * 查询炒店黑名单总数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    int queryShopBlackPhoneTotal(Map<String, Object> paras);

    /**
     * 分页查询炒店黑名单
     *
     * @return
     */
    List<ShopBlackDomain> queryShopBlackPhoneByPage(Map<String, Object> paras);

    /**
     * 查询场景位置号段地区树结构
     *
     * @param areaIds 已选中的地区ID
     * @return
     */
    List<MarketAreaDomain> queryShopBlackBaseAreas(@Param(value = "areaIds") String areaIds, @Param(value = "businessCodes") String businessCodes);

    /**
     * @param paras
     * @return
     */
    List<Map<String, String>> queryShopBlockBases(Map<String, Object> paras);

    /**
     * 新增炒店用户黑名单
     *
     * @param shopBlackDomain
     * @return
     */
    Integer createShopUserBlackList(@Param(value = "info") ShopBlackDomain shopBlackDomain);

    /**
     * 删除炒店用户黑名单
     *
     * @param paras
     * @return
     */
    Integer deleteShopUserBlackList(Map<String, Object> paras);

    /**
     * 删除多个炒店用户黑名单
     *
     * @param paras
     * @return
     */
    Integer deleteAllShopUserBlackList(Map<String, Object> paras);

    /**
     * @param fileId
     * @return
     */
    List<Map<String, Object>> queryShopUserBlackListPhoneByFileId(@Param(value = "fileId") String fileId);

    /**
     * 查询炒店黑名单
     *
     * @param paras
     * @return
     */
    List<ShopBlackDomain> queryShopBlackPhone(Map<String, Object> paras);

    Integer queryShopUserBlackListByPhone(@Param(value = "phone") String phone);

    /**
     * 批量新增黑名单用户
     * @param shopBlackDomainList
     * @return
     */
    int insertShopBlackUserBatch(@Param(value = "list") List<ShopBlackDomain> shopBlackDomainList);

    /**
     * 删除黑名单中所有的已经存在shopBlackUserPhones中的数据
     * @param shopBlackUserPhones
     * @return
     */
    int deleteShopBlackUserBatch(@Param(value = "shopBlackUserPhones") String shopBlackUserPhones);
}
