<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple">
          <el-card shadow="hover">
            <el-tooltip effect="dark" content="总角色数" placement="top-start">
              <el-button>总用户数</el-button>
            </el-tooltip>
            <el-divider direction="vertical"></el-divider>
            <el-button>{{
              studentNum + teacherNum + companyNum + adminNum
            }}</el-button>
            <el-divider direction="vertical"></el-divider>
            <i class="el-icon-s-data" style="font-size: 20px "></i>
          </el-card></div
      ></el-col>
    </el-row>
    <br />
    <el-row :gutter="12">
      <el-col :span="5">
        <el-card shadow="hover">
          <el-tooltip effect="dark" content="学生人数" placement="top-start">
            <el-button>学生人数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          <el-button>{{ studentNum }}</el-button>
          <el-divider direction="vertical"></el-divider>
          <i class="el-icon-notebook-1" style="font-size: 20px "></i>
        </el-card>
      </el-col>

      <el-col :span="5">
        <el-card shadow="hover">
          <el-tooltip effect="dark" content="教师人数" placement="top-start">
            <el-button>教师数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          <el-button>{{ teacherNum }}</el-button>
          <el-divider direction="vertical"></el-divider>
          <i class="el-icon-s-custom" style="font-size: 20px "></i>
        </el-card>
      </el-col>

      <el-col :span="5">
        <el-card shadow="hover">
          <el-tooltip effect="dark" content="企业人数" placement="top-start">
            <el-button>企业人数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          <el-button>{{ companyNum }}</el-button>
          <el-divider direction="vertical"></el-divider>
          <i class="el-icon-office-building" style="font-size: 20px "></i>
        </el-card>
      </el-col>

      <el-col :span="5">
        <el-card shadow="hover">
          <el-tooltip effect="dark" content="管理员数" placement="top-start">
            <el-button>管理员数</el-button>
          </el-tooltip>
          <el-divider direction="vertical"></el-divider>
          <el-button>{{ adminNum }}</el-button>
          <el-divider direction="vertical"></el-divider>
          <i class="el-icon-s-management" style="font-size: 20px "></i>
        </el-card>
      </el-col>
    </el-row>
    <br />
    <template>
      <el-table
        ref="multipleTable"
        :data="userList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"> </el-table-column>

        <el-table-column prop="timeString" label="注册日期" width="150">
        </el-table-column>

        <el-table-column prop="id" label="ID" width="200"> </el-table-column>

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

        <el-table-column prop="alias" label="昵称" width="120">
        </el-table-column>

        <el-table-column prop="email" label="邮箱" width="200">
        </el-table-column>

        <el-table-column prop="score" label="积分" width="120">
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
      </el-table>
    </template>
  </div>
</template>

<script>
import { fetchUserList } from "@/api/apis/user.js";
import { fetchUserNumOfRole } from "@/api/apis/user.js";
import Sortable from "sortablejs";

export default {
  name: "dataTables",
  data() {
    return {
      roleList: [],
      userList: [],
      multipleSelection: [],
      studentNum: 0,
      teacherNum: 0,
      companyNum: 0,
      adminNum: 0
    };
  },

  created() {
    this.getUserList();
    this.getUserNumOfRole();
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
        console.log(data);
      });
    },

    /**
     * 获取各身份的用户数
     */
    async getUserNumOfRole() {
      fetchUserNumOfRole().then(value => {
        const { data } = value.data;
        this.studentNum = data.studentNum;
        this.teacherNum = data.teacherNum;
        this.companyNum = data.companyNum;
        this.adminNum = data.adminNum;
      });
    },

    /**
     * 身份筛选器
     */
    filterTag(value, row) {
      return row.role === value;
    }
  }
};
</script>

<style scoped>
h3 {
  margin: 25px 0 20px;
  font-weight: 400;
  color: #1f2f3d;
  font-size: 22px;
}
p {
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}

.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
