<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import useContactStore, {type Contact, type User} from "../../stores/contact.ts";
import Button from "primevue/button";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import InputText from "primevue/inputtext";
import useChatStore from "../../stores/chat.ts";

const contactStore = useContactStore();
const chatStore = useChatStore();
const filteredContacts = ref<Contact[]>([]);
const foundUsers = ref<User[]>([]);
const usernameQuery = ref("");
const searchResult = ref<string>();

onMounted(async () => {
  filteredContacts.value = await contactStore.loadContacts();
})

async function submitSearch() {
  if (usernameQuery.value.trim().length === 0) {
    clearSearch();
    return;
  }
  if (usernameQuery.value.length < 3) {
    clearSearch();
    searchResult.value = "No results found";
    return;
  }

  const found = contactStore.contacts.filter(user =>
    user.username.toLowerCase().includes(usernameQuery.value.toLowerCase()));
  if (found.length > 0) {
    clearSearch();
    filteredContacts.value = found;
    return;
  }

  foundUsers.value = await contactStore.searchUsers(usernameQuery.value);
  usernameQuery.value = "";

  if (foundUsers.value.length === 0) {
    clearSearch();
    searchResult.value = "No results found";
  } else {
    filteredContacts.value = [];
    searchResult.value = undefined;
  }
}

function selectContact(contact: Contact) {
  clearSearch();
  contactStore.selectContact(contact.conversationId);
  chatStore.setChat(contact.conversationId);
}

async function submitAddContact(userId: string) {
  try {
    await contactStore.addContact(userId);
    await contactStore.loadContacts();
  } catch (error) {
    console.error("Failed to add contact", error);
  } finally {
    clearSearch();
  }
}

function clearSearch() {
  filteredContacts.value = contactStore.contacts;
  foundUsers.value = [];
  searchResult.value = undefined;
  usernameQuery.value = "";
}

watch(usernameQuery, () => {
  if (usernameQuery.value.trim().length > 3) {
    filteredContacts.value = contactStore.contacts.filter(user =>
        user.username.toLowerCase().includes(usernameQuery.value.toLowerCase()));
  }
})

</script>

<template>

  <div id="contacts-container">
    <div>
      <form style="padding: 1rem">
        <IconField>
          <InputIcon class="pi pi-search"/>
          <InputText v-model="usernameQuery"
                     placeholder="Search..."
                     @keydown.enter.prevent="submitSearch"/>
        </IconField>
      </form>
      <div v-if="searchResult" style="margin-left: 2rem; margin-bottom: 1rem">
        {{ searchResult }}
      </div>
      <div v-for="user in foundUsers" class="found-user-card">
        <div>
          {{user.username}}
        </div>
        <Button v-if="!contactStore.isContact(user.id)"
                @click="submitAddContact(user.id)"
                size="small"
                icon="pi pi-plus"/>
      </div>
    </div>

    <div v-if="filteredContacts.length > 0">
      <h2 style="padding-left: 1rem">Contacts</h2>
      <div v-for="contact in filteredContacts"
           @click="selectContact(contact)"
           class="contact-card"
           :class="contactStore.selectedContact?.id === contact.id ? 'selected' : ''"
      >
        {{ contact.username }}
      </div>
    </div>
  </div>

</template>

<style scoped>

#contacts-container {
  display: flex;
  flex-direction: column;
  border-right: 1px solid var(--p-gray-300);
}

.found-user-card {
  display: flex;
  margin: 0 2rem;
  align-items: center;
  justify-content: space-between;
}

.contact-card {
  padding: 0.5rem 2rem;
  font-weight: bold;
}

.contact-card:hover {
  background-color: var(--p-gray-100);
  cursor: pointer;
}

.selected {
  background-color: var(--p-gray-100);
}

</style>