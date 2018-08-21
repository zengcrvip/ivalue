package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Component("userDao")
public interface IUserMapper extends IMyBatisMapper
{
    /**
     * 根据条件查询所有用户的数目
     * @param condition
     * @return
     */
    int queryUsersCount(@Param(value = "loginUser") UserDomain loginUser,@Param(value = "condition") Map<String,Object> condition);

    /**
     * 分页查询用户列表
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    List<UserDomain> queryUsersByPage(@Param(value = "offset") long offset,@Param(value = "maxRecord") long maxRecord,@Param(value = "loginUser") UserDomain loginUser,@Param(value = "condition") Map<String,Object> condition);

    /**
     * 查询对应类型的审批者
     * @param auditType
     * @return
     */
    List<UserDomain> queryAuditUsers(@Param(value = "auditType") String auditType,@Param(value = "areaId") Integer areaId,@Param(value = "beHandleUser") Integer beHandleUser);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    UserDomain queryUserById(@Param(value = "userId") Integer userId);

    /**
     * 根据电话号码查询用户
     * @param telephone
     * @return
     */
    UserDomain queryUserByTelephone(@Param(value = "telephone") String telephone);

    /**
     * 查询是否有需要该用户审批的用户，有则返回所有需要审批用户的名称
     * @param auditUserId
     * @return
     */
    List<String> queryUsersNameByAuditUser(@Param(value = "auditUserId") Integer auditUserId);

    /**
     * 查询拥有相同的营业厅
     * @param businessHandleIds
     * @return
     */
    String querySameBusinessHallNames(@Param(value = "businessHandleIds") String businessHandleIds,@Param(value = "editUserId") Integer editUserId);

    /**
     * 创建用户
     * @param userDomain
     * @return
     */
    int insertUser(@Param(value = "user") UserDomain userDomain);

    /**
     * 修改用户信息
     * @param userDomain
     * @return
     */
    int updateUser(@Param(value = "user") UserDomain userDomain);

    /**
     *
     * @param userDomain
     * @return
     */
    int updatePersonalInfo(@Param(value = "user") UserDomain userDomain);

    /**
     * 对用户进行启停操作
     * @param status
     * @param userId
     * @return
     */
    int startStopUser(@Param(value = "status") Integer status,@Param(value = "userId") Integer userId);

    /**
     * 查询用户名下是否有需要审批的模型
     * @param userId
     * @return
     */
    List<String> queryNeedAuditOfModel(@Param(value = "userId") Integer userId);

    /**
     * 查询用户名下是否有需要审批的标签
     * @param userId
     * @return
     */
    List<String> queryNeedAuditOfTag(@Param(value = "userId") Integer userId);

    /**
     * 查询用户名下是否有需要审批的营销任务
     * @param userId
     * @return
     */
    List<Map<String,String>> queryNeedAuditOfMarketingTask(@Param(value = "userId") Integer userId);

    /**
     * 查询用户的审批中的模型、标签、营销任务、炒店任务等
     * @param userId
     * @return
     */
    List<String> queryNeedAuditNamesOfUser(@Param(value = "userId") Integer userId);

    /**
     * 查询角色对应的所有用户的号码
     * @param roleIds
     * @return
     */
    List<String> queryPhonesByUserRoleIds(@Param(value = "roleIds")  String roleIds);

    /**
     *
     * @param token
     * @return
     */
    UserDomain queryUserByToken(@Param(value = "token") String token);

    /**
     *
     * @param userId
     * @return
     */
    List<String> queryUserCanOperateTaskGX(@Param(value = "userId") Integer userId);

    /**
     *
     * @param userId
     * @return
     */
    List<Map<String,Object>> queryAllMyCreatedSubUsers(@Param(value = "userId") Integer userId,@Param(value = "areaId") Integer areaId);

    /**
     *
     * @param userId
     * @param areaId
     * @return
     */
    List<Integer> queryAllSubUserIdsCreatedByI(@Param(value = "userId") Integer userId,@Param(value = "areaId") Integer areaId,@Param(value = "targetUser") Integer targetUser);

    /**
     * 区分出修改时批量处理删除新增审批人的用户id
     * @param subAdmin
     * @param businessHallIds
     * @return
     */
    List<Map<String,Object>> queryBatchAuditUserIdsByType(@Param(value = "subAdmin") Integer subAdmin,@Param(value = "businessHallIds") String businessHallIds);

    /**
     * 批量给营业厅用户指定审批人
     * @param userDomain
     * @return
     */
    int batchAddUsersAuditUser(@Param(value = "userDomain") UserDomain userDomain,@Param(value = "typeUserIds") String typeUserIds);

    /**
     * 批量给营业厅用户初始化审批人
     * @param userDomain
     * @return
     */
    int batchDeleteUsersAuditUser(@Param(value = "userDomain") UserDomain userDomain,@Param(value = "typeUserIds") String typeUserIds);

    List<UserDomain> queryUsersByOrgIds(@Param(value = "orgIds") String orgIds);
}
