import { createApp } from 'vue';
import { gapi } from 'gapi-script';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './router';
import 'bootstrap-icons/font/bootstrap-icons.css';


const app = createApp(App);

// 创建 script 元素
const script = document.createElement('script');
script.src = 'https://apis.google.com/js/api.js';
script.async = true;
script.defer = true;
script.onload = () => {
  // Google API 客户端库加载完成后执行初始化逻辑
  app.mount('#app');
};
document.head.appendChild(script);

// 注册 ElementPlusIconsVue 组件
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(ElementPlus);
app.use(router);
