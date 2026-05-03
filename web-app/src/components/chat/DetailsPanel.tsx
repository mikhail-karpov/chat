import { useApproveContact, useBlockContact } from '@/hooks/useContacts'
import { cn } from '@/lib/utils'
import type { Contact } from '@/types/chat'

interface Props {
  contact: Contact
}

export function DetailsPanel({ contact }: Props) {
  const approve = useApproveContact(contact.id)
  const block = useBlockContact(contact.id)
  const isPending = approve.isPending || block.isPending

  const initials = contact.displayName
    .split(' ')
    .map((w) => w[0])
    .join('')
    .toUpperCase()

  return (
    <aside className="flex h-full w-[280px] shrink-0 flex-col border-l border-line bg-paper overflow-hidden">
      {/* Profile */}
      <div className="flex flex-col items-center gap-3 px-5 py-6">
<div className="relative flex h-16 w-16 shrink-0 items-center justify-center rounded-full border border-line bg-paper-2 font-sans text-xl font-medium text-ink-2">
          {initials}
          <span
            className={cn(
              'absolute -bottom-0.5 -right-0.5 h-3.5 w-3.5 rounded-full border-2 border-paper',
              contact.online ? 'bg-online' : 'bg-ink-3',
            )}
          />
        </div>
        <div className="flex flex-col items-center gap-0.5">
          <span className="text-[15px] font-semibold tracking-[-0.005em] text-ink">
            {contact.displayName}
          </span>
          <span className="font-mono text-[11.5px] text-ink-3">@{contact.username}</span>
        </div>
      </div>

      {/* Actions */}
      <div
        className="grid gap-2 px-5 py-4"
        style={{ gridTemplateColumns: contact.status === 'PENDING' ? 'repeat(2, 1fr)' : '1fr' }}
      >
        {contact.status === 'PENDING' && (
          <button
            disabled={isPending}
            onClick={() => approve.mutate()}
            className="rounded border border-transparent bg-paper-2 px-1.5 py-2.5 font-mono text-[10.5px] uppercase tracking-[0.04em] text-ink-2 transition-all hover:border-line hover:text-ink disabled:opacity-50"
          >
            Approve
          </button>
        )}
        {contact.status === 'BLOCKED' ? (
          <button
            disabled={isPending}
            onClick={() => approve.mutate()}
            className="rounded border border-transparent bg-paper-2 px-1.5 py-2.5 font-mono text-[10.5px] uppercase tracking-[0.04em] text-ink-2 transition-all hover:border-line hover:text-ink disabled:opacity-50"
          >
            Unblock
          </button>
        ) : (
          <button
            disabled={isPending}
            onClick={() => block.mutate()}
            className="rounded border border-transparent bg-paper-2 px-1.5 py-2.5 font-mono text-[10.5px] uppercase tracking-[0.04em] text-ink-2 transition-all hover:border-line hover:text-ink disabled:opacity-50"
          >
            Block
          </button>
        )}
      </div>
    </aside>
  )
}
