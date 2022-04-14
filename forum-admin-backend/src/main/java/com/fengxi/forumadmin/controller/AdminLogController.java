package com.fengxi.forumadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.model.entity.ExceptionLog;
import com.fengxi.forumadmin.model.entity.OperationLog;
import com.fengxi.forumadmin.model.vo.PostVO;
import com.fengxi.forumadmin.service.IExceptionLogService;
import com.fengxi.forumadmin.service.IOperationLogService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/admin/log")
public class AdminLogController {

    @Resource
    private IOperationLogService iOperationLogService;
    @Resource
    private IExceptionLogService iExceptionLogService;

    /**
     * 操作日志列表
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/oper/list")
    public ApiResult<Page<OperationLog>> getOperLogList(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                                         @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<OperationLog> list = iOperationLogService.getOperLogList(new Page<>(pageNo, pageSize), tab);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 单个操作日志详情
     * @param id
     * @return
     */
    @RequestMapping("/oper")
    public ApiResult<OperationLog> getOperLogInfo(@RequestParam("id") String id){
        OperationLog operLog = iOperationLogService.getBaseMapper().selectById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        operLog.setTimeString(sdf.format(operLog.getOperCreateTime()));
        return ApiResult.success(operLog);
    }

    /**
     * 按日期查询操作Log列表
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/oper/search/time")
    public ApiResult<Page<OperationLog>> getOperLogListByTime(@RequestParam(value = "startTime", defaultValue = "2021-08-30") String startTime,
                                                        @RequestParam(value = "endTime", defaultValue = "2050-08-30") String endTime,
                                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<OperationLog> list = iOperationLogService.getOperLogListByTime(new Page<>(pageNo, pageSize), startTime, endTime);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 操作日志列表
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/exception/list")
    public ApiResult<Page<ExceptionLog>> getExceptionLogList(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<ExceptionLog> list = iExceptionLogService.getExcLogList(new Page<>(pageNo, pageSize), tab);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 单个异常日志详情
     * @param id
     * @return
     */
    @RequestMapping("/exception")
    public ApiResult<ExceptionLog> getExceptionLogInfo(@RequestParam("id") String id){
        ExceptionLog exceptionLog = iExceptionLogService.getBaseMapper().selectById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        exceptionLog.setTimeString(sdf.format(exceptionLog.getOperCreateTime()));
        return ApiResult.success(exceptionLog);
    }


    /**
     * 按日期查询操作Log列表
     * @param startTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/exception/search/time")
    public ApiResult<Page<ExceptionLog>> getExceptionLogListByTime(@RequestParam(value = "startTime", defaultValue = "2021-08-30") String startTime,
                                                              @RequestParam(value = "endTime", defaultValue = "2050-08-30") String endTime,
                                                              @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                                              @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<ExceptionLog> list = iExceptionLogService.getExceptionLogListByTime(new Page<>(pageNo, pageSize), startTime, endTime);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }
}
