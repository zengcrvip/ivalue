package com.axon.market.core.service.itag;

import com.axon.market.common.bean.BaseTableBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.constant.isystem.MarketSystemElementCreateEnum;
import com.axon.market.common.constant.isystem.ModelStatusEnum;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.ResourcesUserDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.dao.mapper.itag.IResourcesUserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by yuanfei on 2017/8/28.
 */
@Component("resourcesUserService")
public class ResourcesUserService
{
    private static final Logger LOG = Logger.getLogger(ResourcesUserService.class.getName());

    @Autowired
    @Qualifier("resourcesUserDao")
    private IResourcesUserMapper resourcesUserDao;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String fullTableName = "model.resource_";

    private BaseTableBean baseTableBean = BaseTableBean.getInstance();

    public static ResourcesUserService getInstance()
    {
        return (ResourcesUserService) SpringUtil.getSingletonBean("resourcesUserService");
    }

    /**
     *
     * @return
     */
    public int queryResourcesUsersCount(UserDomain userDomain)
    {
        return resourcesUserDao.queryResourcesUsersCount(userDomain.getAreaId(), userDomain.getRoleIds());
    }

    /**
     *
     * @param offset
     * @param maxRecord
     * @return
     */
    public List<ResourcesUserDomain> queryResourcesUsersByPage(Integer offset, Integer maxRecord, UserDomain userDomain)
    {
        return resourcesUserDao.queryResourcesUsersByPage(offset, maxRecord, userDomain.getAreaId(), userDomain.getRoleIds());
    }

    /**
     *
     * @param resourceId
     * @return
     */
    public ResourcesUserDomain queryResourceModelById(Integer resourceId)
    {
        return resourcesUserDao.queryResourceUserModelById(resourceId);
    }

