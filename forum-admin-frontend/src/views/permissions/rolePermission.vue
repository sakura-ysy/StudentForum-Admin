<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple">
          <el-card shadow="hover">
            <el-tooltip
              effect="dark"
              content="角色-权限 表单"
              placement="top-start"
            >
              <el-button>角色-权限 表单</el-button>
            </el-tooltip>
            <el-divider direction="vertical"></el-divider>
            <i class="el-icon-link" style="font-size: 20px "></i>
          </el-card></div
      ></el-col>
    </el-row>
    <br />

    <el-row :gutter="12" style="margin-left:20px">
      <el-col :span="12">
        <el-card class="box-card" style="width:100%">
          <div slot="header" class="clearfix">
            <span>学生权限</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="
                dialogVisible = true;
                addPermRole.roleId = 1;
              "
              >添加权限</el-button
            >
            <el-dialog
              title="添加权限"
              :visible.sync="dialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="addPermRole" label-width="80px">
                <el-form-item label="权限Id">
                  <el-input v-model="addPermRole.permId"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd()">确 定</el-button>
              </span>
            </el-dialog>
          </div>

          <template>
            <el-table :data="studentPermList" style="width: 100%">
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

              <el-table-column prop="url" label="Url" width="180">
              </el-table-column>

              <el-table-column label="操作">
                <template slot-scope="scope">
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
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="box-card" style="width:100%">
          <div slot="header" class="clearfix">
            <span>教师权限</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="
                dialogVisible = true;
                addPermRole.roleId = 2;
              "
              >添加权限</el-button
            >
            <el-dialog
              title="添加权限"
              :visible.sync="dialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="addPermRole" label-width="80px">
                <el-form-item label="权限Id">
                  <el-input v-model="addPermRole.permId"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd()">确 定</el-button>
              </span>
            </el-dialog>
          </div>
          <template>
            <el-table :data="teacherPermList" style="width: 100%">
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

              <el-table-column prop="url" label="Url" width="180">
              </el-table-column>

              <el-table-column label="操作">
                <template slot-scope="scope">
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
        </el-card>
      </el-col>
    </el-row>
    <br />
    <br />
    <el-row :gutter="12" style="margin-left:20px">
      <el-col :span="12">
        <el-card class="box-card" style="width:100%">
          <div slot="header" class="clearfix">
            <span>企业权限</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="
                dialogVisible = true;
                addPermRole.roleId = 3;
              "
              >添加权限</el-button
            >
            <el-dialog
              title="添加权限"
              :visible.sync="dialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="addPermRole" label-width="80px">
                <el-form-item label="权限Id">
                  <el-input v-model="addPermRole.permId"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd()">确 定</el-button>
              </span>
            </el-dialog>
          </div>
          <template>
            <el-table :data="companyPermList" style="width: 100%">
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

              <el-table-column prop="url" label="Url" width="180">
              </el-table-column>

              <el-table-column label="操作">
                <template slot-scope="scope">
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
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="box-card" style="width:100%">
          <div slot="header" class="clearfix">
            <span>管理员权限</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="
                dialogVisible = true;
                addPermRole.roleId = 0;
              "
              >添加权限</el-button
            >
            <el-dialog
              title="添加权限"
              :visible.sync="dialogVisible"
              width="30%"
            >
              <el-form ref="form" :model="addPermRole" label-width="80px">
                <el-form-item label="权限Id">
                  <el-input v-model="addPermRole.permId"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd()">确 定</el-button>
              </span>
            </el-dialog>
          </div>
          <template>
            <el-table :data="adminPermList" style="width: 100%">
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

              <el-table-column prop="url" label="Url" width="180">
              </el-table-column>

              <el-table-column label="操作">
                <template slot-scope="scope">
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
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 480px;
}
</style>

<script>
import { fetchPermListOfRole } from "@/api/apis/permission.js";
import { fetchAddPermForRole } from "@/api/apis/permission.js";
import Sortable from "sortablejs";

export default {
  name: "filterTable",
  data() {
    return {
      dialogVisible: false,
      studentPermList: [],
      teacherPermList: [],
      companyPermList: [],
      adminPermList: [],
      addPermRole: {
        roleId: "",
        permId: ""
      },
      editPermInfo: {
        permName: "",
        permTag: "",
        url: ""
      }
    };
  },
  created() {
    this.getPermListOfRole();
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
     * 角色权限列表
     */
    async getPermListOfRole() {
      fetchPermListOfRole(0).then(value => {
        const { data } = value.data;
        this.adminPermList = data;
      });
      fetchPermListOfRole(1).then(value => {
        const { data } = value.data;
        this.studentPermList = data;
      });
      fetchPermListOfRole(2).then(value => {
        const { data } = value.data;
        this.teacherPermList = data;
      });
      fetchPermListOfRole(3).then(value => {
        const { data } = value.data;
        this.companyPermList = data;
      });
    },

    /**
     * 删除权限
     */
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm("此操作将永久删除角色的该权限, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //fetchDeleteUser(row.id);
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
     * 为角色新增权限
     */
    async handleAdd() {
      this.dialogVisible = false;
      fetchAddPermForRole(this.addPermRole).then(value => {
        const { data } = value;
        console.log(data);
        location.reload();

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
