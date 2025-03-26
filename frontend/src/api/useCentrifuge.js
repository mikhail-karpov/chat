import {Centrifuge} from "centrifuge";
import {onMounted, onUnmounted, ref, watch} from "vue";

const WS_URL = `${import.meta.env.VITE_WS_URL}/api/v1/centrifugo/connection/websocket`;

export function useCentrifuge() {

  const isError = ref(false);
  const isConnected = ref(false);

  const centrifuge = ref(new Centrifuge(WS_URL, {debug: import.meta.env.DEV}));
  centrifuge.value.on("error", () => {
    isError.value = true
  });
  centrifuge.value.on("connected", () => {
    isError.value = false;
    isConnected.value = true;
  });
  centrifuge.value.on("disconnected", () => {
    isConnected.value = false;
  });
  const subscriptions = ref(new Map());

  onMounted(() => centrifuge.value.connect());
  onUnmounted(() => centrifuge.value.disconnect());

  function subscribe(channel) {
    let subscription = subscriptions.value.get(channel);
    if (subscription === undefined) {
      subscription = centrifuge.value.newSubscription(channel);
      subscription.subscribe();
      subscriptions.value.set(channel, subscription);
    }
    return subscription;
  }

  return {isConnected, isError, subscribe};
}