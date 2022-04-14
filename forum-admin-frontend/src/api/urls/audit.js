import { auditArticle } from "../apis/audit";

/**
 * Created by Ysy.
 * User: Ysy
 * Date: 2021/9/24
 * Description: 内容审核
 */
 export default {
    // 分页全部待审文章
    getNotAuditedArticles:"/admin/audit/post/list",
    // 审核文章
    auditArticle: "/admin/audit/post",
    // 分页获取待审评论
    getNotAuditedComments:"/admin/audit/comment/list",
    // 审核评论
    auditComment:"/admin/audit/comment"
  }