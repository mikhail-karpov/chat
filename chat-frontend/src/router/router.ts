import {createRouter, createWebHistory} from "vue-router";
import ChatView from "../views/ChatView.vue";
import NotFoundView from "../views/NotFoundView.vue";
import LoginView from "../views/LoginView.vue";
import useAuthStore from "../stores/auth.ts";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "chat",
      component: ChatView
    },
    {
      path: '/login',
      name: "login",
      component: LoginView
    },
    {
      path: '/:catchAll(.*)',
      name: 'not-found',
      component: NotFoundView,
    },
  ]
});

router.beforeEach((to) => {
  const publicRoutes = [
    "login",
    "not-found"
  ];
  const isAuthRequired = !publicRoutes.includes(to.name as string);
  const authStore = useAuthStore();
  if (isAuthRequired && !authStore.isAuthenticated) {
    return "/login";
  }
});

export default router;