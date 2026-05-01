import { useState } from 'react'
import { X } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'
import { ScrollArea } from '@/components/ui/scroll-area'
import { useAddNewContact as useAddContact, useContacts } from '@/hooks/useContacts'
import { useUserSearch } from '@/hooks/useUserSearch'
import { ContactItem } from './ContactItem'
import { UserDisplay } from './UserDisplay'

function ContactsSkeleton() {
  return (
    <>
      {Array.from({ length: 6 }).map((_, i) => (
        <div
          key={i}
          className="flex animate-pulse items-center gap-2.5 border-b border-muted px-3.5 py-2.5"
        >
          <div className="h-9 w-9 shrink-0 rounded-full bg-paper-2" />
          <div className="h-3 w-28 rounded bg-paper-2" />
        </div>
      ))}
    </>
  )
}

function ContactsError({ onRetry }: { onRetry: () => void }) {
  return (
    <div className="flex flex-col items-center gap-2 px-3.5 py-6">
      <span className="font-mono text-[12px] text-ink-3">failed to load contacts</span>
      <button onClick={onRetry} className="font-mono text-[12px] text-ink underline">
        retry
      </button>
    </div>
  )
}

function Hint({ text }: { text: string }) {
  return <div className="px-3.5 py-3 font-mono text-[12px] text-ink-3">{text}</div>
}

export function Sidebar() {
  const { contactId } = useParams()
  const navigate = useNavigate()
  const { data: contacts = [], isPending, isError, refetch } = useContacts()

  const [query, setQuery] = useState('')
  const [submittedQuery, setSubmittedQuery] = useState('')

  const { localMatches, apiResults, isFetching } = useUserSearch(query, submittedQuery, contacts)
  const addNewContact = useAddContact()

  const isSearching = query.trim().length > 0
  const showLoading = isFetching && submittedQuery.length > 0
  const showEnterHint = !submittedQuery && isSearching && localMatches.length === 0
  const showApiEmpty =
    !showLoading &&
    submittedQuery.length > 0 &&
    localMatches.length === 0 &&
    apiResults.length === 0

  function clearSearch() {
    setQuery('')
    setSubmittedQuery('')
  }

  function handleQueryChange(val: string) {
    setQuery(val)
    if (submittedQuery) setSubmittedQuery('')
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === 'Escape') {
      clearSearch()
    } else if (e.key === 'Enter' && query.trim().length > 0) {
      setSubmittedQuery(query.trim())
    }
  }

  return (
    <aside className="flex w-[280px] shrink-0 flex-col border-r border-line bg-paper">
      <div className="border-b border-dashed border-muted px-3.5 pb-2.5 pt-3.5">
        <div className="flex items-center gap-2 border border-line bg-paper-2 px-2.5 py-[7px]">
          <span className="relative h-3 w-3 shrink-0 rounded-full border border-ink-2">
            <span className="absolute -bottom-[3px] -right-[3px] h-px w-[5px] origin-left rotate-45 bg-ink-2" />
          </span>
          <input
            value={query}
            onChange={(e) => handleQueryChange(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Find people"
            className="flex-1 bg-transparent font-mono text-[12px] text-ink outline-none placeholder:text-ink-3"
          />
          {query && (
            <button
              onClick={clearSearch}
              className="shrink-0 text-ink-3 hover:text-ink"
              aria-label="Clear search"
            >
              <X size={12} strokeWidth={2.5} />
            </button>
          )}
        </div>
      </div>

      <ScrollArea className="flex-1">
        {!isSearching && (
          <>
            {isPending && <ContactsSkeleton />}
            {isError && <ContactsError onRetry={refetch} />}
            {!isPending && !isError && contacts.length === 0 && <Hint text="no contacts yet" />}
            {contacts.map((c) => (
              <ContactItem
                key={c.id}
                contact={c}
                active={c.id === contactId}
                onClick={() => navigate(`/chat/${c.id}`)}
              />
            ))}
          </>
        )}

        {isSearching && (
          <>
            {showLoading && <Hint text="searching…" />}
            {showEnterHint && <Hint text="press ↵ to search" />}
            {showApiEmpty && <Hint text="no results" />}

            {localMatches.map((c) => (
              <ContactItem
                key={c.id}
                contact={c}
                active={c.id === contactId}
                onClick={() => {
                  navigate(`/chat/${c.id}`)
                  clearSearch()
                }}
              />
            ))}

            {apiResults.map((u) => (
              <div
                key={u.id}
                className="flex items-center gap-2.5 border-b border-muted px-3.5 py-2.5"
              >
                <UserDisplay {...u} />
                <button
                  onClick={() =>
                    addNewContact.mutate(u.id, {
                      onSuccess: clearSearch,
                    })
                  }
                  disabled={addNewContact.isPending && addNewContact.variables === u.id}
                  className="shrink-0 font-mono text-[11px] text-accent hover:underline disabled:opacity-50"
                >
                  add
                </button>
              </div>
            ))}
          </>
        )}
      </ScrollArea>
    </aside>
  )
}
