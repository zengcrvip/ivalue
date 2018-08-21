package com.axon.market.core.service.ikeeper;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ikeeper.KeeperAppResponseCodeEnum;
import com.axon.market.common.domain.ikeeper.KeepWelfareRecordCusDomain;
import com.axon.market.common.domain.ikeeper.KeepWelfareRecordDomain;
import com.axon.market.common.domain.ikeeper.KeeperUserDomain;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperProductDomain;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperWelfareDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperWelfareMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperWelfareRecordCusMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperWelfareRecordMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Created by Zhuwen on 2017/8/21.
 */
@Component("keeperWelfareService")
public class KeeperWelfareService
{
    private static final Logger LOG = Logger.getLogger(KeeperWelfareService.class);
    private static final HttpUtil HTTP_UTIL = HttpUtil.getInstance();

    @Autowired
    @Qualifier("keeperWelfareDao")
    private IKeeperWelfareMapper keeperWelfareDao;

    @Autowired
    @Qualifier("keeperTaskDao")
    private IKeeperTaskMapper keeperTaskDao;

    @Autowired
    @Qualifier("keeperWelfareRecordDao")
    private IKeeperWelfareRecordMapper keeperWelfareRecordDao;

    @Autowired
    @Qualifier("keeperWelfareRecordCusDao")
    private IKeeperWelfareRecordCusMapper keeperWelfareRecordCusDao;

    @Autowired
    @Qualifier("keeperUserService")
    private KeeperUserService keeperUserService;

    @Autowired
    @Qualifier("keeperTaskService")
    private KeeperTaskService keeperTaskService;

    @Autowired
    @Qualifier("keeperProductService")
    private KeeperProductService keeperProductService;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    /**
     * 查询福利数据
     *
     * @param welfareName
     * @param limit
     * @param offset
     * @return
     */
    public Table<ShopKeeperWelfareDomain> queryWelfare(Integer welfareId, String welfareName, Integer typeId, Integer limit, Integer offset, Integer areaId, String netType)
    {
        try
        {
            int count = keeperWelfareDao.queryWelfareCount(welfareName, welfareId, typeId, areaId, netType);
            List<ShopKeeperWelfareDomain> list = keeperWelfareDao.queryWelfare(welfareName, welfareId, typeId, limit, offset, areaId, netType);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("查询福利数据异常：", e);
            return new Table<>();
        }
    }

    /**
     * 查询福利数据
     *
     * @param welfareId
     * @return
     */
    public ShopKeeperWelfareDomain queryWelfareById(Integer welfareId)
    {
        try
        {
            List<ShopKeeperWelfareDomain> list = keeperWelfareDao.queryWelfare(null, welfareId, null, null, null,null, null);
            if (list == null || list.isEmpty())
            {
                return null;
            }
            return list.get(0);
        }
        catch (Exception e)
        {
            LOG.error("查询福利数据异常：", e);
            return null;
        }
    }

