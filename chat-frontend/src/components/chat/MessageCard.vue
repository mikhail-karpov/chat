<script setup lang="ts">
import {computed} from "vue";
import type {Message} from "../../stores/chat.ts";
import useAuthStore from "../../stores/auth.ts";
import {formatDate} from "../../utils/dateUtils.ts";

const props = defineProps<{
  message: Message
}>();

const authStore = useAuthStore();

const isMineMessage = computed(() => authStore.user?.id === props.message.userId);

const timestamp = computed(() => {
  const messageDate = new Date(props.message.createdAt);
  return formatDate(messageDate);
})
</script>

<template>

  <div class="message-row">
    <div class="message-item" :class="isMineMessage ? 'mine' : 'not-mine'">
      <div class="message-text">
        {{ props.message.text }}
      </div>
      <div class="message-timestamp">
        {{ timestamp }}
      </div>
    </div>
  </div>

</template>

<style scoped>

.message-row {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}

.message-item {
  padding: 1rem;
  min-width: 10rem;
  max-width: 65%;
  border-radius: 0.5rem;
}

.mine {
  align-self: flex-end;
  background-color: var(--p-emerald-200);
  border: 1px solid var(--p-emerald-300);
  text-align: right;
  margin-right: 2rem;
}

.not-mine {
  align-self: flex-start;
  background-color: white;
  border: 1px solid var(--p-gray-200);
}

.message-text {
  overflow-y: auto;
  margin-bottom: 0.5rem;
}

.message-timestamp {
  font-size: small;
  color: gray;
}

</style>