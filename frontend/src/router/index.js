import {createRouter, createWebHistory} from "vue-router";
import ChatPage from "../pages/ChatPage.vue";
import NotFoundPage from "../pages/NotFoundPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "chat",
      component: ChatPage
    },
    {
      path: '/:catchAll(.*)',
      name: 'not-found',
      component: NotFoundPage,
    }
  ]
});

export default router;