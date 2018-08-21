package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.MonitorConfigDomain;
import com.axon.market.common.domain.isystem.MonitorConfigEmailDomain;
import com.axon.market.common.domain.isystem.MonitorConfigPhoneDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2017/4/17.
 */
@Component("monitorConfigDao")
public interface IMonitorConfigMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryMonitorConfigCounts(@Param(value = "serverIp") String serverIp);

    /**
     * @param limit
     * @param offset
     * @return
     */
    List<MonitorConfigDomain> queryMonitorConfig(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "serverIp") String serverIp);

    /**
     * 根据id查询监控配置
     * @param id
     * @return
     */
    List<MonitorConfigDomain> queryMonitorById(@Param(value = "id") Integer id);

    /**
     * 新增模型
     *
     * @param info 监控配置model
     * @return
     */
    Integer addMonitor(@Param(value = "info") MonitorConfigDomain info);

    /**
     * 新增查询当前插入数据的id
     * @return
     */
    Integer selectMaxId();

    /**
     * 删除监控配置
     * @param id
     * @return
     */
    Integer deleteMonitor(@Param(value = "id") Integer id);

    /**
     * 根据监控配置id删除对应邮件
     * @param id
     * @return
     */
    Integer deleteEmailAccount(@Param(value = "id") Integer id);

    /**
     * 根据监控配置id删除对应号码
     * @param id
     * @return
     */
    Integer deleteMessagePhone(@Param(value = "id") Integer id);

    /**
     * 更新模型
     *
     * @param info 监控配置model
     * @return
     */
    Integer editMonitor(@Param(value = "info") MonitorConfigDomain info);

    //批量上传emial
    void batchUploadEmail(@Param("path") String fileNamePath,@Param(value = "id") Integer id);

    //查询网址条数
    int queryEmailCount(@Param("id") Integer id);

    //查询号码条数
    int queryPhoneCount(@Param("id") Integer id);

    void UpdateEmail(@Param(value = "id") Integer id);

    //批量上传Phone
    void batchUploadPhone(@Param("path") String fileNamePath,@Param(value = "id") Integer id);

    void UpdatePhone(@Param(value = "id") Integer id);

    /**
     * 列表查询邮箱
     * @return
     */
    Integer queryEmailListCounts(@Param(value = "id") Integer id);

    /**
     * 列表查询邮箱
     * @param limit
     * @param offset
     * @return
     */
    List<MonitorConfigEmailDomain> queryEmailList(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "id") Integer id);

    /**
     * 列表查询号码
     * @return
     */
    Integer queryPhoneListCounts(@Param(value = "id") Integer id);

    /**
     * 列表查询号码
     * @param limit
     * @param offset
     * @return
     */
    List<MonitorConfigPhoneDomain> queryPhoneList(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "id") Integer id);

    /**
     * 根据邮箱id删除邮箱
     * @param id
     * @return
     */
    Integer deleteEmail(@Param(value = "id") Integer id);

    /**
     * 根据号码id删除号码
     * @param id
     * @return
     */
    Integer deletePhone(@Param(value = "id") Integer id);

    /**
     * 新增email
     *
     * @param info 邮箱model
     * @return
     */
    Integer addEmail(@Param(value = "info") MonitorConfigEmailDomain info);

    /**
     * 修改email
     *
     * @param info 邮箱model
     * @return
     */
    Integer editEmail(@Param(value = "info") MonitorConfigEmailDomain info);

    /**
     * 新增phone
     *
     * @param info 号码model
     * @return
     */
    Integer addPhone(@Param(value = "info") MonitorConfigPhoneDomain info);

    /**
     * 修改phone
     *
     * @param info 号码model
     * @return
     */
    Integer editPhone(@Param(value = "info") MonitorConfigPhoneDomain info);
}
