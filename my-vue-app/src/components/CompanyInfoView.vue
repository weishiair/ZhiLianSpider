<template>
  <div>
    <!-- 公司信息表格 -->
    <el-table :data="companyInfos" style="width: 100%" row-key="id">
      <el-table-column prop="id" label="公司ID" width="80"></el-table-column>
      <el-table-column prop="companyName" label="公司名称" width="180"></el-table-column>
      <el-table-column prop="companySize" label="公司规模" width="120"></el-table-column>
      <el-table-column prop="companyNature" label="公司性质" width="120"></el-table-column>
      <el-table-column prop="establishmentDate" label="成立时间" width="150"></el-table-column>
      <el-table-column prop="registeredCapital" label="注册资本" width="120"></el-table-column>
      <el-table-column prop="companyAddress" label="公司地址" width="200"></el-table-column>
      <el-table-column prop="companyWebsite" label="公司主页" width="180"></el-table-column>

      <el-table-column prop="companyIntroduce" label="公司介绍" width="200">
        <template v-slot="{ row }">
          <el-tooltip class="item" effect="dark" :content="row.companyIntroduce" placement="top-start">
            <p class="text-truncate">{{ row.companyIntroduce }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <!-- 操作列 -->
      <el-table-column label="操作" width="180">
        <template v-slot="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
        layout="total, prev, pager, next"
        :current-page="currentPage">
    </el-pagination>

    <!-- 编辑公司信息的对话框 -->
    <el-dialog :visible="editDialogVisible" @update:visible="editDialogVisible = false" title="编辑公司信息">
      <el-form :model="editCompanyInfo">
        <el-form-item label="公司名称">
          <el-input v-model="editCompanyInfo.companyName"></el-input>
        </el-form-item>
        <!-- 其他编辑表单项 -->
      </el-form>
      <template v-slot:footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateCompanyInfo">更新</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      companyInfos: [],
      total: 0,
      pageSize: 10,
      currentPage: 1,
      editDialogVisible: false,
      editCompanyInfo: {}
    };
  },
  created() {
    this.fetchCompanyInfos();
  },
  methods: {
    fetchCompanyInfos() {
      axios.get(`companyinfo/all?page=${this.currentPage}&size=${this.pageSize}`)
          .then(response => {
            this.companyInfos = response.data.records;
            this.total = response.data.total;
          })
          .catch(error => console.error(error));
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.fetchCompanyInfos();
    },
    handleEdit(companyInfo) {
      this.editCompanyInfo = {...companyInfo};
      this.editDialogVisible = true;
    },
    handleDelete(id) {
      axios.delete(`companyinfo/delete/${id}`)
          .then(response => {
            this.$message.success(response.data);
            this.fetchCompanyInfos();
          })
          .catch(error => {
            this.$message.error('删除失败');
            console.error(error);
          });
    },
    updateCompanyInfo() {
      axios.put('companyinfo/update', this.editCompanyInfo)
          .then(response => {
            this.$message.success(response.data);
            this.editDialogVisible = false;
            this.fetchCompanyInfos();
          })
          .catch(error => {
            this.$message.error('更新失败');
            console.error(error);
          });
    }
  }
};
</script>

<style scoped>
.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.search-and-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center; /* 确保所有元素垂直居中 */
}

.search-wrapper {
  display: flex;
  flex-grow: 1; /* 允许搜索区域根据需要进行伸缩 */
  margin-right: 10px; /* 与按钮组保持一定距离 */
}

.buttons {
  flex-grow: 2; /* 允许按钮组根据需要进行伸缩 */
  display: flex;
  justify-content: flex-end; /* 按钮组靠右对齐 */
}

/* 可能需要根据实际情况调整内部元素的样式，如下面的例子 */
.el-input,
.el-button {
  margin-right: 10px; /* 给元素之间添加间距 */
}

/* 输入框右侧搜索按钮样式，如果只有图标没有文字 */
.el-input-group__append .el-button {
  padding: 0 12px; /* 为图标提供足够的空间 */
}

/* 操作按钮样式调整 */

/* 按钮内文本居中 */
.button-group .el-button {
  position: relative; /* 相对定位，用于绝对定位内部元素 */
}

.button-group .el-button span {
  position: absolute; /* 绝对定位 */
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%); /* 使用变换来居中 */
  display: block; /* 使span变为块级元素 */
}

/* 分页样式调整 */

/* 对话框底部按钮样式调整 */

/* 对话框按钮内文本居中 */
.dialog-footer .el-button span {
  position: absolute;
  text-align: center;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: block;
}

</style>
