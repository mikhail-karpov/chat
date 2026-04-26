import type { UserSearchResult } from '../types/chat'
import { apiClient } from './client'

export async function searchUsers(query: string): Promise<UserSearchResult[]> {
  const res = await apiClient.get('/api/v1/users/search', { params: { query } })
  return res.data.users
}
