<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';

const appEnv = import.meta.env.VITE_MODE; // dev / prod

const devModel = [
    {
        label: '수익관리',
        items: [
            {
                label: '카페메뉴원가목록',
                icon: 'pi pi-wallet',
                to: '/finance/cafe-menu-cost'
            },
            {
                label: '영수증업로드',
                icon: 'pi pi-receipt',
                to: '/finance/receipt-upload'
            },
            {
                label: '영수증목록',
                icon: 'pi pi-receipt',
                to: '/finance/receipt-view'
            }
        ]
    },
    {
        label: '기초정보관리',
        items: [
            { label: '단위관리', icon: 'pi pi-sliders-h', to: '/standard/unit' },
            { label: '단위환산관리', icon: 'pi pi-sync', to: '/standard/unit-conversion' },
            { label: '원자재가격관리', icon: 'pi pi-box', to: '/standard/material' },
            { label: '메뉴관리', icon: 'pi pi-list', to: '/standard/cafe-menu' },
            { label: '레시피관리', icon: 'pi pi-cog', to: '/standard/cafe-menu-price' }
        ]
    },

    {
        label: 'Home',
        items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/' }]
    },
    {
        label: 'UI Components',
        items: [
            { label: 'Form Layout', icon: 'pi pi-fw pi-id-card', to: '/uikit/formlayout' },
            { label: 'Input', icon: 'pi pi-fw pi-check-square', to: '/uikit/input' },
            { label: 'Button', icon: 'pi pi-fw pi-mobile', to: '/uikit/button', class: 'rotated-icon' },
            { label: 'Table', icon: 'pi pi-fw pi-table', to: '/uikit/table' },
            { label: 'List', icon: 'pi pi-fw pi-list', to: '/uikit/list' },
            { label: 'Tree', icon: 'pi pi-fw pi-share-alt', to: '/uikit/tree' },
            { label: 'Panel', icon: 'pi pi-fw pi-tablet', to: '/uikit/panel' },
            { label: 'Overlay', icon: 'pi pi-fw pi-clone', to: '/uikit/overlay' },
            { label: 'Media', icon: 'pi pi-fw pi-image', to: '/uikit/media' },
            { label: 'Menu', icon: 'pi pi-fw pi-bars', to: '/uikit/menu' },
            { label: 'Message', icon: 'pi pi-fw pi-comment', to: '/uikit/message' },
            { label: 'File', icon: 'pi pi-fw pi-file', to: '/uikit/file' },
            { label: 'Chart', icon: 'pi pi-fw pi-chart-bar', to: '/uikit/charts' },
            { label: 'Timeline', icon: 'pi pi-fw pi-calendar', to: '/uikit/timeline' },
            { label: 'Misc', icon: 'pi pi-fw pi-circle', to: '/uikit/misc' },
            { label: 'Excel', icon: 'pi pi-fw pi-clone', to: '/uikit/excel-upload' }
        ]
    },
    {
        label: 'Prime Blocks',
        icon: 'pi pi-fw pi-prime',
        items: [
            {
                label: 'Free Blocks',
                icon: 'pi pi-fw pi-eye',
                to: '/blocks'
            },
            {
                label: 'All Blocks',
                icon: 'pi pi-fw pi-globe',
                url: 'https://blocks.primevue.org/',
                target: '_blank'
            }
        ]
    },
    {
        label: 'Pages',
        icon: 'pi pi-fw pi-briefcase',
        to: '/pages',
        items: [
            {
                label: 'Landing',
                icon: 'pi pi-fw pi-globe',
                to: '/landing'
            },
            {
                label: 'Auth',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'Login',
                        icon: 'pi pi-fw pi-sign-in',
                        to: '/auth/login'
                    },
                    {
                        label: 'Error',
                        icon: 'pi pi-fw pi-times-circle',
                        to: '/auth/error'
                    },
                    {
                        label: 'Access Denied',
                        icon: 'pi pi-fw pi-lock',
                        to: '/auth/access'
                    }
                ]
            },
            {
                label: 'Crud',
                icon: 'pi pi-fw pi-pencil',
                to: '/pages/crud'
            },
            {
                label: 'Not Found',
                icon: 'pi pi-fw pi-exclamation-circle',
                to: '/pages/notfound'
            },
            {
                label: 'Empty',
                icon: 'pi pi-fw pi-circle-off',
                to: '/pages/empty'
            }
        ]
    },
    {
        label: 'Hierarchy',
        items: [
            {
                label: 'Submenu 1',
                icon: 'pi pi-fw pi-bookmark',
                items: [
                    {
                        label: 'Submenu 1.1',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            { label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-bookmark' }
                        ]
                    },
                    {
                        label: 'Submenu 1.2',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [{ label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-bookmark' }]
                    }
                ]
            },
            {
                label: 'Submenu 2',
                icon: 'pi pi-fw pi-bookmark',
                items: [
                    {
                        label: 'Submenu 2.1',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            { label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-bookmark' }
                        ]
                    },
                    {
                        label: 'Submenu 2.2',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [{ label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-bookmark' }]
                    }
                ]
            }
        ]
    },
    {
        label: 'Get Started',
        items: [
            {
                label: 'Documentation',
                icon: 'pi pi-fw pi-book',
                to: '/documentation'
            },
            {
                label: 'View Source',
                icon: 'pi pi-fw pi-github',
                url: 'https://github.com/primefaces/sakai-vue',
                target: '_blank'
            }
        ]
    }
];

const prodModel = [
    {
        label: '수익관리',
        items: [
            {
                label: '카페메뉴원가목록',
                icon: 'pi pi-wallet',
                to: '/finance/cafe-menu-cost'
            }
        ]
    },
    {
        label: '기초정보관리',
        items: [
            { label: '단위관리', icon: 'pi pi-sliders-h', to: '/standard/unit' },
            { label: '단위환산관리', icon: 'pi pi-sync', to: '/standard/unit-conversion' },
            { label: '원자재가격관리', icon: 'pi pi-box', to: '/standard/material' },
            { label: '메뉴관리', icon: 'pi pi-list', to: '/standard/cafe-menu' },
            { label: '레시피관리', icon: 'pi pi-cog', to: '/standard/cafe-menu-price' }
        ]
    }
];

const model = ref([...(appEnv === 'development' ? devModel : []), ...(appEnv === 'production' ? prodModel : [])]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
