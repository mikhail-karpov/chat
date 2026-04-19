import { cn } from '@/lib/utils'
import { useAuth } from '@/hooks/useAuth'
import type { Message } from '@/types/chat'

interface Props {
  message: Message
}

export function MessageBubble({ message }: Props) {
  const { user } = useAuth()
  const isMe = message.userId === user?.id
  const time = message.createdAt.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' })
  return (
    <div className={cn('flex', isMe ? 'justify-end' : 'justify-start')}>
      <div
        className={cn(
          'max-w-[62%] border px-3 py-2 text-[13px] leading-relaxed',
          isMe
            ? 'rounded-[14px] rounded-br-[4px] border-ink bg-ink text-paper'
            : 'rounded-[14px] rounded-bl-[4px] border-line bg-paper text-ink',
        )}
      >
        {message.text}
        <span className="mt-1 block font-mono text-[9.5px] opacity-60">{time}</span>
      </div>
    </div>
  )
}
