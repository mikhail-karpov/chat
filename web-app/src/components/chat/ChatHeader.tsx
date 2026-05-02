import { UserCheck, UserX } from 'lucide-react'
import { useApproveContact, useBlockContact } from '@/hooks/useContacts'
import type { Contact } from '@/types/chat'
import { UserDisplay } from './UserDisplay'

interface Props {
  contact: Contact
}

export function ChatHeader({ contact }: Props) {
  const approve = useApproveContact(contact.id)
  const block = useBlockContact(contact.id)

  const isPending = approve.isPending || block.isPending

  return (
    <div className="flex shrink-0 items-center gap-3.5 border-b border-line bg-paper px-8 py-[18px]">
      <UserDisplay {...contact} />

      {/* Actions */}
      <div className="flex items-center gap-2">
        {contact.status === 'PENDING' && (
          <button
            disabled={isPending}
            onClick={() => approve.mutate()}
            className="flex items-center gap-1.5 rounded px-2.5 py-1.5 text-[13px] text-ink-2 transition-colors hover:bg-paper-2 disabled:opacity-50"
          >
            <UserCheck size={14} strokeWidth={1.75} />
            Approve
          </button>
        )}
        {contact.status === 'BLOCKED' ? (
          <button
            disabled={isPending}
            onClick={() => approve.mutate()}
            className="flex items-center gap-1.5 rounded px-2.5 py-1.5 text-[13px] text-ink-2 transition-colors hover:bg-paper-2 disabled:opacity-50"
          >
            <UserCheck size={14} strokeWidth={1.75} />
            Unblock
          </button>
        ) : (
          <button
            disabled={isPending}
            onClick={() => block.mutate()}
            className="flex items-center gap-1.5 rounded px-2.5 py-1.5 text-[13px] text-ink-2 transition-colors hover:bg-paper-2 disabled:opacity-50"
          >
            <UserX size={14} strokeWidth={1.75} />
            Block
          </button>
        )}
      </div>
    </div>
  )
}
