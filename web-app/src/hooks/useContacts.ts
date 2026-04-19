import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { addContact, blockContact, getContacts } from '../api/contacts'
import type { Contact } from '../types/chat'

export function useContacts() {
  return useQuery({
    queryKey: ['contacts'],
    queryFn: () => getContacts(['APPROVED', 'PENDING']),
    staleTime: 30 * 1000,
  })
}

export function useAddNewContact() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (userId: string) => addContact(userId),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['contacts'] }),
  })
}

export function useApproveContact(contactId: string) {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: () => addContact(contactId),
    onSuccess: () =>
      qc.setQueryData<Contact[]>(['contacts'], (old) =>
        old?.map((c) => (c.id === contactId ? { ...c, status: 'APPROVED' } : c)),
      ),
  })
}

export function useBlockContact(contactId: string) {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: () => blockContact(contactId),
    onSuccess: () =>
      qc.setQueryData<Contact[]>(['contacts'], (old) => old?.filter((c) => c.id !== contactId)),
  })
}
