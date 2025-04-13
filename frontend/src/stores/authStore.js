import {defineStore} from "pinia";
import {fetchCurrentUser, login} from "../api/api.js";
import {computed, ref} from "vue";

export const useAuthStore = defineStore("auth", () => {

  const user = ref();

  const isAuthenticated = computed(() => {
    return user.value !== undefined;
  })

  function authenticate() {
      return fetchCurrentUser()
      .then(u => user.value = u)
      .catch(() => login());
  }

  return {user, isAuthenticated, authenticate}

});
