package com.axon.market.core.service.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.iscene.TempleTypeDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.PictureTypeEnum;
import com.axon.market.dao.mapper.iscene.ITempleTypeMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2017/1/4.
 */
@Component("templeTypeService")
public class TempleTypeService
{
    private static final Logger LOG = Logger.getLogger(TempleTypeService.class.getName());

    @Autowired
    @Qualifier("templeTypeDao")
    private ITempleTypeMapper templeTypeDao;

    /**
     * 查询模型列表
     *
     * @param name   模型名称
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return
     */
    public Table queryTempletype(String name, int offset, int limit)
    {
        List<TempleTypeDomain> list = templeTypeDao.queryTempleType(name, offset, limit);
        Integer count = templeTypeDao.queryTempleTypeCount(name);
        return new Table(list, count);
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param userDomain
     * @return
     */
    public Operation addOrEditTempleType(Map<String, Object> paras, UserDomain userDomain)
    {
        String typeName = String.valueOf(paras.get("typeName"));
        String typeJS = String.valueOf(paras.get("typeJS"));
        String multiPicture = String.valueOf(paras.get("multiPicture"));
        String id = String.valueOf(paras.get("id"));
        TempleTypeDomain model = new TempleTypeDomain();
        model.setTypeName(typeName);
        model.setTypeJS(typeJS);
        model.setMultiPicture("".equals(multiPicture) ? 0 : Integer.parseInt(multiPicture));
        model.setEditUserId(userDomain.getId());
        model.setEditUserName(userDomain.getName());
        int pushId = ("".equals(id) ? 0 : Integer.parseInt(id));
        model.setId(pushId);

        boolean result;
        String msg;
        if (pushId > 0) //修改
        {
            result = templeTypeDao.editTempleType(model) == 1;
            msg = result ? "修改成功" : "修改失败";
        }
        else
        {
            result = templeTypeDao.addTempleType(model) == 1;
            msg = result ? "新增成功" : "新增失败";
        }

        return new Operation(result, msg);
    }

    /**
     * 删除
     *
     * @param paras
     * @return
     */
    public Operation deleteTempleType(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        boolean result = templeTypeDao.deleteTempleType(delId) == 1;
        String msg = result ? "删除成功" : "删除失败";
        return new Operation(result, msg);
    }

    /**
     * 获取模版类型
     *
     * @return
     */
    public Operation getMultilType()
    {
        StringBuilder sb = new StringBuilder();
        for (PictureTypeEnum usedEnum : PictureTypeEnum.values())
        {
            sb.append("<option value='").append(usedEnum.getIndex()).append("'>").append(usedEnum.getName()).append("</option>");
        }
        return new Operation(true, sb.toString());
    }
}
