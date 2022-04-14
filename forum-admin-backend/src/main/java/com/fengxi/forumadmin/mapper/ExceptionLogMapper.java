package com.fengxi.forumadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.model.entity.ExceptionLog;
import com.fengxi.forumadmin.model.entity.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {
    /**
     * 分页查询异常Log列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<ExceptionLog> selectListAndPage(@Param("page") Page<ExceptionLog> page, @Param("tab") String tab);

    /**
     * 按时间查询操作Log列表
     * <p>
     *
     * @param page
     * @return
     */
    Page<ExceptionLog> selectListAndPageByTime(@Param("page") Page<ExceptionLog> page,String startTime, String endTime);
}
