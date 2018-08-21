package com.axon.market.dao.mapper.iconsumption;

import com.axon.market.common.domain.iconsumption.*;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**低销波次统计结果DAO
 * Created by zhuwen on 2017/6/28.
 */

@Component("PINResultDao")
public interface IPINResultMapper extends IMyBatisMapper {
    /**
     * 分页低销档位分配地市分配结果
     *
     * @return
     */
    List<Map<String,Object>> queryPINRankAreaByPage(Map<String, Object> paras);

    /**
     * 分页查询低销波次统计结果
     *
     * @return
     */
    List<Map<String,Object>> queryPINResultByPage(Map<String, Object> paras);

    /**
     * 查询低销波次统计结果个数
     *
     * @return
     */
    int queryPINResultTotal(Map<String, Object> paras);

    /**
     * 低销分配匹配总人数
     *
     * @return
     */
    int queryPINResultMatchTotal(Map<String, Object> paras);

    /**
     * 低销档位分配地市总条数
     *
     * @return
     */
    int queryPINRankAreaTotal(Map<String, Object> paras);

    /**
     * 低销档位分配地市匹配总人数
     *
     * @return
     */
    int queryPINRankAreaMatchTotal(Map<String, Object> paras);

    /**
     * 查询低销波次统计结果个数
     *
     * @return
     */
    List<Map<String,Object>> queryLatesBatchByMonth(Map<String, Object> paras);

    /**
     * 查询已分配低销产品数据
     *
     * @return
     */
    List<Map<String, Object>> queryAllocatedPINPro(Map<String, Object> paras);

    /**
     * 查询未分配低销产品数据
     *
     * @return
     */
    List<Map<String, Object>> queryUnallocatedPINPro(Map<String, Object> paras);

    /**
     * 插入低销档位分配的地市数据
     *
     * @return
     */
    int insertPINRankArea(Map<String, Object> paras);

    /**
     * 插入低销波次统计结果数据
     *
     * @return
     */
    int insertPINResult(@Param(value = "dataList") List<PINResultDomain> dataList);

    /**
     * 低销波次统计结果数据置为失效
     *
     * @return
     */
    int deletePINResult();

    /**
     * 将所有低销档位分配的地市数据置为失效
     *
     * @return
     */
    int deletePINRankArea();

    /**
     * 将列表中的低销档位分配的地市数据置为失效
     *
     * @return
     */
    int deletePINRankAreaBylist(Map<String, Object> paras);
}