    /**
     * 创建资源用户模型信息
     * @param request
     * @param userDomain
     * @return
     */
    public ServiceResult createResourcesUserModel(HttpServletRequest request,UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        ResourcesUserDomain resourcesUserDomain = new ResourcesUserDomain();
        MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
        resourcesUserDomain.setCreateUser(userDomain.getId());
        resourcesUserDomain.setTitle(request.getParameter("title"));
        resourcesUserDomain.setRemarks(request.getParameter("remarks"));
        resourcesUserDomain.setFileName(fileRequest.getFileMap().get("file").getOriginalFilename());
        resourcesUserDomain.setDataDate(TimeUtil.formatDateToYMD(new Date()));
        resourcesUserDomain.setRoleIds(request.getParameter("roleIds"));
        try
        {
            if (resourcesUserDao.insertResourcesUserModel(resourcesUserDomain) != 1)
            {
                result.setRetValue(-1);
                result.setDesc("数据库新增操作失败");
                return result;
            }

            LOG.info("Resource Model Insert Success ....");

            LOG.info("Start Upload Local File ....");
            String fileName = resourcesUserDomain.getId() + "@model.resource_" + resourcesUserDomain.getId() ;

            final File file = fileUploadService.fileUpload(request, fileName);
            LOG.info("End Upload Local File ...."+file.getName());

            if (file != null)
            {
                ThreadPoolUtil.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        refreshLocalImportResourceData(file);
                    }
                });
            }
        }
        catch (Exception e)
        {
            result.setRetValue(-1);
            result.setDesc("创建失败");
            LOG.error("Create Resource User Model Error. ", e);
        }

        return result;
    }

    /**
     * 删除模型资源信息
     * @param resourceId
     * @param userDomain
     * @return
     */
    public ServiceResult deleteResourceModel(Integer resourceId, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        if (userDomain.getAreaId() != 99999)
        {
            result.setRetValue(-1);
            result.setDesc("无删除权限");
            return result;
        }

        // 如果删除成功，那么删除GP中对应的表信息
        if (1 != resourcesUserDao.deleteResourceModel(resourceId))
        {
            result.setRetValue(-1);
            result.setDesc("删除失败");
            return result;
        }

        // 删除GP中对应的表
        if (greenPlumOperateService.isExistsTable(fullTableName + resourceId) && !greenPlumOperateService.dropTable(fullTableName + resourceId))
        {
            result.setRetValue(-1);
            result.setDesc("GP数据删除失败");
            return result;
        }
        return result;
    }

    /**
     * 修改，只修改标题，权限和备注
     * @param resourcesUserDomain
     * @return
     */
    public ServiceResult editResourceModel(ResourcesUserDomain resourcesUserDomain)
    {
        ServiceResult result = new ServiceResult();
        if (1 != resourcesUserDao.editResourceModel(resourcesUserDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据修改失败");
        }
        return result;
    }

    /**
     * 上传文件移动到监控文件目录下，从而进行数据的导入
     *
     * @param file
     */
    public void refreshLocalImportResourceData(File file)
    {
        LOG.info("Start Refresh Local Import Resource User Model ... File Name = " + file.getName());
        try
        {
            // 移动后的目标文件名称
            String targetFileName = systemConfigBean.getMonitorPath() + file.getName();
            File targetFile = new File(targetFileName);
            // 文件移动
            FileUtils.moveFile(file, targetFile);
            // 文件改名，改为监控执行的文件名
            targetFile.renameTo(new File(targetFileName + ".resource"));
        }
        catch (Exception e)
        {
            LOG.error("Local Resource User Model File Move Error. ", e);
        }
        LOG.info("End Refresh Local Import Resource User Model ... File Name = " + file.getName());
    }

    /**
     * @param response
     * @param userDomain
     * @param resourceId
     * @throws IOException
     */
    public void downloadResourceModel(HttpServletResponse response, UserDomain userDomain, Integer resourceId) throws IOException
    {
        ResourcesUserDomain resourcesUserDomain = resourcesUserDao.queryResourceUserModelById(resourceId);
        String fileName = resourcesUserDomain.getFileName();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + "_" + userDomain.getAreaName() + "_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");
        writeDataToStream(response, userDomain.getAreaCode(), userDomain.getId(), resourcesUserDomain);
    }

    /**
     *
     * @param resourceId
     * @param refreshTime
     * @param result
     * @param totalCount
     * @param successCount
     * @param failCount
     * @return
     */
    public Integer updateResourceRefreshInfo(Integer resourceId, String refreshTime, String result, Integer totalCount, Integer successCount, Integer failCount, String columns)
    {
        return resourcesUserDao.updateResourceRefreshInfo(resourceId, refreshTime, result, totalCount, successCount, failCount, columns);
    }

    /**
     *
     * @param response
     * @param areaCode
     * @param userId
     * @param modelDomain
     * @return
     * @throws IOException
     */
    private Long writeDataToStream(HttpServletResponse response, final Integer areaCode, Integer userId, final ResourcesUserDomain modelDomain) throws IOException
    {
        final OutputStream os = response.getOutputStream();
        final Long[] count = {0L};
        try
        {
            Integer batchSize = 50000;
            final String[] querySql = createQuerySegmentSql(modelDomain, areaCode, userId);

            LOG.info("specialQuerySql:" + querySql[0]);

            os.write(querySql[1].getBytes("GBK"));

            greenPlumOperateService.query(querySql[0], new RowCallbackHandler()
            {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException
                {
                    String phone = AxonEncryptUtil.getInstance().decryptWithoutCountrycode(resultSet.getString("phone"));
                    String downColumnValue = phone + "\t," + resultSet.getString("value") + "\r\n";
                    try
                    {
                        os.write(downColumnValue.getBytes("GBK"));
                        count[0]++;
                    }
                    catch (IOException e)
                    {
                        LOG.error("", e);
                    }
                }
            }, batchSize);
        }
        catch (Exception e)
        {
            LOG.error("writeDataToStream error", e);
        }
        finally
        {
            if (os != null)
            {
                os.close();
            }
        }
        return count[0];
    }

    /**
     * 查询表对应的字段
     * @param resourceId
     * @return
     */
    private String[] queryTableColumn(Integer resourceId)
    {
        String columns = resourcesUserDao.queryTableColumnsById(resourceId);
        return columns.split("[|]");
    }

    /**
     * 获取表的字段
     * @param resourcesModelDomain
     * @param areaCode
     * @param userId
     * @return
     */
    private String[] createQuerySegmentSql(ResourcesUserDomain resourcesModelDomain, Integer areaCode, Integer userId)
    {
        // 表名称
        String mainTableName = "resource_" + resourcesModelDomain.getId();
        boolean isProvincial = areaCode == 99999;

        String[] result = new String[2];

        String[] columnNames = queryTableColumn(resourcesModelDomain.getId());
        // 下载客户群相关属性字段
        List<String> columnInfo = new LinkedList<String>();
        String condition = "";

        for (int i = 1;i < columnNames.length; i++)
        {
            columnInfo.add("coalesce(cast(model." + mainTableName + ".c" + i + " as varchar) || '\\t'  , '')");
            if (!isProvincial)
            {
                condition = ("cast(model." + mainTableName + ".c1 as varchar) = " + "'" + areaCode + "'");
            }
        }

        result[0] = createQuerySegmentSql(mainTableName, columnInfo, condition);
        result[1] = StringUtils.join(columnNames, ",") + "\r\n";

        return result;
    }

    private String createQuerySegmentSql(String mainTableName, List<String> columnInfo,String conditions)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct phone").append(" as phone,");
        sql.append(StringUtils.join(columnInfo, " || ',' || ")).append(" as value");
        sql.append(" from ").append("model.").append(mainTableName);
        if (StringUtils.isNotEmpty(conditions))
        {
            sql.append(" where (").append(conditions).append(")");
        }
        return sql.toString();
    }
}
