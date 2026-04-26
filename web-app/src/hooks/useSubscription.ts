import { useEffect, useRef, useState } from 'react'
import { centrifuge } from '@/lib/centrifuge'

type Status = 'idle' | 'subscribing' | 'subscribed' | 'error'

interface UseSubscriptionOptions<T> {
  onPublication: (data: T) => void
  enabled?: boolean
}

export function useSubscription<T>(
  channel: string,
  options: UseSubscriptionOptions<T>,
): { status: Status } {
  const { onPublication, enabled = true } = options
  const [status, setStatus] = useState<Status>('idle')

  // Keep latest callback in a ref so channel changes are the only trigger for re-subscribing
  const handlerRef = useRef(onPublication)
  useEffect(() => {
    handlerRef.current = onPublication
  })

  useEffect(() => {
    if (!enabled || !channel) return

    centrifuge.connect()

    const sub = centrifuge.newSubscription(channel)

    sub.on('subscribing', () => setStatus('subscribing'))
    sub.on('subscribed', () => setStatus('subscribed'))
    sub.on('error', () => setStatus('error'))
    sub.on('publication', (ctx) => handlerRef.current(ctx.data as T))

    sub.subscribe()

    return () => {
      sub.unsubscribe()
      centrifuge.removeSubscription(sub)
      setStatus('idle')
    }
  }, [channel, enabled])

  return { status }
}
