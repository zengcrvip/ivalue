package com.axon.market.core.service.greenplum;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.exception.RefreshDataException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/13.
 */
@Service("greenPlumOperateService")
public class GreenPlumOperateService
{
    private static final Logger LOG = Logger.getLogger(GreenPlumOperateService.class.getName());

    private static final String MODEL_DATA_TABLE_NAME_PREFIX = "model_";

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("greenPlumServerBean")
    private GreenPlumServerBean greenPlumServerBean;

    /**
     * @return
     */
    public static GreenPlumOperateService getInstance()
    {
        return (GreenPlumOperateService) SpringUtil.getSingletonBean("greenPlumOperateService");
    }

    /**
     * @param modelDomain
     * @return
     */
    public String getModelDataTableName(ModelDomain modelDomain)
    {
        return greenPlumServerBean.getGpCurrentSchemaName() + "." + MODEL_DATA_TABLE_NAME_PREFIX + modelDomain.getId();
    }

    /**
     * 根据模型主键ID获取GP对应的模型表
     *
     * @param modelId
     * @return
     */
    public String getModelDataTableName(String modelId)
    {
        return greenPlumServerBean.getGpCurrentSchemaName() + "." + MODEL_DATA_TABLE_NAME_PREFIX + modelId;
    }

    /**
     * @param fullTableName
     * @return
     */
    public boolean isExistsTable(String fullTableName)
    {
        String[] table = fullTableName.split("\\.");
        return isExistsTable(table[0], table[1]);
    }

    /**
     * @param schemaName
     * @param tableName
     * @return
     */
    public boolean isExistsTable(String schemaName, String tableName)
    {
        String schemaValue = schemaName;
        if (StringUtils.isEmpty(schemaValue))
        {
            schemaValue = greenPlumServerBean.getGpCurrentSchemaName();
        }
        String checkSql = String.format("select count(1) from information_schema.tables where table_schema = '%s' and table_name = '%s' ", schemaValue, tableName);
        return jdbcTemplate.queryForObject(checkSql, Integer.class) > 0;
    }

    /**
     * @param fullTableName
     * @return
     */
    public boolean dropTable(String fullTableName)
    {
        try
        {
            if (StringUtils.isNotEmpty(fullTableName))
            {
                String sql = "drop table if exists " + fullTableName;
                jdbcTemplate.execute(sql);
            }
        }
        catch (Exception e)
        {
            LOG.error("Drop Table error : tableName = " + fullTableName, e);
            return false;
        }
        return true;
    }

    /**
     * @param fullTableName
     * @return
     */
    public boolean truncateTable(String fullTableName)
    {
        try
        {
            if (StringUtils.isNotEmpty(fullTableName))
            {
                String sql = "truncate " + fullTableName;
                jdbcTemplate.execute(sql);
                return true;
            }
        }
        catch (Exception e)
        {
            LOG.error("Truncate Table error : tableName = " + fullTableName, e);
        }
        return false;
    }

    /**
     * @param fullTableName
     * @param fileName
     * @param columns
     * @return
     */
    public boolean insertDataFromFile(String fullTableName, String fileName, List<String> columns) throws RefreshDataException
    {
        try
        {
            StringBuffer sql = new StringBuffer();
            sql.append("copy ");
            sql.append(fullTableName);
            if (CollectionUtils.isNotEmpty(columns))
            {
                sql.append(" (");
                for (String column : columns)
                {
                    sql.append(column);
                    sql.append(",");
                }
                sql.deleteCharAt(sql.lastIndexOf(","));
                sql.append(")");
            }
            sql.append(" from '");
            sql.append(fileName);
            sql.append("' delimiter '");
            sql.append(greenPlumServerBean.getGpDelimiterChar());
            sql.append("' NULL as ''");
            // sql.append("' csv quote '\"';");
            jdbcTemplate.execute(sql.toString());
        }
        catch (Exception e)
        {
            LOG.error("Insert Data From File error:", e);
            throw new RefreshDataException(e.getMessage());
        }
        return true;
    }

    /**
     * @param fullTableName
     * @return
     */
    public boolean createModelTable(String fullTableName)
    {
        try
        {
            StringBuffer sql = new StringBuffer("create table " + fullTableName);
            sql.append("(");
            sql.append("phone varchar(64),area_id varchar(20)");
            sql.append(")");
            jdbcTemplate.execute(sql.toString());
        }
        catch (Exception e)
        {
            LOG.error("Create Table Error : Table Name = " + fullTableName, e);
            return false;
        }
        return true;
    }

