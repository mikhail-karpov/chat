<script setup>

import {computed} from "vue";
import {useAuthStore} from "../stores/authStore.js";
import {useQuery} from "@tanstack/vue-query";
import {fetchUserProfile} from "../api/index.js";

const props = defineProps({
  message: {
    type: Object,
    required: true
  }
});

const {data: user} = useQuery({
  queryKey: ["users", props.message.userId],
  queryFn: () => fetchUserProfile(props.message.userId)
});

const auth = useAuthStore();

const isOwnMessage = computed(() => {
  return auth.getUser?.id === props.message.userId;
});

const messageTime = computed(() => {
  const date = Date.parse(props.message.createdAt);
  const formatter = new Intl.DateTimeFormat("en-UK", {
    hour: "2-digit",
    minute: "2-digit"
  });
  return formatter.format(date);
})

</script>

<template>
  <div class="message-container" :class="isOwnMessage ? 'right' : 'left'">
    <div v-if="user?.username !== undefined" class="message-author" :class="isOwnMessage ? 'right' : 'left'">
      {{ user.username }}
    </div>
    <div class="message-text" :class="isOwnMessage ? 'mine' : 'not-mine'">
      <p class="message-text">
        {{ props.message.text }}
      </p>
      <p class="message-time">
        {{messageTime}}
      </p>
    </div>
  </div>

</template>

<style scoped>

.message-container {
  margin-bottom: 1rem;
  width: 75%;
  display: flex;
  flex-direction: column;
}

.left {
  margin-left: 0.25rem;
  margin-right: auto;
}

.right {
  margin-left: auto;
  margin-right: 0.25rem;
}

.message-author {
  color: #616161;
  font-size: 14px;
}

.message-text {
  padding: 0.5rem;
  border-radius: 5px;
  overflow: auto;
}

.message-time {
  color: slategray;
  font-size: 12px;
  text-align: right;
}

.mine {
  background-color: #E3F2FD;
}

.not-mine {
  background-color: #F5F5F5;
}


</style>