package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.*;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.iscene.PicturesDomain;
import com.axon.market.common.domain.iscene.ScenesDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.dao.mapper.iscene.ISceneManageMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2016/12/22.
 */
@Component("sceneManageService")
public class SceneManageService
{
    private static final Logger LOG = Logger.getLogger(SceneManageService.class.getName());
    @Autowired
    @Qualifier("sceneManageDao")
    private ISceneManageMapper sceneManageDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 查询场景管理列表
     *
     * @param client 场景名称
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table getSceneList(String client, int offset, int limit)
    {
        try
        {
            List<ScenesDomain> list = sceneManageDao.getSceneList(client, offset, limit);
            Integer count = sceneManageDao.getSceneListCount(client);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            return new Table(null, 0);
        }
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param userDomain
     * @return
     */
    public Operation addOrEditScene(Map<String, Object> paras, UserDomain userDomain)
    {
        String id = String.valueOf(paras.get("id"));
        String client = String.valueOf(paras.get("client"));
        String backUrl = String.valueOf(paras.get("backUrl"));
        String showTrackUrl = String.valueOf(paras.get("showTrackUrl"));
        String clickTrackUrl = String.valueOf(paras.get("clickTrackUrl"));
        String modelId = String.valueOf(paras.get("modelId"));
        String imgUrl = String.valueOf(paras.get("imgUrl"));
        String picTitle = String.valueOf(paras.get("picTitle"));
        String multi = String.valueOf(paras.get("multi"));

        ScenesDomain model = new ScenesDomain();
        int sceneId = "".equals(id) ? 0 : Integer.parseInt(id);
        model.setId(sceneId);
        model.setClient(client);
        model.setBackUrl(backUrl);
        model.setShowTrackUrl(showTrackUrl);
        model.setClickTrackUrl(clickTrackUrl);
        model.setModelId("".equals(modelId) ? 0 : Integer.parseInt(modelId));
        model.setImgUrl(imgUrl);
        model.setPicTitle(picTitle);
        model.setMultiPicture("".equals(multi) ? 0 : Integer.parseInt(multi));

        model.setEditUserId(userDomain.getId());
        model.setEditUserName(userDomain.getName());
        model.setProvinceId(1);


        boolean result;
        String msg;

        //修改,新增场景调用接口
        sendMessage(interfaceBean.getScencesUrl());

        if (sceneId > 0) //修改
        {
            result = sceneManageDao.editScene(model) == 1;
            msg = result ? "修改成功" : "修改失败";
        }
        else
        {
            try
            {
                model.setCreateId(userDomain.getId());
                result = sceneManageDao.addScene(model) == 1;
                msg = result ? "新增成功" : "新增失败";
            }
            catch (Exception ex)
            {
                result = false;
                String s = ex.getMessage();
                msg = result ? "新增成功" : "新增失败";
            }
        }

        return new Operation(result, msg);
    }

    /**
     * 删除
     *
     * @param paras
     * @return
     */
    public Operation deleteScene(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = "".equals(id) ? 0 : Integer.parseInt(id);
        boolean result = sceneManageDao.deleteScene(delId) == 1;
        String msg = result ? "删除成功" : "删除失败";
        return new Operation(result, msg);
    }

    /**
     * 获取场景
     *
     * @param paras
     * @return
     */
    public Table getSceneContent(Map<String, Object> paras)
    {
        try
        {
            String id = String.valueOf(paras.get("id"));
            int sid = "".equals(id) ? 0 : Integer.parseInt(id);
            List<ScenesDomain> list = sceneManageDao.getSceneContent(sid);
            return new Table(list, 1);
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            return new Table(null, 1);
        }
    }

    /**
     * 获取图片列表
     *
     * @param paras
     * @return
     */
    public Table getPictureUrl(Map<String, Object> paras)
    {
        String templeId = String.valueOf(paras.get("templeId"));
        int tempId = "".equals(templeId) ? 0 : Integer.parseInt(templeId);
        List<PicturesDomain> list = sceneManageDao.getPictureUrl(tempId);
        return new Table(list, 0);
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

            String result = http.sendHttpGetByHeader(url + "unitUpdateScene", headers);
            //ResultModel json = JsonUtil.stringToObject(result, ResultModel.class);


        }
        catch (Exception ex)
        {
            LOG.error("sendMessage error:", ex);
        }
    }
}
