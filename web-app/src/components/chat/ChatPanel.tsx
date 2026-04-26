import { useEffect, useRef } from 'react'
import { ScrollArea } from '@/components/ui/scroll-area'
import type { Contact } from '@/types/chat'
import { useMessages, useSendMessage } from '@/hooks/useMessages'
import { Composer } from './Composer'
import { MessageBubble } from './MessageBubble'
import { ChatHeader } from './ChatHeader'

interface Props {
  contact: Contact
}

export function ChatPanel({ contact }: Props) {
  const bottomRef = useRef<HTMLDivElement>(null)
  const { data: messages = [] } = useMessages(contact.conversationId)
  const sendMessage = useSendMessage()

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleSend = (text: string) => {
    sendMessage.mutate({ conversationId: contact.conversationId, text })
  }

  return (
    <main className="flex min-w-0 flex-col bg-paper h-full overflow-hidden">
      <ChatHeader contact={contact} />
      <ScrollArea className="flex-1 min-h-0">
        <div className="flex flex-col gap-2.5 px-6 py-5">
          {messages.map((msg) => (
            <MessageBubble key={msg.id} message={msg} />
          ))}
          <div ref={bottomRef} />
        </div>
      </ScrollArea>

      <Composer onSend={handleSend} />
    </main>
  )
}
