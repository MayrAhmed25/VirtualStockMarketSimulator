import axios from 'axios';

// 1. USE YOUR NGROK URL HERE (Make sure to keep /api at the end)
const API_URL = 'https://domelike-zachariah-intrastate.ngrok-free.dev/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
        // 2. SPECIAL HEADER: This tells Ngrok "I am a developer, let me in!"
        // This bypasses the "Visit Site" warning page.
        'ngrok-skip-browser-warning': 'true'
    },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;