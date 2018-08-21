package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iscene.ExtStopCondConfig;
import com.axon.market.common.domain.iscene.TempleTypeDomain;
import com.axon.market.common.domain.iscene.UrlGroupDomain;
import com.axon.market.common.domain.isystem.MonitorConfigDomain;
import com.axon.market.common.domain.isystem.MonitorConfigEmailDomain;
import com.axon.market.common.domain.isystem.MonitorConfigPhoneDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.dao.mapper.isystem.IMonitorConfigMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2017/4/17.
 */
@Component("monitorConfigService")
public class MonitorConfigService
{
    private String templeFile;

    private static final Logger LOG = Logger.getLogger(MonitorConfigService.class.getName());

    @Autowired
    @Qualifier("monitorConfigDao")
    private IMonitorConfigMapper monitorConfigDao;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 列表查询
     * @param param
     * @return
     */
    public Table queryMonitorConfig(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            String serverIp = SearchConditionUtil.optimizeCondition(param.get("serverIp"));
            Integer count = monitorConfigDao.queryMonitorConfigCounts(serverIp);
            List<MonitorConfigDomain> list = monitorConfigDao.queryMonitorConfig(start, length, serverIp);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query queryMonitorConfig Error. ", e);
            return new Table();
        }
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param userDomain
     * @return
     */
    public Operation addOrEditMonitor(Map<String, Object> paras, UserDomain userDomain)
    {
        String serverIp = String.valueOf(paras.get("serverIp"));
        String port = String.valueOf(paras.get("port"));
        String serverAccount = String.valueOf(paras.get("serverAccount"));
        String serverPassWord = String.valueOf(paras.get("serverPassWord"));
        String type = String.valueOf(paras.get("type"));
        String expected = String.valueOf(paras.get("expected"));
        String format = String.valueOf(paras.get("format"));
        String timeTick = String.valueOf(paras.get("timeTick"));
        String dataBaseName = String.valueOf(paras.get("dataBaseName"));
        String messageContent = String.valueOf(paras.get("messageContent"));
        String emailPath = String.valueOf(paras.get("emailPath"));
        String phonePath = String.valueOf(paras.get("phonePath"));
        String id = String.valueOf(paras.get("id"));
        try
        {

            MonitorConfigDomain model = new MonitorConfigDomain();
            model.setServerIp(serverIp);
            model.setPort(port);
            model.setServerAccount(serverAccount);
            model.setServerPassWord(serverPassWord);
            model.setType("".equals(type) ? 0 : Integer.parseInt(type));
            model.setExpected(expected);
            model.setFormat(format);
            model.setTimeTick("".equals(timeTick) ? 0 : Integer.parseInt(timeTick));
            model.setDataBaseName(dataBaseName);
            model.setMessageContent(messageContent);
            model.setEmailPath(emailPath);
            model.setPhonePath(phonePath);
            int monitorId = ("".equals(id) ? 0 : Integer.parseInt(id));
            model.setId(monitorId);

            boolean result;
            String msg;
            if (monitorId > 0) //修改
            {
                result = monitorConfigDao.editMonitor(model) == 1;
                if(result&&emailPath!=""&&emailPath!=""&&!emailPath.contains(": "))
                {
                    //先删除之前附加的
                    //monitorConfigDao.deleteEmailAccount(model.getId());
                    batchUpload(emailPath, monitorId, "1");
                }
                if(result&&phonePath!=""&&!phonePath.contains(": "))
                {
                    //monitorConfigDao.deleteMessagePhone(model.getId());
                    batchUpload(phonePath,monitorId,"2");
                }
                msg = result ? "修改成功" : "修改失败";
                if(result)
                {
                    sendMessage(interfaceBean.getMonitorConfigUrl(),model.getId());
                }
            }
            else
            {
                result = monitorConfigDao.addMonitor(model) == 1;
                //更新email
                if(result)
                {
                    int maxId=monitorConfigDao.selectMaxId();
                    if(result&&emailPath!="")
                    {
                        batchUpload(emailPath,maxId,"1");
                    }
                    if(result&&phonePath!="")
                    {
                        batchUpload(phonePath,maxId,"2");
                    }
                    if(result)
                    {
                        sendMessage(interfaceBean.getMonitorConfigUrl(), maxId);
                    }
                }
                msg = result ? "新增成功" : "新增失败";
            }
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            FileUtils.deleteQuietly(FileUtils.getFile(emailPath));
            FileUtils.deleteQuietly(FileUtils.getFile(phonePath));
            FileUtils.deleteQuietly(FileUtils.getFile(templeFile));
            return new Operation(false, "");
        }


    }

