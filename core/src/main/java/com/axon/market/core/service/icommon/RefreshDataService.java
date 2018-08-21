package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.icommon.RefreshLogDomain;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.core.rule.IRuleParse;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chenyu on 2016/7/14.
 */
@Component("refreshDataService")
public class RefreshDataService
{
    private static final Logger LOG = Logger.getLogger(RefreshDataService.class.getName());

    @Autowired
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("refreshLogService")
    private RefreshLogService refreshLogService;

    @Autowired
    @Qualifier("ruleParseImpl")
    private IRuleParse ruleParse;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    /**
     * 刷新标签，从服务器上获取到文件后，被文件监控察觉，从而实现刷新
     *
     * @param tagDomain
     */
    public void refreshRemoteImportTagData(TagDomain tagDomain)
    {
        LOG.info("Start Refresh Remote Import Tag ... Tag Name = " + tagDomain.getName());

        Integer remoteServerId = tagDomain.getRemoteServerId();
        RemoteServerDomain remoteServerDomain = remoteServerService.queryRemoteServerById(remoteServerId);

        // 路径必须以"/"结尾
        String targetFileName = systemConfigBean.getMonitorPath() + tagDomain.getId() + "@" + tagDomain.getDbSchema() + "." + tagDomain.getTableName();

        // 下载文件
        fileOperateService.downloadFile(remoteServerDomain, tagDomain.getRemoteFile(), targetFileName);

        // 文件上传后，重命名
        new File(targetFileName).renameTo(new File(targetFileName + ".tag"));

        LOG.info("End Refresh Remote Import Tag ... Tag Name = " + tagDomain.getName());
    }

    /**
     * 上传文件移动到监控文件目录下，从而进行数据的导入
     *
     * @param file
     */
    public void refreshLocalImportTagData(File file)
    {
        LOG.info("Start Refresh Local Import Tag ... File Name = " + file.getName());
        try
        {
            // 移动后的目标文件名称
            String targetFileName = systemConfigBean.getMonitorPath() + file.getName();
            File targetFile = new File(targetFileName);
            // 文件移动
            FileUtils.moveFile(file, targetFile);
            // 文件改名，改为监控执行的文件名
            targetFile.renameTo(new File(targetFileName + ".tag"));
        }
        catch (Exception e)
        {
            LOG.error("Local Tag File Move Error. ", e);
        }
        LOG.info("Start Refresh Local Import Tag ... File Name = " + file.getName());
    }

    /**
     * 刷新远程导入模型，从服务器上获取到文件后，被文件监控察觉，从而实现刷新
     *
     * @param modelDomain
     */
    public void refreshRemoteImportModelData(ModelDomain modelDomain)
    {
        LOG.info("Start Refresh Remote Import Model ... Model Name = " + modelDomain.getName());

        Integer remoteServerId = modelDomain.getRemoteServerId();
        RemoteServerDomain remoteServerDomain = remoteServerService.queryRemoteServerById(remoteServerId);

        //路径必须以"/"结尾
        String targetFileName = systemConfigBean.getMonitorPath() + modelDomain.getId() + "@" + greenPlumOperateService.getModelDataTableName(modelDomain);

        //下载文件
        fileOperateService.downloadFile(remoteServerDomain, modelDomain.getRemoteFile(), targetFileName);

        //文件上传后，重命名
        new File(targetFileName).renameTo(new File(targetFileName + ".model"));

        if ("0".equals(modelDomain.getIsNeedDelete()))
        {
            fileOperateService.deleteFile(remoteServerDomain, modelDomain.getRemoteFile());
        }

        LOG.info("End Refresh Remote Import Model ... Model Name = " + modelDomain.getName());
    }

    /**
     * 上传文件移动到监控文件目录下，从而进行数据的导入
     *
     * @param file
     */
    public void refreshLocalImportModelData(File file)
    {
        LOG.info("Start Refresh Local Import Model ... File Name = " + file.getName());
        try
        {
            // 移动后的目标文件名称
            String targetFileName = systemConfigBean.getMonitorPath() + file.getName();
            File targetFile = new File(targetFileName);
            // 文件移动
            FileUtils.moveFile(file, targetFile);
            // 文件改名，改为监控执行的文件名
            targetFile.renameTo(new File(targetFileName + ".model"));
        }
        catch (Exception e)
        {
            LOG.error("Local Model File Move Error. ", e);
        }
        LOG.info("End Refresh Local Import Model ... File Name = " + file.getName());
    }

