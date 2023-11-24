<template>
  <el-container style="height: 100vh;">

    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo">爬虫管理系统</div>
      <!-- 你可以在这里添加更多的导航元素或用户信息 -->
    </el-header>

    <!-- 侧边栏和主内容区 -->
    <el-container>

      <!-- 侧边栏菜单 -->
      <el-aside width="200px" class="aside-menu">
        <el-menu :default-active="activeIndex" class="el-menu-vertical-demo">
          <el-menu-item index="/">
            <i class="el-icon-house"></i>
            <span><router-link to="/">主页</router-link></span>
          </el-menu-item>
          <el-menu-item index="/user-config">
            <i class="el-icon-user"></i>
            <span><router-link to="/user-config">用户配置</router-link></span>
          </el-menu-item>
          <el-menu-item index="/task-schedule-manager">
            <i class="el-icon-s-tools"></i>
            <span><router-link to="/task-schedule-manager">任务计划管理</router-link></span>
          </el-menu-item>
          <!-- 新增工作信息管理菜单项 -->
          <el-menu-item index="/job-info">
            <i class="el-icon-document"></i>
            <span><router-link to="/job-info">工作信息管理</router-link></span>
          </el-menu-item>
          <el-menu-item index="/company-info">
            <i class="el-icon-office-building"></i>
            <span><router-link to="/company-info">公司信息管理</router-link></span>
          </el-menu-item>

          <!-- 更多菜单项 -->
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>

    </el-container>

  </el-container>
</template>

<script>
export default {
  // ...
};
</script>

<style scoped>
.header {
  background-color: #324157; /* 顶部导航栏背景色 */
  color: #fff; /* 文字颜色 */
  line-height: 60px; /* 行高 */
  padding: 0 20px; /* 内边距 */
}

.logo {
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.aside-menu {
  background-color: #2c3e50; /* 侧边栏背景色 */
  color: #fff; /* 侧边栏文字颜色 */
  border-right: 1px solid #1d1e33; /* 侧边栏右边框，增加深色边界线 */
}

/* 优化菜单项样式 */
.el-menu-vertical-demo:not(.el-menu--collapse) {
  background-color: #2c3e50; /* 菜单背景色 */
  color: #fff; /* 菜单文字颜色 */
}

.el-menu-item > span > a {
  color: #fff; /* 链接颜色 */
  text-decoration: none; /* 去除下划线 */
}

.el-menu-item > span > a:hover {
  color: #ffd04b; /* 鼠标悬停颜色，黄金色 */
}

.el-menu-item.is-active > span > a {
  color: #ffd04b; /* 激活菜单项颜色，黄金色 */
}

.el-menu-item:hover {
  background-color: #1f2d3d; /* 鼠标悬停背景色，更深的蓝色 */
}

.main-content {
  background-color: #eef1f6; /* 主内容区域背景色 */
  padding: 20px; /* 内边距 */
}

/* 菜单图标和文字的对齐和间距 */
.el-menu-item i {
  margin-right: 8px; /* 图标和文字之间的间距 */
  vertical-align: middle; /* 垂直居中 */
}

/* 调整路由链接的排版，确保垂直居中 */
.el-menu-item span {
  display: flex;
  align-items: center;
}

/* 滚动条样式，如果侧边栏内容超出高度时 */
.aside-menu::-webkit-scrollbar {
  width: 5px; /* 滚动条宽度 */
}

.aside-menu::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: rgba(255, 255, 255, .2); /* 滚动条颜色 */
}

.aside-menu::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, .1); /* 滚动条轨道颜色 */
}
</style>
