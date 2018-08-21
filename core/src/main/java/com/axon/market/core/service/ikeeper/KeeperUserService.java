package com.axon.market.core.service.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.cache.IRedisAction;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.constant.ikeeper.KeeperAppResponseCodeEnum;
import com.axon.market.common.domain.ikeeper.KeeperUserDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.MD5Util;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.dao.mapper.ikeeper.IKeeperUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by yuanfei on 2017/8/10.
 */
@Service("keeperUserService")
public class KeeperUserService
{
    @Autowired
    @Qualifier("keeperUserDao")
    private IKeeperUserMapper keeperUserDao;

    @Autowired
    @Qualifier("redisCache")
    private RedisCache redisCache;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    private static final String LOGIN_PRE_KEY = "login_";

    private static final String LOGIN_COUNT_PRE_KEY = "login_count_";

    /**
     * app登录
     * @param userPhone
     * @param verificationCode
     * @param request
     * @return
     */
    public ResultVo loginApp(final String userPhone, String verificationCode, HttpServletRequest request)
    {
        ResultVo result = new ResultVo();
        String loginIp = UserUtils.getVisitorIpAdar(request);
        final String[] ipLoginCount = new String[1];
        final String[] code = new String[1];

        //判断同一用户是否恶意尝试登陆，如果超过三次验证码输入不正确，则进行重新获取验证码提醒
        redisCache.doAction(new IRedisAction()
        {
            @Override
            public boolean action(Jedis jedis)
            {
                ipLoginCount[0] = jedis.get(LOGIN_COUNT_PRE_KEY + loginIp.replace(".","") + userPhone);
                code[0] = jedis.get(LOGIN_PRE_KEY + loginIp.replace(".","") + userPhone);
                return true;
            }
        });

        if (StringUtils.isEmpty(code[0]))
        {
            result.setResultCode("4444");
            result.setResultMsg("请点击获取验证码");
            return result;
        }
        else if (!code[0].equals(verificationCode))
        {
            if (Integer.valueOf(ipLoginCount[0]) < 3)
            {
                redisCache.doAction(new IRedisAction()
                {
                    @Override
                    public boolean action(Jedis jedis)
                    {
                        String key = LOGIN_COUNT_PRE_KEY + loginIp.replace(".", "") + userPhone;
                        jedis.set(key, String.valueOf(Integer.valueOf(jedis.get(key)) + 1));
                        return true;
                    }
                });
                result.setResultCode("4444");
                result.setResultMsg("验证码错误");
            }
            else
            {
                result.setResultCode("4444");
                result.setResultMsg("验证码连续输入错误超过3次，请重新获取验证码");
                redisCache.doAction(new IRedisAction()
                {
                    @Override
                    public boolean action(Jedis jedis)
                    {
                        jedis.del(LOGIN_COUNT_PRE_KEY + loginIp.replace(".", "") + userPhone);
                        jedis.del(LOGIN_PRE_KEY + loginIp.replace(".","") + userPhone);
                        return true;
                    }
                });

            }
            return result;
        }

        KeeperUserDomain keeperUser = keeperUserDao.queryKeeperUserByTelephone(userPhone);
        if (keeperUser != null)
        {
            //登录成功，生成Token
            String token = createToken(axonEncrypt.encrypt(userPhone));
            keeperUserDao.updateKeeperUserToken(token, keeperUser.getUserId());
            keeperUser.setToken(token);

            Map<String,Object> resultObj = new HashMap<String,Object>();
            result.setResultObj(keeperUser);

            redisCache.doAction(new IRedisAction()
            {
                @Override
                public boolean action(Jedis jedis)
                {
                    jedis.del(LOGIN_COUNT_PRE_KEY + loginIp.replace(".", "") + userPhone);
                    jedis.del(LOGIN_PRE_KEY + loginIp.replace(".","") + userPhone);
                    return true;
                }
            });
        }
        else
        {
            result.setResultCode("4444");
            result.setResultMsg("该用户不存在或被禁用");
        }
        return result;
    }

