<script setup>
import {ref} from "vue";

const message = ref("");
const emit = defineEmits(['send-message'])

function submitMessage() {
  const text = message.value;
  emit('send-message', text);
  message.value = "";
}
</script>

<template>
  <form @submit.prevent="submitMessage" id="send-message-form">
    <input type="text" v-model.trim="message" placeholder="Your message..." maxlength="128"/>
    <button type="submit" :disabled="message.length < 3">Send</button>
  </form>
</template>

<style scoped>
#send-message-form {
  margin-top: 0.5rem;
  display: flex;
}

#send-message-form input {
  flex: 1;
}

#send-message-form button {
  margin-left: 1rem;
}
</style>
