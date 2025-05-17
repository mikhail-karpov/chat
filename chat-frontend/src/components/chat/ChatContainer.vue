<script setup lang="ts">

import MessageCard from "./MessageCard.vue";
import MessageForm from "./MessageForm.vue";
import useContactStore from "../../stores/contact.ts";
import useChatStore from "../../stores/chat.ts";
import {computed, nextTick, useTemplateRef, watch} from "vue";

const contactStore = useContactStore();
const chatStore = useChatStore();
const scroll = useTemplateRef("scroll");
const messages = computed(() => chatStore.messages);

async function submitMessage(text: string) {
  if (contactStore.selectedContact?.conversationId === undefined) {
    return;
  }
  await chatStore.sendMessage(text);
  await nextTick();
  scroll.value?.scrollIntoView({behavior: "smooth"});
}

watch(messages, async () => {
  await nextTick();
  scroll.value?.scrollIntoView({behavior: "smooth"});
}, {deep: true});

</script>

<template>

  <div v-if="!contactStore.selectedContact" class="chat-container">
    Select contact to start chatting
  </div>

  <div v-else class="chat-container">
    <div class="user-card">
      {{contactStore.selectedContact.username}}
    </div>

    <div class="messages-list">
      <div v-for="message in messages">
        <MessageCard :message="message" :key="message.id"/>
      </div>
      <span ref="scroll"/>
    </div>

    <MessageForm @send-message="submitMessage"/>
  </div>

</template>

<style scoped>

.chat-container {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  gap: 2rem;
  background-color: var(--p-gray-50);
}

.user-card {
  font-weight: bold;
}

.messages-list {
  flex: 1;
  overflow-y: scroll;
}
</style>
