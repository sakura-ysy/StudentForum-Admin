/**
 * Created by Ysy
 * User: Ysy
 * Date: 2021/8/31
 * Description: 日志记录
 */
 import axios from "../axios"
 import logUrls from "../urls/log"
 
// 分页获取操作日志列表
export function fetchOperLogList (pageNo, size, tab) {
return axios.request(({
    url: logUrls.operLogList,
    method: 'get',
    params:{
    pageNo: pageNo,
    size: size,
    tab: tab
    },
}))
}

// 获取操作日志详情
export function fetchOperLogInfo (id) {
    return axios.request(({
        url: logUrls.operLogInfo,
        method: 'get',
        params:{
        id: id
        },
    }))
}

// 按日期查询操作Log
export function fetchOperLogByTime(pageNo, size,startTime,endTime){
    return axios.request(({
        url:logUrls.operLogSearchByTime,
        method: 'get',
        params:{
          pageNo: pageNo,
          size: size,
          startTime: startTime,
          endTime: endTime
        }
      }))
}


// 分页获取异常日志列表
export function fetchExcLogList (pageNo, size, tab) {
    return axios.request(({
        url: logUrls.excLogList,
        method: 'get',
        params:{
        pageNo: pageNo,
        size: size,
        tab: tab
        },
    }))
}

// 获取操作日志详情
export function fetchExcLogInfo (id) {
    return axios.request(({
        url: logUrls.excLogInfo,
        method: 'get',
        params:{
        id: id
        },
    }))
}

// 按日期查询操作Log
export function fetchExcLogByTime(pageNo, size,startTime,endTime){
    return axios.request(({
        url:logUrls.excLogSearchByTime,
        method: 'get',
        params:{
          pageNo: pageNo,
          size: size,
          startTime: startTime,
          endTime: endTime
        }
      }))
}
