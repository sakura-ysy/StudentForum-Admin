<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple"><h3>文章审核</h3></div></el-col
      >
    </el-row>
    <br />
    <template>
      <el-table :data="articleList" style="width: 100%">
        <el-table-column type="index"> </el-table-column>
        <el-table-column
          prop="timeString"
          label="发布日期"
          sortable
          width="180"
        >
        </el-table-column>

        <el-table-column label="标题" width="180">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>标题: {{ scope.row.title }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.title }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="username" label="发布者用户名" width="180">
        </el-table-column>

        <el-table-column prop="alias" label="发布者昵称" width="180">
        </el-table-column>

        <el-table-column prop="tags" label="标签" width="180">
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleGetInfo(scope.$index, scope.row)"
              >查看</el-button
            >
            <el-button
              size="mini"
              type="success"
              @click="handlePass(scope.$index, scope.row)"
              >通过</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleReject(scope.$index, scope.row)"
              >拒绝</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <br />
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="5"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
import { fetchArticleList, auditArticle } from "@/api/apis/audit.js";
import Sortable from "sortablejs";

export default {
  name: "auditArticle",
  data() {
    return {
      dialogVisible: false,
      articleList: [],
      currentPage: 1,
      pageSize: 5,
      total: 0
    };
  },
  created() {
    this.getArticleList();
  },

  mounted() {
    // 表格拖拽
    let el = document.querySelectorAll(
      ".el-table__body-wrapper > table > tbody"
    )[0];
    this.sortable = Sortable.create(el);
  },

  methods: {
    /**
     * 分页有关函数
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getArticleList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getArticleList();
    },
    /**
     * 文章列表
     */
    async getArticleList() {
      fetchArticleList(this.currentPage, this.pageSize, "latest").then(
        value => {
          const { data } = value.data;
          this.articleList = data.records;
          this.total = data.total;
        }
      );
    },

    /**
     * 查看文章
     */
    async handleGetInfo(index, row) {
      this.$router.push("/article/" + row.id);
    },

    /**
     * 身份筛选器
     */
    filterTag(value, row) {
      return row.role === value;
    },

    /**
     * 允许通过
     */
    handlePass(index, row) {
      console.log(index, row);
      this.$confirm("确定允许该文章发布?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          auditArticle(row.id, true);
          location.reload();
          this.$message({
            type: "success",
            showClose: true,
            message: "文章审核通过",
            row,
            type: "success"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    /**
     * 拒绝通过
     */
    handleReject(index, row) {
      console.log(index, row);
      this.$confirm("确定拒绝该文章?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          auditArticle(row.id, false);
          location.reload();
          this.$message({
            type: "success",
            showClose: true,
            message: "审核成功",
            row,
            type: "success"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消拒绝"
          });
        });
    },

    /**
     * 编辑帖子
     */
    async handleEdit() {},

    filterHandler(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    formatter(row, column) {
      return row.address;
    }
  }
};
</script>

<style scoped></style>
