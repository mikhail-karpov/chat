import axios from "axios";

const BASE_URL = "http://localhost:8080";

const API = axios.create({
  baseURL: BASE_URL,
  withCredentials: true
});

export function fetchCurrentUser() {
  return API.get("/api/v1/auth")
  .then(response => response.data);
}

export function login() {
  window.location.href = `${BASE_URL}/oauth2/authorization/auth-server`;
}

export function fetchUserProfile(userId) {
  return API.get(`/api/v1/users/${userId}/profile`)
  .then(response => response.data);
}

export function fetchMessages(limit = 10) {
  return API.get(`/api/v1/messages?limit=${limit}`)
  .then(response => response.data)
  .then(messages => messages.sort((a, b) =>
      Date.parse(a.createdAt) - Date.parse(b.createdAt)
  ));
}

export function postMessage(text) {
  return API.post("/api/v1/messages", {text})
  .then(response => response.data);
}