import { createApp } from 'vue';
import {createPinia} from "pinia";
import 'primeicons/primeicons.css'
import PrimeVue from "primevue/config";
import Aura from '@primeuix/themes/aura';
import App from './App.vue';
import router from "./router/router.ts";
import './style.css';

const pinia = createPinia();
const app = createApp(App);

app.use(pinia);
app.use(router);
app.use(PrimeVue, {
  theme: {
    preset: Aura
  }
});
app.mount('#app')
