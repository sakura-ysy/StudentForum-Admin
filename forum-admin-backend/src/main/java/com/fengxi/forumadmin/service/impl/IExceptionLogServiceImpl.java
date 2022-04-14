package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.ExceptionLogMapper;
import com.fengxi.forumadmin.model.entity.ExceptionLog;
import com.fengxi.forumadmin.model.entity.OperationLog;
import com.fengxi.forumadmin.service.IExceptionLogService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class IExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements IExceptionLogService {
    /**
     * 分页获取异常Log列表
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<ExceptionLog> getExcLogList(Page<ExceptionLog> page, String tab) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询log
        Page<ExceptionLog> iPage = this.baseMapper.selectListAndPage(page, tab);
        iPage.getRecords().forEach(operLog ->{
            String format = sdf.format(operLog.getOperCreateTime());
            operLog.setTimeString(format);
        });
        return iPage;
    }

    /**
     * 按日期获取异常Log列表
     * @param page
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Page<ExceptionLog> getExceptionLogListByTime(Page<ExceptionLog> page, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询log
        Page<ExceptionLog> iPage = this.baseMapper.selectListAndPageByTime(page,startTime,endTime);
        iPage.getRecords().forEach(operLog ->{
            String format = sdf.format(operLog.getOperCreateTime());
            operLog.setTimeString(format);

        });
        return iPage;
    }
}
