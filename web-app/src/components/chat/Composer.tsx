import { useState, type KeyboardEvent } from 'react'

interface Props {
  onSend: (text: string) => void
}

export function Composer({ onSend }: Props) {
  const [value, setValue] = useState('')

  const submit = () => {
    const trimmed = value.trim()
    if (!trimmed) return
    onSend(trimmed)
    setValue('')
  }

  const handleKey = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      submit()
    }
  }

  return (
    <div className="flex items-center gap-2.5 border-t border-line bg-paper px-4 py-3">
      {/* Input */}
      <input
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        onKeyDown={handleKey}
        placeholder={`enter your message...`}
        className="flex-1 border border-line bg-paper-2 px-3 py-2 font-mono text-[12px] text-ink placeholder:text-ink-3 outline-none focus:border-ink-2"
      />

      {/* Send */}
      <button
        onClick={submit}
        className="border border-ink bg-ink px-3.5 py-1.5 font-sans text-sm font-medium text-paper transition-opacity hover:opacity-80"
      >
        send
      </button>
    </div>
  )
}
