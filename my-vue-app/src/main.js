import { createApp } from 'vue';
import App from './App.vue';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const app = createApp(App);

// 设置全局 Axios 访问点
app.config.globalProperties.$axios = axios;

app.mount('#app');
