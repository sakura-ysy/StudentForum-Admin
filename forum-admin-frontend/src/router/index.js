import en from "../i18n/lang/en"
import Vue from "vue"
import Router from "vue-router"
import CommerViews from "@/views/commerViews"
import Login from "@/views/login/index"
import Layout from "@/views/layout/layout"
import HomeMain from "@/views/index/mainIndex"

// 不是必须加载的组件使用懒加载
const Icon = () => import("@/views/icon/index")
const Erji = () => import("@/views/duoji/erji")
const Erji2 = () => import("@/views/duoji/erji2")
const Sanji = () => import("@/views/duoji/sanji")
const Sanji2 = () => import("@/views/duoji/sanji2")
const Siji = () => import("@/views/duoji/siji")
const Wuji = () => import("@/views/duoji/wuji")
const Transfer = () => import("@/views/transfer/transfer")
const DataTable = () => import("@/views/userTable/userTable")
const FilterTable = () => import("@/views/userTable/adminUser")
const AddUser = () => import("@/views/userTable/addUser")
const Upload = () => import("@/views/upload/upload")
const Markdown = () => import("@/views/editor/markdownView")
const WangeditorView = () => import("@/views/editor/wangeditorView")
const NotFound = () => import("@/views/page404")
const AddArticle = () => import("@/views/article/addArticle")
const AddArticleEditor = () => import("@/views/article/addArticleEditor")
const AdminArticle = () => import("@/views/article/adminArticle")
const ArticleDetail = () => import("@/views/article/articleDetail")
const NavClassify = () => import("@/views/syssetting/navClassify")
const pagePermissions = () => import("@/views/permissions/pagePermissions")
const btnPermissions = () => import("@/views/permissions/btnPermissions")
const roleTable = () => import("@/views/permissions/roleTable")
const rolePermission = () => import("@/views/permissions/rolePermission")
const permissionTable = () => import("@/views/permissions/adminPermisson")
const operLogTable = () => import("@/views/log/operLogTable")
const execptionLogTable = () => import("@/views/log/exceptionLogTable")
const operLogInfo = () => import("@/views/log/operLogInfo")
const execptionLogInfo = () => import("@/views/log/exceptionLogInfo")
const articleAuditList = () => import("@/views/audit/articleAudit")
const commentAuditList = () => import("@/views/audit/commentAudit")

/**
 * 重写路由的push方法
 */
const routerPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return routerPush.call(this, location).catch(error => error)
}
Vue.use(Router)
let routeName = en.routeName
let defaultRouter = [
  { path: "/",
    redirect: "/index",
    hidden: true,
    children: []
  },
  {
    path: "/login",
    component: Login,
    name: "",
    hidden: true,
    children: []
  },
  {
    path: "/index",
    iconCls: "fa fa-dashboard", // 图标样式class
    name: routeName.home,
    component: Layout,
    alone: true,
    children: [
      {
        path: "/index",
        iconCls: "fa fa-dashboard", // 图标样式class
        name: "主页",
        component: HomeMain,
        children: []
      }
    ]
  },
  {
    path: "/404",
    component: NotFound,
    name: "404",
    hidden: true,
    children: []
  }
]

