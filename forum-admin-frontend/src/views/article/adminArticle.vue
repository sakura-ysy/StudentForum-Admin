<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple"><h3>文章管理</h3></div></el-col
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

        <el-table-column prop="comments" label="评论" width="80">
        </el-table-column>

        <el-table-column prop="praises" label="点赞" width="80">
        </el-table-column>

        <el-table-column prop="collects" label="收藏" width="80">
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="handleGetInfo(scope.$index, scope.row)"
              >查看</el-button
            >
            <el-button
              size="mini"
              @click="
                dialogVisible = true;
                editUserInfo.id = scope.row.id;
              "
              >编辑</el-button
            >
            <el-dialog title="编辑" :visible.sync="dialogVisible" width="30%">
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleEdit()"
                  >确 定</el-button
                >
              </span>
            </el-dialog>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
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
import { fetchArticleList } from "@/api/apis/article.js";
import { fetchDelArticle } from "@/api/apis/article.js";
import Sortable from "sortablejs";

export default {
  name: "adminArticle",
  data() {
    return {
      dialogVisible: false,
      articleList: [],
      editUserInfo: {
        id: "",
        username: "",
        alias: "",
        roleId: ""
      },
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
     * 删除文章
     */
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm("此操作将永久删除该文章, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          fetchDelArticle(row.id);
          location.reload();
          this.$message({
            type: "success",
            showClose: true,
            message: "删除成功",
            row,
            type: "success"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
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
