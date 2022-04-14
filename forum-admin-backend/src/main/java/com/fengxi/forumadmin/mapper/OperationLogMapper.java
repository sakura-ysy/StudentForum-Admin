package com.fengxi.forumadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.model.entity.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    /**
     * 分页查询操作Log列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<OperationLog> selectListAndPage(@Param("page") Page<OperationLog> page, @Param("tab") String tab);

    /**
     * 按时间查询操作Log列表
     * <p>
     *
     * @param page
     * @return
     */
    Page<OperationLog> selectListAndPageByTime(@Param("page") Page<OperationLog> page,String startTime, String endTime);
}
