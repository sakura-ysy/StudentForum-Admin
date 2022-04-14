<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple"><h3>文章管理</h3></div></el-col
      >
    </el-row>
    <br />
    <template>
      <el-table :data="commentList" style="width: 100%">
        <el-table-column type="index"> </el-table-column>
        <el-table-column
          prop="timeString"
          label="评论日期"
          sortable
          width="180"
        >
        </el-table-column>

        <el-table-column label="评论者用户名" width="120">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.username }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="content" label="评论内容" width="300">
        </el-table-column>

        <el-table-column label="所属文章" width="140">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.postTitle }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="level" label="级数" width="80">
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
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
import { fetchCommentList, auditComment } from "@/api/apis/audit.js";
import Sortable from "sortablejs";

export default {
  name: "auditArticle",
  data() {
    return {
      dialogVisible: false,
      commentList: [],
      currentPage: 1,
      pageSize: 5,
      total: 0
    };
  },
  created() {
    this.getCommentList();
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
      this.getCommentList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getCommentList();
    },
    /**
     * 待审评论列表
     */
    async getCommentList() {
      fetchCommentList(this.currentPage, this.pageSize, "latest").then(
        value => {
          const { data } = value.data;
          this.commentList = data.records;
          this.total = data.total;
        }
      );
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
      this.$confirm("确定允许该评论发布?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          auditComment(row.id, true);
          location.reload();
          this.$message({
            type: "success",
            showClose: true,
            message: "评论审核通过",
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
      this.$confirm("确定拒绝该评论?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          auditComment(row.id, false);
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
