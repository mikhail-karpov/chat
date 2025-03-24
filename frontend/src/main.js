import { createApp } from 'vue'
import { createPinia } from "pinia";
import { VueQueryPlugin } from "@tanstack/vue-query";
import Toast from "vue-toastification";
import App from './App.vue'
import router from "./router";
import "vue-toastification/dist/index.css";
import './style.css'

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
app.use(router);
app.use(Toast);
app.use(VueQueryPlugin, {
  queryClientConfig: {
    defaultOptions: {
      queries: {
        staleTime: 1000 * 60 // 1 min
      }
    }
  }
});
app.mount('#app');
