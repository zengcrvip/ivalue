package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 位置场景站点配置管理DAO
 * Created by zengcr on 2016/12/2.
 */
@Component("positionBaseDao")
public interface IPositionBaseMapper extends IMyBatisMapper
{
    /**
     * 获取位置基站站点总数
     * @param baseId 基站站点ID
     * @param baseName 基站站点名称
     * @param baseArea 基站站点所属城市
     * @return 总数
     */
    int queryPositionBaseTotal(@Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea);
    /**
     * 获取位置基站站点列表
     * @param offset 每次查询数量
     * @param limit  起始标记位
     * @param baseId  基站站点ID
     * @param baseName 基站站点名称
     * @param baseArea 基站站点所属城市
     * @return 站点列表
     */
    List<PositionBaseDomain> queryPositionBaseByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea);
    /**
     * 新建位置场景基站
     * 当站点ID存在时修改，不存在时新增
     * @param positionBaseDomain 基站站点对象实体
     * @return
     */
    int createPositionBase(PositionBaseDomain positionBaseDomain);
    /**
     * 修改位置场景基站
     * 当站点ID存在时修改，不存在时新增
     * @param positionBaseDomain 基站站点对象实体
     * @return
     */
    int updatePositionBase(PositionBaseDomain positionBaseDomain);
    /**
     * 根据主键ID查询位置基站
     * @param baseId 基站站点
     * @return
     */
    PositionBaseDomain queryPositionBaseById(@Param(value = "baseId") Integer baseId);

    /**
     * 根据ID查询营业厅信息(对应任务ID，对应任务名称)
     * @param baseId 基站站点
     * @return
     */
    List<Map<String,Object>> delTaskPositionBase(@Param(value = "baseId") Integer baseId);

    /**
     * 根据ID查询营业厅信息(对应管理员)
     * @param baseId 基站站点
     * @return
     */

    Map<String,Object> delUserPositionBase(@Param(value = "baseId") Integer baseId);
    /**
     * 根据ID备注删除原因，执行者，操作时间
     * @param input 删除原因
     * @param userId 执行者
     * @param baseId 基站站点
     * @return
     */
    int updPositionBase(@Param(value = "input") String input,@Param(value = "userId") String userId,@Param(value = "baseId") Integer baseId);
    /**
     * 根据ID删除位置场景
     * @param baseId 基站站点
     * @return
     */
    int deletePositionBaseById(@Param(value = "baseId") Integer baseId);
    /**
     * 查询基站详情
     * @param baseId 站点ID
     * @param baseName 站点名称
     * @return
     */
    List<Map<String,Object>> queryPositionBase(@Param(value = "baseId") String baseId, @Param(value = "baseName") String baseName, @Param(value = "baseArea") String baseArea, @Param(value = "createUserId") Integer createUserId, @Param(value = "baseIdArray") String baseIdArray, @Param(value = "cityCode") String cityCode,@Param(value="buscoding") String buscoding);
    /**
     * 插入导入EXECL文件行数据
     * @param paras
     * @return
     */
    int insertRow(Map<String, Object> paras);

    /**
     * 插入导入EXECL文件本身相关数据（文件名，大小，时间等）
     * @param paras
     * @return
     */
    int insertFile(Map<String, Object> paras);
    /**
     * 查询位置场景基站导入数据总数
     * @param fileId 文件ID
     * @return
     */
    int queryPositionBaseImportTotal(@Param(value = "fileId") Long fileId);
    /**
     * 查询位置场景基站分页导入数据展示列表
     * @param offset 每次查询数量
     * @param limit  起始标记位
     * @param fileId 文件ＩＤ
     * @return
     */
    List<Map<String,Object>> queryPositionBaseImport(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "fileId") Long fileId);

    /**
     * 查询位置场景基站导入数据
     * @param fileId
     * @return
     */
    List<Map<String,Object>> queryPositionBaseImportAll(@Param(value = "fileId") Long fileId);
    /**
     * * @param paras 文件ＩＤ
     * 保存导入基站数据
     * @return
     */
    int createPositionBaseImport(@Param(value = "dataList") List<Map<String, Object>> dataList);
}
