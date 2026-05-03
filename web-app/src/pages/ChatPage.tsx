import { useParams } from 'react-router-dom'
import { ChatPanel } from '@/components/chat/ChatPanel'
import { DetailsPanel } from '@/components/chat/DetailsPanel'
import { Sidebar } from '@/components/chat/Sidebar'
import { useContacts } from '@/hooks/useContacts'
import { useMessageSubscription } from '@/hooks/useMessageSubscription'

export function ChatPage() {
  const { contactId } = useParams()
  const { data: contacts } = useContacts()
  useMessageSubscription()

  const activeContact = contacts?.find((c) => c.id === contactId)

  return (
    <div
      className="grid h-screen overflow-hidden border border-line"
      style={{
        gridTemplateColumns: activeContact ? '280px 1fr 280px' : '280px 1fr',
      }}
    >
      <Sidebar />
      {activeContact ? <ChatPanel contact={activeContact} /> : <EmptyPanel />}
      {activeContact && <DetailsPanel contact={activeContact} />}
    </div>
  )
}

function EmptyPanel() {
  return (
    <main className="flex min-w-0 flex-1 flex-col items-center justify-center bg-paper">
      <span className="font-mono text-[12px] text-ink-3">select a contact to start chatting</span>
    </main>
  )
}
