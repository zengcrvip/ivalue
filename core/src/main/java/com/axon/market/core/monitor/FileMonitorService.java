
package com.axon.market.core.monitor;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.common.domain.icommon.RefreshLogDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.excel.ExcelReader;
import com.axon.market.core.exception.RefreshDataException;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chenyu on 2016/6/13.
 */
@Component("fileMonitorService")
public class FileMonitorService
{
    private static final Logger LOG = Logger.getLogger(FileMonitorService.class.getName());

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("filePreProcess")
    private IFilePreProcess filePreProcess;

    @Autowired
    @Qualifier("greenPlumServerBean")
    private GreenPlumServerBean greenPlumServerBean;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    public static FileMonitorService getInstance()
    {
        return (FileMonitorService) SpringUtil.getSingletonBean("fileMonitorService");
    }

    /**
     * 批处理大小
     */
    private static final int BATCH_FILE_SIZE = 10000;

    /**
     * 数字数组
     */
    private static final String[] NUMBER = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 操作文件入GP数据库
     *
     * @param file 数据文件
     */
    public void operateFileToRefreshGreenPlum(File file, RefreshLogDomain refreshLogDomain, String fileType)
    {
        String fileName = file.getName();

        // 创建入GP文件，文件名为表名称
        String greenPlumFileName = file.getParent() + File.separator + fileName.substring(fileName.indexOf('@') + 1, fileName.lastIndexOf('.'));
        File greenPlumFile = new File(greenPlumFileName);

        refreshData(file, greenPlumFile, fileType, refreshLogDomain);
    }

    /**
     * 刷新数据
     *
     * @param file          数据文件
     * @param greenPlumFile 导入GP文件
     * @param fileType      是否为标签文件
     */
    private void refreshData(File file, File greenPlumFile, String fileType, RefreshLogDomain refreshLogDomain)
    {
        LineIterator iterator = null;
        try
        {
            // 生成文件行迭代
            iterator = FileUtils.lineIterator(file, "UTF-8");
            switch (fileType)
            {
                case "tag":
                {
                    tagDataRefresh(iterator, greenPlumFile, refreshLogDomain);
                    break;
                }
                case "model":
                {
                    modelDataRefresh(iterator, greenPlumFile, refreshLogDomain);
                    break;
                }
                case "resource":
                {
                    resourceDataRefresh(iterator, greenPlumFile, refreshLogDomain);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Refresh Data Read File Error. ", e);
        }
        finally
        {
            // 关闭迭代
            iterator.close();
        }
    }

    /**
     * @param iterator
     * @param greenPlumFile
     */
    private void tagDataRefresh(LineIterator iterator, File greenPlumFile, RefreshLogDomain refreshLogDomain) throws RefreshDataException
    {
        refreshLogDomain.setRefreshResult("fail");
        refreshLogDomain.setRefreshCount(0);
        refreshLogDomain.setRefreshSuccessCount(0);
        refreshLogDomain.setRefreshFailCount(0);

        String fullTableName = greenPlumFile.getName();

        // 备份表数据（3天）
        metaTableBackup(fullTableName);
        // 清空数据
        if (!greenPlumOperateService.truncateTable(fullTableName))
        {
            LOG.error("Truncate Table error");
            refreshLogDomain.setRefreshResultReason("Truncate Table Error");
            return;
        }

        List<String> lineList = new ArrayList<String>();
        // 记录数，GP设置一次批量导入文件行数
        int recordCount = 0, gpBatchCopyCount = greenPlumServerBean.getGpBatchCopyCount(), totalCount = 0, successCount = 0;

        try
        {
            String line;

            while (iterator.hasNext())
            {
                line = iterator.next();
                totalCount++;

                // 剔除错误行
                if (StringUtils.isEmpty(line) || !StringUtils.startsWithAny(line, NUMBER))
                {
                    continue;
                }

                // 做行入GP的预处理
                lineList.add(filePreProcess.tagFileLinePreProcess(line));
                recordCount++;
                successCount++;

                if (recordCount % BATCH_FILE_SIZE == 0)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                }
                if (recordCount >= gpBatchCopyCount)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                    // 刷新GP数据
                    refreshGreenPlumData(greenPlumFile, fullTableName, null);
                    recordCount = 0;
                }
            }

            if (recordCount != 0)
            {
                createGreenPlumDataFile(greenPlumFile, lineList);
                lineList.clear();
                refreshGreenPlumData(greenPlumFile, fullTableName, null);
            }
            refreshLogDomain.setRefreshResult("success");
            refreshLogDomain.setRefreshCount(totalCount);
            refreshLogDomain.setRefreshSuccessCount(successCount);
            refreshLogDomain.setRefreshFailCount(totalCount - successCount);
        }
        catch (RefreshDataException e)
        {
            LOG.error("Refresh Tag Data Error. ", e);
            refreshLogDomain.setRefreshResultReason(e.getMessage());
        }
        finally
        {
            FileUtils.deleteQuietly(greenPlumFile);
        }
    }

