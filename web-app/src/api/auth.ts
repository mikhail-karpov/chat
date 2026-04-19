import { isAxiosError } from 'axios'
import type { CurrentUser } from '../types/auth'
import { apiClient } from './client'

export async function getCurrentUser(): Promise<CurrentUser | null> {
  try {
    const { data } = await apiClient.get<CurrentUser>('/api/v1/auth')
    return data
  } catch (err) {
    if (isAxiosError(err) && err.response?.status === 401) return null
    throw err
  }
}