let addRouter = [
  /**
   * 文章
   */
  {
    path: "/",
    iconCls: "el-icon-tickets", // 图标样式class
    name: routeName.article,
    component: Layout,
    children: [
      {
        path: "/addArticle",
        iconCls: "el-icon-edit-outline", // 图标样式class
        name: routeName.publishArticle,
        component: AddArticle,
        children: []
      },
      {
        path: "/addArticleEditor",
        iconCls: "el-icon-edit-outline", // 图标样式class
        name: routeName.publishArticleEditor,
        hidden: true,
        component: AddArticleEditor,
        children: []
      },
      {
        path: "/adminArticle",
        iconCls: "el-icon-document-copy", // 图标样式class
        name: routeName.adminArticle,
        component: AdminArticle,
        children: []
      },
      {
        path: "/article/:id",
        iconCls: "el-icon-edit-outline", // 图标样式class
        name:  routeName.articleDetail,
        hidden: true,
        component: ArticleDetail,
        children: []
      },
    ]
  },
  /**
   * 用户
   */
  {
    path: "/",
    iconCls: "fa fa-newspaper-o", // 图标样式class
    name: routeName.table,
    component: Layout,
    children: [
      {
        path: "/dataTable",
        iconCls: "fa fa-sliders", // 图标样式class
        name: routeName.multiDataTable,
        component: DataTable,
        children: []
      },
      {
        path: "/filterTable",
        iconCls: "fa fa-sort-amount-asc", // 图标样式class
        name: routeName.filterTable,
        component: FilterTable,
        children: []
      },
      {
        path: "/addUser",
        iconCls: "el-icon-circle-plus-outline", // 图标样式class
        name: routeName.addUser,
        component: AddUser,
        children: []
      },
    ]
  },
  /**
   * 权限
   */
  {
    path: "/",
    iconCls: "fa fa-universal-access", // 图标样式class
    name: routeName.permissions,
    component: Layout,
    children: [
      {
        path: "/roleTable",
        iconCls: "el-icon-user-solid", // 图标样式class
        name: routeName.roleTable,
        component: roleTable,
        children: []
      },
      {
        path: "/permissionTable",
        iconCls: "el-icon-link", // 图标样式class
        name: routeName.permissionTable,
        component: permissionTable,
        children: []
      },
      {
        path: "/rolePermission",
        iconCls: "el-icon-key", // 图标样式class
        name: routeName.rolePermission,
        component: rolePermission,
        children: []
      },
      {
        path: "/pagePermissions",
        iconCls: "fa fa-expeditedssl", // 图标样式class
        name: routeName.pageControl,
        component: pagePermissions,
        children: []
      },
      {
        path: "/btnPermissions",
        iconCls: "fa fa-toggle-on", // 图标样式class
        name: routeName.btnControl,
        component: btnPermissions,
        children: []
      }
    ]
  },
  /**
   * 审核
   */
  {
    path: "/",
    iconCls:"el-icon-place",
    name : routeName.audit,
    component: Layout,
    children: [
      {
        path: "/articleAudit",
        iconCls: "el-icon-tickets",
        name : routeName.articleAudit,
        component: articleAuditList,
        children:[],
      },
      {
        path: "/commentAudit",
        iconCls: "el-icon-chat-line-round",
        name : routeName.commentAudit,
        component: commentAuditList,
        children:[],
      }
    ]
  },
  /**
   * 日志
   */
  {
    path: "/",
    iconCls: "el-icon-notebook-2", // 图标样式class
    name: routeName.logRecord,
    component: Layout,
    children: [
      {
        path: "/operLog",
        iconCls: "el-icon-link", // 图标样式class
        name: routeName.operLog,
        component: operLogTable,
        children: []
      },
      {
        path: "/exceptionLog",
        iconCls: "el-icon-document-delete", // 图标样式class
        name: routeName.exceptionLog,
        component: execptionLogTable,
        children: []
      },
      {
        path: "/operLog/:id",
        iconCls: "el-icon-link", // 图标样式class
        hidden: true,
        component: operLogInfo,
        
        children: []
      },
      {
        path: "/excLog/:id",
        iconCls: "el-icon-link", // 图标样式class
        hidden: true,
        component: execptionLogInfo,
        
        children: []
      }
    ]
  },

  {
    path: "/",
    iconCls: "fa fa-paw", // 图标样式class
    name: routeName.icon,
    component: Layout,
    children: [
      {
        path: "/icon",
        iconCls: "fa fa-life-ring", // 图标样式class
        name: routeName.builtInIcon,
        component: Icon,
        children: []
      }
    ]
  },
  
  {
    path: "/",
    iconCls: "fa fa-exchange", // 图标样式class
    name: routeName.shuttleBox,
    component: Layout,
    children: [
      {
        path: "/transfer",
        iconCls: "fa fa-sign-in", // 图标样式class
        name: routeName.demoShuttle,
        component: Transfer,
        children: []
      }
    ]
  },
  
  {
    path: "/",
    iconCls: "fa fa-server",
    name: routeName.multiDirectory,
    component: Layout,
    children: [
      {
        path: "/erji1",
        iconCls: "fa fa-server",
        name: routeName["menu2-1"],
        component: Erji,
        children: []
      },
      {
        path: "/erji2",
        iconCls: "fa fa-server",
        name: routeName["menu2-2"],
        component: Erji2,
        children: []
      },
      {
        path: "/erji3",
        iconCls: "fa fa-server",
        name: routeName["menu2-3"],
        component: CommerViews, // 无限极菜单的容器
        children: [
          {
            path: "/sanji1",
            iconCls: "fa fa-server",
            name: routeName["menu3-1"],
            component: Sanji,
            children: []
          },
          {
            path: "/sanji2",
            iconCls: "fa fa-server",
            name: routeName["menu3-2"],
            component: Sanji2,
            children: []
          },
          {
            path: "/sanji3",
            iconCls: "fa fa-server",
            name: routeName["menu3-3"],
            component: CommerViews,
            children: [
              {
                path: "/siji",
                iconCls: "fa fa-server",
                name: routeName["menu4-1"],
                component: Siji,
                children: []
              },
              {
                path: "/siji1",
                iconCls: "fa fa-server",
                name: routeName["menu4-2"],
                component: CommerViews,
                children: [
                  {
                    path: "/wuji",
                    iconCls: "fa fa-server",
                    name: routeName["menu5-1"],
                    component: Wuji,
                    children: []
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: "/",
    iconCls: "fa fa-cloud-upload", // 图标样式class
    name: routeName.upload,
    component: Layout,
    meta: {role: ["superAdmin"]},
    children: [
      {
        path: "/upload",
        iconCls: "el-icon-upload2", // 图标样式class
        name: routeName.fileUpload,
        component: Upload,
        children: []
      }
    ]
  },
  {
    path: "/",
    iconCls: "el-icon-edit", // 图标样式class
    name: routeName.editor,
    component: Layout,
    meta: {role: ["superAdmin", "admin"]},
    children: [
      {
        path: "/markdown",
        iconCls: "fa fa-file-code-o", // 图标样式class
        name: routeName.markdown,
        component: Markdown,
        children: []
      },
      {
        path: "/wangeditor",
        iconCls: "fa fa-file-code-o", // 图标样式class
        name: routeName.wangeditor,
        component: WangeditorView,
        children: []
      }
    ]
  },
  {
    path: "/",
    iconCls: "el-icon-setting", // 图标样式class
    name: routeName.systemSettings,
    component: Layout,
    meta: {role: ["superAdmin"]},
    children: [
      {
        path: "/navClassifies",
        iconCls: "el-icon-menu", // 图标样式class
        name: routeName.navMenu,
        component: NavClassify,
        children: []
      }
    ]
  },
  { path: "*",
    redirect: "/404",
    hidden: true,
    children: []
  }

]
export default new Router({
  routes: defaultRouter
})
export {defaultRouter, addRouter}
