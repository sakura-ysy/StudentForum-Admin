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
          {{ this.excLog.timeString }}
        </el-card>

        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作时间" placement="top-start">
            <el-button>操作方法</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.excLog.excMethod }}
        </el-card>

        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作时间" placement="top-start">
            <el-button>异常名</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.excLog.excName }}
        </el-card>

        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作时间" placement="top-start">
            <el-button>Param参数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          {{ this.excLog.excRequParam }}
        </el-card>

        <el-card shadow="never">
          <el-tooltip effect="dark" content="操作时间" placement="top-start">
            <el-button>异常Messgae</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          <br />
          <el-card> {{ this.excLog.excMessage }} </el-card>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchExcLogInfo } from "@/api/apis/log.js";

export default {
  name: "TopicDetail",
  data() {
    return {
      flag: false,
      excLog: {}
    };
  },
  mounted() {
    this.fetchExcLogInfo();
  },

  methods: {
    // 初始化
    async fetchExcLogInfo() {
      fetchExcLogInfo(this.$route.params.id).then(response => {
        const { data } = response.data;
        this.excLog = data;
      });
    },
    // 跳转
    jump() {
      this.$router.push("/exceptionLog");
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
