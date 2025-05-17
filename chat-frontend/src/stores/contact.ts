import {defineStore} from "pinia";
import {ref} from "vue";
import {API} from "../api/api.ts";

export type User = {
  id: string,
  username: string
}

export type Contact = {
  conversationId: string,
  id: string,
  username: string,
  status?: 'PENDING' | 'APPROVED' | 'BLOCKED'
}

const useContactStore = defineStore("contacts", () => {

  const contacts = ref<Contact[]>([]);

  async function loadContacts(): Promise<Contact[]> {
    const response = await API.get("/api/v1/contacts");
    contacts.value = await response.data.contacts;
    return contacts.value;
  }

  async function addContact(userId: string) {
    if (isContact(userId)) {
      return;
    }
    try {
      await API.post(`/api/v1/contacts/${userId}`);
      await loadContacts();
    } catch (error) {
      console.error("Failed to add contact", error);
    }
  }

  function isContact(userId: string): boolean {
    return contacts.value.some(contact => contact.id === userId);
  }

  async function searchUsers(username: string): Promise<User[]> {
    try {
      const response = await API.get(`/api/v1/users/search?query=${username}`);
      return response.data.users;
    } catch (error) {
      console.error("Failed to search users", error);
      return [];
    }
  }

  const selectedContact = ref<Contact>();

  async function selectContact(conversationId: string | undefined) {
    if (!conversationId) {
      selectedContact.value = undefined;
      return;
    }
    selectedContact.value = contacts.value.find(contact => contact.conversationId === conversationId);
  }

  return {
    contacts,
    loadContacts,
    addContact,
    isContact,
    selectedContact,
    selectContact,
    searchUsers
  };

});

export default useContactStore;