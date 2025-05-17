<script lang="ts">

import {defineComponent} from "vue";
import ContactContainer from "../components/contacts/ContactContainer.vue";
import ChatContainer from "../components/chat/ChatContainer.vue";

export default defineComponent({
  components: {ChatContainer, ContactContainer}
})
</script>

<template>

  <div class="container">
    <ContactContainer />
    <ChatContainer style="flex: 1"/>
  </div>

</template>

<style scoped>

.container {
  display: flex;
  height: 100vh;
}

</style>