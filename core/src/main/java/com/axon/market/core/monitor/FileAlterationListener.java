package com.axon.market.core.monitor;

import com.axon.market.common.domain.icommon.RefreshLogDomain;
import com.axon.market.common.monitor.FileAlterationListenerAdaptor;
import com.axon.market.common.monitor.FileEntry;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.core.service.icommon.RefreshLogService;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.itag.ResourcesUserService;
import com.axon.market.core.service.itag.TagService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2016/6/13.
 */
public class FileAlterationListener extends FileAlterationListenerAdaptor
{
    private static final Logger LOG = Logger.getLogger(FileAlterationListener.class.getName());

    private static List<String> fileSuffixes = new ArrayList<String>();

    private FileMonitorService service = FileMonitorService.getInstance();

    private TagService tagService = TagService.getInstance();

    private ResourcesUserService resourceService = ResourcesUserService.getInstance();

    private ModelService modelService = ModelService.getInstance();

    private RefreshLogService refreshLogService = RefreshLogService.getInstance();

    static
    {
        // 初始化监控文件后缀名
        fileSuffixes.add(".tag");
        fileSuffixes.add(".model");
        fileSuffixes.add(".resource");
    }


    @Override
    public boolean accept(FileEntry fileEntry)
    {
        for (String fileSuffix : fileSuffixes)
        {
            if (fileEntry.getFile().getName().endsWith(fileSuffix))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void fileCreateAction(FileEntry fileEntry)
    {

        String fileName = fileEntry.getFile().getName();
        if (fileName.endsWith(".tag"))
        {
            LOG.info("Start Tag File Create Action : " + fileName);
            Integer index = fileName.indexOf('@');
            Integer tagId = Integer.parseInt(fileName.substring(0, index));
            loadTagData(fileEntry, tagId);
            LOG.info("End Tag File Create Action : " + fileName);
        }
        else if (fileName.endsWith(".model"))
        {
            LOG.info("Start Model File Create Action : " + fileName);
            Integer index = fileName.indexOf('@');
            Integer modelId = Integer.parseInt(fileName.substring(0, index));
            loadModelData(fileEntry, modelId);
            LOG.info("End Model File Create Action : " + fileName);
        }
        else if (fileName.endsWith(".resource"))
        {
            LOG.info("Start Resource User Model File Create Action : " + fileName);
            Integer index = fileName.indexOf('@');
            Integer resourceId = Integer.parseInt(fileName.substring(0, index));
            loadResourceData(fileEntry, resourceId);
            LOG.info("End Resource User Model Create Action : " + fileName);
        }
    }

    /**
     * 加载标签数据
     *
     * @param fileEntry 标签数据文件
     * @param tagId 标签id
     */
    private void loadTagData(FileEntry fileEntry, Integer tagId)
    {
        File file = fileEntry.getFile();
        try
        {
            RefreshLogDomain refreshLogDomain = new RefreshLogDomain(tagId, "tag");

            // 刷新操作
            service.operateFileToRefreshGreenPlum(file, refreshLogDomain, "tag");

            String endTime = MarketTimeUtils.formatDate(new Date());
            refreshLogDomain.setRefreshTime(endTime);
            // 更新tag表刷新数据字段
            tagService.updateTagRefreshInfo(tagId, endTime, refreshLogDomain.getRefreshResult(), refreshLogDomain.getRefreshResultReason(), refreshLogDomain.getRefreshCount(), refreshLogDomain.getRefreshSuccessCount(), refreshLogDomain.getRefreshFailCount());
            // 插入刷新日志表
            refreshLogService.insertRefreshLog(refreshLogDomain);
        }
        catch (Exception e)
        {
            LOG.error("Load Tag Data Error. ", e);
        }
        finally
        {
            // 删除生成的标签数据文件
            FileUtils.deleteQuietly(file);
        }
    }

    /**
     * 加载模型数据
     *
     * @param fileEntry 模型数据文件
     */
    private void loadModelData(FileEntry fileEntry, Integer modelId)
    {
        File file = fileEntry.getFile();
        try
        {
            RefreshLogDomain refreshLogDomain = new RefreshLogDomain(modelId, "model");

            // 刷新操作
            service.operateFileToRefreshGreenPlum(file, refreshLogDomain, "model");

            String endTime = MarketTimeUtils.formatDate(new Date());
            refreshLogDomain.setRefreshTime(endTime);
            // 更新model表刷新数据字段
            modelService.updateModelRefreshInfo(modelId, refreshLogDomain.getRefreshSuccessCount(), endTime);
            // 插入刷新日志表
            refreshLogService.insertRefreshLog(refreshLogDomain);
        }
        catch (Exception e)
        {
            LOG.error("Load Model Data Error. ", e);
        }
        finally
        {
            // 删除生成的模型数据文件
            FileUtils.deleteQuietly(file);
        }
    }

    /**
     * 加载资源用户信息数据
     * @param fileEntry
     * @param resourceId
     */
    private void loadResourceData(FileEntry fileEntry, Integer resourceId)
    {
        File file = fileEntry.getFile();
        try
        {
            RefreshLogDomain refreshLogDomain = new RefreshLogDomain(resourceId, "resource");

            // 刷新操作
            service.operateFileToRefreshGreenPlum(file, refreshLogDomain, "resource");

            String endTime = MarketTimeUtils.formatDate(new Date());
            refreshLogDomain.setRefreshTime(endTime);
            // 更新resource表刷新数据字段
            resourceService.updateResourceRefreshInfo(resourceId, endTime, refreshLogDomain.getRefreshResult(), refreshLogDomain.getRefreshCount(), refreshLogDomain.getRefreshSuccessCount(), refreshLogDomain.getRefreshFailCount(), refreshLogDomain.getRemarks());
            // 插入刷新日志表
            refreshLogService.insertRefreshLog(refreshLogDomain);
        }
        catch (Exception e)
        {
            LOG.error("Load Tag Data Error. ", e);
        }
        finally
        {
            // 删除生成的标签数据文件
            FileUtils.deleteQuietly(file);
        }
    }
}
