import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  },
});

axiosInstance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('Authorization');
      if (token) {
        config.headers['Authorization'] = token.trim(); // Ensure no extra spaces
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
);

export default axiosInstance;