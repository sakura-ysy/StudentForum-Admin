<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple">
          <el-card shadow="hover">
            <el-button @click="addDialogVisible = true">添加权限</el-button>
            <el-dialog
              title="编辑权限"
              :visible.sync="addDialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="addPermInfo" label-width="80px">
                <el-form-item label="权限名">
                  <el-input v-model="addPermInfo.permName"></el-input>
                </el-form-item>
                <el-form-item label="权限Tag">
                  <el-input v-model="addPermInfo.permTag"></el-input>
                </el-form-item>
                <el-form-item label="URL">
                  <el-input v-model="addPermInfo.url"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd()">确 定</el-button>
              </span>
            </el-dialog>
            <el-divider direction="vertical"></el-divider>
            <i class="el-icon-link" style="font-size: 20px "></i>
          </el-card></div
      ></el-col>
    </el-row>
    <br />
    <template>
      <el-table :data="permissionList" style="width: 100%">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="ID" sortable width="100">
        </el-table-column>

        <el-table-column prop="permName" label="权限名" width="180">
        </el-table-column>

        <el-table-column label="权限Tag" width="180">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>Tag: {{ scope.row.permTag }}</p>
              <p>权限名: {{ scope.row.permName }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.permTag }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="url" label="Url" width="180"> </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="
                editDialogVisible = true;
                editPermInfo.id = scope.row.id;
                editPermInfo.permName = scope.row.permName;
                editPermInfo.permTag = scope.row.permTag;
                editPermInfo.url = scope.row.url;
              "
              >编辑</el-button
            >
            <el-dialog
              title="编辑权限"
              :visible.sync="editDialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="editPermInfo" label-width="80px">
                <el-form-item label="权限名">
                  <el-input v-model="editPermInfo.permName"></el-input>
                </el-form-item>
                <el-form-item label="权限Tag">
                  <el-input v-model="editPermInfo.permTag"></el-input>
                </el-form-item>
                <el-form-item label="URL">
                  <el-input v-model="editPermInfo.url"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
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
import {
  fetchPermList,
  fetchEditPerm,
  fetchDelPerm,
  fetchAddPerm
} from "@/api/apis/permission.js";
import Sortable from "sortablejs";

export default {
  name: "filterTable",
  data() {
    return {
      addDialogVisible: false,
      editDialogVisible: false,
      permissionList: [],
      editPermInfo: {
        id: "",
        permName: "",
        permTag: "",
        url: ""
      },
      addPermInfo: {
        permName: "",
        permTag: "",
        url: ""
      }
    };
  },
  created() {
    this.getPermList();
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
     * 权限列表
     */
    async getPermList() {
      fetchPermList().then(value => {
        const { data } = value.data;
        this.permissionList = data;
      });
    },

    /**
     * 身份筛选器
     */
    filterTag(value, row) {
      return row.role === value;
    },

    /**
     * 删除权限
     */
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm("此操作将永久删除该权限, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          console.log(row.id);
          fetchDelPerm(row.id);
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
     * 编辑权限
     */
    async handleEdit() {
      this.editDialogVisible = false;

      this.$confirm("权限Tag与后端代码有关, 请谨慎更改Tag！", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          fetchEditPerm(this.editPermInfo).then(response => {
            let data = response.data;
            if (data.code === 200) {
              this.$message({
                type: "success",
                message: "权限更改成功"
              });
              location.reload();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消更改"
          });
        });
    },

    // 新增权限
    async handleAdd() {
      this.addDialogVisible = false;
      fetchAddPerm(this.addPermInfo).then(response => {
        let data = response.data;
        if (data.code === 200) {
          this.$message({
            type: "success",
            message: "权限新增成功"
          });
          location.reload();
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
