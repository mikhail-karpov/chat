<script setup lang="ts">

import {ref} from "vue";
import InputText from "primevue/inputtext";
import Button from "primevue/button";

const message = ref("");
const emit = defineEmits(['send-message'])

function submitMessage() {
  const text = message.value.trim();
  if (text.length >= 3) {
    message.value = "";
    emit('send-message', text);
  }
}

</script>

<template>

  <form @submit.prevent="submitMessage" class="message-form">
    <InputText v-model="message"
               placeholder="Enter your message..."
               :maxlength="128"
               style="width: 100%"/>
    <Button
        type="submit"
        icon="pi pi-send"
        :disabled="message.trim().length < 3"
        style="margin-left: 1rem; padding: 0.5rem 2rem"
    />
  </form>

</template>

<style scoped>

.message-form {
  display: flex;
  gap: 0.5rem;
}

</style>