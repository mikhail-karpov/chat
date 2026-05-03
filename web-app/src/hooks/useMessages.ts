import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { getMessages, sendMessage } from '../api/messages'
import { useAuth } from './useAuth'
import type { Message } from '../types/chat'

export function useMessages(conversationId: string, limit?: number) {
  return useQuery({
    queryKey: ['messages', conversationId],
    queryFn: () => getMessages(conversationId, limit),
    staleTime: 30 * 1000,
    select: (msgs) => [...msgs].sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime()),
  })
}

export function useSendMessage() {
  const qc = useQueryClient()
  const { user } = useAuth()
  return useMutation({
    mutationFn: ({ conversationId, text }: { conversationId: string; text: string }) =>
      sendMessage(conversationId, text),
    onMutate: async ({ conversationId, text }) => {
      await qc.cancelQueries({ queryKey: ['messages', conversationId] })
      const previous = qc.getQueryData<Message[]>(['messages', conversationId])
      const optimistic: Message = {
        id: `optimistic-${Date.now()}`,
        conversationId,
        userId: user?.id ?? '',
        text,
        createdAt: new Date(),
      }
      qc.setQueryData<Message[]>(['messages', conversationId], (old) => [
        ...(old ?? []),
        optimistic,
      ])
      return { previous, conversationId }
    },
    onError: (_err, _vars, context) => {
      if (context?.previous !== undefined) {
        qc.setQueryData<Message[]>(['messages', context.conversationId], context.previous)
      }
    },
    onSettled: (_data, _err, { conversationId }) => {
      qc.invalidateQueries({ queryKey: ['messages', conversationId] })
    },
  })
}
