package com.axon.market.core.service.iscene;

import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.UrlDomain;
import com.axon.market.common.domain.iscene.UrlGroupDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.dao.mapper.iscene.IUrlGroupMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2016/12/23.
 */
@Service("urlGroupService")
public class UrlGroupService
{

    private String templeFile;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    @Autowired
    @Qualifier("urlGroupDao")
    private IUrlGroupMapper urlGroupDao;

    private static final Logger LOG = Logger.getLogger(UrlGroupService.class.getName());

    /**
     * 获取网址分类数据
     *
     * @param offset
     * @param limit
     * @param groupName
     * @param urlName
     * @param urlWord
     * @return
     */
    public Table<UrlGroupDomain> getUrlGroupList(Integer offset, Integer limit,
                                                 String groupName, String urlName,
                                                 String urlWord)
    {
        if (StringUtils.isEmpty(groupName))
        {
            groupName = null;
        }
        if (StringUtils.isEmpty(urlName))
        {
            urlName = null;
        }
        if (StringUtils.isEmpty(urlWord))
        {
            urlWord = null;
        }
        List<UrlGroupDomain> list = new ArrayList<UrlGroupDomain>();
        int i = 0;
        try
        {
            i = urlGroupDao.queryUrlGroupCount(groupName, urlName, urlWord);
            if (i > 0)
            {
                list = urlGroupDao.queryUrlGroup(offset, limit, groupName, urlName, urlWord);
            }
        }
        catch (Exception e)
        {
            LOG.error("获取网址分类异常：" + e.toString());
            e.printStackTrace();
            return new Table<UrlGroupDomain>(list, i);
        }
        return new Table<UrlGroupDomain>(list, i);
    }

    /**
     * 获取网址数据
     *
     * @param offset
     * @param limit
     * @param id
     * @return
     */
    public Table<UrlDomain> queryUrlList(Integer offset, Integer limit, Integer id)
    {
        if (id == null || id == 0)
        {
            return new Table<UrlDomain>();
        }
        try
        {
            List<UrlDomain> list = urlGroupDao.queryUrl(offset, limit, id);
            int count = urlGroupDao.queryUrlCount(id);
            return new Table<UrlDomain>(list, count);
        }
        catch (Exception e)
        {
            LOG.error("获取网址信息异常：" + e.toString());
            e.printStackTrace();
            return new Table<UrlDomain>();
        }
    }

