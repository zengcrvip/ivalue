package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.iscene.PushConfigDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.PushConfigEnum;
import com.axon.market.common.constant.iscene.UsedEnum;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.dao.mapper.iscene.IPushConfigMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2016/12/7.
 */
@Component("pushConfigService")
public class PushConfigService
{
    @Autowired
    @Qualifier("pushConfigDao")
    private IPushConfigMapper pushConfigDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;
    private static final Logger LOG = Logger.getLogger(PushConfigService.class.getName());

    /**
     * 查询推送配置列表
     *
     * @param name 推送类型名称
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table queryPushConfig(String name, int offset, int limit)
    {
        List<PushConfigDomain> list = pushConfigDao.queryPushConfig(name, offset, limit);
        Integer count = pushConfigDao.queryPushConfigCount(name);
        return new Table(list, count);
    }

    /**
     * 获取流量包类型 下拉列表
     * @return
     */
    public Operation getSelectType()
    {
        StringBuilder sb = new StringBuilder();
        for( PushConfigEnum pushConfigEnum : PushConfigEnum.values()){
            sb.append("<option value='" + pushConfigEnum.getIndex() + "'>" + pushConfigEnum.getName() + "</option>");
        }
        return new Operation(true,sb.toString());
    }

    /**
     * 获取是否启用 下拉列表
     * @return
     */
    public Operation getIsUsed()
    {
        StringBuilder sb = new StringBuilder();
        for( UsedEnum usedEnum : UsedEnum.values()){
            sb.append("<option value='" + usedEnum.getIndex() + "'>" + usedEnum.getName() + "</option>");
        }
        return new Operation(true,sb.toString());
    }

    /**
     * 新增/修改
     * @param paras
     * @param userDomain
     * @return
     */
    public Operation addOrEditPushConfig(Map<String, Object> paras, UserDomain userDomain)
    {
        String id = String.valueOf(paras.get("id"));
        String type = String.valueOf(paras.get("type"));
        String tId = String.valueOf(paras.get("tId"));
        String name = String.valueOf(paras.get("name"));
        String sort = String.valueOf(paras.get("sort"));
        String isUsed= String.valueOf(paras.get("isUsed"));
        PushConfigDomain model = new PushConfigDomain();
        model.setType("".equals(type) ? 0 : Integer.parseInt(type));
        model.settId(Integer.parseInt(tId));
        model.setName(name);
        model.setSort(Integer.parseInt(sort));
        model.setIsUsed("".equals(isUsed) ? 0 : Integer.parseInt(isUsed));
        model.setEditUserId(userDomain.getId());
        model.setEditUserName(userDomain.getName());
        model.setProvinceId(1);
        int pushId = ("".equals(id) ? 0 : Integer.parseInt(id));
        model.setId(pushId);

        boolean result;
        String msg;
        if (pushId > 0) //修改
        {
            result = pushConfigDao.EditPushConfig(model) == 1;
             msg = result ? "修改成功" : "修改失败";
        }
        else
        {
            model.setCreateId(userDomain.getId());
            result = pushConfigDao.addPushConfig(model) == 1;
            msg = result ? "新增成功" : "新增失败";
        }

        sendMessage(interfaceBean.getScencesUrl());

        return new Operation(result, msg);
    }

    /**
     * 发送消息
     *
     * @param url
     */
    public void sendMessage(String url)
    {

        try
        {
            HttpUtil http = HttpUtil.getInstance();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("authorize", "gloomysw@axon2014");

            String result = http.sendHttpGetByHeader(url + "unitUpdateTaskPushConfig", headers);
            //ResultModel json = JsonUtil.stringToObject(result, ResultModel.class);


        }
        catch (Exception ex)
        {
            LOG.error("sendMessage error:", ex);
        }
    }
    /**
     * 删除
     * @param paras
     * @return
     */
    public Operation deletePushConfig(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        boolean result = pushConfigDao.deletePushConfig(delId) == 1;
        sendMessage(interfaceBean.getScencesUrl());
        String msg = result ? "删除成功" : "删除失败";
        return new Operation(result, msg);
    }

}