    /**
     * 新增福利数据
     *
     * @param shopKeeperWelfareDomain
     * @return
     */
    public ServiceResult addWelfare(ShopKeeperWelfareDomain shopKeeperWelfareDomain)
    {
        try
        {
            keeperWelfareDao.addWelfare(shopKeeperWelfareDomain);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "新增福利失败");
        }
        return new ServiceResult(0, "新增福利成功");
    }

    public ServiceResult updateWelfare(ShopKeeperWelfareDomain shopKeeperWelfareDomain)
    {
        try
        {
            //在任务列表中
            if (keeperTaskDao.queryTaskCountByWelfareId(String.valueOf(shopKeeperWelfareDomain.getWelfareId())) > 0)
            {
                return new ServiceResult(-1, "该福利已被使用，不允许修改");
            }
            keeperWelfareDao.updateWelfare(shopKeeperWelfareDomain);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "修改福利失败");
        }
        return new ServiceResult(0, "修改福利成功");
    }

    public ServiceResult deleteWelfare(Integer welfareId)
    {
        try
        {
            //在任务列表中
            if (keeperTaskDao.queryTaskCountByWelfareId(String.valueOf(welfareId)) > 0)
            {
                return new ServiceResult(-1, "该福利已被使用，不允许删除");
            }

            keeperWelfareDao.deleteWelfare(welfareId);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "删除福利失败");
        }
        return new ServiceResult(0, "删除福利成功");
    }

    public ResultVo giveCustWelfare(String welfareId, Integer userId, String telephone, boolean autograph, boolean notify)
    {
        try
        {
            //号码是否属于该用户的维系号码
            if (!keeperUserService.checkCustomerBeMaintenanceByKeeperUser(userId, telephone))
            {
                return KeeperAppResponseCodeEnum.INSUFFICIENT_AUTHORITY.getValue("输入的号码您无权限进行福利赠送!");
            }

            List<ShopKeeperProductDomain> productList = keeperProductService.queryProductListByCompositId(Integer.valueOf(welfareId));
            if (productList == null || productList.isEmpty())
            {
                return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("福利无产品数据，请联系系统管理员!");
            }

            //福利赠送提示短信在福利信息里
            List<ShopKeeperWelfareDomain> list = new ArrayList();
            list = keeperWelfareDao.queryWelfare(null, Integer.valueOf(welfareId), null, null, null,null,null);
            if (list == null || list.size() == 0)
            {
                return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("福利数据异常，请联系系统管理员!");
            }
            ShopKeeperWelfareDomain welfareDomain = list.get(0);
            KeepWelfareRecordDomain recordDomain = new KeepWelfareRecordDomain();
            recordDomain.setWelfareType(1);//1表示直接赠送
            recordDomain.setTaskid(0);//直接赠送直接写死0
            recordDomain.setWelfareIds(welfareId);
            recordDomain.setEffTime(TimeUtil.formatDate(new Date()));
            recordDomain.setExpTime(TimeUtil.formatDate(new Date()));
            recordDomain.setUserId(userId);

            //调用第三方接口进行产品订购
            Map<String, String> body = new HashMap<String, String>();
            body.put("mob", telephone);
            body.put("flag", "O");
            body.put("effectMode", "NOW");
            body.put("reserve", "");
            body.put("remark", "");

            String partnerId = "sk2u9a";
            String timeStamp = TimeUtil.formatDateToYMDHMS(new Date());
            String serviceName = "orderproduct";
            String encryptKey = "d@#hQ9mP";
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("partnerId", partnerId);
            para.put("timeStamp", timeStamp);
            para.put("serviceName", serviceName);
            para.put("encryptStr", MD5Util.getMD5Code(partnerId + timeStamp + serviceName + encryptKey));

            Integer cusstate = 1;
            Integer recordState = 3;
            String desc = "";
            List<KeepWelfareRecordCusDomain> recordCusList = new ArrayList<>();
            for (int i = 0; i < productList.size(); i++)
            {
                String productCode = productList.get(i).getProductCode();
                body.put("productId", productCode);
                para.put("param", body);
                String jsonStr = JsonUtil.objectToString(para);
                String result = HTTP_UTIL.sendHttpPostByJson(interfaceBean.getSceneOrderUrl(), jsonStr);
                Map<String, Object> map = JsonUtil.stringToObject(result, Map.class);

                KeepWelfareRecordCusDomain recordCusDomain = new KeepWelfareRecordCusDomain();
                recordCusDomain.setProductCode(productCode);
                recordCusDomain.setPhone(axonEncrypt.encrypt(telephone));
                recordCusDomain.setOrderTime(new Date());
                recordCusDomain.setSmsState(1);//直接赠送写死为1，表示已处理

                if (map.get("ErrorCode") == null){
                    cusstate = 2;
                    recordState = 5;
                    desc = "fail to call interface:" + interfaceBean.getSceneOrderUrl();
                    recordCusDomain.setState(cusstate);//根据订购返回结果确认是成功还是失败
                    recordCusDomain.setOrderDesc(desc);//根据订购返回结果给描述
                    recordCusList.add(recordCusDomain);
                    break;
                }else{
                    if (!map.get("ErrorCode").equals("00000")){
                        cusstate = 2;
                        recordState = 5;
                        if (map.get("Result") != null)
                        {
                            desc = map.get("ErrorCode") + ":" + JsonUtil.objectToString(map.get("Result"));
                        }
                        recordCusDomain.setState(cusstate);//根据订购返回结果确认是成功还是失败
                        recordCusDomain.setOrderDesc(desc);//根据订购返回结果给描述
                        recordCusList.add(recordCusDomain);
                        break;
                    }
                }
                recordCusList.add(recordCusDomain);
            }

            recordDomain.setState(recordState);//3表示执行成功 5表示失败
            keeperWelfareRecordDao.addWelfareRecord(recordDomain);

            //插入主表后将主表生成的id更新到副表
            for (KeepWelfareRecordCusDomain domain:recordCusList){
                domain.setRecordId(recordDomain.getRecordId());
            }
            keeperWelfareRecordCusDao.addWelfareRecordCus(recordCusList);

            if (recordState != 3)
            {
                return KeeperAppResponseCodeEnum.ORDER_ERR.getValue("订购失败，请联系系统管理员!");
            }

            if (notify)
            {
                // 发送短信
                reminder(telephone, autograph, userId, welfareDomain.getSmsContent());
            }
        }
        catch (Exception e)
        {
            return KeeperAppResponseCodeEnum.OTHER_ERR.getValue("赠送失败，请联系系统管理员!");
        }
        return new ResultVo();
    }

    public void reminder(String telephone, boolean autograph, Integer userId, String content)
    {
        String message = null;
        try
        {
            if (autograph)
            {
                KeeperUserDomain userdomain = keeperUserService.queryKeeperUserDetail(userId);
                message = content + "[" + userdomain.getSmsSignature() + "]";
            }
            else
            {
                message = content;
            }
            sendSmsService.sendReminderNoticeSms(telephone, message);
        }
        catch (Exception e)
        {
            LOG.error("Keeper reminder return error");
        }
    }

    public Table<Map<String, Object>> queryKeeperPhonelist(Integer welfareId, Integer userId)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        try
        {
            list = keeperWelfareRecordDao.queryPhoneListByWelfareId(welfareId, userId);
            //解密
            if (list !=null && !list.isEmpty()){
                count = list.size();
                for (Map<String, Object> map:list){
                    String phone = axonEncrypt.decryptWithoutCountrycode((String)map.get("phone"));
                    map.put("phone", phone);
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return new Table(list, count);
    }

    public List<Map<String, Object>> queryWelfareForApp()
    {
        List<Map<String, Object>> taskTypeList = keeperTaskService.queryKeeperWelfareType();
        List<ShopKeeperWelfareDomain> welfareList = keeperWelfareDao.queryWelfareForApp();
        List<Map<String, Object>> resultList = new ArrayList<>();

        List<String> typeList = new ArrayList<String>();

        for (ShopKeeperWelfareDomain domain : welfareList)
        {
            String typeId = String.valueOf(domain.getTypeId());
            if (!typeList.contains(typeId))
            {
                typeList.add(typeId);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("parent", domain.getTypeId());
            map.put("typeId", domain.getTypeId());
            map.put("typeName", domain.getTypeName());
            map.put("welfareId", domain.getWelfareId());
            map.put("welfareName", domain.getWelfareName());
            map.put("smsContent", domain.getSmsContent());
            map.put("orgIds", domain.getOrgIds());
            map.put("orgNames", domain.getOrgNames());
            map.put("areaId", domain.getAreaId());
            map.put("areaName", domain.getAreaNames());
            map.put("comments", domain.getComments());
            map.put("state", domain.getState());
            map.put("productCodes", domain.getProductCodes());
            map.put("productNames", domain.getProductNames());
            map.put("createUserId", domain.getCreateUserId());
            map.put("createTime", domain.getCreateTime());
            resultList.add(map);
        }
        for (Map<String, Object> item : taskTypeList)
        {
            if (typeList.contains(String.valueOf(item.get("typeId"))))
            {
                Map<String, Object> map = new HashMap<>();
                map.put("typeId", item.get("typeId"));
                map.put("typeName", item.get("typeName"));
                map.put("welfareId", "");
                map.put("welfareName", "");
                map.put("parent", 0);
                resultList.add(map);
            }
        }
        return resultList;
    }

    /**
     * 根据任务ID批量新增福利待订购记录
     * @param taskId 任务ID
     * @param welfareIds 福利ID
     * @param executeUserId 执行人ID
     *  @param effTime 生效时间
     *  @param effTime 失效时间
     *  @param phoneSet 待订购的号码清单
     * add by zengcr
     */
    @Transactional
    public void addWelfareRecordByTask(Integer taskId,String welfareIds,Integer executeUserId,String effTime,String expTime,Set<String> phoneSet) throws Exception
    {
        KeepWelfareRecordDomain recordDomain = new KeepWelfareRecordDomain();
        recordDomain.setWelfareType(2);//2表示基于任务赠送
        recordDomain.setTaskid(taskId);
        recordDomain.setWelfareIds(welfareIds);
        recordDomain.setEffTime(effTime);
        recordDomain.setExpTime(expTime);
        recordDomain.setUserId(executeUserId);
        recordDomain.setState(1);//1表示待处理
        keeperWelfareRecordDao.addWelfareRecord(recordDomain);
        //根据当前任务的福利ID找出所有的产品
        List<KeepWelfareRecordCusDomain> keepWelfareRecordCusDomainList = new ArrayList<KeepWelfareRecordCusDomain>();
        List<Map<String,String>> productList = keeperWelfareRecordDao.queryProductByWelfareId(welfareIds);
        //新增掌柜福利赠送客户详情
        for(Map<String,String> productMap : productList){
            for(String phone : phoneSet){
                KeepWelfareRecordCusDomain keepWelfareRecordCusDomain = new KeepWelfareRecordCusDomain();
                keepWelfareRecordCusDomain.setRecordId(recordDomain.getRecordId());
                keepWelfareRecordCusDomain.setProductCode(productMap.get("productCode"));
                keepWelfareRecordCusDomain.setNetType(productMap.get("netType"));
                keepWelfareRecordCusDomain.setPhone(phone);
                keepWelfareRecordCusDomain.setState(0);   //未处理
                keepWelfareRecordCusDomain.setSmsState(0); //待订购
                keepWelfareRecordCusDomainList.add(keepWelfareRecordCusDomain);
            }
        }
        keeperWelfareRecordCusDao.addWelfareRecordCus(keepWelfareRecordCusDomainList);
    }


    /**
     * 根据日期及状态查询待处理的福利记录
     * @param date
     * @return
     */
    public List<Map<String,Object>> queryRecordsByState(String date){
        return keeperWelfareRecordDao.queryRecordsByState(date);
    }

    /**
     * 根据记录ID查询送福利产品成功的用户
     * @param recordId
     * @return
     */
    public Set<String> queryCustPhoneListByRecordId(String recordId){
        return keeperWelfareRecordCusDao.queryCustPhoneListByRecordId(recordId);
    }

    /**
     * 根据记录ID修改福利记录的处理状态
     * @param recordId
     */
    public void updateRecordById( String recordId){
         keeperWelfareRecordDao.updateRecordById(recordId);
    }


}
