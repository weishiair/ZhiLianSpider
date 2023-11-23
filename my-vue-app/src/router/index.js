// router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import UserConfig from '../components/UserConfig.vue';
import TaskScheduleManager from '../components/TaskScheduleManager.vue';

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
    }
];

const index = createRouter({
    history: createWebHistory(),
    routes
});

export default index;
