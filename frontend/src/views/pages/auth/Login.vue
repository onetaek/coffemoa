<script setup>
import FloatingConfigurator from '@/components/FloatingConfigurator.vue';
import { useAuthStore } from '@/stores/authStore';
import { useToast } from 'primevue/usetoast';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const email = ref('');
const password = ref('');
const checked = ref(false);

const auth = useAuthStore();
const router = useRouter();
const toast = useToast();

const login = async () => {
    try {
        await auth.login({
            username: email.value,
            password: password.value
        });

        router.push('/finance/cafe-menu-cost');
    } catch (err) {
        toast.add({
            severity: 'error',
            summary: '로그인 실패',
            detail: err.response?.data?.message || '아이디 또는 비밀번호를 다시 확인해주세요.',
            life: 2000
        });
    }
};
</script>

<template>
    <FloatingConfigurator />
    <Toast />

    <div
        class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden"
    >
        <div class="flex flex-col items-center justify-center">
            <div
                style="
                    border-radius: 56px;
                    padding: 0.3rem;
                    background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%);
                "
            >
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                    <div class="text-center mb-8">
                        <!-- ☕ 새로운 커피콩 로고 -->
                        <img src="@/assets/logo.png" alt="Coffemoa Logo" class="mx-auto mb-6" style="width: 60px" />

                        <!-- 타이틀 변경 -->
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-bold mb-4">
                            커피모아에 오신 것을 환영합니다
                        </div>

                        <span class="text-muted-color font-medium"> 서비스 이용을 위해 로그인해주세요 </span>
                    </div>

                    <div>
                        <label class="block text-xl mb-2">아이디 또는 이메일</label>
                        <InputText
                            type="text"
                            placeholder="아이디 또는 이메일"
                            class="w-full md:w-[30rem] mb-8"
                            v-model="email"
                            @keyup.enter="login"
                        />

                        <label class="block font-medium text-xl mb-2">비밀번호</label>
                        <Password
                            v-model="password"
                            placeholder="비밀번호"
                            :toggleMask="true"
                            class="mb-4"
                            fluid
                            :feedback="false"
                            @keyup.enter="login"
                        />

                        <div class="flex items-center justify-between mt-2 mb-8 gap-8">
                            <div class="flex items-center">
                                <Checkbox v-model="checked" id="rememberme1" binary class="mr-2"></Checkbox>
                                <label for="rememberme1">자동 로그인</label>
                            </div>
                        </div>

                        <Button label="로그인" class="w-full" @click="login" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.pi-eye,
.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}
</style>
