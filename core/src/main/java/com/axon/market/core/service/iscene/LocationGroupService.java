package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.LocationDomain;
import com.axon.market.common.domain.iscene.LocationGroupDomain;

import com.axon.market.common.domain.iscene.ScenePilotDomain;
import com.axon.market.common.domain.iscene.TaskDomain;
import com.axon.market.dao.mapper.iscene.ILocationGroupMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/1/3.
 */
@Service("locationGroupService")
public class LocationGroupService
{
    private static final Logger LOG = Logger.getLogger(LocationGroupService.class.getName());

    @Autowired
    @Qualifier("locationGroupDao")
    private ILocationGroupMapper locationGroupDao;

    /**
     * 查询区域群组列表
     *
     * @param offset
     * @param limit
     * @param groupName
     * @return
     */
    public Table<LocationGroupDomain> getLocalGroup(Integer offset, Integer limit, String groupName)
    {
        List<LocationGroupDomain> list = null;
        int i = 0;
        try
        {
            list = locationGroupDao.getLocationGroupList(offset, limit, groupName);
            i = locationGroupDao.getLocationGroupListCount(offset, limit, groupName);
        }
        catch (Exception e)
        {
            LOG.error("查询区域群组列表异常 ：" + e.toString());
            e.printStackTrace();
            return new Table<LocationGroupDomain>();
        }
        return new Table<LocationGroupDomain>(list, i);
    }

