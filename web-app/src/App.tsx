import { Navigate, Route, Routes } from 'react-router-dom'
import { ProtectedRoute } from '@/components/auth/ProtectedRoute'
import { ChatPage } from '@/pages/ChatPage'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/chat" replace />} />
      <Route
        path="/chat/:contactId?"
        element={
          <ProtectedRoute>
            <ChatPage />
          </ProtectedRoute>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
