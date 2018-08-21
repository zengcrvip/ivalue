package com.axon.market.dao.mapper.iconsumption;

import com.axon.market.common.domain.iconsumption.DixiaoCodeDomain;
import com.axon.market.common.domain.iconsumption.DixiaoResultDomain;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuwen on 2017/7/21.
 */
@Component("DixiaoResultDao")
public interface IDixiaoResultMapper extends IMyBatisMapper {

    /**
     * 查询低销波次统计结果
     *
     * @return
     */
    List<DixiaoResultDomain> queryDixiaoResult(Map<String, Object> paras);


    /**
     * 分页查询营业厅渠道编码信息
     *
     * @return
     */
    List<DixiaoCodeDomain> queryBusinessCodeByPage(Map<String, Object> paras);

    /**
     * 查询营业厅渠道编码信息记录条数
     *
     * @return
     */
    int queryBusinessCodeTotal(Map<String, Object> paras);

    /**
     * 分页查询合作伙伴渠道编码信息
     *
     * @return
     */
    List<DixiaoCodeDomain> queryPartnerCodeByPage(Map<String, Object> paras);

    /**
     * 查询合作伙伴渠道编码信息记录条数
     *
     * @return
     */
    int queryPartnerCodeTotal(Map<String, Object> paras);

    /**
     * 分页查询低消任务
     *
     * @return
     */
    List<DixiaoTaskDomain> queryDixiaoTaskByPage(Map<String, Object> paras);

    /**
     * 查询低消任务(仅一条记录)
     *
     * @return
     */
    DixiaoTaskDomain queryOneDixiaoTask(@Param(value = "taskid") long taskid);

    /**
     * 查询低消任务总数
     *
     * @return
     */
    int queryDixiaoTaskTotal(Map<String, Object> paras);


    /**
     * 分配低消档位
     *
     * @return
     */
    int updateToAllocated(Map<String, Object> paras);

    /**
     * 分配低消档位推送方式
     *
     * @return
     */
    int updateTaskMethod(Map<String, Object> paras);


    /**
     * 取消分配
     *
     * @return
     */
    int updateToUnallocated(Map<String, Object> paras);

    /**
     * 修改低消任务状态
     *
     * @return
     */
    int modifyTaskStatus(Map<String, Object> paras);

    /**
     * 修改是否推送风雷标志位
     *
     * @return
     */
    int modifyNotifyFtp(Map<String, Object> paras);

    /**
     * 线上团队分配
     *
     * @return
     */
    int insertPartnerAllocate(@Param(value = "dataList") List<DixiaoCodeDomain> dataList);

    /**
     * 线上团队去分配
     *
     * @return
     */
    int deletePartner(Map<String, Object> paras);

    /**
     * 线上团队去分配
     *
     * @return
     */
    DixiaoResultDomain queryResultByPartner(Map<String, Object> paras);

    /**
     * 根据低消配置文件查询低消任务
     * @return
     */
    List<DixiaoTaskDomain> queryDixiaoTaskByConfig(Map<String, Object> paras);

    /**
     * 根据低消配置文件查询低消任务
     * @return
     */
    int setTaskInvalidBySaleID(@Param(value = "saleid") String saleid);

    /**
    * 插入低消任务数据
    * @param taskDomain
    * @return
    */
    int insertDixiaoTask(DixiaoTaskDomain taskDomain);

    /**
     * 插入低销统计结果数据
     *
     * @return
     */
    int insertDixiaoResult(@Param(value = "dataList") List<DixiaoResultDomain> dataList);

    /**
     * 根据活动id查询不失效的低消任务总数
     *
     * @return
     */
    int queryDixiaoTaskBySaleIDTotal(Map<String, Object> paras);

    /**
     * 渠道编码数据清除
     *
     * @return
     */
    int deleteBusinessCode();

    /**
     * 合作伙伴编码数据清除
     *
     * @return
     */
    int deletePartnerCode();

    /**
     * 清除失效的已分配的合作伙伴编码
     *
     * @return
     */
    int deleteInvalidPartner();


    /**
     * 插入渠道编码数据
     *
     * @return
     */
    int insertBusinessCode(@Param(value = "dataList") List<DixiaoCodeDomain> dataList);

    /**
     * 插入合作伙伴编码数据
     *
     * @return
     */
    int insertPartnerCode(@Param(value = "dataList") List<DixiaoCodeDomain> dataList);

    /**
     * 更新任务的用户文件名
     *
     * @return
     */
    int updateTaskFilename(@Param(value = "taskid") long taskid, @Param(value = "user_file_name") String user_file_name);

    /**
     * 根据saleid/boid查询taskid
     *
     * @return
     */
    DixiaoTaskDomain queryDixiaoTaskID(@Param(value = "saleid") String saleid,@Param(value = "boid") String boid);

    /**
     * 根据saleid查询任务
     *
     * @return
     */
    DixiaoTaskDomain queryDixiaoTaskBySaleID(@Param(value = "saleid") String saleid);

    /**
     * 根据saleid和boid查询任务
     *
     * @return
     */
    DixiaoTaskDomain queryDixiaoTaskBySaleBoID(@Param(value = "saleid") String saleid,@Param(value = "boid") String boid);

    /**
     * 更新matchno
     *
     * @return
     */
    int updateResultMatchno(@Param(value = "id") long id, @Param(value = "matchno") long matchno);

    /**
     * 插入低销统计结果数据（一条）
     *
     * @return
     */
    int insertOneDixiaoResult(DixiaoResultDomain resultDomain);

    /**
     * 将线下统计结果更新为线上
     *
     * @return
     */
    int updateResultToOnline(@Param(value = "taskid") long taskid);

    /**
     * 根据taskid更新分配结果表档位类型
     *
     * @return
     */
    int updateDixiaoRankType(@Param(value = "taskid") long taskid,@Param(value = "ranktype") String ranktype);

    /**
     * 根据taskid更新任务档位类型
     *
     * @return
     */
    int updateTaskRankType(@Param(value = "taskid") long taskid,@Param(value = "ranktype") String ranktype);

    /**
     * 查询统计个数
     *
     * @return
     */
    Map<String, Object> querySumResult(Map<String, Object> paras);

    /**
     * 统计线上团队本批次分配人数
     *
     * @return
     */
    List<Map<String, Object>> queryPartnerSumResult(@Param(value = "taskid") long taskid);

    /**
     * 查询统计个数
     *
     * @return
     */
    List<Map<String, Object>> queryPartnerRankIDSumResult(Map<String, Object> paras);

    /**
     * 根据低消配置文件查询未失效的低消任务
     *
     * @return
     */
    List<DixiaoTaskDomain> queryValidDixiaoTaskByConfig(Map<String, Object> paras);

    /**
     * 根据taskid更新ftpflag
     *
     * @return
     */
    int updateDixiaoFtpflag(@Param(value = "taskid") long taskid);

    /**
     * 查询列表中已经推送档位个数
     * @param taskid
     * @return
     */
    int queryOnlineFtpTotal(@Param(value = "taskid") long taskid, @Param(value = "idlist") String idlist);

    /**
     * 查询列表中有可以推送给话加的档位
     * @param taskid
     * @return
     */
    int queryToVoiceOnlineFtpTotal(@Param(value = "taskid") long taskid);

}
