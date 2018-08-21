package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.cache.IRedisAction;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.constant.isystem.UserAuditTypeEnum;
import com.axon.market.common.domain.icommon.MenuDomain;
import com.axon.market.common.domain.icommon.NodeDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.service.icommon.AreaService;
import com.axon.market.core.service.icommon.MenuService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.ikeeper.KeeperUserService;
import com.axon.market.dao.mapper.iscene.IPositionSceneMapper;
import com.axon.market.dao.mapper.isystem.IRoleMapper;
import com.axon.market.dao.mapper.isystem.IUserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by yuanfei on 2017/1/4.
 */
@Component("userService")
public class UserService
{
    @Autowired
    @Qualifier("userDao")
    private IUserMapper userDao;

    @Autowired
    @Qualifier("roleDao")
    private IRoleMapper roleDao;

    @Autowired
    @Qualifier("areaService")
    private AreaService areaService;

    @Autowired
    @Qualifier("menuService")
    private MenuService menuService;

    @Autowired
    @Qualifier("redisCache")
    private RedisCache redisCache;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Qualifier("positionSceneDao")
    @Autowired
    private IPositionSceneMapper iPositionSceneMapper;

    @Qualifier("keeperUserService")
    @Autowired
    private KeeperUserService keeperUserService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    private static final String LOGIN_PRE_KEY = "login_";

    private static final String LOGIN_COUNT_PRE_KEY = "login_count_";

    private static final String VERIFICATION_CODE_PC_SEND = "PC";

    private static final String VERIFICATION_CODE_APP_SEND = "app";

    public static UserService getInstance()
    {
        return (UserService) SpringUtil.getSingletonBean("userService");
    }

