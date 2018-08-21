package com.axon.market.core.service.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.IdAndNameDomain;
import com.axon.market.common.domain.itag.DimensionDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.itag.IDimensionMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/6/4.
 */
@Component("dimensionService")
public class DimensionService
{
    private static final Logger LOG = Logger.getLogger(DimensionService.class.getName());

    @Autowired
    @Qualifier("dimensionDao")
    private IDimensionMapper dimensionDao;

    /**
     * @param offset
     * @param maxRecord
     * @return
     */
    public Table queryDimensionsByPage(Integer offset, Integer maxRecord)
    {
        try
        {
            Integer count = dimensionDao.queryAllDimensionCounts();
            List<DimensionDomain> list = dimensionDao.queryDimensionsByPage(offset, maxRecord);
            if (CollectionUtils.isNotEmpty(list))
            {
                for (DimensionDomain dimensionDomain : list)
                {
                    String value = dimensionDomain.getValue();
                    List<Map<String, String>> dimensionList = (List<Map<String, String>>) JsonUtil.stringToObject(value, List.class);
                    dimensionDomain.setDimensionList(dimensionList);
                }
            }
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Dimensions Error. ", e);
            return new Table();
        }
    }

    /**
     * 新增/修改
     *
     * @param dimensionDomain
     * @return
     */
    public Operation addOrEditDimension(DimensionDomain dimensionDomain)
    {
        try
        {
            Boolean result;
            String message;
            //新增
            if (dimensionDomain.getId() == null || dimensionDomain.getId() == 0)
            {
                dimensionDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = dimensionDao.createDimension(dimensionDomain) == 1;
                message = result ? "新增属性维度成功" : "新增属性维度失败";
            }
            else
            {
                result = dimensionDao.updateDimension(dimensionDomain) == 1;
                message = result ? "更新属性维度成功" : "更新属性维度失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Dimension Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param id
     * @return
     */
    public Operation deleteDimension(Integer id)
    {
        try
        {
            Boolean result = dimensionDao.deleteDimension(id) == 1;
            String message = result ? "删除属性维度成功" : "删除属性维度失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Dimension Error. ", e);
            return new Operation();
        }
    }

    /**
     * @return
     */
    public List<IdAndNameDomain> queryAllDimensionIdAndNames()
    {
        try
        {
            return dimensionDao.queryAllDimensionIdAndNames();
        }
        catch (Exception e)
        {
            LOG.error("Query Dimension Id And Names Error. ", e);
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    public Table queryDimensionValueById(Integer id)
    {
        try
        {
            String value = dimensionDao.queryDimensionValueById(id);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if (value != null)
            {
                list = JsonUtil.stringToObject(value, List.class);
            }
            // 前端设置不分页，size为0
            return new Table(list, 0);
        }
        catch (Exception e)
        {
            LOG.error("Dimension Value Convert Json Error. ", e);
            return new Table();
        }
    }

    /**
     * @return
     */
    public Map<String, Object> queryAllDimensions()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<DimensionDomain> dimensions = dimensionDao.queryAllDimensions();
        Map<Integer, Object> idRefName = new HashMap<Integer, Object>();
        Map<Integer, List<Map<String, String>>> idRefValue = new HashMap<Integer, List<Map<String, String>>>();
        if (CollectionUtils.isNotEmpty(dimensions))
        {
            for (DimensionDomain dimensionDomain : dimensions)
            {
                String value = dimensionDomain.getValue();
                try
                {
                    List<Map<String, String>> dimensionList = JsonUtil.stringToObject(value, List.class);
                    idRefName.put(dimensionDomain.getId(), dimensionDomain.getName());
                    idRefValue.put(dimensionDomain.getId(), dimensionList);
                }
                catch (IOException e)
                {
                    LOG.error("Query Dimension Error : Id " + dimensionDomain.getId(), e);
                    continue;
                }
            }
            result.put("dimensionDetail", idRefValue);
            result.put("dimension", idRefName);
        }
        return result;
    }
}
