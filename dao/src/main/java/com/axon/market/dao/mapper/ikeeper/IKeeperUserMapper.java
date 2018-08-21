package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.KeeperUserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/10.
 */
@Component("keeperUserDao")
public interface IKeeperUserMapper extends IMyBatisMapper
{
    /**
     * 查询掌柜用户数目
     * @param condition
     * @return
     */
    int queryKeeperUsersCount(@Param(value = "userId") Integer userId, @Param(value = "areaId") Integer areaId, @Param(value = "condition") Map<String,Object> condition);

    /**
     * 查询掌柜用户分页信息
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    List<KeeperUserDomain> queryKeeperUsersByPage(@Param(value = "offset") long offset, @Param(value = "maxRecord") long maxRecord, @Param(value = "userId") Integer userId,@Param(value = "areaId") Integer areaId, @Param(value = "condition") Map<String, Object> condition);

    /**
     * 查询用户对应的掌柜部分信息详情
     * @param userId
     * @return
     */
    KeeperUserDomain queryKeeperUserDetail(@Param(value = "userId") Integer userId);

    /**
     * 根据掌柜用户号码查询用户信息
     * @param telephone
     * @return
     */
    KeeperUserDomain queryKeeperUserByTelephone(@Param(value = "telephone") String telephone);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    KeeperUserDomain queryKeeperUserByToken(@Param(value = "token") String token);

    /**
     * 新增掌柜用户
     * @param keeperUserDomain
     * @return
     */
    int insertKeeperUser(KeeperUserDomain keeperUserDomain);

    /**
     * 修改掌柜用户主信息
     * @param keeperUserDomain
     * @return
     */
    int updateKeeperUser(KeeperUserDomain keeperUserDomain);

    /**
     * 删除掌柜用户
     * @param userId
     * @return
     */
    int deleteKeeperUser(@Param(value = "userId") Integer userId, @Param(value = "loginUserId") Integer loginUserId);

    /**
     * 更新掌柜用户状态(用户的删除，禁用；短信签名的审批和生效)
     * @param userId
     * @return
     */
    int handleKeeperUserStatus(@Param(value = "userId") Integer userId,@Param(value = "status") Integer status);

    /**
     * 查询当前用户同组织业务的接口名称
     * @param token
     * @return
     */
    Map<String,Object> queryInterfaceManUnderSameOrg(@Param(value = "token") String token);

    /**
     * 选择能够某区域未被
     * @param areaId
     * @return
     */
    List<Map<String,Object>> queryUsersForKeeperUser(@Param(value = "areaId") Integer areaId);

    /**
     * 查询能够审批的同业务同组织的其他员工
     * @param keeperUser
     * @return
     */
    List<Map<String,Object>> queryKeeperAuditUsers(@Param(value = "keeperUser") KeeperUserDomain keeperUser, @Param(value = "areaId") Integer areaId, @Param(value = "auditType") String auditType);

    /**
     * 保存用户的token信息
     * @param token
     * @return
     */
    int updateKeeperUserToken(@Param(value = "token") String token,@Param(value = "userId") Integer userId);

    /**
     * 判断客户是否被指定末梢人员维系
     * @param userId
     * @param customerPhone
     * @return
     */
    int checkCustomerBeMaintenanceByKeeperUser(@Param(value = "userId") Integer userId, @Param(value = "customerPhone") String customerPhone);

    /**
     * 根据token信息查询用户的部门信息
     * @param token
     * @return
     */
    List<Map<String,Object>> queryMyOrgByToken(@Param(value = "token") String token);

    /**
     * 查询我的短信签名
     * @param token
     * @return
     */
    String queryMySmsSignature(@Param(value = "token") String token);

    /**
     * 删除末梢人员未审批的短信签名
     * @param userId
     * @return
     */
    int deleteUnAuditSmsSignature(@Param(value = "userId") Integer userId);

    /**
     * 插入末梢人员的短信签名审批信息
     * @param userId
     * @param newSmsSignature
     * @return
     */
    int insertAuditKeeperUserSmsSignature(@Param(value = "userId") Integer userId, @Param(value = "newSmsSignature") String newSmsSignature);

    /**
     * 查询需要审批的短信签名
     * @param userId
     * @return
     */
    int queryAuditingSmsSignatureCount(@Param(value = "userId") Integer userId);

    /**
     * 分页查询需要审批的短信签名
     * @param offset
     * @param maxRecord
     * @param userId
     * @return
     */
    List<Map<String,Object>> queryAuditingSmsSignatureByPage(@Param(value = "offset") long offset,@Param(value = "maxRecord") long maxRecord,@Param(value = "userId") Integer userId);

    /**
     * 审批短信签名操作
     * @param auditSmsSignatureId
     * @param auditResultDesc
     * @param auditResult
     * @return
     */
    int auditSmsSignature(@Param(value = "auditSmsSignatureId") Integer auditSmsSignatureId, @Param(value = "auditResultDesc") String auditResultDesc, @Param(value = "auditResult") String auditResult);

    /**
     * 根据组织Id 查询部门组织
     * @param orgId 组织Id
     * @return
     */
    Integer querySystemOrgByOrgId(@Param(value = "orgId") Integer orgId);

    /**
     * 根据组织Id 查询业务组织
     * @param orgId 组织Id
     * @return
     */
    Integer queryBusinessOrgByOrgId(@Param(value = "orgId") Integer orgId);

}
