<template>
  <div>
    <!-- 搜索和操作按钮 -->
    <el-row class="toolbar">
      <el-col :span="24">
        <div class="search-and-buttons">
          <div class="search-wrapper">
            <el-input
                v-model="searchQuery"
                placeholder="搜索配置名称、城市或关键词"
                clearable
                @clear="getUserConfigPage(1)">
            </el-input>
            <el-button @click="searchUserConfigs">搜索</el-button>
          </div>
          <div class="buttons">
            <el-button type="primary" @click="showCreateDialog">新建</el-button>
            <el-button type="success" @click="showUpdateDialog" :disabled="isUpdateDisabled">修改</el-button>
            <el-button type="danger" @click="showDeleteConfirm" :disabled="isDeleteDisabled">删除</el-button>
            <el-button type="info" @click="showTaskScheduleDialog" :disabled="isTaskScheduleDisabled">关联任务计划</el-button>

          </div>
        </div>
      </el-col>
    </el-row>
    <!-- 用户配置列表 -->
    <el-table :data="userConfigs" stripe border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="配置ID" sortable width="120"></el-table-column>
      <el-table-column prop="configName" label="配置名称" width="180"></el-table-column>
      <el-table-column prop="userId" label="用户ID" width="120"></el-table-column>
      <el-table-column prop="city" label="城市"></el-table-column>
      <el-table-column prop="keyword" label="关键词"></el-table-column>
    </el-table>



    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="getUserConfigPage"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        :total="totalPages"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

    <!-- 创建用户配置的对话框 -->
    <el-dialog title="创建新配置" v-model="createDialogVisible">
      <el-form ref="createForm" :model="newConfig">
        <el-form-item label="配置名称" :label-width="formLabelWidth">
          <el-input v-model="newConfig.configName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户ID" :label-width="formLabelWidth">
          <el-input v-model="newConfig.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="城市" :label-width="formLabelWidth">
          <el-input v-model="newConfig.city" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="关键词" :label-width="formLabelWidth">
          <el-input v-model="newConfig.keyword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="createDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="createUserConfig">创建</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 更新用户配置的对话框 -->
    <el-dialog title="更新配置" v-model="updateDialogVisible">
      <el-form ref="updateForm" :model="updateConfig">
        <el-form-item label="配置ID" :label-width="formLabelWidth">
          <el-input v-model="updateConfig.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="配置名称" :label-width="formLabelWidth">
          <el-input v-model="updateConfig.configName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户ID" :label-width="formLabelWidth">
          <el-input v-model="updateConfig.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="城市" :label-width="formLabelWidth">
          <el-input v-model="updateConfig.city" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="关键词" :label-width="formLabelWidth">
          <el-input v-model="updateConfig.keyword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="updateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateUserConfig">更新</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
        title="删除确认"
        v-model="deleteConfirmVisible"
        width="30%">
      <span>确定删除这个配置吗？</span>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="deleteConfirmVisible = false">取消</el-button>
          <el-button type="primary" @click="deleteUserConfig">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="管理任务计划关联" v-model="taskScheduleDialogVisible">
      <el-select v-model="taskScheduleId" placeholder="请选择任务计划" filterable>
        <el-option
            v-for="taskSchedule in taskSchedules"
            :key="taskSchedule.id"
            :label="taskSchedule.scheduleName"
            :value="taskSchedule.id">
        </el-option>
      </el-select>
      <span>当前配置ID: {{ selectedConfig ? selectedConfig.id : '未选择' }}</span>
      <template v-slot:footer>
        <el-button @click="taskScheduleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addTaskSchedule" v-if="!relatedTaskScheduleIds.includes(taskScheduleId)">添加关联</el-button>
        <el-button type="danger" @click="removeTaskSchedule" v-if="relatedTaskScheduleIds.includes(taskScheduleId)">删除关联</el-button>

      </template>

    </el-dialog>


  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      createDialogVisible: false,
      updateDialogVisible: false,
      deleteConfirmVisible: false,
      taskScheduleDialogVisible: false,
      isUpdateDisabled: true,
      isDeleteDisabled: true,
      isTaskScheduleDisabled: true,
      formLabelWidth: '100px',
      taskSchedules: [], // 存储任务计划的数组
      relatedTaskScheduleIds: [], // 存储与选中配置相关联的任务计划ID
      searchQuery: '', // 一个综合搜索条件
      newConfig: {
        configName: '',
        userId: null,
        city: '',
        keyword: '',
        deleteFlag: 'N',
      },
      updateConfig: {
        id: '',
        configName: '',
        userId: null,
        city: '',
        keyword: '',
        deleteFlag: 'N',
      },
      deleteId: '',
      getConfigId: '',
      userConfig: null,
      userConfigs: [],
      totalPages: 0,
      userConfigId: null,
      taskScheduleId: null,
      currentPage: 1,
      pageSize: 10,
      selectedConfig: null,
    };
  },
  methods: {
    createUserConfig() {
      axios.post('/userconfig/', this.newConfig)
          .then(() => {
            this.createDialogVisible = false;
            this.getUserConfigPage(this.currentPage);
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    updateUserConfig() {
      axios.put('/userconfig/', this.updateConfig)
          .then(() => {
            this.updateDialogVisible = false;
            this.getUserConfigPage(this.currentPage);
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    deleteUserConfig() {
      axios.delete(`/userconfig/${this.deleteId}`)
          .then(() => {
            this.deleteConfirmVisible = false;
            this.getUserConfigPage(this.currentPage);
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    getUserConfig() {
      if (!this.getConfigId) {
        // 如果没有提供ID，则不执行搜索
        return;
      }
      axios.get(`/userconfig/${this.getConfigId}`)
          .then(response => {
            // 假设API返回单个配置对象时
            this.userConfigs = [response.data]; // 将单个对象放入数组中以更新表格
            // 如果API返回的是数组，直接赋值：
            // this.userConfigs = response.data;
            this.totalPages = 1; // 更新总页数，如果适用
          })
          .catch(error => {
            // 处理错误
            console.error("Search error:", error);
            // 可以添加用户通知或错误处理逻辑
          });
    },

    getUserConfigPage(page) {
      axios.get('/userconfig/page', {
        params: {
          pageNo: page,
          pageSize: this.pageSize
        }
      })
          .then(response => {
            this.userConfigs = response.data.records; // 假设后端返回的数据格式为 { records: [...], total: ... }
            this.totalPages = response.data.total;
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    addTaskScheduleToUserConfig() {
      axios.post(`/userconfig/${this.userConfigId}/task-schedules/${this.taskScheduleId}`)
          .then(() => {
            // TODO: 添加处理成功响应的逻辑
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    removeTaskScheduleFromUserConfig() {
      axios.delete(`/userconfig/${this.userConfigId}/task-schedules/${this.taskScheduleId}`)
          .then(() => {
            // TODO: 添加处理成功响应的逻辑
          })
          .catch(() => {
            // TODO: 添加处理错误的逻辑
          });
    },
    getAllTaskSchedules() {
      axios.get('/task-schedules')
          .then(response => {
            this.taskSchedules = response.data;
          })
          .catch(error => {
            console.error('Error fetching task schedules:', error);
          });
    },

    handleSelectionChange(selection) {
      this.selectedConfig = selection[0]; // 假设只选择了一个配置
      this.isUpdateDisabled = selection.length !== 1;
      this.isTaskScheduleDisabled = selection.length !== 1;
      this.isDeleteDisabled = selection.length === 0;

      // 如果选中了一个配置，获取与之相关的任务计划ID列表
      if (selection.length === 1) {
        this.getRelatedTaskSchedules(selection[0].id);
      } else {
        this.relatedTaskScheduleIds = []; // 如果没有选择或选择了多个，则清空关联ID列表
      }
    },


    showCreateDialog() {
      this.newConfig = { configName: '', userId: null, city: '', keyword: '', deleteFlag: 'N' };
      this.createDialogVisible = true;
    },
    showUpdateDialog() {
      if (this.selectedConfig && this.selectedConfig.length === 1) {
        // 假设selectedConfig是一个数组，包含了所有选中的配置项
        this.updateConfig = { ...this.selectedConfig[0] }; // 创建一个新对象以避免直接修改原始数据
        this.updateDialogVisible = true;
      }
    },

    showDeleteConfirm() {
      if (this.selectedConfig) {
        this.deleteConfirmVisible = true;
      }
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.getUserConfigPage(1);
    },
    showTaskScheduleDialog() {
      if (this.selectedConfig) {
        this.taskScheduleDialogVisible = true;
      } else {
        // 提示用户选择一个配置
        this.$message.error('请选择一个配置');
      }
    },
    addTaskSchedule() {
      if (this.selectedConfig && this.taskScheduleId) {
        axios.post(`/userconfig/${this.selectedConfig.id}/task-schedules/${this.taskScheduleId}`)
            .then(() => {
              this.getRelatedTaskSchedules(this.selectedConfig.id); // 更新关联的任务计划ID列表
            })
            .catch(error => {
              console.error('Error adding task schedule:', error);
            });
      }
    },

    removeTaskSchedule() {
      if (this.selectedConfig && this.taskScheduleId) {
        axios.delete(`/userconfig/${this.selectedConfig.id}/task-schedules/${this.taskScheduleId}`)
            .then(() => {
              this.getRelatedTaskSchedules(this.selectedConfig.id); // 更新关联的任务计划ID列表
            })
            .catch(error => {
              console.error('Error removing task schedule:', error);
            });
      }
    },

    getRelatedTaskSchedules(userConfigId) {
      axios.get(`userconfig/user-config-schedule/${userConfigId}`)
          .then(response => {
            this.relatedTaskScheduleIds = response.data; // 假设响应数据是任务计划ID的数组
          })
          .catch(error => {
            console.error('Error fetching related task schedules:', error);
            this.relatedTaskScheduleIds = []; // 出错时清空数组
          });
    },

    searchUserConfigs() {
      const params = {
        searchValue: this.searchQuery, // 将用户输入的搜索词作为查询参数
      };
      axios.get('/userconfig/search', { params })
          .then(response => {
            this.userConfigs = response.data;
            // 这里假设后端接口不提供分页信息，如果有分页信息，需要相应地处理
          })
          .catch(error => {
            console.error("Search error:", error);
            // 可以添加用户通知或错误处理逻辑
          });
    },
    clearSearch() {
      this.searchQuery = '';
      this.getUserConfigPage(1);
    },


  },
  mounted() {
    this.getUserConfigPage(this.currentPage);
    this.getAllTaskSchedules(); // 调用获取任务计划的方法
  }
};
</script>


<style scoped>
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

/* 最后一个元素不需要margin-right */
.el-button:last-child {
  margin-right: 0;
}
/* 全局字体设置，如果需要 */
body {
  font-family: 'Source Sans Pro', sans-serif;
}

/* 搜索和操作按钮区域样式 */
.toolbar {
  margin-bottom: 20px;
}

/* 输入框右侧搜索按钮样式，如果只有图标没有文字 */
.el-input-group__append .el-button {
  padding: 0 12px; /* 为图标提供足够的空间 */
}

/* 操作按钮样式调整 */
.button-group {
  text-align: right;
}

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
.el-pagination {
  margin-top: 20px;
  text-align: center; /* 分页居中 */
}

/* 对话框底部按钮样式调整 */
.dialog-footer {
  text-align: right; /* 按钮靠右 */
}

/* 对话框按钮内文本居中 */
.dialog-footer .el-button span {
  position: absolute;
  text-align: center;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: block;
}

/* 表格样式 */
</style>
