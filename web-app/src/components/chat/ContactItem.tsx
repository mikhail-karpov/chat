import { cn } from '@/lib/utils'
import type { Contact } from '@/types/chat'

interface Props {
  contact: Contact
  active: boolean
  onClick: () => void
}

export function ContactItem({ contact, active, onClick }: Props) {
  return (
    <button
      onClick={onClick}
      className={cn(
        'grid w-full cursor-pointer items-center gap-2.5 border-b border-solid border-muted px-3.5 py-2.5 text-left transition-colors',
        'grid-cols-[36px_1fr_auto]',
        active ? 'border-l-[3px] border-l-accent bg-paper-2 pl-[11px]' : 'hover:bg-paper-2',
      )}
    >
      {/* Avatar */}
      <div className="relative flex h-9 w-9 shrink-0 items-center justify-center rounded-full border border-line bg-paper-2 font-sans text-sm font-medium text-ink-2">
        {contact.username[0]}
        <span
          className={cn(
            'absolute -bottom-px -right-px h-2.5 w-2.5 rounded-full border-[1.5px] border-paper',
            contact.online ? 'bg-online' : 'bg-ink-3',
          )}
        />
      </div>

      {/* Name */}
      <div className="truncate text-[13.5px] font-medium leading-tight text-ink">
        {contact.username}
      </div>

      {/* Unread */}
      <div className="flex flex-col items-end gap-1">
        {contact.unread != null && (
          <span className="min-w-[18px] rounded-full bg-accent px-1.5 py-px text-center font-mono text-[10px] font-semibold text-white">
            {contact.unread}
          </span>
        )}
      </div>
    </button>
  )
}
