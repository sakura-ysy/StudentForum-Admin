<template>
  <div class="container is-fluid">
    <div class="columns">
      <!--Log详情-->
      <div class="column is-four-fifths">
        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作时间" placement="top-start">
            <el-button>操作时间</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.timeString }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="用户名" placement="top-start">
            <el-button>操作者</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operUserName }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作模块" placement="top-start">
            <el-button>操作模块</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operModule }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作说明" placement="top-start">
            <el-button>操作说明</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operDesc }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="请求类型" placement="top-start">
            <el-button>请求类型</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operType }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="请求IP" placement="top-start">
            <el-button>请求IP</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operIp }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="请求URL" placement="top-start">
            <el-button>请求URL</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operUrl }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="调用方法" placement="top-start">
            <el-button>调用方法</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operMethod }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="Param参数" placement="top-start">
            <el-button>Param参数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.operLog.operRequParam }}
        </el-card>
        <el-card shadow="never">
          <el-tooltip effect="dark" content="响应参数" placement="top-start">
            <el-button>响应参数</el-button>
          </el-tooltip>
          <el-card shadow="hover"> {{ this.operLog.operRespParam }}</el-card>
        </el-card>
        <el-card shadow="never"
          ><el-button type="primary" @click="jump()">确认</el-button></el-card
        >
      </div>
    </div>
  </div>
</template>

<script>
import { fetchOperLogInfo } from "@/api/apis/log.js";

export default {
  name: "TopicDetail",
  data() {
    return {
      flag: false,
      operLog: {}
    };
  },
  mounted() {
    this.fetchOperLogInfo();
  },

  methods: {
    // 初始化
    async fetchOperLogInfo() {
      fetchOperLogInfo(this.$route.params.id).then(response => {
        const { data } = response.data;
        this.operLog = data;
        console.log(data);
      });
    },
    // 跳转
    jump() {
      this.$router.push("/operLog");
    }
  }
};
</script>

<style>
#preview {
  min-height: 300px;
}
.card {
  margin-top: 1px;
  margin-bottom: 15px;
} /*这里定义的是侧边栏的间距*/
.box {
  padding: 1px;
}
</style>
