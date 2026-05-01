import { cn } from '@/lib/utils'

export type Props = {
  username: string
  displayName: string
  online?: boolean
}

export function UserDisplay({ username, displayName, online }: Props) {
  return (
    <>
      {/* Avatar */}
      <div className="relative flex h-9 w-9 shrink-0 items-center justify-center rounded-full border border-line bg-paper-2 font-sans text-sm font-medium text-ink-2">
        {displayName
          .split(' ')
          .map((w) => w[0])
          .join('')
          .toUpperCase()}
        <span
          className={cn(
            'absolute -bottom-px -right-px h-2.5 w-2.5 rounded-full border-[1.5px] border-paper',
            online ? 'bg-online' : 'bg-ink-3',
          )}
        />
      </div>

      <div className="flex min-w-0 flex-1 flex-col">
        <div className="text-[15px] font-semibold leading-tight tracking-[-0.005em] text-ink">
          {displayName}
        </div>
        <div className="mt-0.5 flex items-center gap-1.5 font-mono text-[11.5px] text-ink-3">
          {`@${username}`}
        </div>
      </div>
    </>
  )
}
