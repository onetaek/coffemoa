import { useAuthStore } from '@/stores/authStore';
import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 10000
});

// 요청 인터셉터
api.interceptors.request.use((config) => {
    const auth = useAuthStore();

    if (auth.token) {
        config.headers['Authorization'] = `Bearer ${auth.token}`;
    }
    return config;
});

// 응답 인터셉터
api.interceptors.response.use(
    (response) => response,
    (error) => {
        const requestUrl = error.config?.url || '';

        // 🔥 로그인 API 실패는 인터셉터에서 무시
        if (requestUrl.includes('/auth/login')) {
            return Promise.reject(error);
        }

        // 🔥 인증 실패(401) → 자동 로그아웃 처리
        if (error.response?.status === 401) {
            const auth = useAuthStore();
            auth.logout();
            window.location.href = '/auth/login';
        }

        return Promise.reject(error);
    }
);

export default api;
