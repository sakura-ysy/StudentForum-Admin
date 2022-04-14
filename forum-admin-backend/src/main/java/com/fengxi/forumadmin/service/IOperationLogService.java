package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.entity.OperationLog;
import com.fengxi.forumadmin.model.vo.PostVO;

public interface IOperationLogService extends IService<OperationLog> {
    /**
     * 获取操作LOG列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<OperationLog> getOperLogList(Page<OperationLog> page, String tab);

    /**
     * 按日期获取操作LOG列表
     *
     * @param page
     * @return
     */
    Page<OperationLog> getOperLogListByTime(Page<OperationLog> page, String startTime, String endTime);
}
