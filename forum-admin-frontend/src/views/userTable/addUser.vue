<template>
  <div class="addUser">
    <el-form ref="userInfo" :model="userInfo" label-width="80px">
      <el-form-item label="用户名">
        <el-autocomplete
          class="inline-input"
          v-model="userInfo.name"
          :fetch-suggestions="querySearch"
          placeholder="请输入用户名"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>

      <el-form-item label="密码">
        <el-autocomplete
          class="inline-input"
          v-model="userInfo.pass"
          :fetch-suggestions="querySearch"
          placeholder="请输入密码"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>

      <el-form-item label="邮箱">
        <el-autocomplete
          class="inline-input"
          v-model="userInfo.email"
          :fetch-suggestions="querySearch"
          placeholder="请输入邮箱"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>

      <el-form-item label="手机号">
        <el-autocomplete
          class="inline-input"
          v-model="userInfo.mobile"
          :fetch-suggestions="querySearch"
          placeholder="请输入手机号"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>

      <el-form-item label="简介">
        <el-autocomplete
          class="inline-input"
          v-model="userInfo.bio"
          :fetch-suggestions="querySearch"
          placeholder="请输入简介"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>

      <el-form-item label="身份">
        <el-radio-group v-model="userInfo.role">
          <el-radio label="0">管理员</el-radio>
          <el-radio label="1">学生</el-radio>
          <el-radio label="2">教师</el-radio>
          <el-radio label="3">企业</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <el-row type="flex" class="row-bg" justify="end">
      <el-button class="subBtn" type="primary" @click="submitAddUser"
        >确认添加</el-button
      >
    </el-row>
  </div>
</template>

<script>
import Markdown from "../../components/markdown/markdown-editor";
import initData from "../../markData.js";
import { FetchAddUser } from "@/api/apis/user.js";

export default {
  name: "addUser",
  data() {
    return {
      userInfo: {
        name: "",
        pass: "",
        email: "",
        mobile: "",
        role: "",
        bio: ""
      },
      initData: initData,
      restaurants: []
    };
  },
  components: { Markdown },
  methods: {
    change() {
      console.log(arguments[0], this.article);
      this.article.content = arguments[0];
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return restaurant => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
    loadAll() {
      return [{ value: "vue" }, { value: "node.js" }];
    },
    handleSelect(item) {
      console.log(item);
    },

    /**
     * 确认添加
     */
    async submitAddUser() {
      FetchAddUser(this.userInfo).then(value => {
        const { data } = value;
        console.log(data);
        if (data.code === 200) {
          this.$message({
            type: "success",
            showClose: true,
            message: "添加成功"
          });
        } else if (data.code === -1) {
          this.$message({
            type: "error",
            showClose: true,
            message: data.message
          });
        }
      });
    }
  },
  mounted() {
    this.restaurants = this.loadAll();
  }
};
</script>

<style scoped>
.subBtn {
  width: 100px;
  margin: 0 auto;
}
</style>
