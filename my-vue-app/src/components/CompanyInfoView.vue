<template>
  <div>
    <!-- 公司信息表格 -->
    <el-table :data="companyInfos" style="width: 100%" row-key="id">
      <el-table-column prop="id" label="公司ID" width="80"></el-table-column>
      <el-table-column prop="companyName" label="公司名称" width="180"></el-table-column>
      <el-table-column prop="companySize" label="公司规模" width="120"></el-table-column>
      <el-table-column prop="companyNature" label="公司性质" width="120"></el-table-column>
      <el-table-column prop="establishmentDate" label="成立时间" width="150">
        <template v-slot="{ row }">
          {{ formatDate(row.establishmentDate) }}
        </template>
      </el-table-column>
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
      <!-- 操作列（只保留删除操作） -->
      <el-table-column label="操作" width="180">
        <template v-slot="{ row }">
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
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: 'CompanyInfoView',
  setup() {
    const companyInfos = ref([]);
    const total = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);

    const fetchCompanyInfos = async () => {
      try {
        const response = await axios.get(`companyinfo/all?page=${currentPage.value}&size=${pageSize.value}`);
        companyInfos.value = response.data.records;
        total.value = response.data.total;
      } catch (error) {
        console.error('获取公司信息失败:', error);
      }
    };

    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      fetchCompanyInfos();
    };

    const handleDelete = async (id) => {
      try {
        const response = await axios.delete(`companyinfo/delete/${id}`);
        if (response.data === "删除成功") {
          fetchCompanyInfos();
        } else {
          console.error('删除失败');
        }
      } catch (error) {
        console.error('删除公司信息失败:', error);
      }
    };

    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' });
    };

    onMounted(() => {
      fetchCompanyInfos();
    });

    return {
      companyInfos, total, currentPage, pageSize, handlePageChange, handleDelete, formatDate
    };
  },
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

.el-input,
.el-button {
  margin-right: 10px; /* 给元素之间添加间距 */
}

.el-input-group__append .el-button {
  padding: 0 12px; /* 为图标提供足够的空间 */
}

/* 操作按钮样式调整 */
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

/* 对话框按钮样式，由于删除了编辑对话框，此样式可能不再需要 */
.dialog-footer .el-button span {
  position: absolute;
  text-align: center;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: block;
}
</style>

