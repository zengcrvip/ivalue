package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.axon.market.common.domain.iscene.TaskForUserBlackListDomain;
import com.axon.market.common.domain.iscene.UserBlackListDomain;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.dao.mapper.iscene.IUserBlackListMapper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Created by wangtt on 2017/3/6.
 */
@Service("userBlackListService")
public class UserBlackListService
{
    private static final Logger LOG = Logger.getLogger(UserBlackListService.class);

    Gson gs = new Gson();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    @Qualifier("userBlackListDao")
    private IUserBlackListMapper userBlackListDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil encryptUtil;

    /**
     * 处理任务名
     *
     * @param ids
     * @return
     */
    public String handleTaskName(String ids) throws DataFormatException
    {
        if (ids.length() > 0)
        {
//            ids = "," + ids + ",";
            String[] tids = ids.split(",");
            if (Integer.parseInt(tids[0]) == -1)
            {
                return "[所有任务]";//-1代表屏蔽所有任务
            }
            /*
                查询出所有的场景导航任务ID和name
                集合遍历组装数组 map<id,name>
                最后拼接字符串
             */
            List<MarketJobDomain> taskList = userBlackListDao.queryTaskName();
            Map<Integer, String> taskMap = new HashMap<>();
            StringBuilder text = new StringBuilder("");
            for (MarketJobDomain mj : taskList)
            {
                taskMap.put(mj.getId(), mj.getName());
            }
            for (String id : tids)
            {
                int taskId = Integer.parseInt(id);
                text.append(taskMap.get(taskId) + ",");
            }
            String res = text.toString();
            return res.substring(0, res.length() - 1);
        }
        return "";
    }

    /**
     * 查询
     *
     * @param offset
     * @param limit
     * @param mob
     * @return
     */
    public Table<UserBlackListDomain> queryUserBlackList(Integer offset, Integer limit, String mob)
    {
        try
        {
            int count = userBlackListDao.queryUserBlackCount(mob);
            List<UserBlackListDomain> list = userBlackListDao.queryUserBlackList(offset, limit, mob);
            List<UserBlackListDomain> newList = new ArrayList<>();
            for (UserBlackListDomain ud : list)
            {
                //处理taskID
                String ids = ud.getTaskId();
                ud.setTaskName(handleTaskName(ids));
                //解密手机号
                String mobNum = encryptUtil.decrypt(ud.getMobile());
                ud.setMobile(mobNum);
                newList.add(ud);
            }
            return new Table<UserBlackListDomain>(newList, count);
        }
        catch (Exception e)
        {
            LOG.error("获取用户黑名单异常" + e);
            return new Table<UserBlackListDomain>();
        }
    }

