// router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import UserConfig from '../components/UserConfig.vue';
import TaskScheduleManager from '../components/TaskScheduleManager.vue';
import JobInfoView from '../components/JobInfoView.vue'; // 引入 JobInfoView 组件
import CompanyInfoView from '../components/CompanyInfoView.vue';
import HomePage from "../components/homePage.vue";

const routes = [
    {
        path: '/user-config',
        name: 'UserConfig',
        component: UserConfig
    },
    {
        path: '/task-schedule-manager',
        name: 'TaskScheduleManager',
        component: TaskScheduleManager
    },
    {
        path: '/job-info',
        name: 'JobInfo',
        component: JobInfoView
    },
    {
        path: '/company-info', // 新增路由路径
        name: 'CompanyInfo',
        component: CompanyInfoView
    },
    {
        path: '/',
        name: 'HomePage',
        component: HomePage
    },
];

const index = createRouter({
    history: createWebHistory(),
    routes
});

export default index;
