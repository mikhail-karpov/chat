import {createRouter, createWebHistory} from "vue-router";
import {useAuthStore} from "../stores/authStore.js";
import ChatPage from "../pages/ChatPage.vue";
import NotFoundPage from "../pages/NotFoundPage.vue";
import LoginPage from "../pages/LoginPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "chat",
      component: ChatPage
    },
    {
      path: '/login',
      name: "login",
      component: LoginPage
    },
    {
      path: '/:catchAll(.*)',
      name: 'not-found',
      component: NotFoundPage,
    },
  ]
});

router.beforeEach((to) => {
  const publicRoutes = [
      "login",
      "not-found"
  ];
  const isAuthRequired = !publicRoutes.includes(to.name);
  const auth = useAuthStore();

  if (isAuthRequired && !auth.isAuthenticated) {
    return "/login";
  }
});

export default router;