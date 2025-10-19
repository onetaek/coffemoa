<script setup>
import { useAuthStore } from '@/stores/authStore';
import Button from 'primevue/button';
import Popover from 'primevue/popover';
import { ref } from 'vue';

const auth = useAuthStore();

// Popover 참조
const profileMenu = ref(null);

function toggleProfileMenu(event) {
    profileMenu.value.toggle(event);
}

function goProfile() {
    // 프로필 페이지 이동 (라우터 필요하면 import해서 push)
    console.log('Go Profile');
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
                <img src="@/assets/logo.png" alt="Coffemoa Logo" class="mx-auto" style="width: 30px" />
                <span>커피모아</span>
            </router-link>
        </div>

        <div class="layout-topbar-actions">
            <div class="layout-config-menu">
                <button type="button" class="layout-topbar-action" @click="toggleDarkMode">
                    <i :class="['pi', { 'pi-moon': isDarkTheme, 'pi-sun': !isDarkTheme }]"></i>
                </button>
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
            </div>

            <button
                class="layout-topbar-menu-button layout-topbar-action"
                v-styleclass="{
                    selector: '@next',
                    enterFromClass: 'hidden',
                    enterActiveClass: 'animate-scalein',
                    leaveToClass: 'hidden',
                    leaveActiveClass: 'animate-fadeout',
                    hideOnOutsideClick: true
                }"
            >
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <!-- <button type="button" class="layout-topbar-action">
                        <i class="pi pi-calendar"></i>
                        <span>Calendar</span>
                    </button>
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-inbox"></i>
                        <span>Messages</span>
                    </button> -->
                    <div class="relative">
                        <!-- Profile 버튼 -->
                        <button class="layout-topbar-action cursor-pointer" @click="toggleProfileMenu">
                            <i class="pi pi-user"></i>
                            <span>Profile</span>
                        </button>

                        <!-- Profile 메뉴 Popover -->
                        <Popover ref="profileMenu">
                            <div class="flex flex-col">
                                <Button
                                    label="Profile"
                                    icon="pi pi-user"
                                    class="justify-start w-full"
                                    text
                                    @click="goProfile"
                                />

                                <Button
                                    label="Log out"
                                    icon="pi pi-power-off"
                                    severity="danger"
                                    class="justify-start w-full mt-1"
                                    text
                                    @click="logout"
                                />
                            </div>
                        </Popover>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
