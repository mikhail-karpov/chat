import axios from "axios";

export const BASE_URL = "http://localhost:8080";

export const API = axios.create({
  baseURL: BASE_URL,
  withCredentials: true
});
