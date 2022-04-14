package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.entity.ExceptionLog;
import com.fengxi.forumadmin.model.entity.OperationLog;

public interface IExceptionLogService extends IService<ExceptionLog> {
    /**
     * 获取异常LOG列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<ExceptionLog> getExcLogList(Page<ExceptionLog> page, String tab);

    /**
     * 按日期获取异常LOG列表
     *
     * @param page
     * @return
     */
    Page<ExceptionLog> getExceptionLogListByTime(Page<ExceptionLog> page, String startTime, String endTime);
}
