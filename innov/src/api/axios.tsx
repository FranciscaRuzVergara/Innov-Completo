import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:19090', // Puerto del Gateway
  headers: {
    'Content-Type': 'application/json'
  }
});

// Este interceptor "inyecta" el token antes de que la petición salga
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    // Es vital que el formato sea "Bearer [token]"
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default api;