    /**
     * @param iterator
     * @param greenPlumFile
     * @return
     */
    private void modelDataRefresh(LineIterator iterator, File greenPlumFile, RefreshLogDomain refreshLogDomain) throws RefreshDataException
    {
        refreshLogDomain.setRefreshResult("fail");
        refreshLogDomain.setRefreshCount(0);
        refreshLogDomain.setRefreshSuccessCount(0);
        refreshLogDomain.setRefreshFailCount(0);

        String fullTableName = greenPlumFile.getName();
        // 初始化表数据
        if (greenPlumOperateService.isExistsTable(fullTableName))
        {
            if (!greenPlumOperateService.truncateTable(fullTableName))
            {
                LOG.error("Truncate Table Error");
                refreshLogDomain.setRefreshResultReason("Truncate Table Error");
                return;
            }
        }
        else
        {
            if (!greenPlumOperateService.createModelTable(fullTableName))
            {
                LOG.error("Create Model Table Error");
                refreshLogDomain.setRefreshResultReason("Create Model Table Error");
                return;
            }
        }

        List<String> lineList = new ArrayList<String>(), columnList = new ArrayList<String>();
        // 行集合
        columnList.add("phone");
        int recordCount = 0, gpBatchCopyCount = greenPlumServerBean.getGpBatchCopyCount(), totalCount = 0, successCount = 0;

        try
        {
            String line, phone;

            while (iterator.hasNext())
            {
                line = iterator.next();
                totalCount++;

                // 如果文件格式是utf-8有bom格式，需要先清除文件开头的标记
                if (line.startsWith("\uFEFF"))
                {
                    line.replace("\uFEFF","");
                }

                // 剔除错误行
                if (StringUtils.isEmpty(line) || !StringUtils.startsWithAny(line, NUMBER))
                {
                    continue;
                }

                // 行预处理
                phone = filePreProcess.modelFileLinePreProcess(filePreProcess.modelFileLinePreProcess(line));
                // 剔除不是数字的
                if (NumberUtils.isDigits(phone))
                {
                    lineList.add(axonEncrypt.encrypt(phone));
                    recordCount++;
                    successCount++;
                }
                else
                {
                    continue;
                }

                if (recordCount % BATCH_FILE_SIZE == 0)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                }
                if (recordCount >= gpBatchCopyCount)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                    // 刷新GP数据
                    refreshGreenPlumData(greenPlumFile, fullTableName, null);
                    recordCount = 0;
                }
            }

