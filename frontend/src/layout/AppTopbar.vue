<script setup>
import AppConfigurator from '@/layout/AppConfigurator.vue';
import { useLayout } from '@/layout/composables/layout';
import { useAuthStore } from '@/stores/authStore';
import { useToast } from 'primevue/usetoast';
import { useRouter } from 'vue-router';

const { toggleMenu, toggleDarkMode, isDarkTheme } = useLayout();
const auth = useAuthStore();
const router = useRouter();
const toast = useToast();

function goProfile() {
    toast.add({ severity: 'info', summary: '구현예정입니다.', life: 3000 });
    // router.push('/profile'); // 실제 프로필 경로에 맞게 수정
}

function logout() {
    auth.logout();
    window.location.href = '/auth/login';
}
</script>

<template>
    <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" @click="toggleMenu">
                <i class="pi pi-bars"></i>
            </button>
            <router-link to="/" class="layout-topbar-logo">
                <img src="@/assets/logo.png" alt="Coffemoa Logo" class="mx-auto" style="width: 1.6rem" />
                <span class="text-xl">커피모아</span>
            </router-link>
        </div>

        <div class="layout-topbar-actions">
            <!-- 다크모드 -->
            <button type="button" class="layout-topbar-action" @click="toggleDarkMode">
                <i :class="['pi', { 'pi-moon': isDarkTheme, 'pi-sun': !isDarkTheme }]"></i>
            </button>

            <!-- 테마 설정 버튼 -->
            <div class="relative">
                <button
                    v-styleclass="{
                        selector: '@next',
                        enterFromClass: 'hidden',
                        enterActiveClass: 'animate-scalein',
                        leaveToClass: 'hidden',
                        leaveActiveClass: 'animate-fadeout',
                        hideOnOutsideClick: true
                    }"
                    type="button"
                    class="layout-topbar-action layout-topbar-action-highlight"
                >
                    <i class="pi pi-palette"></i>
                </button>
                <AppConfigurator />
            </div>

            <!-- Profile -->
            <button type="button" class="layout-topbar-action" @click="goProfile">
                <i class="pi pi-user"></i>
            </button>

            <!-- Logout -->
            <button type="button" class="layout-topbar-action" @click="logout">
                <i class="pi pi-sign-out"></i>
            </button>
        </div>
    </div>
</template>
