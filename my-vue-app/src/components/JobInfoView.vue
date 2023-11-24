<template>
  <div>
    <!-- 工作信息列表 -->
    <el-table :data="jobInfos" style="width: 100%" row-key="id" class="custom-table">
    <el-table-column prop="id" label="ID" width="60"></el-table-column>
      <el-table-column prop="jobName" label="职位名" width="120"></el-table-column>
      <el-table-column prop="companyId" label="公司ID" width="100"></el-table-column>
      <el-table-column prop="salary" label="薪资" width="100"></el-table-column>
      <el-table-column prop="city" label="城市" width="100"></el-table-column>
      <el-table-column prop="experience" label="经验" width="100"></el-table-column>
      <el-table-column prop="labels" label="标签" width="120"></el-table-column>
      <el-table-column prop="lastUpdateTime" label="最后更新时间" width="180"></el-table-column>
      <el-table-column prop="searchKeywords" label="搜索关键词" width="150"></el-table-column>
      <el-table-column prop="jobDescription" label="职位描述" width="180">
        <template v-slot="{ row }">
          <el-tooltip class="item" effect="light" :content="row.jobDescription" placement="top-start">
            <p class="text-truncate">{{ row.jobDescription }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="jobLocation" label="工作地点" width="120">
        <template v-slot="{ row }">
          <el-tooltip class="item" effect="light" :content="row.jobLocation" placement="top-start">
            <p class="text-truncate">{{ row.jobLocation }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="jobDetails" label="详情页链接" width="150">
        <template v-slot="{ row }">
          <el-tooltip class="item" effect="light" :content="row.jobDetails" placement="top-start">
            <a :href="row.jobDetails" target="_blank" class="text-truncate">{{ row.jobDetails }}</a>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="education" label="学历" width="100"></el-table-column>
      <!-- 操作列 -->
      <el-table-column label="操作" width="150">
        <template v-slot="{ row }">
          <div class="button-group">
            <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
          </div>
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

    <!-- 编辑工作信息的对话框 -->
    <el-dialog :visible="editDialogVisible" @update:visible="editDialogVisible = $event" title="编辑工作信息">
      <el-form :model="editJobInfo">
        <el-form-item label="职位名">
          <el-input v-model="editJobInfo.jobName"></el-input>
        </el-form-item>
        <!-- 其他编辑表单项 -->
        <el-form-item label="公司ID">
          <el-input v-model.number="editJobInfo.companyId"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateJobInfo">更新</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: 'JobInfoView',
  setup() {
    const jobInfos = ref([]);
    const total = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const editDialogVisible = ref(false);
    const editJobInfo = ref({});

    // 获取工作信息
    const fetchJobInfos = async () => {
      try {
        const response = await axios.get(`jobinfo/all?page=${currentPage.value}&size=${pageSize.value}`);
        jobInfos.value = response.data.records; // 假设返回的数据格式为 { records: [], total: number }
        total.value = response.data.total;
      } catch (error) {
        console.error('获取工作信息失败:', error);
      }
    };


    // 处理分页
    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      fetchJobInfos();
    };

    // 编辑工作信息
    const handleEdit = (jobInfo) => {
       editJobInfo.value = { ...jobInfo };
       editDialogVisible.value = true;
      console.log(editDialogVisible.value)
    };

    // 更新工作信息
    const updateJobInfo = async () => {
      try {
        const response = await axios.put('jobinfo/update', editJobInfo.value);
        if (response.data === "更新成功") {
          fetchJobInfos();
          editDialogVisible.value = false;
        } else {
          console.error('更新失败');
        }
      } catch (error) {
        console.error('更新工作信息失败:', error);
      }
    };

    // 删除工作信息
    const handleDelete = async (jobInfo) => {
      try {
        const response = await axios.delete(`jobinfo/delete/${jobInfo.id}`);
        if (response.data === "删除成功") {
          fetchJobInfos();
        } else {
          console.error('删除失败');
        }
      } catch (error) {
        console.error('删除工作信息失败:', error);
      }
    };

    onMounted(() => {
      fetchJobInfos();
    });


    return {
      jobInfos, total, currentPage, pageSize, editDialogVisible, editJobInfo, handlePageChange, handleEdit, updateJobInfo, handleDelete
    };

  },

  methods: {
    showTooltip(event, content) {
      // 当内容溢出时才显示 tooltip
      const target = event.target;
      if (target.offsetWidth < target.scrollWidth) {
        this.$refs.tooltip.setContent(content);
        this.$refs.tooltip.handleShowPopper();
      }
    },
  },
};
</script>

<style scoped>
/* 表格样式 */
.custom-table {
  border-collapse: separate;
  border-spacing: 0;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.custom-table th, .custom-table td {
  border-bottom: 1px solid #ebeef5;
}

.custom-table th:first-child, .custom-table td:first-child {
  border-left: 1px solid #ebeef5;
}

.custom-table th:last-child, .custom-table td:last-child {
  border-right: 1px solid #ebeef5;
}

.custom-table tr:hover {
  background-color: #f5f7fa;
}

/* 按钮样式，确保所有按钮高度和样式一致 */
.el-button {
  margin-right: 8px; /* 统一间距 */
  border-radius: 4px; /* 统一圆角 */
  height: 32px; /* 统一高度 */
  line-height: 32px; /* 统一行高 */
  padding: 0 12px; /* 统一内填充 */
  box-sizing: border-box; /* 确保包括边框和内填充在内的总宽度和高度 */
}

/* 最后一个按钮不需要右边距 */
.el-button:last-child {
  margin-right: 0;
}

/* 确保操作列按钮垂直居中对齐 */
.el-table .cell .button-group {
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 从左侧开始排列按钮 */
}

/* 文本截断样式 */
.text-truncate {
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}

/* 工具提示样式 */
.el-tooltip {
  font-size: 14px;
}

/* 分页组件样式 */
.el-pagination {
  text-align: center;
  margin-top: 20px;
}

/* 对话框样式 */
.el-dialog .el-dialog__header {
  background-color: #f5f7fa;
  color: #606266;
}

.el-dialog .el-dialog__body {
  padding: 20px;
}

/* 表单项样式 */
.el-form-item {
  margin-bottom: 16px;
}
</style>
