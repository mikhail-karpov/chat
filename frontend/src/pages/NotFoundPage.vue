<script setup>
import {RouterLink} from "vue-router";
</script>

<template>
<div id="not-found-container">
  <div>
    <h1>404</h1>
    <div>
      <p>This page does not exist</p>
      <p><RouterLink to="/" >Back to homepage</RouterLink></p>
    </div>
  </div>
</div>
</template>

<style scoped>

#not-found-container {
  text-align: center;
}
</style>