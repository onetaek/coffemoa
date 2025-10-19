import api from '@/api/axios';
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: null,
        username: null,
        role: null,
        roleId: null,
        permissions: []
    }),

    persist: {
        key: 'coffemoa-auth',
        storage: localStorage
    },

    actions: {
        async login({ username, password }) {
            const response = await api.post('/auth/login', { username, password });

            // 백엔드 ApiResponse 기반
            const data = response.data.data;

            this.token = data.token;
            this.username = data.username;
            this.role = data.role;
            this.roleId = data.roleId;
            this.permissions = data.permissions || [];
        },

        logout() {
            this.token = null;
            this.username = null;
            this.role = null;
            this.roleId = null;
            this.permissions = [];
        }
    }
});
