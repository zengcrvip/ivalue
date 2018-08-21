package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.ImageMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by hale on 2016/12/13.
 */

@Controller("imageMgrController")
public class ImageMgrController
{
    @Autowired
    @Qualifier("imageMgrService")
    private ImageMgrService imageMgrService;

    /**
     * 查询图片
     *
     * @param name  图片名称
     * @param start  从第几页开始
     * @param length 获取几条数据
     * @return Table 列表统一返回对象
     */
    @RequestMapping(value = "queryImageMgrList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryImageMgrList(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length)
    {
        Table table = imageMgrService.queryImageMgrList(SearchConditionUtil.optimizeCondition(name), start, length);
        return table;
    }

    /**
     * 获取模型下拉框
     * @return
     */
    @RequestMapping(value = "getSelectTempleType.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getTempleType()
    {
        return imageMgrService.getTempleType();
    }

    /**
     * 新增上传图片
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "addImage.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addImage(@RequestBody Map<String, String> paras, HttpSession session)
    {
        String tempId = String.valueOf(paras.get("tempId"));
        String name = String.valueOf(paras.get("name"));
        String pictureByte = String.valueOf(paras.get("pictureByte"));

        return imageMgrService.addImage(tempId, name, pictureByte, UserUtils.getLoginUser(session));
    }

    /**
     * 删除图片
     *
     * @param paras   id 图片id pictureByte 图片地址
     * @param session HttpSession对象
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteImage.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteImage(@RequestBody Map<String, String> paras, HttpSession session)
    {
        int id = Integer.parseInt(String.valueOf(paras.get("id")));
        String pictureByte = String.valueOf(paras.get("pictureByte"));

        return imageMgrService.deleteImage(id, pictureByte, UserUtils.getLoginUser(session));
    }

}
