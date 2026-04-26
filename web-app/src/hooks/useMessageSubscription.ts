import { useQueryClient } from '@tanstack/react-query'
import { useAuth } from '@/hooks/useAuth'
import { useSubscription } from '@/hooks/useSubscription'
import type { Message } from '@/types/chat'

export function useMessageSubscription() {
  const { user } = useAuth()
  const qc = useQueryClient()

  useSubscription<Message & { createdAt: string | Date }>(user ? `personal:#${user.id}` : '', {
    enabled: !!user,
    onPublication: (raw) => {
      const msg: Message = { ...raw, createdAt: new Date(raw.createdAt) }
      qc.setQueryData<Message[]>(['messages', msg.conversationId], (old) => [...(old ?? []), msg])
    },
  })
}