    /**
     * 删除监控配置
     *
     * @param paras
     * @return
     */
    public Operation deleteMonitor(Map<String, Object> paras)
    {
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        boolean result = monitorConfigDao.deleteMonitor(delId) == 1;
        if(result)
        {
            monitorConfigDao.deleteEmailAccount(delId);
            monitorConfigDao.deleteMessagePhone(delId);
        }
        String msg = result ? "删除成功" : "删除失败";
        sendMessage(interfaceBean.getMonitorConfigUrl(),delId);
        return new Operation(result, msg);
    }

    /**
     * 批量上传
     *
     * @param fileNamePath
     * @return
     */
    public boolean batchUpload(String fileNamePath,int id,String type)
    {
        try
        {
            if (type.equals("1"))//email
            {

                monitorConfigDao.batchUploadEmail(fileNamePath,id);//批量提交
                //批量更新monitor_syncinfo_id
                if(id!=0)
                {
                    monitorConfigDao.UpdateEmail(id);
                }
                LOG.info("====批量提交结束=======");
                //count = monitorConfigDao.queryEmailCount(id);
            }
            else
            {
                monitorConfigDao.batchUploadPhone(fileNamePath,id);//批量提交
                //批量更新monitor_syncinfo_id
                if(id!=0)
                {
                    monitorConfigDao.UpdatePhone(id);
                }
                LOG.info("====批量提交结束=======");
                //count = monitorConfigDao.queryEmailCount(id);
            }

            return true;
        }
        catch (Exception e)
        {
            LOG.error("监控设置 导入email或phone txt异常：" + "或者更新email或phone异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        finally
        {
            FileUtils.deleteQuietly(FileUtils.getFile(fileNamePath));
            FileUtils.deleteQuietly(FileUtils.getFile(templeFile));
        }
    }

    //上传文件
    public Operation uploadUrlFile(String fileName, String orgFileName, HttpServletRequest request)
    {
        LineIterator iterator = null;
        File file = null;
        File newFile = null;
        try
        {
            file = fileUploadService.fileUpload(request, "temple.txt");
            templeFile = file.getPath();
            newFile = new File(request.getSession().getServletContext().getRealPath(fileName));
            List<String> newList = new ArrayList<String>();
            InputStream in = new FileInputStream(file);
            byte[] b = new byte[3];
            in.read(b);
            in.close();
            if (b[0] == -17 && b[1] == -69 && b[2] == -65)//编码判断，暂时支持UTF-8和GBK
            {
                iterator = FileUtils.lineIterator(file, "UTF-8");
            }
            else
            {
                iterator = FileUtils.lineIterator(file, "GBK");
            }
            int count = 0;
            //逐行读取
            while (iterator.hasNext())
            {
                String line = iterator.next();
                String newLine = line + "," ;//重新排版
                newList.add(newLine);
                count++;
                if (count % 1000 == 0)
                {
                    FileUtils.writeLines(newFile, "UTF-8", newList, true);
                    newList.clear();
                    count = 0;
                }
            }
            FileUtils.writeLines(newFile, "UTF-8", newList, true);
        }
        catch (IOException e)
        {
            LOG.error("文件上传IO异常：" + e.toString());
            e.printStackTrace();
            return new Operation(false, "");
        }
        finally
        {
            if (iterator != null)
            {
                LineIterator.closeQuietly(iterator);
            }
        }
        return new Operation(true, "");
    }

    public void sendMessage(String url,int id)
    {
        try
        {
            HttpUtil http = HttpUtil.getInstance();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("authorize", "gloomysw@axon2014");

            String result = http.sendHttpPostByHeader(url + "unitMonitorConfigInit?monitorId="+id, headers);
            //ResultModel json = JsonUtil.stringToObject(result, ResultModel.class);
        }
        catch (Exception ex)
        {
            LOG.error("sendMessage error:", ex);
        }
    }
    /**
     * 根据id查询监控配置
     * @param param
     * @return
     */
    public Table queryMonitorById(Map<String, Object> param)
    {
        try
        {
            String id = String.valueOf(param.get("id"));
            int sid = "".equals(id) ? 0 : Integer.parseInt(id);
            List<MonitorConfigDomain> list = monitorConfigDao.queryMonitorById(sid);
            return new Table(list,1);
        }
        catch (Exception e)
        {
            LOG.error("Query queryMonitorById Error. ", e);
            return new Table();
        }
    }

    /**
     * 查询导入的email和phone的数量
     * @param paras
     * @return
     */
    public Operation queryEmailOrPhone(Map<String, Object> paras)
    {
        try
        {
            String id = String.valueOf(paras.get("id"));
            Integer queryId = ("".equals(id) ? 0 : Integer.parseInt(id));
            int emailCount = monitorConfigDao.queryEmailCount(queryId);
            int phoneCount=monitorConfigDao.queryPhoneCount(queryId);
            return new Operation(true, emailCount+"|"+phoneCount);
        }
        catch (Exception ex)
        {
            LOG.error("queryEmailOrPhone error:", ex);
            return new Operation(false, 0+"|"+0);
        }
    }

    /**
     * 查询导入的email的数量
     * @param paras
     * @return
     */
    public Operation queryEmail(Map<String, Object> paras)
    {
        try
        {
            String id = String.valueOf(paras.get("id"));
            Integer queryId = ("".equals(id) ? 0 : Integer.parseInt(id));
            int emailCount = monitorConfigDao.queryEmailCount(queryId);
            return new Operation(true, String.valueOf(emailCount));
        }
        catch (Exception ex)
        {
            LOG.error("queryEmail error:", ex);
            return new Operation(false, 0+"|"+0);
        }
    }

    /**
     * 查询导入的Phone的数量
     * @param paras
     * @return
     */
    public Operation queryPhone(Map<String, Object> paras)
    {
        try
        {
            String id = String.valueOf(paras.get("id"));
            Integer queryId = ("".equals(id) ? 0 : Integer.parseInt(id));
            int phoneCount=monitorConfigDao.queryPhoneCount(queryId);
            return new Operation(true,String.valueOf(phoneCount));
        }
        catch (Exception ex)
        {
            LOG.error("queryPhone error:", ex);
            return new Operation(false, 0+"|"+0);
        }
    }


    /**
     * 列表查询邮箱
     *
     * @param param
     * @return
     */
    public Table queryEmailList(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            Integer id = Integer.parseInt(param.get("id"));
            Integer count = monitorConfigDao.queryEmailListCounts(id);
            List<MonitorConfigEmailDomain> list = monitorConfigDao.queryEmailList(start, length, id);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query queryEmailList Error. ", e);
            return new Table();

        }
    }

    /**
     * 列表查询号码
     *
     * @param param
     * @return
     */
    public Table queryPhoneList(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            Integer id = Integer.parseInt(param.get("id"));
            Integer count = monitorConfigDao.queryPhoneListCounts(id);
            List<MonitorConfigPhoneDomain> list = monitorConfigDao.queryPhoneList(start, length, id);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query queryPhoneList Error. ", e);
            return new Table();

        }
    }

