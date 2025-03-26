<script setup>

import {onMounted, ref, watch} from "vue";
import {fetchMessages, postMessage} from "../api/api.js";
import MessageCard from "../components/MessageCard.vue";
import {useToast} from "vue-toastification";
import {useCentrifuge} from "../api/useCentrifuge.js";

const {isConnected, isError, subscribe} = useCentrifuge();
const scroll = ref(null);
const message = ref("");
const messages = ref([]);
const toast = useToast();

onMounted(() => {
  listMessages();
  const subscription = subscribe("chat");
  subscription.on("publication", ctx => {
    messages.value = [...messages.value, ctx.data];
  });
});

watch(isError, () => {
  if (isError.value) {
    toast.error("Disconnected");
  }
});

function listMessages() {
  fetchMessages()
  .then(m => messages.value = [...m])
  .then(() => scrollToEnd())
  .catch(() => toast.error("Failed to get messages"));
}

function submitMessage() {
  const text = message.value;
  message.value = "";
  postMessage(text)
  .then(() => scrollToEnd())
  .catch(() => toast.error("Failed to send message"));
}

function scrollToEnd() {
  scroll.value?.scrollIntoView({behavior: "smooth"});
}

</script>

<template>
  <div v-if="!isConnected || isError" id="chat-container">
    Connecting...
  </div>
  <div v-else id="chat-container">
    <h1>Chat</h1>
    <div id="messages-container">
      <div v-for="message in messages" :key="message.id">
        <MessageCard :message="message"/>
      </div>
      <span ref="scroll"></span>
    </div>
    <form @submit.prevent="submitMessage" id="send-message-form">
      <input type="text" v-model.trim="message" placeholder="Your message..." maxlength="128"/>
      <button type="submit" :disabled="message.length < 3">Send</button>
    </form>
  </div>
</template>

<style scoped>

#chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 4rem);
  width: 640px;
}

#messages-container {
  padding: 1rem;
  margin: 1rem 0;
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow-y: scroll;
  justify-content: safe flex-end;
}

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
