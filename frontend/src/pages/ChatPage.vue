<script setup>
import {onMounted, ref} from "vue";
import {fetchMessages, postMessage} from "../api/api.js";
import {useCentrifuge} from "../api/useCentrifuge.js";
import MessageCard from "../components/MessageCard.vue";
import MessageForm from "../components/MessageForm.vue";

const {isConnected, subscribe} = useCentrifuge();
const scroll = ref(null);
const messages = ref([]);

onMounted(() => {
  listMessages();
  const subscription = subscribe("chat");
  subscription.on("publication", ctx => {
    messages.value.push(ctx.data);
  });
});

function listMessages() {
  fetchMessages()
  .then(m => messages.value = m)
  .then(() => scrollToEnd())
  .catch(() => console.error("Failed to get messages"));
}

function sendMessage(text) {
  postMessage(text)
  .then(() => scrollToEnd())
  .catch(() => console.error("Failed to send message"));
}

function scrollToEnd() {
  scroll.value?.scrollIntoView({behavior: "smooth"});
}

</script>

<template>
  <div v-if="!isConnected">
    <h1>Connecting...</h1>
  </div>
  <div v-else id="chat-container">
    <h1>Chat</h1>
    <div id="messages-container">
      <div v-for="message in messages" :key="message.id">
        <MessageCard :message="message"/>
      </div>
      <span ref="scroll"></span>
    </div>
    <MessageForm @send-message="sendMessage"/>
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
</style>