    /**
     * 新增
     *
     * @param params
     * @return
     */
    public Operation addUserBlackList(Map<String, String> params)
    {
        String mobile = params.get("Mob");

        //region ===手机号码加密===
        if (StringUtils.isEmpty(mobile))
        {
            return new Operation(false, "未能获取您输入的手机号");
        }
        String mobNum = encryptUtil.encrypt(mobile);
        //endregion ===手机号码加密结束

        String taskId = params.get("TaskId");
        String blockStart = params.get("BlockStartTime");
        String blockEnd = params.get("BlockEndTime");
        String blockType = params.get("BlockType");//屏蔽类型
        String time = params.get("Time");//屏蔽周期类型
        try
        {
            int type = Integer.parseInt(blockType);

            //region ===屏蔽周期日期转换===
            if (type == 1)
            {
                Calendar cal = Calendar.getInstance();

                blockStart = format.format(cal.getTime());
                switch (time)
                {
                    case "1":
                        cal.add(Calendar.MONTH, 1);
                        blockEnd = format.format(cal.getTime());
                        break;
                    case "2":
                        cal.add(Calendar.MONTH, 2);
                        blockEnd = format.format(cal.getTime());
                        break;
                    case "3":
                        cal.add(Calendar.MONTH, 6);
                        blockEnd = format.format(cal.getTime());
                        break;
                    case "4":
                        cal.add(Calendar.YEAR, 1);
                        blockEnd = format.format(cal.getTime());
                        break;
                }
            }
            //endregion  ===屏蔽周期日期转换结束===

            //taskId 处理
            String[] taskIdArr = taskId.split(",");
            //region 循环任务id，调接口创建屏蔽任务
            for (String tid : taskIdArr)
            {
                HttpUtil httpUtil = HttpUtil.getInstance();
                Map<String, Object> table = new Hashtable<>();
                table.put("operation", 1);//新增1||删除2
                table.put("mob", Long.parseLong(mobNum));
                table.put("taskId", Integer.parseInt(tid));
                table.put("blockStm", blockStart);
                table.put("blockEtm", blockEnd);
                String jsonStr = gs.toJson(table);
                Map<String, String> map = new HashMap<>();
                map.put("message", jsonStr);
                String res = httpUtil.sendHttpPost(interfaceBean.getScenePilotUrl(), map);
                Map m = gs.fromJson(res, Map.class);
                if (!"00000".equals(m.get("resultCode")))
                {
                    if ("00003".equals(m.get("resultCode")))
                    {
                        return new Operation(false, "该手机已经配置过相同的屏蔽任务了，请勿重复配置！");
                    }
                    return new Operation(false, "远程接口新增屏蔽任务失败");
                }
            }
            //endregion 接口调用结束
            //region 接口创建成功后本地入库
            //数据组装
            UserBlackListDomain ub = new UserBlackListDomain();
            ub.setTaskId(taskId);
            ub.setMobile(mobNum);
            ub.setBlockStart(format.parse(blockStart));
            ub.setBlockEnd(format.parse(blockEnd));
            ub.setIsDelete(0);
            ub.setBlockType(type);
            int res = userBlackListDao.addUserBlackList(ub);
            if (res <= 0)
            {
                return new Operation(false, "本地入库失败,新增失败");
            }
            //endregion 本地入库结束

        }
        catch (ParseException e)
        {
            LOG.error("新增异常", e);
            return new Operation(false, "出现异常，新增失败");
        }
        return new Operation(true, "新增成功");
    }

    /**
     * 查询任务列表
     *
     * @param offset
     * @param limit
     * @param taskName
     * @return
     */
    public Table<TaskForUserBlackListDomain> queryTaskForUserBlackList(Integer offset, Integer limit, String taskName)
    {
        try
        {
            List<TaskForUserBlackListDomain> list = userBlackListDao.queryTaskForUserBlackList(offset, limit, taskName);
            int count = userBlackListDao.queryTaskCount(taskName);
            return new Table<TaskForUserBlackListDomain>(list, count);
        }
        catch (Exception e)
        {
            LOG.error("查询任务列表异常", e);
            return new Table<TaskForUserBlackListDomain>();
        }
    }

    /**
     * 删除用户黑名单
     *
     * @param id
     * @return
     */
    public Operation deleteUserBlackList(Integer id)
    {
        try
        {
            //region 先调用接口删除
            String mobile = userBlackListDao.queryMobById(id);
            if (StringUtils.isEmpty(mobile))
            {
                return new Operation(false, "该手机号不存在！");
            }
            HttpUtil httpUtil = HttpUtil.getInstance();
            Map<String, Object> table = new Hashtable<>();
            table.put("ID", id);
            table.put("Mob", mobile);
            table.put("operation", 2);//新增1||删除2
            String jsonStr = gs.toJson(table);
            Map<String, String> map = new HashMap<>();
            map.put("message", jsonStr);
            String result = httpUtil.sendHttpPost(interfaceBean.getScenePilotUrl(), map);
            Map p = gs.fromJson(result, Map.class);
            if (!"00000".equals(p.get("resultCode")))
            {
                return new Operation(false, "远程接口删除失败");
            }
            //endregion 接口调用结束
            //region 本地库删除
            int res = userBlackListDao.delUserBlackList(id);
            if (res > 0)
            {
                return new Operation(true, ReturnMessage.DELETE_SUCCESS);
            }
            return new Operation(false, ReturnMessage.DELETE_FAILED);
            //endregion 本地库删除结束
        }
        catch (Exception e)
        {
            LOG.error("删除失败", e);
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

}
