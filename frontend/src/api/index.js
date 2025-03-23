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

export function fetchMessages(limit = 20) {
  return API.get(`/api/v1/messages?limit=${limit}`)
  .then(response => response.data);
}

export function postMessage(text) {
  return API.post("/api/v1/messages", {text})
  .then(response => response.data);
}