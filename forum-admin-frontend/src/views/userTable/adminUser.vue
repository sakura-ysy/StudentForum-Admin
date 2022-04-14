<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple"><h3>用户管理</h3></div></el-col
      >
    </el-row>
    <br />
    <template>
      <el-table :data="userList" style="width: 100%">
        <el-table-column type="index"> </el-table-column>
        <el-table-column prop="timeString" label="日期" sortable width="180">
        </el-table-column>

        <el-table-column label="用户名" width="180">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>姓名: {{ scope.row.username }}</p>
              <p>身份: {{ scope.row.role }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.username }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="alias" label="昵称" width="180">
        </el-table-column>

        <el-table-column prop="email" label="邮箱" width="180">
        </el-table-column>

        <el-table-column
          prop="role"
          label="身份"
          width="100"
          :filters="[
            { text: '管理员', value: '管理员' },
            { text: '学生', value: '学生' },
            { text: '教师', value: '教师' },
            { text: '企业', value: '企业' }
          ]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.tag === '家' ? 'primary' : 'success'"
              disable-transitions
              >{{ scope.row.role }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="
                dialogVisible = true;
                editUserInfo.id = scope.row.id;
                editUserInfo.alias = scope.row.alias;
              "
              >编辑</el-button
            >
            <el-dialog
              title="编辑用户"
              :visible.sync="dialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="editUserInfo" label-width="80px">
                <el-form-item label="用户名">
                  <el-input v-model="editUserInfo.username"></el-input>
                </el-form-item>
                <el-form-item label="昵称">
                  <el-input v-model="editUserInfo.alias"></el-input>
                </el-form-item>
                <el-form-item label="身份">
                  <el-radio-group v-model="editUserInfo.roleId">
                    <el-radio label="0">管理员</el-radio>
                    <el-radio label="1">学生</el-radio>
                    <el-radio label="2">教师</el-radio>
                    <el-radio label="3">企业</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
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
    </template>
  </div>
</template>

<script>
import { fetchUserList } from "@/api/apis/user.js";
import { fetchDeleteUser } from "@/api/apis/user.js";
import { fetchEditUser } from "@/api/apis/user.js";
import Sortable from "sortablejs";

export default {
  name: "filterTable",
  data() {
    return {
      dialogVisible: false,
      userList: [],
      editUserInfo: {
        id: "",
        username: "",
        alias: "",
        roleId: ""
      }
    };
  },
  created() {
    this.getUserList();
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
     * 用户列表
     */
    async getUserList() {
      fetchUserList().then(value => {
        const { data } = value.data;
        this.userList = data;
      });
    },

    /**
     * 身份筛选器
     */
    filterTag(value, row) {
      return row.role === value;
    },

    /**
     * 删除用户
     */
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm("此操作将永久删除该用户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          fetchDeleteUser(row.id);
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
     * 编辑用户
     */
    async handleEdit() {
      this.dialogVisible = false;
      fetchEditUser(this.editUserInfo).then(value => {
        const { data } = value;
        console.log(data);
        location.reload();

        if (data.code === 200) {
          this.$message({
            type: "success",
            showClose: true,
            message: "编辑成功"
          });
        } else if (data.code === -1) {
          this.$message({
            type: "error",
            showClose: true,
            message: data.message
          });
        }
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
