import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'https://localhost:443',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosInstance;
