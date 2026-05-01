import { cn } from '@/lib/utils'
import type { Contact } from '@/types/chat'
import { UserDisplay } from './UserDisplay'

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
      <UserDisplay {...contact} />

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
