import axios from 'axios';

const assignmentApi = axios.create({
  baseURL: 'http://localhost:19090/assignments', 
});

assignmentApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default assignmentApi;