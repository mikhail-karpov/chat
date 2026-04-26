import { Centrifuge } from 'centrifuge'

const wsUrl =
  import.meta.env.VITE_API_BASE_URL.replace(/^http/, 'ws') +
  '/api/v1/centrifugo/connection/websocket'

export const centrifuge = new Centrifuge(wsUrl, {
  debug: import.meta.env.DEV,
})
