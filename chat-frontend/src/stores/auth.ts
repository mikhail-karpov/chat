import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {API, BASE_URL} from "../api/api.ts";

export type CurrentUser = {
  id: string,
  username: string
}

async function getCurrentUser(): Promise<CurrentUser> {
  const response = await API.get("/api/v1/auth");
  return await response.data;
}

function login() {
  window.location.href = `${BASE_URL}/oauth2/authorization/auth-server`;
}

const useAuthStore = defineStore("auth", () => {

  const user = ref<CurrentUser>();

  const isAuthenticated = computed(() => {
    return user.value !== undefined;
  });

  async function authenticate() {
    try {
      user.value = await getCurrentUser();
    } catch (error) {
      login();
    }
  }

  return {user, isAuthenticated, authenticate};
});

export default useAuthStore;