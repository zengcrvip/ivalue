package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.TaskTodoDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/8/15.
 */
@Component("taskTodoDao")
public interface ITaskTodoMapper extends IMyBatisMapper {

    /**
     * 新增待办事项
     * @param taskTododomain
     * @return
     */
    void insertTodoTask(TaskTodoDomain taskTododomain);

    /**
     * 查询每天的待办事项个数
     * @return
     */
    List<Map<String, Object>> queryTodoTaskCount(@Param("userid") Integer userid);

    /**
     * 根据日期查询待办事项
     * @param createdate
     * @return
     */
    List<TaskTodoDomain> queryTodoTaskList(@Param("createdate") String createdate, @Param("userid") Integer userid);

    /**
     * 根据taskid删除待办事项
     * @param taskid
     * @return
     */
    void deleteTodoTask(@Param("taskid") int taskid);

    /**
     * 根据taskid更新为已读
     * @param taskid
     * @return
     */
    void updateTaskStatus(@Param("taskid") int taskid);

    /**
     * 查询未读待办任务个数
     * @param userid
     * @return
     */
    Integer queryUnreadTaskCount(@Param("userid") int userid);
}
