import {onMounted, onUnmounted, ref} from "vue";
import {defineStore} from "pinia";
import {API} from "../api/api.ts";
import {Centrifuge} from "centrifuge";

export type Message = {
  id: string,
  conversationId: string,
  userId: string,
  text: string,
  createdAt: string
}

const WS_URL = "ws://localhost:8080/api/v1/centrifugo/connection/websocket";

const useChatStore = defineStore('chat', () => {

  const centrifuge = ref(new Centrifuge(WS_URL, {
    debug: import.meta.env.DEV,
  }));
  centrifuge.value.on("publication", (ctx) => {
    const message: Message = ctx.data;
    if (message.conversationId === conversationId.value) {
      messages.value = [...messages.value, message];
    }
  });

  onMounted(() => centrifuge.value.connect());
  onUnmounted(() => centrifuge.value.disconnect());

  const messages = ref<Message[]>([]);
  const conversationId = ref<string>();

  async function setChat(chatId: string) {
    if (conversationId.value === chatId) {
      return;
    }
    conversationId.value = chatId;
    try {
      const response = await API.get(`/api/v1/messages?conversationId=${conversationId.value}`);
      const loadedMessages: Message[] = response.data;
      messages.value = loadedMessages.sort((a, b) => a.createdAt < b.createdAt ? 1 : -1);
    } catch (error) {
      console.error("Failed to load messages", error);
    }
  }

  async function sendMessage(text: string) {
    try {
      await API.post("/api/v1/messages", {
        conversationId: conversationId.value,
        text: text
      });
    } catch (error) {
      console.error("Failed to send message", error);
    }
  }

  return {setChat, messages, sendMessage};
});

export default useChatStore;