    /**
     * 获取掌柜用户信息
     * @param token
     * @return
     */
    public KeeperUserDomain queryKeeperUserByToken(String token)
    {
        KeeperUserDomain keeperUser =  keeperUserDao.queryKeeperUserByToken(token);
        keeperUser.setOnlineDays(getKeeperUserOnlineDays(keeperUser.getCreateTime()));
        return keeperUser;
    }

    /**
     *
     * @param condition
     * @return
     */
    public int queryKeeperUsersCount(Integer userId, Integer areaId,Map<String,Object> condition)
    {
        return keeperUserDao.queryKeeperUsersCount(userId, areaId, condition);
    }

    /**
     *
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    public List<KeeperUserDomain> queryKeeperUsersByPage(long offset, long maxRecord, Integer userId,Integer areaId, Map<String, Object> condition)
    {
        return keeperUserDao.queryKeeperUsersByPage(offset, maxRecord, userId, areaId, condition);
    }

    /**
     *
     * @param userId
     * @return
     */
    public KeeperUserDomain queryKeeperUserDetail(Integer userId)
    {
        return keeperUserDao.queryKeeperUserDetail(userId);
    }

    /**
     *
     * @param keeperUserDomain
     * @return
     */
    public ServiceResult createKeeperUser(KeeperUserDomain keeperUserDomain, UserDomain loginUser)
    {
        ServiceResult result = new ServiceResult();
        // 如果是省级的创建的就是地市掌柜管理员，否则就是末梢用户
        if (loginUser.getAreaId() == 99999)
        {
            keeperUserDomain.setIsCanManage(1);
        }
        else
        {
            keeperUserDomain.setIsCanManage(0);
        }
        keeperUserDomain.setCreateUser(loginUser.getId());

        if (1 != keeperUserDao.insertKeeperUser(keeperUserDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据库操作异常，新增掌柜用户信息");
        }
        return result;
    }

    /**
     *
     * @param keeperUserDomain
     * @return
     */
    public ServiceResult updateKeeperUser(KeeperUserDomain keeperUserDomain)
    {
        ServiceResult result = new ServiceResult();
        if (1 != keeperUserDao.updateKeeperUser(keeperUserDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据库操作异常，修改掌柜用户信息失败");
        }
        return result;
    }

    /**
     *
     * @param userId
     * @return
     */
    public ServiceResult deleteKeeperUser(Integer userId, Integer loginUserId)
    {
        ServiceResult result = new ServiceResult();
        if(1 != keeperUserDao.deleteKeeperUser(userId, loginUserId))
        {
            result.setRetValue(-1);
            result.setDesc("删除掌柜用户失败");
            return result;
        }
        return result;
    }

    /**
     * 处理掌柜用户
     * @param userId
     * @return
     */
    public ServiceResult handleKeeperUserStatus(Integer userId,Integer status)
    {
        ServiceResult result = new ServiceResult();
        if (1 != keeperUserDao.handleKeeperUserStatus(userId, status))
        {
            result.setRetValue(-1);
            result.setDesc("数据库操作异常，掌柜用户处理失败");
        }
        return result;
    }

    /**
     *
     * @param areaId
     * @return
     */
    public List<Map<String,Object>> queryUsersForKeeperUser(Integer areaId)
    {
        return keeperUserDao.queryUsersForKeeperUser(areaId);
    }

    /**
     *
     * 查询短信签名审批人：同地市的管理员
     * 查询掌柜任务审批人：同地市同部门同组织的末梢人员
     * @param userDomain
     * @return
     */
    public List<Map<String,Object>> queryKeeperAuditUsers(UserDomain userDomain, String auditType,Integer areaId)
    {
        // 如果当前用户是省级，那么直接返回当前登录用户信息作为审批人
        if (userDomain.getAreaId() == 99999)
        {
            Map<String,Object> auditUser = new HashMap<String,Object>(){{
                put("id",userDomain.getId());
                put("name",userDomain.getName());
            }};

            return new ArrayList<Map<String,Object>>(){{ add(auditUser);}};
        }
        return keeperUserDao.queryKeeperAuditUsers(userDomain.getKeeperUser(), areaId, auditType);
    }

    /**
     * 判断客户是否被指定末梢人员维系
     * @param userId
     * @param customerPhone
     * @return
     */
    public Boolean checkCustomerBeMaintenanceByKeeperUser(Integer userId,String customerPhone)
    {
        if (keeperUserDao.checkCustomerBeMaintenanceByKeeperUser(userId, axonEncrypt.encrypt(customerPhone) ) > 0)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param token
     * @return
     */
    public ResultVo queryMyOrg(String token)
    {
        ResultVo result = new ResultVo();
        result.setResultObj(keeperUserDao.queryMyOrgByToken(token));
        return result;
    }

    /**
     * 查询我的短信签名
     * @param token
     * @return
     */
    public ResultVo queryMySmsSignature(String token)
    {
        ResultVo result = new ResultVo();
        result.setResultObj(keeperUserDao.queryMySmsSignature(token));
        return result;
    }

    /**
     * 修改短信签名
     * @param token
     * @param newSmsSignature
     * @return
     */
    @Transactional
    public ResultVo updateKeeperUserSmsSignature(String token, String newSmsSignature)
    {
        ResultVo result = new ResultVo();
        KeeperUserDomain keeperUserDomain = keeperUserDao.queryKeeperUserByToken(token);
        // 先清除还未审批的短信签名
        keeperUserDao.deleteUnAuditSmsSignature(keeperUserDomain.getUserId());

        if (1 != keeperUserDao.insertAuditKeeperUserSmsSignature(keeperUserDomain.getUserId(), newSmsSignature))
        {
            return KeeperAppResponseCodeEnum.DB_OPERATE_ERR.getValue("更新短信签名失败");
        }
        return result;
    }

    /**
     *
     * @param userId
     * @return
     */
    public int queryAuditingSmsSignatureCount(Integer userId)
    {
        return keeperUserDao.queryAuditingSmsSignatureCount(userId);
    }

    /**
     *
     * @param offset
     * @param maxRecord
     * @param userId
     * @return
     */
    public List<Map<String,Object>> queryAuditingSmsSignatureByPage(long offset, long maxRecord, Integer userId)
    {
        return keeperUserDao.queryAuditingSmsSignatureByPage(offset, maxRecord, userId);
    }

    /**
     *
     * @param auditSmsSignatureId
     * @param auditDesc
     * @param auditResult
     * @return
     */
    public ServiceResult auditSmsSignature(Integer auditSmsSignatureId,String auditDesc,String auditResult)
    {
        ServiceResult result = new ServiceResult();
        keeperUserDao.auditSmsSignature(auditSmsSignatureId, auditDesc, auditResult);
        return result;
    }

    /**
     * app登录生成md5加密的token
     * @param phone
     * @return
     */
    private String createToken(String phone)
    {
        //Token规则：MD5加密(APIKEY + 加密手机号 + 时间戳)
        String tokenBasic = "keeper" + phone + new Date().getTime();
        return MD5Util.getMD5Code(tokenBasic);
    }

    /**
     * 获取掌柜用户自创建以来在线的天数
     * @param createTime
     * @return
     */
    private int getKeeperUserOnlineDays(String createTime)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            Date createDate = TimeUtil.formatDateToYMDDevide(createTime);
            Date currentDate = TimeUtil.formatDateToYMDDevide(TimeUtil.formatDateToYMDDevide(new Date()));

            calendar.setTime(createDate);
            int createDays = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(currentDate);
            int currentDays = calendar.get(Calendar.DAY_OF_YEAR);
            return currentDays - createDays;
        }
        catch (ParseException e)
        {

        }
        return 0;
    }
}