            if (recordCount != 0)
            {
                createGreenPlumDataFile(greenPlumFile, lineList);
                lineList.clear();
                refreshGreenPlumData(greenPlumFile, fullTableName, columnList);
            }
            refreshLogDomain.setRefreshResult("success");
            refreshLogDomain.setRefreshCount(totalCount);
            refreshLogDomain.setRefreshSuccessCount(successCount);
            refreshLogDomain.setRefreshFailCount(totalCount - successCount);
        }
        catch (RefreshDataException e)
        {
            LOG.error("Refresh Tag Data Error. ", e);
            refreshLogDomain.setRefreshResultReason(e.getMessage());
        }
        finally
        {
            FileUtils.deleteQuietly(greenPlumFile);
        }
    }

    /**
     *
     * @param iterator
     * @param greenPlumFile
     * @param refreshLogDomain
     * @throws RefreshDataException
     */
    private void resourceDataRefresh(LineIterator iterator, File greenPlumFile, RefreshLogDomain refreshLogDomain) throws RefreshDataException
    {
        refreshLogDomain.setRefreshResult("fail");
        refreshLogDomain.setRefreshCount(0);
        refreshLogDomain.setRefreshSuccessCount(0);
        refreshLogDomain.setRefreshFailCount(0);

        String fullTableName = greenPlumFile.getName();

        List<String> lineList = new ArrayList<String>();
        // 记录数，GP设置一次批量导入文件行数
        int recordCount = 0, gpBatchCopyCount = greenPlumServerBean.getGpBatchCopyCount(), totalCount = 0, successCount = 0;
        String columns = "";
        Boolean hasCreateTab = false;
        try
        {
            String line;

            while (iterator.hasNext())
            {
                line = iterator.next();
                line = line.replace("\t","|");

                // 如果文件格式是utf-8有bom格式，需要先清除文件开头的标记
                if (line.startsWith("\uFEFF"))
                {
                    line.replace("\uFEFF","");
                }

                // 获取表头，并将表头保存到mysql中,便于下载使用
                if (totalCount == 0 && !hasCreateTab)
                {
                    int columnLength = line.split("[|]").length;
                    //创建表头
                    if (greenPlumOperateService.isExistsTable(fullTableName))
                    {
                        if (!greenPlumOperateService.truncateTable(fullTableName))
                        {
                            LOG.error("Truncate Table Error");
                            refreshLogDomain.setRefreshResultReason("Truncate Table Error");
                            return;
                        }
                    }
                    else
                    {
                        if (!greenPlumOperateService.createResourceTable(fullTableName, columnLength))
                        {
                            LOG.error("Create Resource Model Table Error");
                            refreshLogDomain.setRefreshResultReason("Create Resource Model Table Error");
                            return;
                        }
                    }
                    columns = line;
                    hasCreateTab = true;
                    continue;
                }

                totalCount++;

                // 剔除错误行
                if (StringUtils.isEmpty(line) || !StringUtils.startsWithAny(line, NUMBER))
                {
                    continue;
                }

                //号码加密操作
                // 获取到第一个分隔符|的位置
                int oneIndex = line.indexOf("|");
                String phone = axonEncrypt.encrypt(line.substring(0, oneIndex));
                line = phone + line.substring(oneIndex);

                // 做行入GP的预处理
                lineList.add(filePreProcess.tagFileLinePreProcess(line));
                recordCount++;
                successCount++;

                if (recordCount % BATCH_FILE_SIZE == 0)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                }
                if (recordCount >= gpBatchCopyCount)
                {
                    // 生成入GP数据文件
                    createGreenPlumDataFile(greenPlumFile, lineList);
                    lineList.clear();
                    // 刷新GP数据
                    refreshGreenPlumData(greenPlumFile, fullTableName, null);
                    recordCount = 0;
                }
            }

            if (recordCount != 0)
            {
                createGreenPlumDataFile(greenPlumFile, lineList);
                lineList.clear();
                refreshGreenPlumData(greenPlumFile, fullTableName, null);
            }
            refreshLogDomain.setRefreshResult("success");
            refreshLogDomain.setRefreshCount(totalCount);
            refreshLogDomain.setRefreshSuccessCount(successCount);
            refreshLogDomain.setRefreshFailCount(totalCount - successCount);
            refreshLogDomain.setRemarks(columns);
        }
        catch (RefreshDataException e)
        {
            LOG.error("Refresh Tag Data Error. ", e);
            refreshLogDomain.setRefreshResultReason(e.getMessage());
        }
        finally
        {
            FileUtils.deleteQuietly(greenPlumFile);
        }
    }

    /**
     * 写GP数据文件
     *
     * @param file     GP数据文件
     * @param lineList 数据行集合
     */
    private void createGreenPlumDataFile(File file, List<String> lineList)
    {
        try
        {
            // 以UTF-8编码，可追加形式写文件
            FileUtils.writeLines(file, "UTF-8", lineList, true);
        }
        catch (IOException e)
        {
            LOG.error("Write GreenPlum Data File Error. ", e);
        }
    }

    /**
     * 刷新GP数据
     *
     * @param dataFile      数据文件
     * @param fullTableName 数据库名称
     * @param columnList    列名集合（有则插入相应列）
     */
    private void refreshGreenPlumData(File dataFile, String fullTableName, List<String> columnList) throws RefreshDataException
    {
        String gpFile = greenPlumServerBean.getGpDataFilePath() + dataFile.getName();
        String host = greenPlumServerBean.getGpServer();
        String user = greenPlumServerBean.getGpServerUser();
        String password = greenPlumServerBean.getGpServerPassword();
        String port = String.valueOf(greenPlumServerBean.getGpServerPort());
        RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(host, user, password, port, "sftp");
        try
        {
            String localFilePath = dataFile.getAbsolutePath();

            LOG.info("Start Move To GP Server ... File name : " + localFilePath);
            fileOperateService.uploadFile(remoteServerDomain, localFilePath, greenPlumServerBean.getGpDataFilePath());
            LOG.info("End Move To GP Server ... File name : " + localFilePath);

            // 删除数据文件
            dataFile.delete();
            //greenPlumFileName 和本地文件名相同，没有重命名
            String greenPlumFileName = dataFile.getName();

            LOG.info("Start Refresh GP Data ... Table name : " + fullTableName);
            copyFileDataToGreenPlum(greenPlumFileName, fullTableName, columnList);
            LOG.info("End Refresh GP Data ... Table name : " + fullTableName);
        }
        catch (RefreshDataException e)
        {
            throw e;
        }
        finally
        {
            fileOperateService.deleteFile(remoteServerDomain, gpFile);
        }
    }

    /**
     * COPY文件数据入GP
     *
     * @param greenPlumFileName GP数据文件
     * @param fullTableName     数据库名称
     * @param columnList        列名集合（有则插入相应列）
     */
    private void copyFileDataToGreenPlum(String greenPlumFileName, String fullTableName, List<String> columnList) throws RefreshDataException
    {
        greenPlumOperateService.insertDataFromFile(fullTableName, greenPlumServerBean.getGpDataFilePath() + greenPlumFileName, columnList);
    }

    /**
     * 刷新数据库备份
     *
     * @param fullTableName 数据库表名
     */
    private void metaTableBackup(String fullTableName)
    {
        String[] schemaAndTable = fullTableName.split("\\.");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // 备份刷新元数据表
        String backTableName = "backup." + schemaAndTable[1] + "_" + TimeUtil.formatDateToYMD(calendar.getTime());
        if (!greenPlumOperateService.isExistsTable(backTableName))
        {
            greenPlumOperateService.update("create table " + backTableName + " as ( select * from " + fullTableName + ")");
        }
        else
        {
            if (greenPlumOperateService.dropTable(backTableName))
            {
                greenPlumOperateService.update("create table " + backTableName + " as ( select * from " + fullTableName + ")");
            }
        }

        // 删除三天前备份的元数据表
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        backTableName = "backup." + schemaAndTable[1] + "_" + TimeUtil.formatDateToYMD(calendar.getTime());
        if (greenPlumOperateService.isExistsTable(backTableName))
        {
            // 删除表
            greenPlumOperateService.dropTable(backTableName);
        }
    }
}