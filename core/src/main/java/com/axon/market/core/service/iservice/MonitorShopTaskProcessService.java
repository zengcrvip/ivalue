package com.axon.market.core.service.iservice;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.dao.mapper.ishop.IShopTaskMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuanfei on 2017/5/5.
 */
@Service("monitorShopTaskProcessService")
public class MonitorShopTaskProcessService
{
    private static final Logger LOG = Logger.getLogger(MonitorShopTaskProcessService.class.getName());

    @Autowired
    @Qualifier("shopTaskDao")
    private IShopTaskMapper shopTaskDao;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public ServiceResult checkRecommendationProcess(String fileName)
    {
        ServiceResult result = new ServiceResult();

        if (StringUtils.isEmpty(fileName))
        {
            result.setRetValue(-1);
            result.setDesc("文件名为空");
            return result;
        }

        //判断文件名是【营销活动文件】还是【活动用户文件】
        //根据文件名的前六位判断(AZ1101)，最后一位1：营销活动文件，为2：活动用户文件
        String fileType = fileName.substring(5,6);
        if ("1".equals(fileType))
        {
            List<String> saleIds = shopTaskDao.queryShopTaskSaleIdsBySaleFileName(fileName);
            //先判断营销活动文件是否已入库，
            if (CollectionUtils.isEmpty(saleIds))
            {
                result.setRetValue(-2);
                result.setDesc("营销活动文件未入库");
                return result;
            }
            //如果库中有对应的数据，再判断对应的活动用户文件是否入库
            try
            {
                StringBuffer sql = new StringBuffer();
                sql.append("select count(0) from shop.shop_user where ");
                for (String saleId : saleIds)
                {
                    sql.append("sale_id = "+saleId +" or ");
                }

                sql.append("false");
                int count = jdbcTemplate.queryForObject(sql.toString(), java.lang.Integer.class);
                if (count <= 0)
                {
                    result.setRetValue(-3);
                    result.setDesc("活动用户文件未入库");
                    return result;
                }
            }
            catch (Exception e)
            {

            }
        }
        else if ("2".equals(fileType))
        {
            if(StringUtils.isEmpty(RedisUtil.getInstance().get("/"+fileName)))
            {
                result.setRetValue(-3);
                result.setDesc("活动用户文件未入库");
            }
        }
        else
        {
            result.setRetValue(1);
            result.setDesc("No file");
            return result;
        }

        return result;
    }

}
