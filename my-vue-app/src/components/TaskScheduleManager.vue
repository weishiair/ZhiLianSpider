<template>
  <div class="container my-5">
    <h2 class="text-center mb-5">任务计划管理器</h2>
    <div class="mb-3 row">
      <label class="form-label">任务ID（更新、删除、启动、停止、状态查询时使用）</label>
      <div class="col">
        <input type="number" class="form-control" v-model="taskId">
      </div>
    </div>
    <div class="mb-3 row">
      <label class="form-label">任务计划名称</label>
      <div class="col">
        <input type="text" class="form-control" v-model="scheduleName">
      </div>
    </div>
    <div class="mb-3 row">
      <label class="form-label">CRON表达式</label>
      <div class="col">
        <input type="text" class="form-control" v-model="cronExpression">
      </div>
    </div>
    <div class="mb-3 row">
      <label class="form-label">状态</label>
      <div class="col">
        <input type="text" class="form-control" v-model="status">
      </div>
    </div>
    <div class="mb-4 row">
      <div class="col d-flex">
        <button @click="createTaskSchedule" class="btn btn-primary me-2">创建任务计划</button>
        <button @click="updateTaskSchedule" class="btn btn-outline-secondary">更新任务计划</button>
      </div>
    </div>
    <div class="row">
      <div class="col d-flex flex-wrap gap-2">
        <button @click="getAllTaskSchedules" class="btn btn-outline-info">获取所有任务计划</button>
        <button @click="getTaskScheduleById" class="btn btn-outline-info">根据ID获取任务计划</button>
        <button @click="deleteTaskSchedule" class="btn btn-outline-danger">删除任务计划</button>
        <button @click="startTaskSchedule" class="btn btn-outline-success">启动任务</button>
        <button @click="stopTaskSchedule" class="btn btn-outline-warning">停止任务</button>
        <button @click="getTaskStatus" class="btn btn-outline-primary">获取任务状态</button>
      </div>
    </div>
    <div id="response" class="mt-4">
      <pre>{{ formattedResponse }}</pre>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'TaskManager',
  data() {
    return {
      taskId: null,
      scheduleName: '',
      cronExpression: '',
      status: '',
      response: 'null',
    };
  },
  computed: {
    formattedResponse() {
      // 如果response是一个对象，直接格式化；如果是字符串，尝试解析后格式化
      if (this.response && typeof this.response === 'object') {
        return JSON.stringify(this.response, null, 2);
      } else if (this.response && typeof this.response === 'string') {
        try {
          const jsonObj = JSON.parse(this.response);
          return JSON.stringify(jsonObj, null, 2);
        } catch (e) {
          return this.response; // 如果解析失败，返回原始字符串
        }
      }
      return ''; // 如果response为空，返回空字符串
    },
  },
  methods: {
    createTaskSchedule() {
      const taskData = {
        scheduleName: this.scheduleName,
        cronExpression: this.cronExpression,
        status: this.status
      };
      axios.post('/task-schedules', taskData)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    updateTaskSchedule() {
      const taskData = {
        scheduleName: this.scheduleName,
        cronExpression: this.cronExpression,
        status: this.status
      };
      axios.put(`/task-schedules/${this.taskId}`, taskData)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    getAllTaskSchedules() {
      axios.get('/task-schedules')
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    getTaskScheduleById() {
      axios.get(`/task-schedules/${this.taskId}`)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    deleteTaskSchedule() {
      axios.delete(`/task-schedules/${this.taskId}`)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    startTaskSchedule() {
      axios.post(`/task-schedules/start/${this.taskId}`)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    stopTaskSchedule() {
      axios.post(`/task-schedules/stop/${this.taskId}`)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
    getTaskStatus() {
      axios.get(`/task-schedules/status/${this.taskId}`)
          .then(response => this.response = JSON.stringify(response.data, null, 2))
          .catch(error => this.response = error.toString());
    },
  }
};
</script>
<style scoped>
.container {
  max-width: 800px;
  background: #fff;
  padding: 2rem;
  border-radius: 0.5rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 2rem;
}

.form-control {
  box-shadow: none;
  border-radius: 0.375rem;
}

.form-control:focus {
  border-color: #86b7fe;
  box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
}

.btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.btn-primary:hover {
  background-color: #0b5ed7;
  border-color: #0a58ca;
}

.btn-outline-secondary {
  border-color: #6c757d;
}

.btn-outline-secondary:hover {
  background-color: #6c757d;
  color: #fff;
}

#response pre {
  background-color: #f8f9fa;
  border: 1px solid #e3e6f0;
  border-radius: 0.375rem;
}

@media (min-width: 768px) {
  .d-md-block .btn {
    display: inline-block;
  }
}
btn-outline-info, .btn-outline-danger, .btn-outline-success, .btn-outline-warning, .btn-outline-primary {
  color: #212529;
}

#response pre {
  background-color: #e9ecef;
  border: 1px solid #adb5bd;
  border-radius: 0.375rem;
  padding: 0.75rem;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>