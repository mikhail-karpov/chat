import type { Message } from '../types/chat'
import { apiClient } from './client'

function deserialize(msg: Message & { createdAt: string | Date }): Message {
  return { ...msg, createdAt: new Date(msg.createdAt) }
}

export async function getMessages(conversationId: string, limit = 50): Promise<Message[]> {
  const res = await apiClient.get('/api/v1/messages', {
    params: { conversationId, limit },
  })
  return (res.data as (Message & { createdAt: string })[]).map(deserialize)
}

export async function sendMessage(conversationId: string, text: string): Promise<Message> {
  const res = await apiClient.post('/api/v1/messages', { conversationId, text })
  return deserialize(res.data)
}
