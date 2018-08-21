package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.domain.iscene.*;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.NetWorkEnum;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.ValidateUtil;
import com.axon.market.dao.mapper.iscene.IPushContentMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2016/12/9.
 */
@Component("pushContentService")
public class PushContentService
{
    @Autowired
    @Qualifier("pushContentDao")
    private IPushContentMapper pushContentDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    private static final Logger LOG = Logger.getLogger(PushConfigService.class.getName());

    /**
     * 查询推送内容列表
     *
     * @param name   任务名称
     * @param kind   类型id
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return
     */
    public Table getPushList(String name, String kind, int offset, int limit)
    {
        try
        {
            List<PushContentDomain> list = pushContentDao.getPushList(kind, name, offset, limit);
            Integer count = pushContentDao.getPushListCount(kind, name);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("getPushList error:" + e.getMessage());
            return new Table();
        }
    }

    /**
     * 获取任务
     *
     * @param offset
     * @param limit
     * @return
     */
    public Table getTaskList(int offset, int limit)
    {
        List<TaskDomain> list = pushContentDao.getTaskList(offset, limit);
        Integer count = pushContentDao.getTaskListCount();
        return new Table(list, count);
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param userDomain
     * @return
     */
    public Operation addOrEditPush(Map<String, Object> paras, UserDomain userDomain)
    {
        String id = String.valueOf(paras.get("id"));
        String taskId = String.valueOf(paras.get("taskId"));
        String title = String.valueOf(paras.get("title"));
        String icon = String.valueOf(paras.get("icon"));
        String content = String.valueOf(paras.get("content"));
        String href = String.valueOf(paras.get("href"));
        String remark = String.valueOf(paras.get("remark"));
        String orderKey = String.valueOf(paras.get("orderKey"));
        String netWork = String.valueOf(paras.get("netWork"));
        String sort = String.valueOf(paras.get("sort"));
        String kind = String.valueOf(paras.get("kind"));
        String kindContent = "";
        String type = String.valueOf(paras.get("type"));
        String isUsed = String.valueOf(paras.get("isUsed"));

        String pictureByte = String.valueOf(paras.get("pictureByte"));

        PushDomain model = new PushDomain();
        int pushId = "".equals(id) ? 0 : Integer.parseInt(id);
        model.setId(pushId);
        model.setTaskId("".equals(taskId) ? 0 : Integer.parseInt(taskId));
        model.setTitle(title);
        model.setIcon("".equals(icon) ? 0 : Integer.parseInt(icon));
        model.setContent(content);
        model.setHref(href);
        model.setRemark(remark);
        model.setOrderKey(orderKey);
        model.setNetWork("".equals(netWork) ? 0 : Integer.parseInt(netWork));
        model.setSort("".equals(sort) ? 0 : Integer.parseInt(sort));
        model.setKind("".equals(kind) ? 0 : Integer.parseInt(kind));
        model.setKindContent(kindContent);
        model.setType("".equals(type) ? 0 : Integer.parseInt(type));
        model.setIsUsed("".equals(isUsed) ? 0 : Integer.parseInt(isUsed));
        model.setImgByte(pictureByte);
        model.setImgUrl(pictureByte);
        model.setEditUserId(userDomain.getId());
        model.setEditUserName(userDomain.getName());
        model.setProvinceId(1);

        boolean result;
        String msg;
        if (pushId > 0) //修改
        {
            result = pushContentDao.editPushContent(model) == 1;
            msg = result ? "修改成功" : "修改失败";
        }
        else
        {
            try
            {
                model.setCreateId(userDomain.getId());
                result = pushContentDao.addPushContent(model) == 1;
                msg = result ? "新增成功" : "新增失败";
            }
            catch (Exception ex)
            {
                result = false;
                String s = ex.getMessage();
                msg = result ? "新增成功" : "新增失败";
            }
        }

        //调用接口
        sendMessage(interfaceBean.getScencesUrl());

        return new Operation(result, msg);
    }

    public Operation deletePush(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = "".equals(id) ? 0 : Integer.parseInt(id);
        boolean result = pushContentDao.deletePush(delId) == 1;
        //调用接口
        sendMessage(interfaceBean.getScencesUrl());
        String msg = result ? "删除成功" : "删除失败";
        return new Operation(result, msg);
    }

    /**
     * 获取推送配置 下拉列表
     *
     * @return
     */
    public Operation getSelectPushConfig()
    {
        StringBuilder sb = new StringBuilder();
        List<PushConfigDomain> list = pushContentDao.getSelectPushConfig();
        for (PushConfigDomain model : list)
        {
            sb.append("<option value='" + model.getId() + "'>" + model.getName() + "</option>");
        }
        return new Operation(true, sb.toString());
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

            String result = http.sendHttpGetByHeader(url + "unitUpdateTaskCarousel", headers);
            //ResultModel json = JsonUtil.stringToObject(result, ResultModel.class);


        }
        catch (Exception ex)
        {
            LOG.error("sendMessage error:", ex);
        }
    }
    /**
     * 删除
     *
     * @return
     */
    public Operation getSelectNetWork()
    {
        StringBuilder sb = new StringBuilder();
        for (NetWorkEnum usedEnum : NetWorkEnum.values())
        {
            sb.append("<option value='" + usedEnum.getIndex() + "'>" + usedEnum.getName() + "</option>");
        }
        return new Operation(true, sb.toString());
    }

    /**
     * 获取推送内容
     *
     * @param paras
     * @return
     */
    public Table getContent(Map<String, Object> paras)
    {
        try
        {
            String id = String.valueOf(paras.get("id"));
            int pushId = "".equals(id) ? 0 : Integer.parseInt(id);
            List<PushContentDomain> list = pushContentDao.getContent(pushId);
            return new Table(list, 1);
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            return new Table(null, 1);
        }
    }
}
