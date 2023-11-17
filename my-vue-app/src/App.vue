<template>
  <div id="app">
    <!-- 移除 HelloWorld 组件，或者你也可以保留它 -->
    <TaskScheduleManager/>
  </div>
</template>

<script>
import TaskScheduleManager from './components/TaskScheduleManager.vue'

export default {
  name: 'App',
  components: {
    // HelloWorld, // 如果你想保留 HelloWorld 组件，解开这行注释
    TaskScheduleManager // 注册你的 TaskScheduleManager 组件
  }
}
</script>

<style>
</style>
