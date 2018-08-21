package com.axon.market.core.service.iscene;

import com.axon.market.common.constant.iscene.DeleteEnum;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.iscene.PicturesDomain;
import com.axon.market.common.domain.iscene.TempleTypeDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.dao.mapper.iscene.IImageMgrMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hale on 2016/12/13.
 */
@Component("imageMgrService")
public class ImageMgrService
{
    @Autowired
    @Qualifier("imageMgrDao")
    private IImageMgrMapper imageMgrDao;

    private static final Logger LOG = Logger.getLogger(ImageMgrService.class.getName());

    /**
     * 查询图片列表
     *
     * @param name 图片名称
     * @param offset 从第几页开始
     * @param limit  获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table queryImageMgrList(String name, int offset, int limit)
    {
        try
        {
            List<PicturesDomain> list = imageMgrDao.queryImageMgrList(name, offset, limit);
            int count = imageMgrDao.queryImageMgrListCount(name);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            LOG.error("queryImageMgrList error:" + ex.getMessage());
            return new Table();
        }
    }

    /**
     * 获取类型下拉 列表
     *
     * @return
     */
    public Operation getTempleType()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            List<TempleTypeDomain> list = imageMgrDao.queryTempleTypeList();
            for (TempleTypeDomain item : list)
            {
                sb.append("<option value='" + item.getId() + "' data-Multi='" + item.getMultiPicture() + "'>" + item.getTypeName() + "</option>");
            }
            return new Operation(true, sb.toString());
        }
        catch (Exception ex)
        {
            LOG.error("getShieldTask error:" + ex.getMessage());
            return new Operation(false, "");
        }
    }

    /**
     * 新增图片
     *
     * @param tempId
     * @param name
     * @param pictureByte
     * @return
     */
    public Operation addImage(String tempId, String name, String pictureByte, UserDomain userDomain)
    {
        try
        {
            PicturesDomain model = new PicturesDomain();
            model.setTempId(StringUtils.isEmpty(tempId) ? 0 : Integer.parseInt(tempId));
            model.setTitle(name);
            model.setPictureByte(pictureByte);
            model.setIsDelete(DeleteEnum.NOTDELETE.getIndex());
            model.setUrl(pictureByte);
            model.setThumbnail(pictureByte);
            model.setEditUserId(userDomain.getId());
            model.setEditUserName(userDomain.getName());
            model.setCreateId(userDomain.getId());
            model.setProvinceId(1);

            boolean result = imageMgrDao.addImage(model) == 1;
            String msg = result ? "新增成功" : "新增失败";

            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("addImage error:" + ex.getMessage());
            return new Operation();
        }
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    public Operation deleteImage(int id, String pictureByte, UserDomain userDomain)
    {
        try
        {
            if (id <= 0)
            {
                return new Operation();
            }

            if (imageMgrDao.queryScenesCount(pictureByte) > 0)
            {
                return new Operation(false, "该图片在某一个场景中存在，不能删除");
            }

            int editUserId = userDomain.getId();
            String editUserName = userDomain.getName();

            boolean result = imageMgrDao.deleteImage(id, DeleteEnum.DELETED.getIndex(), editUserId, editUserName) == 1 ? true : false;
            String msg = result ? "删除成功" : "删除失败";
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("deleteImage error:" + ex.getMessage());
            return new Operation();
        }
    }
}
