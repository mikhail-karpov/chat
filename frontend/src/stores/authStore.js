import {defineStore} from "pinia";
import {fetchCurrentUser, login} from "../api/index.js";

export const useAuthStore = defineStore("auth", {

  state: () => {
    return {
      user: undefined
    }
  },

  getters: {
    isAuthenticated: state => {
      return state.user !== undefined
    },
    getUser: state => {
      return state.user
    }
  },

  actions: {
    authenticate() {
      return fetchCurrentUser()
      .then(user => this.user = user)
      .catch(() => login());
    }
  }

});
