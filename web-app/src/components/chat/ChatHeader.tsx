import { UserCheck, UserX } from 'lucide-react'
import { cn } from '@/lib/utils'
import { useApproveContact, useBlockContact } from '@/hooks/useContacts'
import type { Contact } from '@/types/chat'

interface Props {
  contact: Contact
}

export function ChatHeader({ contact }: Props) {
  const approve = useApproveContact(contact.id)
  const block = useBlockContact(contact.id)

  const isPending = approve.isPending || block.isPending

  return (
    <div className="flex shrink-0 items-center gap-3.5 border-b border-line bg-paper px-8 py-[18px]">
      {/* Avatar */}
      <div className="relative flex h-[38px] w-[38px] shrink-0 items-center justify-center rounded-full border border-line bg-paper-2 font-sans text-[13px] font-medium text-ink-2">
        {contact.username[0].toUpperCase()}
        <span
          className={cn(
            'absolute -bottom-px -right-px h-2.5 w-2.5 rounded-full border-[1.5px] border-paper',
            contact.online ? 'bg-online' : 'bg-ink-3',
          )}
        />
      </div>

      {/* Info */}
      <div className="flex min-w-0 flex-1 flex-col">
        <div className="text-[15px] font-semibold leading-tight tracking-[-0.005em] text-ink">
          {contact.username}
        </div>
        <div className="mt-0.5 flex items-center gap-1.5 font-mono text-[11.5px] text-ink-3">
          {`@${contact.username}`}
        </div>
      </div>

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
