import type { ReactNode } from 'react'
import { useAuth } from '@/hooks/useAuth'

export function ProtectedRoute({ children }: { children: ReactNode }) {
  const { user, isLoading } = useAuth()

  if (isLoading || !user) {
    return (
      <div className="flex h-screen items-center justify-center">
        <span className="text-ink-3 text-sm">Loading…</span>
      </div>
    )
  }

  return <>{children}</>
}
