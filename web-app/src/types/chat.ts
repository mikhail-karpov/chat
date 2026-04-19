export type ContactStatus = 'PENDING' | 'APPROVED' | 'BLOCKED'

export interface Contact {
  id: string
  conversationId: string
  username: string
  status: ContactStatus
  unread?: number
  online: boolean
}

export interface Message {
  id: string
  conversationId: string
  userId: string
  text: string
  createdAt: Date
}

export interface UserSearchResult {
  id: string
  username: string
}