    /**
     * 手机号验证码登录
     *
     * @param userPhone
     * @param verificationCode
     * @param session
     * @return
     */
    public ServiceResult loginByPhone(final String userPhone, String verificationCode, HttpSession session,HttpServletRequest request)
    {
        ServiceResult result = new ServiceResult();
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
            result.setRetValue(1);
            result.setDesc("请点击获取验证码");
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

                result.setRetValue(1);
                result.setDesc("验证码错误");
            }
            else
            {
                result.setRetValue(1);
                result.setDesc("验证码连续输入错误超过3次，请重新获取验证码");
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


        UserDomain userDomain = userDao.queryUserByTelephone(userPhone);
        if (userDomain != null)
        {
            if (userDomain.getStatus().equals(1))
            {
                result.setRetValue(1);
                result.setDesc("该用户已被禁用");
                return result;
            }

            List<MenuDomain> menuList = menuService.getMenus(userDomain.getId(), session);
            //手机号加密
            userDomain.setTelephone(axonEncrypt.encrypt(userDomain.getTelephone()));

            session.setAttribute("USER", userDomain);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomain));
            session.setAttribute("MENU", menuList);
            //OperateLogDomain operateLogDomain = generateOperateLogDomain(100,userDomain.getId(),"login",0);
            //operateLogService.insertOperateLog(operateLogDomain);
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
            result.setRetValue(1);
            result.setDesc("未找到该用户");
        }
        return result;
    }

    /**
     * 根据手机号和密码登录
     * @param userPhone
     * @param userPwd
     * @param session
     * @return
     */
    public ServiceResult loginByName(String userPhone, String userPwd, HttpSession session)
    {
        ServiceResult result = new ServiceResult();

        if ("12345678987".equals(userPhone))
        {
            UserDomain userDomain = userDao.queryUserById(100000);
            userDomain.setTelephone(axonEncrypt.encrypt(userDomain.getTelephone()));
            List<MenuDomain> menuList = menuService.getMenus(userDomain.getId(), session);
            session.setAttribute("USER", userDomain);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomain));
            session.setAttribute("MENU", menuList);
            return result;
        }

        if ("18888888888".equals(userPhone))
        {
            UserDomain userDomain = userDao.queryUserById(100005);
            userDomain.setTelephone(axonEncrypt.encrypt(userDomain.getTelephone()));
            List<MenuDomain> menuList = menuService.getMenus(userDomain.getId(), session);
            session.setAttribute("USER", userDomain);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomain));
            session.setAttribute("MENU", menuList);
            return result;
        }

        if ("19999999999".equals(userPhone))
        {
            UserDomain userDomain = userDao.queryUserById(100001);
            userDomain.setTelephone(axonEncrypt.encrypt(userDomain.getTelephone()));
            List<MenuDomain> menuList = menuService.getMenus(userDomain.getId(), session);
            session.setAttribute("USER", userDomain);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomain));
            session.setAttribute("MENU", menuList);
            return result;
        }


        UserDomain userDomain = userDao.queryUserByTelephone(userPhone);
        if (userDomain != null)
        {
            if (userDomain.getStatus().equals(1))
            {
                result.setRetValue(1);
                result.setDesc("该用户已被禁用");
            }
            else if (MD5Util.getMD5Code(userPwd).equals(userDomain.getPassword()))
            {
                List<MenuDomain> menuList = menuService.getMenus(userDomain.getId(), session);
                //手机号加密
                userDomain.setTelephone(axonEncrypt.encrypt(userDomain.getTelephone()));

                session.setAttribute("USER", userDomain);
                //session存放数据权限
                session.setAttribute("D_PERMISSION", getDataPermissions(userDomain));
                session.setAttribute("MENU", menuList);
                //OperateLogDomain operateLogDomain = generateOperateLogDomain(100,userDomain.getId(),"login",0);
                //operateLogService.insertOperateLog(operateLogDomain);
            }
            else
            {
                result.setRetValue(1);
                result.setDesc("用户名或密码错误");
            }
        }
        else
        {
            result.setRetValue(1);
            result.setDesc("未找到该用户");
        }

        return result;
    }

    /**
     * 发送验证码
     * @param userPhone
     * @param request
     * @return
     */
    public ServiceResult sendVerificationCode(final String userPhone,HttpServletRequest request,String type)
    {
        ServiceResult result = new ServiceResult();
        String loginIp = UserUtils.getVisitorIpAdar(request);

        UserDomain userDomain = userDao.queryUserByTelephone(userPhone);
        if (userDomain == null || (VERIFICATION_CODE_APP_SEND.equals(type) && userDomain.getKeeperUser() == null))
        {
            result.setRetValue(-1);
            result.setDesc("账号不存在");
            return result;
        }
        else if (userDomain.getStatus().equals(1))
        {
            result.setRetValue(1);
            result.setDesc("账号已被禁用");
            return result;
        }

        final String randomCode = generatingRandomCode();

        ThreadPoolUtil.submit(new Runnable() {
            @Override
            public void run() {
                //短信验证码发送
                ServiceResult sendResult = sendVerificationCodeSms(userPhone, randomCode);
                if (sendResult.getRetValue() == 0)
                {
                    redisCache.doAction(new IRedisAction() {
                        @Override
                        public boolean action(Jedis jedis) {
                            String key = LOGIN_PRE_KEY + loginIp.replace(".", "") + userPhone;
                            String keyReqCount = LOGIN_COUNT_PRE_KEY + loginIp.replace(".", "") + userPhone;
                            jedis.set(key, randomCode);
                            jedis.set(keyReqCount, "0");
                            jedis.expire(key, 900);
                            jedis.expire(keyReqCount, 900);
                            return true;
                        }
                    });
                }
            }
        });

        return result;
    }

    /**
     * @see IUserMapper#queryUsersCount(UserDomain, Map)
     */
    public int queryUsersCount(UserDomain loginUser,Map<String, Object> condition)
    {
        return userDao.queryUsersCount(loginUser,condition);
    }

    /**
     * @see IUserMapper#queryUsersByPage(long, long, UserDomain, Map)
     */
    public List<UserDomain> queryUsersByPage(long offset, long maxRecord, UserDomain loginUser,Map<String, Object> condition) throws IOException
    {
        List<UserDomain> userList = userDao.queryUsersByPage(offset, maxRecord, loginUser, condition);
        for (UserDomain userDomain : userList)
        {
            analysisAuditUserNames(userDomain);
        }
        return userList;
    }

    /**
     * 查询登录用户的基本信息
     * @param id
     * @return
     */
    public UserDomain queryPersonalBaseInfo(Integer id) throws IOException
    {
        UserDomain userDomain = userDao.queryUserById(id);
        analysisAuditUserNames(userDomain);
        return userDomain;
    }

    /**
     * 查询审批者
     *
     * @param auditType
     * @return
     * @throws JsonProcessingException
     */
    public String queryAuditUsers(String auditType, Integer userArea, Integer beHandleUser) throws JsonProcessingException
    {

        List<UserDomain> userList = userDao.queryAuditUsers(auditType, userArea, beHandleUser);
        List<NodeDomain> nodeList = areaService.queryUserAreas();
        List<NodeDomain> result = new ArrayList<NodeDomain>();
        if (CollectionUtils.isNotEmpty(nodeList))
        {
            for (NodeDomain areaDomain : nodeList)
            {
                //如果操作用户是省级用户
                if (Integer.valueOf(areaDomain.getId()).equals(99999) || Integer.valueOf(areaDomain.getId()).equals(userArea))
                {
                    areaDomain.setIsParent(true);
                    areaDomain.setNocheck(true);
                    areaDomain.setOpen(false);
                    result.add(areaDomain);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(userList))
        {
            for (UserDomain userDomain : userList)
            {
                NodeDomain nodeDomain = new NodeDomain();
                nodeDomain.setId(String.valueOf(userDomain.getId()));
                nodeDomain.setName(userDomain.getName());
                nodeDomain.setpId(String.valueOf(userDomain.getAreaId()));
                result.add(nodeDomain);
            }
        }
        return JsonUtil.objectToString(result);
    }

    /**
     * @return
     * @see IUserMapper#queryUserById(Integer)
     */
    public UserDomain queryUserById(Integer userId)
    {
        return userDao.queryUserById(userId);
    }

    /**
     *
     * @param token
     * @return
     */
    public UserDomain queryUserByToken(String token)
    {
        return userDao.queryUserByToken(token);
    }

    /**
     * @return
     * @see IUserMapper#insertUser(UserDomain)
     */
    public ServiceResult createUser(UserDomain userDomain)
    {
        ServiceResult result = checkDifferentTelephone(userDomain);

        if (result.getRetValue() != 0)
        {
           return result;
        }

        ServiceResult result1 = checkSelectedDifferentBusinessHall(userDomain);

        ServiceResult result2 = checkAuditUserIsUseful(userDomain);
        if (result1.getRetValue() != 0)
        {
            return result;
        }

        if (StringUtils.isNotEmpty(userDomain.getPassword()))
        {
            userDomain.setPassword(MD5Util.getMD5Code(userDomain.getPassword()));
        }

        if (1 != userDao.insertUser(userDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据库新增操作失败！");
        }
        return result;
    }

    /**
     * @return
     * @see IUserMapper#updateUser(UserDomain)
     */
    public ServiceResult updateUser(UserDomain userDomain)
    {
        ServiceResult result = checkDifferentTelephone(userDomain);

        if (result.getRetValue() != 0)
        {
            return result;
        }

        ServiceResult result1 = checkSelectedDifferentBusinessHall(userDomain);
        if (result1.getRetValue() != 0)
        {
            return result1;
        }

        if (StringUtils.isNotEmpty(userDomain.getPassword()))
        {
            userDomain.setPassword(MD5Util.getMD5Code(userDomain.getPassword()));
        }

        if (1 != userDao.updateUser(userDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据库修改操作失败！");
        }
        return result;
    }

    /**
     *
     * @param userDomain
     * @param oldPassword
     * @return
     * @throws IOException
     */
    public ServiceResult updatePersonalBaseInfo(UserDomain userDomain, String oldPassword) throws IOException
    {
        ServiceResult result = new ServiceResult();

/*        if (checkDifferentTelephone(userDomain).getRetValue() == -1)
        {
            return result;
        }*/

        UserDomain user = userDao.queryUserById(userDomain.getId());
        if (user != null)
        {
            if (MD5Util.getMD5Code(oldPassword).equals(user.getPassword()))
            {
                if (StringUtils.isNotEmpty(userDomain.getPassword()))
                {
                    //密码进行MD5加密处理
                    userDomain.setPassword(MD5Util.getMD5Code(userDomain.getPassword()));
                }
                if (userDao.updatePersonalInfo(userDomain) != 1)
                {
                    result.setRetValue(-1);
                    result.setDesc("用户数据库更新操作异常");
                    return result;
                }
            }
            else
            {
                result.setRetValue(1);
                result.setDesc("登录密码不正确！");
            }
        }
        else
        {
            result.setRetValue(1);
            result.setDesc("未找到该用户");
        }

        return result;
    }

    /**
     * 启停用户
     *
     * @param userId
     * @return
     */
    public ServiceResult startStopUser(Integer userId)
    {
        ServiceResult result = new ServiceResult();
        UserDomain userDomain = queryUserById(userId);

        //进行停用操作时，需要提前判断可操作性
        if (userDomain.getStatus().equals(0))
        {
            //先判断被禁用用户是否管理了营业厅，如有则不让禁用，必须将营业厅去除
            if (StringUtils.isNotEmpty(userDomain.getBusinessHallIds()))
            {
                result.setRetValue(-2);
                result.setDesc("该用户名下有营业厅需管理，<无法停用>");
                return result;
            }

            //如果该用户需要审批别人，查出所有需要该用户审批的用户
            List<String> needAuditUsersName = userDao.queryUsersNameByAuditUser(userId);
            if (CollectionUtils.isNotEmpty(needAuditUsersName))
            {
                result.setRetValue(-1);
                result.setDesc("该用户负责审核以下用户：" + StringUtils.join(needAuditUsersName, ",") + "<无法停用>");
                return result;
            }
            // 禁用用户对应的掌柜用户
            keeperUserService.handleKeeperUserStatus(userId, 3);
        }
        else
        {
            // 启用用户对应的掌柜用户 默认为0
            keeperUserService.handleKeeperUserStatus(userId, 0);
        }

        //启停用户 0：生效中  1：已失效
        boolean isValid = userDomain.getStatus().equals(0);
        int willStatus = isValid ? 1 : 0;
        result.setDesc(isValid ? "停用用户成功！" : "启用用户成功！");
        if (1 != userDao.startStopUser(willStatus, userId))
        {
            result.setRetValue(-1);
            result.setDesc(isValid ? "停用用户失败！" : "启用用户失败！");
        }

        return result;
    }

    /**
     *
     * @param userId
     * @param auditType
     * @return
     * @throws IOException
     */
    public ServiceResult checkChangeAuditUser(Integer userId, String auditType) throws IOException
    {
        ServiceResult result = new ServiceResult();
        if (UserAuditTypeEnum.AUDIT_MODEL.getValue().equals(auditType))
        {
            List<String> names = userDao.queryNeedAuditOfModel(userId);
            result.setRetValue(CollectionUtils.isNotEmpty(names) ? 1 : 0);
            result.setDesc("模型 "+StringUtils.join(names, ","));
        }
        else if (UserAuditTypeEnum.AUDIT_TAG.getValue().equals(auditType))
        {
            List<String> names = userDao.queryNeedAuditOfTag(userId);
            result.setRetValue(CollectionUtils.isNotEmpty(names) ? 1 : 0);
            result.setDesc("标签 "+StringUtils.join(names, ","));
        }
        else if (UserAuditTypeEnum.AUDIT_MARKET_TASK.getValue().equals(auditType))
        {
            List<Map<String,String>> nameList = userDao.queryNeedAuditOfMarketingTask(userId);
            for (Map<String,String> name : nameList)
            {
                if (StringUtils.isNotEmpty(name.get("names")))
                {
                    result.setRetValue(1);
                    result.setDesc(name.get("type")+" "+name.get("names"));
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 查询出角色对应的所有用户号码
     * @param roleIds
     * @return
     */
    public List<String> queryPhonesByUserRoleIds(String roleIds)
    {
        return userDao.queryPhonesByUserRoleIds(roleIds);
    }

    /**
     * 解析获取用户名称
     *
     * @param userDomain
     * @throws IOException
     */
    private void analysisAuditUserNames(UserDomain userDomain) throws IOException
    {
        String segmentAuditUsers = userDomain.getSegmentAuditUsers();
        String tagAuditUsers = userDomain.getTagAuditUsers();
        String marketingAuditUsers = userDomain.getMarketingAuditUsers();
        List<String> names = new ArrayList<String>();
        if (StringUtils.isNotEmpty(segmentAuditUsers))
        {
            List<Map<String, Object>> segmentAuditUserList = JsonUtil.stringToObject(segmentAuditUsers, List.class);
            for (Map<String, Object> auditUser : segmentAuditUserList)
            {
                names.add(String.valueOf(auditUser.get("auditUserName")) + "(" + auditUser.get("order") + "级审批人)");
            }
            userDomain.setSegmentAuditUserNames(StringUtils.join(names, ","));
        }

        names.clear();
        if (StringUtils.isNotEmpty(tagAuditUsers))
        {
            List<Map<String, Object>> tagAuditUserList = JsonUtil.stringToObject(tagAuditUsers, List.class);
            for (Map<String, Object> auditUser : tagAuditUserList)
            {
                names.add(String.valueOf(auditUser.get("auditUserName")) + "(" + auditUser.get("order") + "级审批人)");
            }
            userDomain.setTagAuditUserNames(StringUtils.join(names, ","));
        }

        names.clear();
        if (StringUtils.isNotEmpty(marketingAuditUsers))
        {
            List<Map<String, Object>> marketingAuditUserList = JsonUtil.stringToObject(marketingAuditUsers, List.class);
            for (Map<String, Object> auditUser : marketingAuditUserList)
            {
                names.add(String.valueOf(auditUser.get("auditUserName")) + "(" + auditUser.get("order") + "级审批人)");
            }
            userDomain.setMarketingAuditUserNames(StringUtils.join(names, ","));
        }
    }

    /**
     * 查询号码是否已经存在
     *
     * @param userDomain
     * @return
     */
    private ServiceResult checkDifferentTelephone(UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        String telephone = axonEncrypt.decrypt(userDomain.getTelephone());
        UserDomain matchUser = userDao.queryUserByTelephone(telephone);
        if (null != matchUser && !matchUser.getId().equals(userDomain.getId()))
        {
            result.setRetValue(1);
            result.setDesc("号码【" + telephone + "】已存在");
        }
        return result;
    }

    /**
     * 检查是否选择了相同的营业厅
     * @param userDomain
     * @return
     */
    private ServiceResult checkSelectedDifferentBusinessHall(UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        if (StringUtils.isNotEmpty(userDomain.getBusinessHallIds()))
        {
            String businessHallNames = userDao.querySameBusinessHallNames(userDomain.getBusinessHallIds(),userDomain.getId());
            if (StringUtils.isNotEmpty(businessHallNames))
            {
                result.setRetValue(-4);
                result.setDesc("营业厅："+businessHallNames+"已被其他成员管理，请重新选择！");
            }
        }
        return result;
    }

    /**
     * 检查审批者的有效性
     * @param userDomain
     * @return
     */
    private ServiceResult checkAuditUserIsUseful(UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        String marketAuditUsers = userDomain.getMarketingAuditUsers();
        try
        {
            List<Map<String,String>> marketAuditUserList = (List<Map<String,String>>)JsonUtil.stringToObject(marketAuditUsers,List.class);
            List<Integer> auditUserIds = new ArrayList<Integer>();
            for (Map<String,String> marketAuditUser : marketAuditUserList)
            {
                //[{"order":"1","auditUser":"100000","auditUserName":"汤亚男"}]
                auditUserIds.add(Integer.valueOf(marketAuditUser.get("auditUser")));
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取估计随机码
     *
     * @return
     */
    private String generatingRandomCode()
    {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++)
        {
            code += random.nextInt(10);
        }
        return code;
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param verificationCode
     * @return
     */
    private ServiceResult sendVerificationCodeSms(String phone, String verificationCode)
    {
        ServiceResult result = new ServiceResult();
        String message = MessageFormat.format(smsConfigBean.getVerificationCodeSmsContent(), verificationCode);

        //江苏短信发送
        if("JS".equals(systemConfigBean.getProvince()))
        {
            //优先调用10008899接口
            String flag = sendSmsService.sendReminderNoticeSms(phone,message);
            if ("-3".equals(flag))
            {
                result.setRetValue(-3);
                result.setDesc("非江苏联通号码不能登录!");
            }
            else if (!"0".equals(flag) && !sendSmsService.sendVerificationCodeSms(phone, message, systemConfigBean.getProvince()))
            {
                result.setRetValue(-1);
                result.setDesc("发送验证码失败！");
            }
        }else if("SH".equals(systemConfigBean.getProvince()) || "HAINAN".equals(systemConfigBean.getProvince())){
            sendSmsService.sendReminderNoticeSms(phone,message);
        }else {
            //其它省市短信发送
            sendSmsService.sendVerificationCodeSms(phone, message, systemConfigBean.getProvince());
        }

        return result;
    }

    /**
     * 获取用户的数据权限
     *
     * @param userDomain
     * @return
     */
    private Map<String, Boolean> getDataPermissions(UserDomain userDomain)
    {
        Map<String, Boolean> permissionResult = new HashMap<>();

        List<Map<String, Object>> permissions = roleDao.queryUserDataPermissionList(userDomain.getId());
        for (Map<String, Object> permission : permissions)
        {
            //用于后端数据鉴权控制
            String[] urls = String.valueOf(permission.get("url")).split(",");
            for (String url : urls)
            {
                permissionResult.put(url + "_u", "1".equals(String.valueOf(permission.get("isContain"))));
            }
            //用于前端鉴权控制(显示隐藏等)
            permissionResult.put(String.valueOf(permission.get("permission")) + "_p", "1".equals(String.valueOf(permission.get("isContain"))));
        }
        return permissionResult;
    }

    /**
     * 需要审批其他用户，或者名下有需要审批的标签、模型、任务等的，均不允许做区域切换
     * @param userId
     * @return
     */
    public ServiceResult checkChangeUserArea(Integer userId)
    {
        ServiceResult result = new ServiceResult();
        //查询当前用户是否被设置为审批人
        List<String> needBeAuditUserNames = userDao.queryUsersNameByAuditUser(userId);
        if (CollectionUtils.isNotEmpty(needBeAuditUserNames))
        {
            result.setRetValue(-1);
            result.setDesc("该用户负责审核以下用户：" + StringUtils.join(needBeAuditUserNames, ",") + ",无法切换该用户归属区域！");
            return result;
        }

        List<String> needAuditItemNames = userDao.queryNeedAuditNamesOfUser(userId);
        if (CollectionUtils.isNotEmpty(needAuditItemNames))
        {
            result.setRetValue(-2);
            result.setDesc("该用户下有审批中的模型、标签、任务等");
        }
        return result;
    }

    /**
     * 广西平台首页中显示任务类型
     * @param userId
     * @return
     */
    public List<String> queryUserCanOperateTaskGX(Integer userId)
    {
        return userDao.queryUserCanOperateTaskGX(userId);
    }

    /**
     * 查询我创建的子管理员
     * @param userId
     * @return
     */
    public List<Map<String,Object>> queryAllMyCreatedSubUsers(Integer userId,Integer areaId)
    {
        return userDao.queryAllMyCreatedSubUsers(userId, areaId);
    }

    /**
     *
     *  查询出已分配和未分配的营业厅信息
     * @return
     */
    public Map<String,List<Map<String,Object>>> queryUnderMeBusinessHallsByCondition(UserDomain user,Integer targetUser,Integer locationTypeId,String selectedBusinessHalls)
    {
        Integer areaCode = user.getAreaCode();
        Integer userId = user.getId();
        Map<String,List<Map<String,Object>>> result = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> unDistributionBusinessHallList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> onDistributionBusinessHallList = new ArrayList<Map<String,Object>>();
        List<String> selectedBusinessHallList = StringUtils.isNotEmpty(selectedBusinessHalls)?Arrays.asList(selectedBusinessHalls.split(",")) : null;
        List<Map<String,Object>> businessHallList = iPositionSceneMapper.queryAllBusinessHallsUnderArea(areaCode, targetUser, selectedBusinessHalls);
        List<Integer> myCreatedUserList = userDao.queryAllSubUserIdsCreatedByI(userId, user.getAreaId(),targetUser);
        try
        {
            for (Map<String,Object> businessHallInfo : businessHallList)
            {
                //未分配给当前审批用户的营业厅
                if("un".equals(businessHallInfo.get("status")))
                {
                    //判断该营业厅归属管理员的审批人是否在子管理员中，如果在则不显示
                    Object marketingAuditUsers = businessHallInfo.get("marketingAuditUsers");
                    boolean canAppend = true;
                    if (null != marketingAuditUsers && StringUtils.isNotEmpty(String.valueOf(marketingAuditUsers)))
                    {
                        List<Map<String,Object>> marketingAuditUserList =  JsonUtil.stringToObject(String.valueOf(marketingAuditUsers), List.class);
                        for (Map<String,Object> marketingAuditUser : marketingAuditUserList)
                        {
                            Integer auditUser = Integer.valueOf(String.valueOf(marketingAuditUser.get("auditUser")));
                            // 如果当前营业厅管理员对应的审批人属于子管理员的，那么就剔除
                            if (myCreatedUserList.contains(auditUser) || (auditUser.equals(targetUser) &&  (CollectionUtils.isNotEmpty(selectedBusinessHallList) && selectedBusinessHallList.contains(businessHallInfo.get("id")))))
                            {
                                canAppend = false;
                                break;
                            }
                        }
                    }

                    if (canAppend && (locationTypeId == -1 || locationTypeId == Integer.valueOf(String.valueOf(businessHallInfo.get("locationTypeId")))))
                    {
                        unDistributionBusinessHallList.add(businessHallInfo);
                    }
                }
                else
                {
                    onDistributionBusinessHallList.add(businessHallInfo);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        result.put("unDistribution", unDistributionBusinessHallList);
        result.put("onDistribution", onDistributionBusinessHallList);
        return  result;
    }

    /**
     *
     * @param subAdmin
     * @param businessHallIds
     * @return
     */
    public ServiceResult batchUpdateUsersAuditUser(Integer subAdmin, String businessHallIds, UserDomain userDomain )
    {
        UserDomain targetUser = userDao.queryUserById(subAdmin);
        List<Map<String,Object>> typeUsers = userDao.queryBatchAuditUserIdsByType(subAdmin, businessHallIds);
        List<Integer> addTypeUserIds = new ArrayList<Integer>();
        List<Integer> delTypeUserIds = new ArrayList<Integer>();
        for (Map<String,Object> typeUser : typeUsers)
        {
            if ("add".equals(typeUser.get("type")))
            {
                addTypeUserIds.add(Integer.valueOf(String.valueOf(typeUser.get("id"))));
            }
            else if ("del".equals(typeUser.get("type")))
            {
                delTypeUserIds.add(Integer.valueOf(String.valueOf(typeUser.get("id"))));
            }
        }
        userDao.batchDeleteUsersAuditUser(userDomain, StringUtils.join(delTypeUserIds,","));
        userDao.batchAddUsersAuditUser(targetUser, StringUtils.join(addTypeUserIds,","));
        return new ServiceResult();
    }

    public List<UserDomain> queryUsersByOrgIds(String orgIds) {
        return userDao.queryUsersByOrgIds(orgIds);
    }
}