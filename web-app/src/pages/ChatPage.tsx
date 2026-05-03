import { useState } from 'react'
import { X } from 'lucide-react'
import { useNavigate, useParams } from 'react-router-dom'
import { ChatPanel } from '@/components/chat/ChatPanel'
import { ContactItem } from '@/components/chat/ContactItem'
import { DetailsPanel } from '@/components/chat/DetailsPanel'
import { EmptyPanel } from '@/components/chat/EmptyPanel'
import { UserDisplay } from '@/components/chat/UserDisplay'
import { ScrollArea } from '@/components/ui/scroll-area'
import { useAddNewContact, useContacts } from '@/hooks/useContacts'
import { useMessageSubscription } from '@/hooks/useMessageSubscription'
import { useUserSearch } from '@/hooks/useUserSearch'
import { cn } from '@/lib/utils'
import type { UserSearchResult } from '@/types/chat'

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

function SectionHeading({ text }: { text: string }) {
  return (
    <div className="border-b border-muted px-3.5 pb-1.5 pt-3 font-mono text-[10px] uppercase tracking-wider text-ink-3">
      {text}
    </div>
  )
}

function SearchResultItem({
  user,
  active,
  onClick,
}: {
  user: UserSearchResult
  active: boolean
  onClick: () => void
}) {
  return (
    <button
      onClick={onClick}
      className={cn(
        'grid w-full cursor-pointer grid-cols-[36px_1fr] items-center gap-2.5 border-b border-solid border-muted px-3.5 py-2.5 text-left transition-colors',
        active ? 'border-l-[3px] border-l-accent bg-paper-2 pl-[11px]' : 'hover:bg-paper-2',
      )}
    >
      <UserDisplay username={user.username} displayName={user.displayName} />
    </button>
  )
}

export function ChatPage() {
  const { contactId } = useParams()
  const navigate = useNavigate()
  const { data: contacts = [], isPending, isError, refetch } = useContacts()
  useMessageSubscription()

  const [query, setQuery] = useState('')
  const [submittedQuery, setSubmittedQuery] = useState('')
  const [selectedSearchUser, setSelectedSearchUser] = useState<UserSearchResult | null>(null)

  const {
    localMatches,
    apiResults,
    isFetching: isSearchFetching,
  } = useUserSearch(query, submittedQuery, contacts)
  const addNewContact = useAddNewContact()

  const activeContact = contacts.find((c) => c.id === contactId)
  const showStrangerDetails = !activeContact && !!selectedSearchUser
  const showRightPanel = !!activeContact || showStrangerDetails

  const isSearching = query.trim().length > 0
  const showLoading = isSearchFetching && submittedQuery.length > 0
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

  function handleSelectContact(id: string) {
    setSelectedSearchUser(null)
    navigate(`/chat/${id}`)
    clearSearch()
  }

  function handleSelectSearchUser(user: UserSearchResult) {
    setSelectedSearchUser(user)
    if (contactId) navigate('/chat')
  }

  function handleAddSelectedUser() {
    if (!selectedSearchUser) return
    const userId = selectedSearchUser.id
    addNewContact.mutate(userId, {
      onSuccess: () => {
        setSelectedSearchUser(null)
        clearSearch()
        navigate(`/chat/${userId}`)
      },
    })
  }

  return (
    <div
      className="grid h-screen overflow-hidden border border-line"
      style={{
        gridTemplateColumns: showRightPanel ? '280px 1fr 280px' : '280px 1fr',
      }}
    >
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
              <SectionHeading text="Contacts" />
              {isPending && <ContactsSkeleton />}
              {isError && <ContactsError onRetry={refetch} />}
              {!isPending && !isError && contacts.length === 0 && <Hint text="no contacts yet" />}
              {contacts.map((c) => (
                <ContactItem
                  key={c.id}
                  contact={c}
                  active={c.id === contactId}
                  onClick={() => handleSelectContact(c.id)}
                />
              ))}
            </>
          )}

          {isSearching && (
            <>
              {showLoading && <Hint text="searching…" />}
              {showEnterHint && <Hint text="press ↵ to search" />}
              {showApiEmpty && <Hint text="no results" />}

              {localMatches.length > 0 && <SectionHeading text="Contacts" />}
              {localMatches.map((c) => (
                <ContactItem
                  key={c.id}
                  contact={c}
                  active={c.id === contactId}
                  onClick={() => handleSelectContact(c.id)}
                />
              ))}

              {apiResults.length > 0 && <SectionHeading text="Search results" />}
              {apiResults.map((u) => (
                <SearchResultItem
                  key={u.id}
                  user={u}
                  active={u.id === selectedSearchUser?.id}
                  onClick={() => handleSelectSearchUser(u)}
                />
              ))}
            </>
          )}
        </ScrollArea>
      </aside>

      {activeContact ? <ChatPanel contact={activeContact} /> : <EmptyPanel />}
      {activeContact && <DetailsPanel contact={activeContact} />}
      {showStrangerDetails && selectedSearchUser && (
        <DetailsPanel
          user={selectedSearchUser}
          onAdd={handleAddSelectedUser}
          isAdding={addNewContact.isPending}
        />
      )}
    </div>
  )
}