    /**
     * 删除邮箱
     *
     * @param paras id 邮箱id
     * @return
     */
    public Operation deleteEmail(Map<String, Object> paras)
    {
        //邮件Id
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        //监控Id
        String monitorId=String.valueOf(paras.get("monitorId"));
        int mId=("".equals(monitorId) ? 0 : Integer.parseInt(monitorId));
        boolean result = monitorConfigDao.deleteEmail(delId) == 1;
        String msg = result ? "删除成功" : "删除失败";
        sendMessage(interfaceBean.getMonitorConfigUrl(),mId);
        return new Operation(result, msg);
    }

    /**
     * 删除号码
     *
     * @param paras id 号码id
     * @return
     */
    public Operation deletePhone(Map<String, Object> paras)
    {
        //号码Id
        String id = String.valueOf(paras.get("id"));
        int delId = ("".equals(id) ? 0 : Integer.parseInt(id));
        //监控Id
        String monitorId=String.valueOf(paras.get("monitorId"));
        int mId=("".equals(monitorId) ? 0 : Integer.parseInt(monitorId));
        boolean result = monitorConfigDao.deletePhone(delId) == 1;
        String msg = result ? "删除成功" : "删除失败";
        sendMessage(interfaceBean.getMonitorConfigUrl(),mId);
        return new Operation(result, msg);
    }

