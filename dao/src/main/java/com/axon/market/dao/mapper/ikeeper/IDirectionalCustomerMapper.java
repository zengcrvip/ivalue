package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.UserMaintainDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/8/10.
 */
@Component("directionalCustomerDao")
public interface IDirectionalCustomerMapper extends IMyBatisMapper
{

    /**
     * 查询用户维系关系表
     *
     * @param param
     * @return
     */
    List<Map<String,Object>> queryDirectonalCustomerBase(Map<String, Object> param);


    /**
     * 查询用户维系关系表分页情况
     * @param param
     * @return
     */
    int queryDirectonalCustomerBaseCount(Map<String, Object> param);

    /**
     * 查询用户是否有管理权限
     * @param userId
     * @return
     */
    Integer queryUserManageJurisdiction(@Param("userId") int userId);

    /**
     * 检查用户是否已经存在
     * @param userEntryPhone
     * @return
     */
    Integer checkUserIsExist(@Param("userPhone") String userEntryPhone);

    /**
     * 删除已经存在的用户
     * @param userId
     * @param userId
     * @param delDesc
     * @return
     */
    Integer delExistUser(@Param("userId") Integer userId,@Param("updateUserId") Integer updateUserId,@Param("delDesc") String delDesc);


    /**
     * 创建用户维系关系
     * @param paramMap
     * @return
     */
    int createUserMaintain(Map<String,Object> paramMap);


    /**
     * 查询用户维系关系明细
     * @param userId
     * @return
     */
    UserMaintainDomain queryUserMaintainDetail(@Param("userId") Integer userId);

    /**
     * 更新用户维系关系
     * @param param
     * @return
     */
    int updateUserMaintain(Map<String,Object> param);

    /**
     * 删除用户维系关系
     * @param userId
     * @return
     */
    int deleteUserMaintain(@Param("userId") String userId);

    /**
     * 获取当前登录用户的手机号和掌柜信息
     * @param userId
     * @return
     */
    Map<String,Object> queryCurrentKeeperUser(@Param("loginUserId") Integer userId);


    List<Map<String,String>> queryUserCustPhoneNumberList(@Param("maintainUserId")Integer maintainUserId);


    /**
     * 校验成功的导入临时表
     * @param rowMap
     * @return
     */
    int importDirectionCustomerToTemple(Map<String,Object> rowMap);

    /**
     * 校验失败的导入临时表
     *
     */
    int importFailedDirectionCustomer(Map<String,Object> rowMap);

    /**
     * 获取所有的地市掌柜员工信息
     * @return
     */
    List<String> queryAllMaintainUser();

    /**
     * 插入导入文件信息
     * @param paras
     * @return
     */
    int insertFileMessage(Map<String, Object> paras);

    /**
     * 查询是否已经存在
     * @param fileId
     * @return
     */
    String queryIsExist(@Param("fileId") String fileId);


    /**
     * 覆盖已经存在的数据
     * @param ids
     * @return
     */
    int coverIsExistData(@Param("ids") String ids , @Param("updateUserId") Integer userId);


    /**
     * 保存导入信息
     * @param fileId
     * @return
     */
    int saveDirectionCustomer(@Param("fileId") String fileId,@Param("createUserId") Integer userId);

}