    /**
     * 查询导入类别的id值
     *
     * @param name
     * @param session
     * @return
     */
    public int selectId(String name, HttpSession session)
    {
        // UserDomain user = UserUtils.getLoginUser(session);
        UrlGroupDomain urlGroup = urlGroupDao.queryUrlByName(name);//根据类别名称查询url类别对象
        if (urlGroup == null)
        {//查不到则新建，获取新建id
            UrlGroupDomain urlGroupDomain = new UrlGroupDomain();
            urlGroupDomain.setName(name);
            urlGroupDomain.setIsDelete(0);
            urlGroupDomain.setCount(0);
            // urlGroupDomain.setCreateId(user.getId());
            //暂时写死
            urlGroupDomain.setCreateId(7);
            urlGroupDomain.setProvinceId(0);//数据库中没有这个字段
            urlGroupDao.returnAddId(urlGroupDomain);//返回新增的主键id
            int i = urlGroupDomain.getId();
            return i;
        }
        else
        {
            //查到直接获取id
            return urlGroup.getId();
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
            int id = selectId(orgFileName, request.getSession());
            //逐行读取
            while (iterator.hasNext())
            {
                String line = iterator.next();
                String newLine = line + "," + id;//重新排版
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


    /**
     * 批量上传
     *
     * @param fileNamePath
     * @param fileName
     * @param session
     * @return
     */
    public boolean batchUpload(String fileNamePath, String fileName, HttpSession session)
    {
        try
        {
            int id = selectId(fileName, session);//查ID，查不到就创建
            LOG.info("======批量提交开始======");
            urlGroupDao.batchUpload(fileNamePath);//批量提交
            LOG.info("====批量提交结束=======");
            int count = urlGroupDao.queryUrlCount(id);
            if (count > 0)
            {
                urlGroupDao.updateUrlGroup(count, fileName);//更新UrlGroup的count
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOG.error("网址分类 导入网址txt异常：" + "或者更新网址群组异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        finally
        {
            FileUtils.deleteQuietly(FileUtils.getFile(fileNamePath));
            FileUtils.deleteQuietly(FileUtils.getFile(templeFile));
        }
    }

    /**
     * 新增或修改网址列表名
     *
     * @param param
     * @param session
     * @return
     */
    public Operation addOrEditUrlGroup(Map<String, String> param, HttpSession session)
    {
        String name = param.get("Name");
        if (StringUtils.isEmpty(name))
        {
            return new Operation(false, ReturnMessage.EMPTY_CONTENT);
        }
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = null;
//        try
//        {
//            now = format.parse(new Date().toString());//获取当前时间
//        }
//        catch (ParseException e)
//        {
//            LOG.error("日期格式转换异常 ：" + e.toString());
//            e.printStackTrace();
//            return new Operation(false, ReturnMessage.ERROR);
//        }
        String uid = param.get("Id");
        Integer provinceId = 0;//暂时占位
        Integer id = StringUtils.isEmpty(uid) ? 0 : Integer.parseInt(uid);
        if (id > 0)
        {//修改
            try
            {
                return urlGroupDao.updateUrlGroupName(name, id, provinceId) > 0 ? new Operation(true, ReturnMessage.EDIT_SUCCESS) : new Operation(false, ReturnMessage.EDIT_FAILED);
            }
            catch (Exception e)
            {
                LOG.error("修改网址列表名失败 ：" + e.toString());
                e.printStackTrace();
                return new Operation(false, ReturnMessage.ERROR);
            }
        }
        else
        {//新增
            UrlGroupDomain urlGroup = new UrlGroupDomain();
            urlGroup.setProvinceId(0);//占位
//            urlGroup.setCreateId(UserUtils.getLoginUser(session).getId());
            //暂时写死
            urlGroup.setCreateId(7);
            urlGroup.setCount(0);
            urlGroup.setIsDelete(0);
            urlGroup.setName(name);
            try
            {
                return urlGroupDao.addUrlGroupName(urlGroup) > 0 ? new Operation(true, ReturnMessage.ADD_SUCCESS) : new Operation(false, ReturnMessage.ADD_FAILED);
            }
            catch (Exception e)
            {
                LOG.error("新增网址列表名失败 ：" + e.toString());
                e.printStackTrace();
                return new Operation(false, ReturnMessage.ERROR);
            }
        }
    }

    /**
     * 删除网址群组
     *
     * @param id
     * @return
     */
    public Operation deleteUrlGroup(Integer id)
    {
        try
        {
            //判断要删除的网址群组是否存在于未删除的配置任务中
            if (urlGroupDao.selectIsExistInTask(id) > 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED_HASTASK);
            }
            //判断网址群组表中是否存在该ID
            if (urlGroupDao.selectIsExistById(id) <= 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
            if (urlGroupDao.delUrlGroupById(id) <= 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
            return new Operation(true, ReturnMessage.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            LOG.error("删除网址群组异常 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

    /**
     * 删除单条数据
     *
     * @param id
     * @param groupId
     * @return
     */
    public Operation deleteUrl(Integer id, Integer groupId)
    {
        try
        {
            if (urlGroupDao.delUrlById(id) > 0)
            {
                Integer count = urlGroupDao.queryUrlCount(groupId);
                urlGroupDao.updateUrlGroupCount(count, groupId);
                return new Operation(true, ReturnMessage.DELETE_SUCCESS);
            }
            return new Operation(false, ReturnMessage.DELETE_FAILED);
        }
        catch (Exception e)
        {
            LOG.error("删除单挑url失败 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

    /**
     * 新增单条url数据
     *
     * @param url
     * @param urlGroupId
     * @return
     */
    public Operation saveUrl(UrlDomain url, Integer urlGroupId)
    {
        try
        {
            if (urlGroupDao.addUrl(url) <= 0)
            {
                return new Operation(false, ReturnMessage.ADD_FAILED);
            }
            if (urlGroupDao.selectIsExistById(urlGroupId) <= 0)
            {
                return new Operation(false, ReturnMessage.ADD_FAILED);
            }
            int count = urlGroupDao.queryUrlCount(urlGroupId);
            //更新网址群组的统计字段
            urlGroupDao.updateUrlGroupCount(count, urlGroupId);
            return new Operation(true, ReturnMessage.ADD_SUCCESS);
        }
        catch (Exception e)
        {
            LOG.error("新增单条数据异常 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }
}
