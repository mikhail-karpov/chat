import { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { getCurrentUser } from '@/api/auth'

const REDIRECT_KEY = 'auth:redirect'

export function useAuth() {
  const { data: user, isLoading } = useQuery({
    queryKey: ['currentUser'],
    queryFn: getCurrentUser,
    retry: false,
    staleTime: 5 * 60 * 1000,
  })

  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    if (!isLoading && user) {
      const savedPath = sessionStorage.getItem(REDIRECT_KEY)
      if (savedPath) {
        sessionStorage.removeItem(REDIRECT_KEY)
        navigate(savedPath, { replace: true })
      }
    }
  }, [user, isLoading, navigate])

  useEffect(() => {
    if (!isLoading && !user) {
      sessionStorage.setItem(REDIRECT_KEY, location.pathname + location.search)
      window.location.href = `${import.meta.env.VITE_API_BASE_URL}/oauth2/authorization/auth-server`
    }
  }, [user, isLoading, location])

  return { user, isLoading }
}
