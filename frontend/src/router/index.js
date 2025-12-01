import AppLayout from '@/layout/AppLayout.vue';
import { useAuthStore } from '@/stores/authStore';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue')
                },
                {
                    path: '/uikit/formlayout',
                    name: 'formlayout',
                    component: () => import('@/views/uikit/FormLayout.vue')
                },
                {
                    path: '/uikit/input',
                    name: 'input',
                    component: () => import('@/views/uikit/InputDoc.vue')
                },
                {
                    path: '/uikit/button',
                    name: 'button',
                    component: () => import('@/views/uikit/ButtonDoc.vue')
                },
                {
                    path: '/uikit/table',
                    name: 'table',
                    component: () => import('@/views/uikit/TableDoc.vue')
                },
                {
                    path: '/uikit/list',
                    name: 'list',
                    component: () => import('@/views/uikit/ListDoc.vue')
                },
                {
                    path: '/uikit/tree',
                    name: 'tree',
                    component: () => import('@/views/uikit/TreeDoc.vue')
                },
                {
                    path: '/uikit/panel',
                    name: 'panel',
                    component: () => import('@/views/uikit/PanelsDoc.vue')
                },

                {
                    path: '/uikit/overlay',
                    name: 'overlay',
                    component: () => import('@/views/uikit/OverlayDoc.vue')
                },
                {
                    path: '/uikit/media',
                    name: 'media',
                    component: () => import('@/views/uikit/MediaDoc.vue')
                },
                {
                    path: '/uikit/message',
                    name: 'message',
                    component: () => import('@/views/uikit/MessagesDoc.vue')
                },
                {
                    path: '/uikit/file',
                    name: 'file',
                    component: () => import('@/views/uikit/FileDoc.vue')
                },
                {
                    path: '/uikit/menu',
                    name: 'menu',
                    component: () => import('@/views/uikit/MenuDoc.vue')
                },
                {
                    path: '/uikit/charts',
                    name: 'charts',
                    component: () => import('@/views/uikit/ChartDoc.vue')
                },
                {
                    path: '/uikit/misc',
                    name: 'misc',
                    component: () => import('@/views/uikit/MiscDoc.vue')
                },
                {
                    path: '/uikit/timeline',
                    name: 'timeline',
                    component: () => import('@/views/uikit/TimelineDoc.vue')
                },
                {
                    path: '/uikit/excel-upload',
                    name: 'excelUpload',
                    component: () => import('@/views/uikit/ExcelUploadDoc.vue')
                },
                {
                    path: '/blocks',
                    name: 'blocks',
                    meta: {
                        breadcrumb: ['Prime Blocks', 'Free Blocks']
                    },
                    component: () => import('@/views/utilities/Blocks.vue')
                },
                {
                    path: '/pages/empty',
                    name: 'empty',
                    component: () => import('@/views/pages/development/Empty.vue')
                },
                {
                    path: '/pages/crud',
                    name: 'crud',
                    component: () => import('@/views/pages/development/Crud.vue')
                },
                {
                    path: '/documentation',
                    name: 'documentation',
                    component: () => import('@/views/pages/development/Documentation.vue')
                },
                // ===================== 추가 =====================
                {
                    path: '/finance/cafe-menu-cost',
                    name: 'CafeMenuCost',
                    component: () => import('@/views/pages/finance/CafeMenuCost.vue')
                },
                {
                    path: '/finance/receipt-upload',
                    name: 'receiptUoload',
                    component: () => import('@/views/pages/finance/ReceiptUpload.vue')
                },
                {
                    path: '/finance/receipt-view',
                    name: 'receiptView',
                    component: () => import('@/views/pages/finance/ReceiptView.vue')
                },
                {
                    path: '/standard/unit',
                    name: 'Unit',
                    component: () => import('@/views/pages/standard/Unit.vue')
                },
                {
                    path: '/standard/unit-conversion',
                    name: 'UnitConversion',
                    component: () => import('@/views/pages/standard/UnitConversion.vue')
                },
                {
                    path: '/standard/cafe-menu',
                    name: 'CafeMenu',
                    component: () => import('@/views/pages/standard/CafeMenu.vue')
                },
                {
                    path: '/standard/cafe-menu-price',
                    name: 'CafeMenuPrice',
                    component: () => import('@/views/pages/standard/CafeMenuPrice.vue')
                },
                {
                    path: '/standard/material',
                    name: 'Material',
                    component: () => import('@/views/pages/standard/Material.vue')
                }
            ]
        },
        {
            path: '/landing',
            name: 'landing',
            component: () => import('@/views/pages/development/Landing.vue')
        },
        {
            path: '/pages/notfound',
            name: 'notfound',
            component: () => import('@/views/pages/development/NotFound.vue')
        },

        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/pages/auth/Login.vue')
        },
        {
            path: '/auth/access',
            name: 'accessDenied',
            component: () => import('@/views/pages/auth/Access.vue')
        },
        {
            path: '/auth/error',
            name: 'error',
            component: () => import('@/views/pages/auth/Error.vue')
        }
    ]
});

// =========================
// 전역 라우터 가드
// =========================
router.beforeEach(async (to, from, next) => {
    const auth = useAuthStore();

    // 로그인 페이지는 토큰 없이도 접근 허용
    if (to.path === '/auth/login') {
        return next();
    }

    // 토큰이 없는 경우 → 로그인 페이지로 이동
    if (!auth.token) {
        return next('/auth/login');
    }

    // 토큰이 있지만 검증이 필요할 경우(백엔드에 validate API가 있다면)
    // await auth.validateToken() 같은 함수 추가 가능

    next();
});

export default router;
