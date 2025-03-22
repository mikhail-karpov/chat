<script setup>

import {onMounted, ref, useTemplateRef} from "vue";
import {fetchMessages, postMessage} from "../api/index.js";

const message = ref("");
const messages = ref([]);
const scroll = useTemplateRef("scroll");

function getMessages() {
  fetchMessages()
  .then(m => messages.value = [...m])
  .then(() => scrollToEnd())
  .catch(error => console.error("Failed to get messages", error.message));
}

function submitMessage() {
  const text = message.value;
  if (!text.length) {
    return;
  }
  message.value = "";
  postMessage(text)
  .then(m => messages.value = [...messages.value, m])
  .then(() => scrollToEnd())
  .catch(error => console.error("Failed to send messages", error.message));
}

function scrollToEnd() {
  scroll.value.scrollIntoView({behavior: "smooth"});
}

onMounted(() => getMessages());

</script>

<template>
  <div id="chat-container">
    <h1>Chat</h1>
    <div id="messages-container">
      <div v-for="message in messages" :key="message.id" id="message">
        {{ message.text }}
      </div>
      <div ref="scroll"></div>
    </div>
    <form @submit.prevent="submitMessage" id="send-message-form">
      <input type="text" v-model.trim="message" placeholder="Your message..."/>
      <button type="submit" :disabled="message.trim().length === 0">Send</button>
    </form>
  </div>
</template>

<style scoped>

#chat-container {
  display: flex;
  flex-direction: column;
  margin: 2rem 5rem;
  height: calc(100vh - 4rem);
  max-width: 640px;
}

#messages-container {
  margin: 1rem 0;
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow-y: auto;
  justify-content: safe flex-end;
  border: 1px solid black;
}

#message {
  padding: 0.5rem;
}

#send-message-form {
  display: flex;
}

#send-message-form input {
  flex: 1;
}

#send-message-form button {
  margin-left: 1rem;
}

</style>
