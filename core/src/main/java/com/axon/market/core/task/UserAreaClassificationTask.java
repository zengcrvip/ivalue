package com.axon.market.core.task;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.common.domain.icommon.AreaDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.AreaService;
import com.axon.market.core.service.ishop.ShopListService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 2017/2/20.
 */
public class UserAreaClassificationTask extends RunJob
{
    private Logger LOG = Logger.getLogger(UserAreaClassificationTask.class.getName());

    private ShopListService shopListService = ShopListService.getInstance();

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private GreenPlumServerBean greenPlumServerBean = GreenPlumServerBean.getInstance();

    private AreaService areaService = AreaService.getInstance();

    @Override
    public void runBody()
    {
        List<Integer> cityCodeList = new ArrayList<Integer>();
        List<AreaDomain> areaList = areaService.queryAllAreas();
        for (AreaDomain areaDomain : areaList)
        {
            if (!"99999".equals(areaDomain.getCode()) )
            {
                cityCodeList.add(Integer.valueOf(areaDomain.getCode()));
            }
        }

        List<Integer> baseIdList;
        List<String> sqls;
        String fullTableName;
        if (CollectionUtils.isNotEmpty(cityCodeList))
        {
            for (Integer cityCode : cityCodeList)
            {
                fullTableName = "model.model_" + cityCode;
                if (greenPlumOperateService.isExistsTable(fullTableName))
                {
                    greenPlumOperateService.truncateTable(fullTableName);
                }
                else
                {
                    greenPlumOperateService.createGreenPlumTable("model", "model_" + cityCode, new ArrayList<String>()
                    {
                        {
                            add("phone varchar(40)");
                            add("base_id numeric");
                            add("addr_type varchar(100)");
                            add("location_type_id numeric");
                        }
                    });
                }

                baseIdList = shopListService.queryBaseIdByCityCode(cityCode);
                if (CollectionUtils.isNotEmpty(baseIdList))
                {
                    sqls = generateUserAreaClassification(fullTableName, baseIdList);
                    for (String sql : sqls)
                    {
                        if (CollectionUtils.isNotEmpty(sqls))
                        {
                            LOG.info("Base Sql : " + sql);
                            greenPlumOperateService.update(sql);
                        }
                    }
                }
            }
        }
    }

    private List<String> generateUserAreaClassification(String fullTableName, List<Integer> baseIdList)
    {

        List<String> sqlList = new ArrayList<String>();
        List<Integer> temp = new ArrayList<Integer>();
        int count = 0;
        StringBuffer sql = new StringBuffer();

        for (Integer baseId : baseIdList)
        {
            temp.add(baseId);
            count++;
            if (count == 10)
            {
                count = 0;
                sql.append("insert into ").append(fullTableName);
                sql.append(" ( select phone,base_id,addr_type,location_type_id from ").append(greenPlumServerBean.getGpShopFullTableName());
                sql.append(" where base_id in (").append(StringUtils.join(temp, ",")).append(") )");
                sqlList.add(sql.toString());
                sql = new StringBuffer();
                temp = new ArrayList<Integer>();
            }
        }

        if (CollectionUtils.isNotEmpty(temp))
        {
            sql.append("insert into ").append(fullTableName);
            sql.append(" ( select phone,base_id,addr_type,location_type_id from ").append(greenPlumServerBean.getGpShopFullTableName());
            sql.append(" where base_id in (").append(StringUtils.join(temp, ",")).append(") )");
            sqlList.add(sql.toString());
        }

        return sqlList;
    }
}
