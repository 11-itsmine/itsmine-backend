import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 추가
axiosInstance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('Authorization');
      if (token) {
        config.headers['Authorization'] = token; // JWT 토큰을 헤더에 추가
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
);
export default axiosInstance;
