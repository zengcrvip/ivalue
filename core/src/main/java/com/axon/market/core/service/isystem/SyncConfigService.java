package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.MonitorConfigDomain;
import com.axon.market.common.domain.isystem.SyncConfigDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.dao.mapper.isystem.ISyncConfigMapper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2017/4/24.
 */
@Component("syncConfigService")
public class SyncConfigService
{
    private static final Logger LOG = Logger.getLogger(SyncConfigService.class.getName());

    @Autowired
    @Qualifier("syncConfigDao")
    private ISyncConfigMapper syncConfigDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 列表查询
     * @param param
     * @return
     */
    public Table querySyncConfig(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            String queryType = SearchConditionUtil.optimizeCondition(param.get("queryType"));
            Integer count = syncConfigDao.querySyncConfigCounts(queryType);
            List<SyncConfigDomain> list = syncConfigDao.querySyncConfig(start, length, queryType);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query querySyncConfig Error. ", e);
            return new Table();
        }
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @return
     */
    public Operation addOrEditSync(Map<String, Object> paras)
    {
        String mysqlDbName = String.valueOf(paras.get("mysqlDbName"));
        String mysqlTableName = String.valueOf(paras.get("mysqlTableName"));
        String gpDbName = String.valueOf(paras.get("gpDbName"));
        String frequency = String.valueOf(paras.get("frequency"));
        String gpTableName = String.valueOf(paras.get("gpTableName"));
        String selType = String.valueOf(paras.get("selType"));
        String syncField = String.valueOf(paras.get("syncField"));
        String syncFieldStr = String.valueOf(paras.get("syncFieldStr"));
        String ftpName = String.valueOf(paras.get("ftpName"));
        String delimit = String.valueOf(paras.get("delimit"));
        String id = String.valueOf(paras.get("id"));
        try
        {

            SyncConfigDomain model = new SyncConfigDomain();
            model.setMysqlDbName(mysqlDbName);
            model.setMysqlTableName(mysqlTableName);
            model.setGpDbName(gpDbName);
            model.setFrequency("".equals(frequency) ? 0 : Integer.parseInt(frequency));
            model.setGpTableName(gpTableName);
            model.setSyncType("".equals(selType) ? 0 : Integer.parseInt(selType));
            model.setSyncField(syncField);
            model.setSyncFieldStr(syncFieldStr);
            model.setFtpName(ftpName);
            model.setDelimit(delimit);
            int monitorId = ("".equals(id) ? 0 : Integer.parseInt(id));
            model.setId(monitorId);

            boolean result;
            String msg;
            if (monitorId > 0) //修改
            {
                result = syncConfigDao.editSync(model) == 1;
                msg = result ? "修改成功" : "修改失败";
            }
            else
            {
                result = syncConfigDao.addSync(model) == 1;
                msg = result ? "新增成功" : "新增失败";
            }
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            return new Operation(false, "");
        }


    }

    /**
     * 删除
     *
     * @param paras
     * @return
     */
    public Operation deleteSync(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        boolean result = syncConfigDao.deleteSync(delId) == 1;
        String msg = result ? "删除成功" : "删除失败";
        return new Operation(result, msg);
    }

    /**
     * 同步
     * @return
     */
    public Operation syncConfig()
    {
        try
        {
            String url=interfaceBean.getMonitorConfigUrl();
            HttpUtil http = HttpUtil.getInstance();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("authorize", "gloomysw@axon2014");

            String result = http.sendHttpPostByHeader(url + "unitSelectSynchronizeConfig", headers);
            return new Operation(true, "同步成功！");
        }
        catch (Exception ex)
        {
            LOG.error("syncConfig error:", ex);
            return new Operation(false, "同步失败！");
        }
    }

    /**
     * 列表查询
     * @param param
     * @return
     */
    public Table querySyncById(Map<String, Object> param)
    {
        try
        {
            String id = String.valueOf(param.get("id"));
            int sid = "".equals(id) ? 0 : Integer.parseInt(id);
            List<SyncConfigDomain> list = syncConfigDao.querySyncById(sid);
            return new Table(list,1);
        }
        catch (Exception e)
        {
            LOG.error("Query querySyncById Error. ", e);
            return new Table();
        }
    }
}
