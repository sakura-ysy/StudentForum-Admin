/**
 * Created by Ysy.
 * User: Ysy
 * Date: 2021/8/27
 * Description: 权限管理
 */
 import axios from "../axios"
 import auditUrls from "../urls/audit"
 
 // 分页获取待审文章
export function fetchArticleList (pageNo, size, tab) {
return axios.request(({
    url: auditUrls.getNotAuditedArticles,
    method: 'get',
    params:{
    pageNo: pageNo,
    size: size,
    tab: tab
    },
}))
}

// 审核文章
export function auditArticle (postId, isPass) {
    return axios.request(({
        url: auditUrls.auditArticle,
        method: 'put',
        params:{
        postId: postId,
        isPass: isPass
        },
    }))
}

 // 分页获取待审评论
 export function fetchCommentList (pageNo, size, tab) {
    return axios.request(({
        url: auditUrls.getNotAuditedComments,
        method: 'get',
        params:{
        pageNo: pageNo,
        size: size,
        tab: tab
        },
    }))
}


// 审核评论
export function auditComment (commentId, isPass) {
    return axios.request(({
        url: auditUrls.auditComment,
        method: 'put',
        params:{
        commentId: commentId,
        isPass: isPass
        },
    }))
}