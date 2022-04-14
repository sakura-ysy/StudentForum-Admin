package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.OperationLogMapper;
import com.fengxi.forumadmin.model.entity.OperationLog;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.model.vo.PostVO;
import com.fengxi.forumadmin.service.IOperationLogService;
import com.fengxi.forumadmin.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@Service
public class IOperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Resource
    private IUserService iUserService;

    /**
     * 分页获取操作Log列表
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<OperationLog> getOperLogList(Page<OperationLog> page, String tab) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询log
        Page<OperationLog> iPage = this.baseMapper.selectListAndPage(page, tab);
        iPage.getRecords().forEach(operLog ->{
            String format = sdf.format(operLog.getOperCreateTime());
            operLog.setTimeString(format);

        });
        return iPage;
    }

    /**
     * 按日期获取操作Log列表
     * @param page
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Page<OperationLog> getOperLogListByTime(Page<OperationLog> page, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询log
        Page<OperationLog> iPage = this.baseMapper.selectListAndPageByTime(page,startTime,endTime);
        iPage.getRecords().forEach(operLog ->{
            String format = sdf.format(operLog.getOperCreateTime());
            operLog.setTimeString(format);

        });
        return iPage;
    }
}