    /**
     * @param modelDomain
     * @param nodes
     */
    public void refreshRuleModelData(ModelDomain modelDomain, List<RuleNode> nodes)
    {
        LOG.info("Start Refresh Rule Model ... Model Name = " + modelDomain.getName());

        Integer modelId = modelDomain.getId();
        String fullTableName = greenPlumOperateService.getModelDataTableName(modelDomain);
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ").append(fullTableName).append(" (select distinct phone from (").append(ruleParse.parseRuleToSql(nodes, null)).append(") a0 )");

        int count = updateModelDataToGreenPlum(fullTableName, sql.toString());
        String endTime = MarketTimeUtils.formatDate(new Date());

        RefreshLogDomain refreshLogDomain = new RefreshLogDomain(modelId, "model");
        refreshLogDomain.setRefreshCount(count);
        refreshLogDomain.setRefreshTime(endTime);
        refreshLogDomain.setRefreshResult("success");
        refreshLogService.insertRefreshLog(refreshLogDomain);
        modelService.updateModelRefreshInfo(modelId, count, endTime);

        LOG.info("End Refresh Rule Model ... Model Name = " + modelDomain.getName());
    }

    /**
     *
     * @param modelDomain
     * @param rules
     */
    public void refreshKeeperRuleModelData(ModelDomain modelDomain, String rules)
    {
        LOG.info("Start Refresh Rule Model ... Model Name = " + modelDomain.getName());

        Integer modelId = modelDomain.getId();
        String fullTableName = greenPlumOperateService.getModelDataTableName(modelDomain);
        StringBuffer sql = new StringBuffer();
        String sqlStr = "";

        try
        {
            List<Map<String, String>> ruleList = JsonUtil.stringToObject(rules, List.class);
            for (Map<String, String> ruleMap : ruleList)
            {
                Set<String> fields = ruleMap.keySet();
                sqlStr += "select distinct phone from uapp.lab_mobile_tag where 1 = 1 ";
                for (String field : fields)
                {
                    String values = ruleMap.get(field);
                    if (StringUtils.isEmpty(values))
                    {
                        continue;
                    }
                    sqlStr += (" and ( ");
                    String[] valueArray = values.split(",");

                    for (String value : valueArray)
                    {
                        sqlStr += field + "='" + value + "' or ";
                    }
                    sqlStr += sqlStr.substring(0, sql.length() - 4) + ")";
                }
            }
        }
        catch (Exception e)
        {

        }

        sql.append("insert into ").append(fullTableName).append(sqlStr);

        int count = updateModelDataToGreenPlum(fullTableName, sql.toString());
        String endTime = MarketTimeUtils.formatDate(new Date());

        RefreshLogDomain refreshLogDomain = new RefreshLogDomain(modelId, "model");
        refreshLogDomain.setRefreshCount(count);
        refreshLogDomain.setRefreshTime(endTime);
        refreshLogDomain.setRefreshResult("success");
        refreshLogService.insertRefreshLog(refreshLogDomain);
        modelService.updateModelRefreshInfo(modelId, count, endTime);

        LOG.info("End Refresh Rule Model ... Model Name = " + modelDomain.getName());
    }

    /**
     * 刷新GP中的数据（创建表，存入数据）
     *
     * @param fullTableName
     * @param sql
     * @return
     */
    private int updateModelDataToGreenPlum(String fullTableName, String sql)
    {
        int count = 0;

        if (greenPlumOperateService.isExistsTable(fullTableName))
        {
            if (greenPlumOperateService.truncateTable(fullTableName))
            {
                LOG.info("Refresh Table : " + fullTableName + " and Sql : " + sql);
                count = greenPlumOperateService.update(sql);
            }
            else
            {
                LOG.warn("Truncate Table error : tableName = " + fullTableName);
            }
        }
        else
        {
            if (greenPlumOperateService.createModelTable(fullTableName))
            {
                LOG.info("Refresh Table : " + fullTableName + " and Sql : " + sql);
                count = greenPlumOperateService.update(sql);
            }
            else
            {
                LOG.warn("Create Table error : tableName = " + fullTableName);
            }
        }
        return count;
    }
}
