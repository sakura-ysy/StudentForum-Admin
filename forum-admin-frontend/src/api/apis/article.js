/**
 * Created by WebStorm.
 * User: nirongxu
 * Date: 2020/4/20
 * Description: 文件描述
 */
import axios from "../axios"
import articleUrls from "../urls/article"

export default {
  // 文章修改回显
  fetchGetArticle (data) {
    return axios.post(articleUrls.getArticle, data)
  },

  // 图片上传
  fetchUploadImg () {
    return axios.post(articleUrls.uploadImg)
  },
  // 评论列表
  fetchCommentList (data) {
    return axios.post(articleUrls.commentList, data)
  },
  // 删除评论
  fetchDelComment (data) {
    return axios.post(articleUrls.delComment, data)
  }

}

  // 分页获取文章列表
  export function fetchArticleList (pageNo, size, tab) {
    return axios.request(({
      url: articleUrls.articleList,
      method: 'get',
      params:{
        pageNo: pageNo,
        size: size,
        tab: tab
      },
    }))
  }

  // 查看文章
  export function fetchArticleDetail(id){
    return axios.request(({
      url:articleUrls.articleDetail,
      method: 'get',
      params:{
        id: id
      }
    }))
  }


  // 删除文章
  export function fetchDelArticle (id) {
    return axios.request(({
      url:articleUrls.deleteArticle,
      method: 'get',
      params:{
        id: id
      }
    }))
  }

  // 发表文章
  export function fetchAddArticle(article,username){
    return axios.request(({
      url:articleUrls.addArticle,
      method: 'post',
      params:{
        username: username
      },
      data: article
      
    }))
  }

