package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.OperationEnum;
import com.axon.market.common.constant.iscene.UrlBlacklistEnum;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.dao.mapper.iscene.IUrlBlacklistMapper;
import com.axon.market.common.bean.GroupJson;
import com.axon.market.common.domain.iscene.BannedHostsDomain;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hale on 2016/12/5.
 */
@Component("urlBlacklistService")
public class UrlBlacklistService
{
    @Autowired
    @Qualifier("urlBlacklistDao")
    private IUrlBlacklistMapper urlBlacklistDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    private static final Logger LOG = Logger.getLogger(UrlBlacklistService.class.getName());

    /**
     * 查询网址黑名单列表
     *
     * @param url    网址
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table queryUrlBlacklist(String url, Integer offset, Integer limit)
    {
        try
        {
            List<BannedHostsDomain> list = urlBlacklistDao.queryUrlBlacklist(url, offset, limit);
            Integer count = urlBlacklistDao.queryUrlBlacklistCount(url);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            LOG.error("queryUrlBlacklist error:" + ex.getMessage());
            return new Table();
        }
    }

    /**
     * 新增、删除网址黑名单
     *
     * @param type 1:删除 2:修改
     * @param url  网址
     * @return Operation 增删改统一返回对象
     */
    public Operation addOrDeleteUrlBlacklist(String type, String url, Integer userId)
    {
        return OperationEnum.EDIT.getIndex() == Integer.parseInt(type) ? addUrlBlacklist(url, userId) : deleteUrlBlacklist(url);
    }

    /**
     * 新增网址黑名单
     *
     * @param url 网址
     * @return Operation 增删改统一返回对象
     */
    private Operation addUrlBlacklist(String url, Integer userId)
    {
        if (!sendMessage(OperationEnum.EDIT.getIndex()))
        {
            return new Operation(false, "接口异常");
        }

        try
        {
            BannedHostsDomain model = new BannedHostsDomain();
            model.setHost(url);
            model.setTaskId(0);
            model.setBannedType(0);
            model.setBannedBy(userId);

            Boolean result = urlBlacklistDao.addUrlBlacklist(model) == 1;
            String message = result ? "新增成功" : "新增失败";
            return new Operation(result, message);
        }
        catch (Exception ex)
        {
            LOG.error("addUrlBlacklist error:" + ex.getMessage());
            return new Operation();
        }
    }

    /**
     * 删除网址黑名单
     *
     * @param url 网址
     * @return Operation 增删改统一返回对象
     */
    private Operation deleteUrlBlacklist(String url)
    {
        if (!sendMessage(OperationEnum.DELETE.getIndex()))
        {
            return new Operation(false, "接口异常");
        }

        try
        {
            Boolean result = urlBlacklistDao.deleteUrlBlacklist(url) == 1;
            String msg = result ? "删除成功" : "删除失败";
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("deleteUrlBlacklist error:" + ex.getMessage());
            return new Operation();
        }
    }

    /**
     * 发送消息
     *
     * @param type
     * @return
     */
    private Boolean sendMessage(Integer type)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("operation", type);

        GroupJson json = post(interfaceBean.getUrlBlacklistUrl(), map);
        if (0 != json.getResultCode())
        {
            return false;
        }
        return true;
    }

    /**
     * post发送消息
     *
     * @param url  网址
     * @param maps 接口参数对象
     * @return GroupJson 接口统一返回对象
     */
    private GroupJson post(String url, Map<String, Integer> maps)
    {
        try
        {
            Map<String, String> sendMaps = new HashMap<String, String>();
            sendMaps.put("message", JsonUtil.objectToString(maps));

            String result = new HttpUtil().sendHttpPost(url, sendMaps);
            GroupJson json = JsonUtil.stringToObject(result, GroupJson.class);

            return json == null ? new GroupJson() : json;
        }
        catch (Exception ex)
        {
            LOG.error("urlblacklist post error:", ex);
            return new GroupJson();
        }
    }
}
