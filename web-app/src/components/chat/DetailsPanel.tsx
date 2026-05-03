import { useApproveContact, useBlockContact } from '@/hooks/useContacts'
import { cn } from '@/lib/utils'
import type { Contact, UserSearchResult } from '@/types/chat'

type Props =
  | { contact: Contact; user?: never; onAdd?: never; isAdding?: never }
  | { contact?: never; user: UserSearchResult; onAdd: () => void; isAdding: boolean }

const actionClass =
  'rounded border border-transparent bg-paper-2 px-1.5 py-2.5 font-mono text-[10.5px] uppercase tracking-[0.04em] text-ink-2 transition-all hover:border-line hover:text-ink disabled:opacity-50'

export function DetailsPanel(props: Props) {
  const target = props.contact ?? props.user
  const initials = target.displayName
    .split(' ')
    .map((w) => w[0])
    .join('')
    .toUpperCase()

  return (
    <aside className="flex h-full w-[280px] shrink-0 flex-col overflow-hidden border-l border-line bg-paper">
      {/* Profile */}
      <div className="flex flex-col items-center gap-3 px-5 py-6">
        <div className="relative flex h-16 w-16 shrink-0 items-center justify-center rounded-full border border-line bg-paper-2 font-sans text-xl font-medium text-ink-2">
          {initials}
          {props.contact && (
            <span
              className={cn(
                'absolute -bottom-0.5 -right-0.5 h-3.5 w-3.5 rounded-full border-2 border-paper',
                props.contact.online ? 'bg-online' : 'bg-ink-3',
              )}
            />
          )}
        </div>
        <div className="flex flex-col items-center gap-0.5">
          <span className="text-[15px] font-semibold tracking-[-0.005em] text-ink">
            {target.displayName}
          </span>
          <span className="font-mono text-[11.5px] text-ink-3">@{target.username}</span>
        </div>
      </div>

      {/* Actions */}
      <div className="px-5 py-4">
        {props.contact ? (
          <ContactActions contact={props.contact} />
        ) : (
          <button disabled={props.isAdding} onClick={props.onAdd} className={cn(actionClass, 'w-full')}>
            Add contact
          </button>
        )}
      </div>
    </aside>
  )
}

function ContactActions({ contact }: { contact: Contact }) {
  const approve = useApproveContact(contact.id)
  const block = useBlockContact(contact.id)
  const isPending = approve.isPending || block.isPending

  if (contact.status === 'PENDING') {
    return (
      <div className="grid grid-cols-2 gap-2">
        <button disabled={isPending} onClick={() => approve.mutate()} className={actionClass}>
          Approve
        </button>
        <button disabled={isPending} onClick={() => block.mutate()} className={actionClass}>
          Block
        </button>
      </div>
    )
  }

  if (contact.status === 'BLOCKED') {
    return (
      <button
        disabled={isPending}
        onClick={() => approve.mutate()}
        className={cn(actionClass, 'w-full')}
      >
        Unblock
      </button>
    )
  }

  return (
    <button
      disabled={isPending}
      onClick={() => block.mutate()}
      className={cn(actionClass, 'w-full')}
    >
      Block
    </button>
  )
}