    /**
     * 新增/修改 email
     *
     * @param paras
     * @return
     */
    public Operation addOrEditEmail(Map<String, Object> paras)
    {
        String email = String.valueOf(paras.get("email"));
        String id = String.valueOf(paras.get("id"));
        String monitorId=String.valueOf(paras.get("monitorId"));
        try
        {

            MonitorConfigEmailDomain model = new MonitorConfigEmailDomain();
            model.setEmail(email);
            model.setMonitorSyncinfoId(("".equals(monitorId) ? 0 : Integer.parseInt(monitorId)));
            int emailId = ("".equals(id) ? 0 : Integer.parseInt(id));
            model.setId(emailId);

            boolean result;
            String msg;
            if (emailId > 0) //修改
            {
                result = monitorConfigDao.editEmail(model) == 1;
                msg = result ? "修改成功" : "修改失败";
            }
            else
            {
                result = monitorConfigDao.addEmail(model) == 1;
                msg = result ? "新增成功" : "新增失败";
            }
            if(result)
            {
                sendMessage(interfaceBean.getMonitorConfigUrl(),model.getMonitorSyncinfoId());
            }
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("evtOnSaveEmail error:", ex);
            return new Operation(false, "");
        }


    }

    /**
     * 新增/修改 phone
     *
     * @param paras
     * @return
     */
    public Operation addOrEditPhone(Map<String, Object> paras)
    {
        String phone = String.valueOf(paras.get("phone"));
        String id = String.valueOf(paras.get("id"));
        String monitorId=String.valueOf(paras.get("monitorId"));
        try
        {

            MonitorConfigPhoneDomain model = new MonitorConfigPhoneDomain();
            model.setPhone(phone);
            model.setMonitorSyncinfoId(("".equals(monitorId) ? 0 : Integer.parseInt(monitorId)));
            int emailId = ("".equals(id) ? 0 : Integer.parseInt(id));
            model.setId(emailId);

            boolean result;
            String msg;
            if (emailId > 0) //修改
            {
                result = monitorConfigDao.editPhone(model) == 1;
                msg = result ? "修改成功" : "修改失败";
            }
            else
            {
                result = monitorConfigDao.addPhone(model) == 1;
                msg = result ? "新增成功" : "新增失败";
            }
            if(result)
            {
                sendMessage(interfaceBean.getMonitorConfigUrl(),model.getMonitorSyncinfoId());
            }
            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("evtOnSaveEmail error:", ex);
            return new Operation(false, "");
        }


    }
}