    /**
     * 新增区域群组名称
     *
     * @param name
     * @param session
     * @return
     */
    public Operation addLocationGroup(String name, HttpSession session)
    {
        LocationGroupDomain localGroup = new LocationGroupDomain();
        localGroup.setIsDelete(0);
        localGroup.setName(name);
        localGroup.setCount(0);
        localGroup.setProvinceId(0);
//        localGroup.setCreateId(UserUtils.getLoginUser(session).getId());
        //暂时写死
        localGroup.setCreateId(7);
        localGroup.setTableName(StringUtils.EMPTY);
        int i = 0;
        try
        {
            i = locationGroupDao.addLocationGroup(localGroup);
        }
        catch (Exception e)
        {
            LOG.error("新增区域群组失败 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
        return i > 0 ? new Operation(true, ReturnMessage.ADD_SUCCESS)
                : new Operation(false, ReturnMessage.ADD_FAILED);
    }

    /**
     * 修改区域群组名
     *
     * @param id
     * @param name
     * @return
     */
    public Operation editLocationGroup(Integer id, String name)
    {
        try
        {
            return locationGroupDao.editLocationGroup(id, name) > 0 ? new Operation(true, ReturnMessage.EDIT_SUCCESS)
                    : new Operation(false, ReturnMessage.EDIT_FAILED);
        }
        catch (Exception e)
        {
            LOG.error("修改区域群组名失败 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }

    }

    /**
     * 获取地区列表
     *
     * @param offset
     * @param limit
     * @param tableName
     * @return
     */
    public Table getLocationList(Integer offset, Integer limit, String tableName)
    {
        List<LocationDomain> list = new ArrayList<LocationDomain>();
        int count = 0;
        try
        {
            locationGroupDao.createLocationTable(tableName);
            list = locationGroupDao.queryLocationList(offset, limit, tableName);
            count = locationGroupDao.queryLocationListCount(tableName);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("创建或查询地区表失败 : " + e.toString());
            e.printStackTrace();
            return new Table(new ArrayList<LocationDomain>(), 0);
        }
    }

    /**
     * 删除地区列表数据
     *
     * @param id
     * @param tableName
     * @return
     */
    public Operation delLocation(Integer id, String tableName)
    {
        try
        {
            if (locationGroupDao.delLocation(id, tableName) <= 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
            if (locationGroupDao.updateAfterDelete(tableName) <= 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
            return new Operation(true, ReturnMessage.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            LOG.error("删除地区列表数据异常 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

    /**
     * 删除地区群组数据
     *
     * @param id
     * @return
     */
    public Operation delLocalGroup(Integer id)
    {
        try
        {
            //判断是否包含在task表中
            List<ScenePilotDomain> list = locationGroupDao.queryTaskList();
            for (ScenePilotDomain task : list)
            {
                String ids = task.getLocationGroupIds();
                String bStr = new String("," + ids + ",");
                String sStr = new String("," + id + ",");
                if (bStr.contains(sStr))
                {
                    return new Operation(false, ReturnMessage.DELETE_FAILED_HASTASK);
                }
            }
            //不包含则逻辑删除
            if (locationGroupDao.delLocationGroup(id) <= 0)
            {
                return new Operation(false, ReturnMessage.DELETE_FAILED);
            }
            return new Operation(true, ReturnMessage.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            LOG.error("删除地区群组失败 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

    /**
     * 上传文件数据到库
     *
     * @param templeFile
     * @param isHead
     * @param uploadName
     * @param session
     * @return
     */
    public Operation addsLocation(String templeFile, boolean isHead, String uploadName, HttpSession session)
    {
        LineIterator iterator = null;
        File newFile = null;
        List<String> bufferList = new ArrayList<String>();//设置一个集合缓存数据
        try
        {
            newFile = FileUtils.getFile(templeFile);
            InputStream in = new FileInputStream(newFile);
            byte[] b = new byte[3];
            in.read(b);
            in.close();
            if (b[0] == -17 && b[1] == -69 && b[2] == -65)//编码判断，暂时支持UTF-8和GBK
            {
                iterator = FileUtils.lineIterator(newFile, "UTF-8");
            }
            else
            {
                iterator = FileUtils.lineIterator(newFile, "GBK");
            }
            if (isHead)//包含标题行
            {
                if (iterator.hasNext())
                {
                    String headTxt = iterator.next();
                    System.out.print(headTxt);//打印第一行标题
                }
                while (iterator.hasNext())
                {
                    String line = iterator.next();
                    bufferList.add(line);
                }
            }
            else//不包含标题行
            {
                while (iterator.hasNext())
                {
                    String line = iterator.next();
                    bufferList.add(line);
                }
            }

            /*创建location子表*/
            Map<String, Object> map = createLocationTable(uploadName, session);
            if (!(boolean) map.get("isOk"))
            {
                return new Operation(false, ReturnMessage.UPLOAD_FAILED);
            }
            String tableName = (String) map.get("tableName");

            /*将缓存的数据插入数据库对应的表中*/
            return upLoadHelper(bufferList, tableName) ? new Operation(true, ReturnMessage.UPLOAD_SUCCESS)
                    : new Operation(false, ReturnMessage.UPLOAD_FAILED);

        }
        catch (Exception e)
        {
            LOG.error("上传文件数据到库异常 ：" + e.toString());
            e.printStackTrace();
            return new Operation(false, "");
        }
        finally
        {
            if (iterator != null)
            {
                iterator.close();
            }
            FileUtils.deleteQuietly(newFile);
        }
    }


    /*step1
     *自定义创建location子表工具类
     * */
    public Map<String, Object> createLocationTable(String uploadName, HttpSession session) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        String name = uploadName.split("-")[0].trim();
        List<LocationGroupDomain> list = locationGroupDao.queryLocationGroupList(name);
        if (list.size() <= 0 || list == null)
        {//查不到，新增
            LocationGroupDomain localGroup = new LocationGroupDomain();
            localGroup.setIsDelete(0);
            localGroup.setName(name);
            localGroup.setCount(0);
            localGroup.setProvinceId(0);
//            localGroup.setCreateId(UserUtils.getLoginUser(session).getId());
            //暂时写死
            localGroup.setCreateId(7);
            localGroup.setTableName(StringUtils.EMPTY);
            if (locationGroupDao.addLocationGroup(localGroup) <= 0)
            {
                map.put("isOk", false);
                return map;
            }
        }
        //有了再查询
        locationGroupDao.queryLocationGroupList(name).size();
        LocationGroupDomain localGroup = locationGroupDao.queryLocationGroupList(name).get(0);
        String tableName = "location" + localGroup.getId();
        if (!tableName.equals(localGroup.getTableName()))
        {
            //tabelName 不符合规范则修改符合规范
            if (locationGroupDao.updateTableName(tableName, localGroup.getId()) < 0)
            {
                map.put("isOk", false);
                return map;
            }
        }
        //判断地区群组是否删除,如果删除则清空旧表，更新整条数据
        if (localGroup.getIsDelete() == 1)
        {
            locationGroupDao.deleteTable(tableName);
            //更新地区群组信息
            LocationGroupDomain newGroup = new LocationGroupDomain();
            newGroup.setCount(0);
            newGroup.setIsDelete(0);
            newGroup.setProvinceId(0);//暂时写死
            newGroup.setId(localGroup.getId());
            if (locationGroupDao.updateLocationGroup(newGroup) <= 0)
            {
                map.put("isOk", false);
                return map;
            }
        }
        //根据tableName创建表
        locationGroupDao.createLocationTable(tableName);
        //判断是否已存在该列，如果已存在则删除
        //code.......
        map.put("isOk", true);
        map.put("tableName", tableName);
        return map;
    }


    /*step2
     *自定义上传工具类
     * */
    public boolean upLoadHelper(List<String> list, String tableName) throws ClassCastException
    {
        List<LocationDomain> beanList = new ArrayList<LocationDomain>();//location对象容器
        if (list.size() == 0)
        {
            return false;
        }
        for (String line : list)
        {//遍历出每一行的数据
            String[] elements = line.split(",");
            //OID`,`City`,`TimeSpan`,`No`,`Class1`,`Class2`,`Class3`,`Class4`,`SceneId`,`SceneName`,
            // `ParentScene`,`SceneDescribe`,`ScenePosition`,`Scenelong`,`Scenelat`,`Coverage`,`IsReport`,
            // `Province`,`AdminRegion`,`GeoRegion`,`RegionName`,`NetWorkType`,`Remarks
            LocationDomain location = new LocationDomain();
            location.setOID(elements[0].trim());
            location.setCity(elements[1].trim());
            location.setTimeSpan(elements[2].trim());
            location.setNo(elements[3].trim());
            location.setClass1(elements[4].trim());
            location.setClass2(elements[5].trim());
            location.setClass3(elements[6].trim());
            location.setClass4(elements[7].trim());
            location.setSceneId(elements[8].trim());
            location.setSceneName(elements[9].trim());
            location.setParentScene(elements[10].trim());
            location.setSceneDescribe(elements[11].trim());
            location.setScenePosition(elements[12].trim());
            location.setScenelong(elements[13].trim());
            location.setScenelat(elements[14].trim());
            location.setCoverage(elements[15].trim());
            location.setIsReport(Integer.parseInt(elements[16].trim()));
            location.setProvince(elements[17].trim());
            location.setAdminRegion(elements[18].trim());
            location.setGeoRegion(elements[19].trim());
            location.setRegionName(elements[20].trim());
            location.setNetWorkType(elements[21].trim());
            location.setRemarks(elements[22].trim());
            //把对象放入容器
            beanList.add(location);
            if (beanList.size() % 50 == 0)
            {//每50个提交一次
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("list", beanList);
                map.put("tableName", tableName);
                int i = locationGroupDao.batchUploadLocation(map);
                if (i <= 0)
                {
                    return false;
                }
                beanList.clear();
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", beanList);
        map.put("tableName", tableName);
        int i = locationGroupDao.batchUploadLocation(map);
        if (i <= 0)
        {
            return false;
        }
        beanList.clear();
        //插入完更新count字段
        if (locationGroupDao.updateCountAfterAdd(tableName, tableName) <= 0)
        {
            return false;
        }
        //最后返回true
        return true;
    }

}
