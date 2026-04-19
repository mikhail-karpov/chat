import { useMemo } from 'react'
import { useQuery } from '@tanstack/react-query'
import { searchUsers } from '../api/users'
import type { Contact, UserSearchResult } from '../types/chat'

interface UseUserSearchResult {
  localMatches: Contact[]
  apiResults: UserSearchResult[]
  isFetching: boolean
}

export function useUserSearch(
  query: string,
  submittedQuery: string,
  contacts: Contact[],
): UseUserSearchResult {
  const localMatches = useMemo(
    () =>
      query.trim()
        ? contacts.filter((c) => c.username.toLowerCase().includes(query.toLowerCase()))
        : [],
    [query, contacts],
  )

  const { data: apiResults = [], isFetching } = useQuery({
    queryKey: ['users', 'search', submittedQuery],
    queryFn: () => searchUsers(submittedQuery),
    enabled: submittedQuery.trim().length > 0,
    staleTime: 30 * 1000,
  })

  return { localMatches, apiResults, isFetching }
}
