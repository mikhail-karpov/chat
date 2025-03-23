<script setup>

import {onMounted} from "vue";
import {useAuthStore} from "../stores/authStore";
import router from "../router/index.js";

const auth = useAuthStore();

onMounted(() => {
  auth.authenticate().then(() => router.push("/"));
});

</script>

<template>
  <div><h1>Loading...</h1></div>
</template>

<style scoped>

</style>