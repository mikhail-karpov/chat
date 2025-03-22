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
  display: flex;
  justify-content: center;
  text-align: center;
  margin: 10rem 0;
}
</style>