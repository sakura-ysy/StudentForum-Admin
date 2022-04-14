<template>
  <div>
    <el-row>
      <el-col :span="24"
        ><div class="grid-content bg-purple"><h3>操作日志</h3></div></el-col
      >
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <div class="block">
        <span class="demonstration"></span>
        <el-date-picker
          v-model="value"
          type="daterange"
          value-format="yyyy-MM-dd"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
        <el-divider direction="vertical"></el-divider>
        <el-button type="primary" @click="searchByTime()">查询</el-button>
        <el-divider direction="vertical"></el-divider>
        <el-button type="primary" @click="reset()">重置</el-button>
      </div>
    </el-row>

    <br />
    <template>
      <el-table :data="operLogList" style="width: 100%">
        <el-table-column type="index"> </el-table-column>
        <el-table-column
          prop="timeString"
          label="操作日期"
          sortable
          width="150"
        >
        </el-table-column>

        <el-table-column label="用户名" width="120">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>用户: {{ scope.row.operUserName }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.operUserName }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column prop="operUrl" label="URL" width="150">
        </el-table-column>

        <el-table-column prop="operModule" label="操作模块" width="100">
        </el-table-column>

        <el-table-column prop="operDesc" label="操作说明" width="150">
        </el-table-column>

        <el-table-column prop="operType" label="请求类型" width="100">
        </el-table-column>

        <el-table-column prop="operIp" label="请求IP" width="100">
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="handleGetInfo(scope.$index, scope.row)"
              >详情</el-button
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
import { fetchOperLogList, fetchOperLogByTime } from "@/api/apis/log.js";
import Sortable from "sortablejs";

export default {
  name: "adminArticle",
  data() {
    return {
      dialogVisible: false,
      operLogList: [],
      currentPage: 1,
      pageSize: 5,
      total: 0,
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      },
      value: ""
    };
  },
  created() {
    this.getOperLogList();
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
      this.getOperLogList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getOperLogList();
    },
    /**
     * 操作日志列表
     */
    async getOperLogList() {
      fetchOperLogList(this.currentPage, this.pageSize, "latest").then(
        value => {
          const { data } = value.data;
          this.operLogList = data.records;
          this.total = data.total;
        }
      );
    },

    /**
     * 按日期查询
     */
    async searchByTime() {
      fetchOperLogByTime(
        this.currentPage,
        this.pageSize,
        this.value[0],
        this.value[1]
      ).then(value => {
        const { data } = value.data;
        this.operLogList = data.records;
        this.total = data.total;
      });
    },

    /**
     * 重置
     */
    async reset() {
      this.value = "";
      this.currentPage = 1;
      this.pageSize = 5;
      this.getOperLogList();
    },
    /**
     * 查看日志
     */
    async handleGetInfo(index, row) {
      this.$router.push("/operLog/" + row.id);
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
