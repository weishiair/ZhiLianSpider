import { createApp } from 'vue';
import App from './App.vue';
import index from '/src/router'; // 导入路由配置
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css'  // 引入 ElementPlus 组件样式
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
axios.defaults.baseURL = 'http://localhost:8080/api';

app.use(index); // 使用路由
app.use(ElementPlus);
app.mount('#app');


const debounce = (fn, delay) => {
    let timer = null;
    return function () {
        let context = this;
        let args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
    constructor(callback) {
        callback = debounce(callback, 16);
        super(callback);
    }
}