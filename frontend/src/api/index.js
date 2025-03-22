import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true
});

export function fetchMessages(limit = 20) {
  return API.get(`/api/v1/messages?limit=${limit}`)
  .then(response => response.data);
}

export function postMessage(text) {
  return API.post("/api/v1/messages", {text})
  .then(response => response.data);
}