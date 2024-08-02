import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    },
});

// 요청 인터셉터 추가
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('Authorization');
        console.log('Fetched Token:', token); // 토큰 확인용 로그
        if (token) {
            config.headers['Authorization'] = token;
        }
        console.log('Config Headers:', config.headers); // 헤더 확인용 로그
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
