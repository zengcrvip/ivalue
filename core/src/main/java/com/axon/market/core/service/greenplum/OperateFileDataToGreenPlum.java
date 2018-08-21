package com.axon.market.core.service.greenplum;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.exception.RefreshDataException;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 2017/3/17.
 */
@Service("operateFileDataToGreenPlum")
public class OperateFileDataToGreenPlum
{
    private static final Logger LOG = Logger.getLogger(OperateFileDataToGreenPlum.class.getName());

    @Autowired
    @Qualifier("greenPlumServerBean")
    private GreenPlumServerBean greenPlumServerBean;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    private byte[] columnByte = {0x01};

    private String columnSeparator = new String(columnByte);

    public static OperateFileDataToGreenPlum getInstance()
    {
        return (OperateFileDataToGreenPlum) SpringUtil.getSingletonBean("operateFileDataToGreenPlum");
    }

    /**
     * @param iterator
     * @param greenPlumFile
     */
    public void dataRefresh(LineIterator iterator, File greenPlumFile, Integer phoneColumn) throws RefreshDataException
    {
        String greenPlumName = greenPlumFile.getName();
        String fullTableName = greenPlumName.contains("@") ? greenPlumName.substring(0, greenPlumName.indexOf("@")) : greenPlumName;

        List<String> lineList = new ArrayList<String>();
        // 记录数，GP设置一次批量导入文件行数
        int gpBatchCopyCount = greenPlumServerBean.getGpBatchCopyCount(), recordCount = 0;

        try
        {
            String line;

            while (iterator.hasNext())
            {
                line = iterator.next().replace(columnSeparator, "|");
                recordCount++;

                if (phoneColumn != null)
                {
                    String[] lineArray = line.split("\\|");
                    lineArray[phoneColumn - 1] = axonEncrypt.encrypt(lineArray[phoneColumn - 1]);
                    line = StringUtils.join(lineArray, "|");
                }

                lineList.add(line);

                if (recordCount % 10000 == 0)
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
        }
        catch (RefreshDataException e)
        {
            LOG.error("Refresh Shop Data Error. ", e);
        }
        finally
        {
            FileUtils.deleteQuietly(greenPlumFile);
            if (iterator != null)
            {
                iterator.close();
            }
        }
    }

    /**
     * 在dataRefresh基础上将参数added添加到每条文件记录后面，同时指定表字段
     * @param iterator
     * @param greenPlumFile
     */
    public void dataRefreshPlus(LineIterator iterator, File greenPlumFile, Integer phoneColumn, String added, String column) throws RefreshDataException
    {
        //表字段解析
        List<String> columnlist = new ArrayList<String>();
        if (!column.equals("")) {
            String[] tmp = column.split(",");
            for (String col:tmp){
                columnlist.add(col);
            }
        }
        String greenPlumName = greenPlumFile.getName();
        String fullTableName = greenPlumName.contains("@") ? greenPlumName.substring(0, greenPlumName.indexOf("@")) : greenPlumName;

        List<String> lineList = new ArrayList<String>();
        // 记录数，GP设置一次批量导入文件行数
        int gpBatchCopyCount = greenPlumServerBean.getGpBatchCopyCount(), recordCount = 0;

        try
        {
            String line;

            while (iterator.hasNext())
            {
                line = iterator.next().replace(columnSeparator, "|");
                recordCount++;

                if (phoneColumn != null)
                {
                    String[] lineArray = line.split("\\|");
                    lineArray[phoneColumn - 1] = axonEncrypt.encrypt(lineArray[phoneColumn - 1]);
                    line = StringUtils.join(lineArray, "|");
                }

                StringBuffer str = new StringBuffer(line);
                str.append(added);
                lineList.add(str.toString());

                if (recordCount % 10000 == 0)
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
                    refreshGreenPlumData(greenPlumFile, fullTableName, columnlist);
                    recordCount = 0;
                }
            }

            if (recordCount != 0)
            {
                createGreenPlumDataFile(greenPlumFile, lineList);
                lineList.clear();
                refreshGreenPlumData(greenPlumFile, fullTableName, columnlist);
            }
        }
        catch (RefreshDataException e)
        {
            LOG.error("Refresh Shop Data Error. ", e);
        }
        finally
        {
            FileUtils.deleteQuietly(greenPlumFile);
            if (iterator != null)
            {
                iterator.close();
            }
        }
    }

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

            // greenPlumFileName 和本地文件名相同，没有重命名
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

    private void copyFileDataToGreenPlum(String greenPlumFileName, String fullTableName, List<String> columnList) throws RefreshDataException
    {
        greenPlumOperateService.insertDataFromFile(fullTableName, greenPlumServerBean.getGpDataFilePath() + greenPlumFileName, columnList);
    }
}
