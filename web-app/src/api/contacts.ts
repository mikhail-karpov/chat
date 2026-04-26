import type { Contact, ContactStatus } from '../types/chat'
import { apiClient } from './client'

export async function getContacts(statuses: ContactStatus[]): Promise<Contact[]> {
  const res = await apiClient.get(`/api/v1/contacts?statuses=${statuses.join(',')}`)
  return res.data.contacts
}

export async function addContact(userId: string): Promise<void> {
  await apiClient.post(`/api/v1/contacts/${userId}`)
}

export async function blockContact(userId: string): Promise<void> {
  await apiClient.post(`/api/v1/contacts/${userId}/block`)
}
