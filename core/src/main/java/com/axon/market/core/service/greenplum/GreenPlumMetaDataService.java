package com.axon.market.core.service.greenplum;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.greenplum.PropertyInfo;
import com.axon.market.common.domain.greenplum.TableColumnDomain;
import com.axon.market.common.domain.icommon.IdAndNameDomain;
import com.axon.market.core.service.itag.DimensionService;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/25.
 */
public class GreenPlumMetaDataService
{
    private static final Logger LOG = Logger.getLogger(GreenPlumMetaDataService.class.getName());

    private DimensionService dimensionService;

    /**
     * 忽略的系统schema
     */
    private List<String> ignoreSchemas;

    private JdbcTemplate jdbcTemplate;

    /**
     * 数据类型map
     */
    private Map<Integer, String> valueTypeMap;

    public DimensionService getDimensionService()
    {
        return dimensionService;
    }

    public void setDimensionService(DimensionService dimensionService)
    {
        this.dimensionService = dimensionService;
    }

    public List<String> getIgnoreSchemas()
    {
        return ignoreSchemas;
    }

    public void setIgnoreSchemas(List<String> ignoreSchemas)
    {
        this.ignoreSchemas = ignoreSchemas;
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer, String> getValueTypeMap()
    {
        return valueTypeMap;
    }

    public void setValueTypeMap(Map<Integer, String> valueTypeMap)
    {
        this.valueTypeMap = valueTypeMap;
    }

    /**
     * 获取连接数据库所有可用schema
     *
     * @return
     */
    public List<String> getSchemas()
    {
        List<String> result = new ArrayList<>();
        Connection conn = null;
        ResultSet resultSet = null;
        try
        {
            conn = jdbcTemplate.getDataSource().getConnection();
            resultSet = conn.getMetaData().getSchemas();
            while (resultSet.next())
            {
                result.add(resultSet.getString("TABLE_SCHEM").toLowerCase());
            }
        }
        catch (SQLException e)
        {
            LOG.error("Get GreenPlum Schemas Error. ", e);
        }
        finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Close Source Error. ", e);
            }
        }

        result.removeAll(ignoreSchemas);
        return result;
    }

    /**
     * 获取某模式下所有表
     *
     * @param schema 模式名
     * @return 表名
     */
    public List<String> getTableAndViews(String schema)
    {
        List<String> result = new ArrayList<String>();
        Connection conn = null;
        ResultSet resultSet = null;
        try
        {
            conn = jdbcTemplate.getDataSource().getConnection();
            resultSet = conn.getMetaData().getTables(conn.getCatalog(), schema, "%", new String[]{"TABLE"});
            while (resultSet.next())
            {
                result.add(resultSet.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e)
        {
            LOG.error("Get GreenPlum Table And Views Error. ", e);
        }
        finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Close Source Error. ", e);
            }
        }
        return result;
    }


    /**
     * 获取表列信息
     *
     * @param schema    模式名
     * @param tableName 表明
     * @return
     */
    public List<TableColumnDomain> getTableColumnInfo(String schema, String tableName)
    {
        List<TableColumnDomain> result = new ArrayList<TableColumnDomain>();
        Connection conn = null;
        ResultSet resultSet = null;
        try
        {
            conn = jdbcTemplate.getDataSource().getConnection();
            resultSet = conn.getMetaData().getColumns(conn.getCatalog(), schema, tableName, "%");
            while (resultSet.next())
            {
                TableColumnDomain columnDomain = new TableColumnDomain();
                columnDomain.setColumnName(resultSet.getString("COLUMN_NAME"));
                columnDomain.setDataType(valueTypeMap.get(resultSet.getInt("DATA_TYPE")));
                columnDomain.setRemarks(resultSet.getString("REMARKS"));
                result.add(columnDomain);
            }
        }
        catch (SQLException e)
        {
            LOG.error("Get GreenPlum Table Column Info Error. ", e);
        }
        finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Close Source Error. ", e);
            }
        }
        return result;
    }

    /**
     * 获取表列信息和维度
     *
     * @param schema
     * @param tableName
     * @return
     */
    public Table getColumnInfoAndDimension(String schema, String tableName)
    {
        Integer count = 0;
        List<IdAndNameDomain> idAndNameDomainList = dimensionService.queryAllDimensionIdAndNames();
        List<PropertyInfo> result = new ArrayList<>();

        Connection conn = null;
        ResultSet resultSet = null;
        try
        {
            conn = jdbcTemplate.getDataSource().getConnection();
            resultSet = conn.getMetaData().getColumns(conn.getCatalog(), schema, tableName, "%");
            while (resultSet.next())
            {
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setId(count);
                propertyInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                propertyInfo.setDataType(valueTypeMap.get(resultSet.getInt("DATA_TYPE")));
                propertyInfo.setRemarks(resultSet.getString("REMARKS"));
                propertyInfo.setSelect(idAndNameDomainList);
                result.add(propertyInfo);
                count++;
            }
        }
        catch (SQLException e)
        {
            LOG.error("Get GreenPlum ColumnInfo And Dimension Error. ", e);
        }
        finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Close Source Error. ", e);
            }
        }
        return new Table(result, count);
    }
}
