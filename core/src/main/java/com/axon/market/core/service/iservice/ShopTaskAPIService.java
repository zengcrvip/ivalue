package com.axon.market.core.service.iservice;

import com.axon.market.common.domain.iservice.ShopTaskApiDomain;
import com.axon.market.dao.mapper.ishop.IShopTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 炒店大屏展示对外接口的服务类
 * Created by zengcr on 2017/7/29.
 */
@Service("shopTaskAPIService")
public class ShopTaskAPIService {

    @Autowired
    @Qualifier("shopTaskDao")
    private IShopTaskMapper shopTaskMapper;

    /**
     * 根据日期查询当天的任务
     * @param date
     * @return
     */
    public List<ShopTaskApiDomain> queryShopTasksByDate(String date){
        return shopTaskMapper.queryShopTasksByDate(date);
    }

    /**
     * 根据日期和任务ID查询当天该任务的详情
     * @param date
     * @param taskId
     * @return
     */
    public ShopTaskApiDomain queryShopTasksByDateAndId(String date, String taskId){
        return  shopTaskMapper.queryShopTasksByDateAndId(date, taskId);
    }
}