    /**
     * 创建用户资源管理(广西定制）表时，由于字段的不确定性，除手机号码外，其余字段均定义为c1,c2,c3,....
     * @param fullTableName
     * @return
     */
    public boolean createResourceTable(String fullTableName, int columnLength)
    {
        try
        {
            StringBuffer sql = new StringBuffer("create table " + fullTableName);
            sql.append("(");
            sql.append("phone varchar(128)");
            for (int i = 1; i< columnLength; i++)
            {
                sql.append(",c"+i+" varchar(128)");
            }
            sql.append(")");
            jdbcTemplate.execute(sql.toString());
        }
        catch (Exception e)
        {
            LOG.error("Create Table Error : Table Name = " + fullTableName, e);
            return false;
        }
        return true;
    }

    /**
     * 创建分组营销任务数据表
     * @param taskTableName
     * @return
     */
    public boolean createGroupingTaskTable(String taskTableName)
    {
        try
        {
            StringBuffer sql = new StringBuffer("create table " + taskTableName);
            sql.append("(");
            sql.append("phone varchar(64),area_id varchar(20),send_date varchar(10)");
            sql.append(")");
            jdbcTemplate.execute(sql.toString());
        }
        catch (Exception e)
        {
            LOG.error("Create Table Error : Table Name = " + taskTableName, e);
            return false;
        }
        return true;
    }

    /**
     * @param fullTableName
     * @return
     */
    public int queryTableRecordCount(String fullTableName)
    {
        return queryRecordCount("select count(1) from " + fullTableName);
    }

    /**
     * @param sql
     * @return
     */
    public int queryRecordCount(String sql)
    {
        return jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
    }

    /**
     * @param sql
     * @return
     */
    public List<Map<String, Object>> query(String sql)
    {
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * @param sql
     * @return
     */
    public Map<String, Object> queryForMap(String sql)
    {
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * @param sql
     * @return
     */
    public int update(final String sql)
    {
        return jdbcTemplate.update(sql);
    }

    /**
     * @param querySql
     * @param rowCallbackHandler
     * @param fetchSize
     */
    public void query(String querySql, RowCallbackHandler rowCallbackHandler, int fetchSize)
    {
        jdbcTemplate.setFetchSize(fetchSize);
        jdbcTemplate.query(querySql, rowCallbackHandler);
    }

    /**
     * @param schema
     * @param tableName
     * @param columnInfos
     * @return
     */
    public int createGreenPlumTable(String schema, String tableName, List<String> columnInfos)
    {
        StringBuffer sql = new StringBuffer();
        if (StringUtils.isEmpty(schema))
        {
            schema = greenPlumServerBean.getGpCurrentSchemaName();
        }
        sql.append("create table ").append(schema).append(".").append(tableName);
        sql.append("(").append(StringUtils.join(columnInfos, ",")).append(")");

        return update(sql.toString());
    }

    /**
     * @param schema
     * @param tableName
     * @param columnInfos
     * @return
     */
    public int createTableDistubute(String schema, String tableName, String distributedColumn, List<String> columnInfos)
    {
        StringBuffer sql = new StringBuffer();
        if (StringUtils.isEmpty(schema))
        {
            schema = greenPlumServerBean.getGpCurrentSchemaName();
        }
        sql.append("create table ").append(schema).append(".").append(tableName);
        sql.append("(").append(StringUtils.join(columnInfos, ",")).append(")");
        sql.append("distributed by (").append(distributedColumn).append(")");

        return update(sql.toString());
    }

    /**
     * @param oldTableName
     * @param newTableName
     */
    public void rename(String schema, String oldTableName, String newTableName)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("drop table if exists ").append(schema).append(".").append(newTableName).append(";");
        update(sql.toString());
        StringBuffer renamesql = new StringBuffer();
        renamesql.append("alter table ").append(schema).append(".").append(oldTableName).append(" rename to ").append(newTableName);
        update(renamesql.toString());
    }

    /**
     * 将表数据下载到文件，注意wheresql的字符必须带where
     *
     * @param fullTableName
     * @param fileName
     * @param columns
     * @return
     */
    public boolean downloadDataFromTable(String fullTableName, String fileName, List<String> columns, String wheresql, String delimiter)
    {
        try
        {
            StringBuffer sql = new StringBuffer();
            sql.append("copy (select ");
            if (CollectionUtils.isNotEmpty(columns))
            {
                for (String column : columns)
                {
                    sql.append(column);
                    sql.append(",");
                }
                sql.deleteCharAt(sql.lastIndexOf(","));
            } else {
                sql.append("*");
            }
            sql.append(" from ").append(fullTableName).append(" ").append(wheresql).append(")");
            sql.append(" to '").append(fileName).append("' delimiter '").append(delimiter).append("'");
            jdbcTemplate.execute(sql.toString());
        }
        catch (Exception e)
        {
            LOG.error("Download Data To File error:", e);
            return false;
        }
        return true;
    }
}
