<script setup lang="ts">
import {RouterLink} from "vue-router";
</script>

<template>
  <div style="text-align: center">
    <h1>404</h1>
    <div>
      <p>This page does not exist</p>
      <p><RouterLink to="/" >Back to homepage</RouterLink></p>
    </div>
  </div>
</template>
