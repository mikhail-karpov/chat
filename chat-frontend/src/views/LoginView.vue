<script setup lang="ts">

import {onMounted} from "vue";
import router from "../router/router.js";
import useAuthStore from "../stores/auth.ts";

const authStore = useAuthStore();

onMounted(() => {
  authStore.authenticate().then(() => router.push("/"));
});

</script>

<template>
  <div style="text-align: center">
    <h1>Loading...</h1>
  </div>
</template>
