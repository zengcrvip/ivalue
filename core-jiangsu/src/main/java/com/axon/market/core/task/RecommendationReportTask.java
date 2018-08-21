package com.axon.market.core.task;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


/**
 * Created by Chris on 2017/3/30.
 */
public class RecommendationReportTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(RecommendationReportTask.class.getName());

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private FileOperateService fileOperateService = FileOperateService.getInstance();

    private RemoteServerService remoteServerService = RemoteServerService.getInstance();

    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    private RecommendationConfigBean recommendationConfigBean = RecommendationConfigBean.getInstance();

    private AxonEncryptUtil axonEncryptUtil = AxonEncryptUtil.getInstance();

    private byte[] columnByte = {0x01};

    private String columnSeparator = new String(columnByte);

    private byte[] lineByte = {0x0A};

    private String lineSeparator = new String(lineByte);

    @Override
    public void runBody()
    {
        LOG.info("Start Recommendation Report......");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String time = TimeUtil.formatDateToYMD(calendar.getTime());

        String sql = generateSql(time);
        LOG.info("Recommendation Report Sql : " + sql);
        final List<String> lineList = new ArrayList<String>();

        greenPlumOperateService.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException
            {
                String saleId = resultSet.getString("saleId"),
                        saleBoidId = resultSet.getString("saleBoidId"),
                        aimSubId = resultSet.getString("aimSubId"),
                        userId = resultSet.getString("userId"),
                        phone = resultSet.getString("phone"),
                        baseId = resultSet.getString("baseId"),
                        pushTime = resultSet.getString("pushTime"),
                        accessNumber = resultSet.getString("accessNumber");
                lineList.add(saleId + columnSeparator + saleBoidId + columnSeparator + aimSubId + columnSeparator + userId + columnSeparator
                        + axonEncryptUtil.decrypt(phone) + columnSeparator + baseId + columnSeparator + pushTime + columnSeparator + accessNumber);
            }
        }, 10000);

        if (CollectionUtils.isEmpty(lineList))
        {
            lineList.add("");
        }

        uploadFileToFtp(lineList, time);
    }

    private void uploadFileToFtp(List<String> lineList, String time)
    {
        LOG.info("Start Upload Recommendation Report File......");
        File directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
        if (!directory.exists())
        {
            directory.mkdir();
        }
        String fileName = directory.getPath() + File.separator + "DFD101" + time + "010001.AVL";
        LOG.info("Recommendation Report File Name : " + fileName);
        File file = new File(fileName);
        try
        {
            FileUtils.writeStringToFile(file, StringUtils.join(lineList, lineSeparator), "GBK");
            LOG.info("Recommendation Report File Name Exist : " + file.exists());
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), "21", "ftp");
            boolean result = fileOperateService.uploadFile(remoteServerDomain, fileName, "/feedback/" + file.getName());
            LOG.info("Upload Recommendation Report File Result : " + result);
        }
        catch (Exception e)
        {
            LOG.error("Generate Recommendation Execute Report File Error. ", e);
        }
        finally
        {
            try
            {
                if (file != null)
                {
                    FileUtils.deleteQuietly(file);
                }
                if (directory != null)
                {
                    FileUtils.deleteDirectory(directory);
                }
            }
            catch (Exception e)
            {
                LOG.error("Delete Directory Error. ", e);
            }
        }
    }

    private String generateSql(String time)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select ")
                .append("sale_id as saleId,")
                .append("sale_boid_id as saleBoidId,")
                .append("aim_sub_id as aimSubId,")
                .append("user_id as userId,")
                .append("serial_number as phone,")
                .append("depart_id as baseId,")
                .append("push_time as pushTime,")
                .append("sp_number as accessNumber")
                .append(" from dware.push_user_send")
                .append(" where timest = ").append("'").append(time).append("'");
        return sql.toString();
    }
}
