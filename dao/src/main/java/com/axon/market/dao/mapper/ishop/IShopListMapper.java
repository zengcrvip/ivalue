package com.axon.market.dao.mapper.ishop;

import com.axon.market.common.domain.ishop.BusinessHallPortraitDomain;
import com.axon.market.common.domain.ishop.ShopListDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.ishop.ShopReviewDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 我的炒店列表
 * Created by gloomysw on 2017/02/20.
 */
@Component("shopListDao")
public interface IShopListMapper extends IMyBatisMapper
{
    /**
     * 查询我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日10:55:08
     *
     * @return 我的炒店实体
     */
    List<ShopListDomain> selectMyShopList();

    /**
     * 新增我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:41:42
     *
     * @param model        我的炒店实体
     * @param createUserId 创建人ID
     * @return 受影响行数
     */
    int insertMyShopList(@Param(value = "info") ShopListDomain model, @Param(value = "createUserId") Integer createUserId);

    /**
     * 修改我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:42:00
     *
     * @param model 我的炒店实体
     * @return 受影响行数
     */
    int updateMyShopList(@Param(value = "info") ShopListDomain model, @Param(value = "createUserId") Integer createUserId, @Param(value = "baseIdArray") String baseIdArray, @Param(value = "cityCode") String cityCode);

    /**
     * 根据城市编码查询营业厅id
     *
     * @param cityCode 城市编码
     * @return
     */
    List<Integer> queryBaseIdByCityCode(Integer cityCode);

    /**
     * 获取位置基站站点总数
     *
     * @param baseId       基站站点ID
     * @param baseName     基站站点名称
     * @param baseArea     基站站点所属城市
     * @param createUserId 创建人ID
     * @return 总数
     */
    int selectPositionBaseTotal(@Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea, @Param(value = "createUserId") Integer createUserId, @Param(value = "baseIdArray") String baseIdArray, @Param(value = "cityCode") String cityCode,@Param(value="busCoding") String busCoding);

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
    List<ShopListDomain> selectPositionBaseByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea, @Param(value = "createUserId") Integer createUserId, @Param(value = "baseIdArray") String baseIdArray, @Param(value = "cityCode") String cityCode,@Param(value = "busCoding") String busCoding);

    /**
     * 查询待审核店面审核人员手机号码
     * 创建人:邵炜
     * 创建时间:2017年3月11日17:37:26
     * @param createUserId 创建人ID
     * @param cityCode 城市ID
     * @param baseId 店面主键
     * @return
     */
    UserDomain selectReviewUserPhoneByBaseId(@Param(value="createUserId")Integer createUserId,@Param(value="cityCode") String cityCode,@Param(value = "baseId")String baseId,@Param(value = "baseIdArray") String baseIdArray);

    /**
     * 获取待审核位置基站站点列表
     *
     * @param offset   每次查询数量
     * @param limit    起始标记位
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @return 站点列表
     */
    List<ShopListDomain> selectBaseInfoByPageStatus(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "reviewUserId") Integer reviewUserId);

    /**
     * 根据主键及审核人ID查询数据
     * 创建人:邵炜
     * 创建时间:2017年2月27日23:38:02
     * @param baseId 主键
     * @param reviewUserId 审核人ID
     * @return 炒店表实体
     */
    ShopReviewDomain selectShopByBaseId(@Param(value="baseId") String baseId, @Param(value="reviewUserId") Integer reviewUserId);

    /**
     * 根据主键查询炒店信息
     * @param baseId
     * @return
     */
    ShopListDomain queryBaseInfoById(@Param(value="baseId") String baseId);

    /**
     * 获取待审核位置基站站点总数
     *
     * @param baseId   基站站点ID
     * @param baseName 基站站点名称
     * @return 总数
     */
    int selectBaseInfoByPageStatusTotal(@Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "reviewUserId") Integer reviewUserId);

    /**
     * 炒店审核
     * 创建人:邵炜
     * 创建时间:2017年2月22日22:13:14
     *
     * @param baseId 炒店表主键
     * @param status 状态
     * @return
     */
    int updateShopStatusByBaseId(@Param(value="baseId") String baseId,@Param(value="status") String status,@Param(value="reviewUserId") Integer reviewUserId,@Param(value="updateReviewUserId") String updateReviewUserId,@Param(value="updateReviewFrom") Integer updateReviewFrom);


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
    int insertShopAuditThrough(@Param(value = "shopId") String shopId, @Param(value = "remark") String remark, @Param(value = "loginUserId") Integer loginUserId);

    /**
     * 新增的炒店分配给当前用户
     * @param businessIdStr 店面id
     * @param id 用户id
     * @return
     */
    int updateUserBusinessHallIds(@Param(value = "businessIdStr") String businessIdStr,@Param(value = "userId") Integer id);

    // 根据用户id查询用户号码
    // create by gloomy 2017-04-07 14:26:03
    // 用户主键
    // 用户手机号码
    String selectPhoneByUserId(@Param(value="id") String id);

    /**
     * 根据编码查询营业厅画像
     * @param businessHallCoding
     * @return
     */
    BusinessHallPortraitDomain queryBusinessHallPortraitById(@Param("businessHallCoding") String businessHallCoding);
}
