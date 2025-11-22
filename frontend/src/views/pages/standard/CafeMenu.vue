<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* -----------------------------------------------------
    상태값
----------------------------------------------------- */
const menus = ref([]);
const selectedMenus = ref(null);
const menuDialog = ref(false);
const deleteMenuDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* PK: cafeMenuName */
const menu = ref({
    cafeMenuName: ''
});

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    조회
----------------------------------------------------- */
onMounted(async () => {
    try {
        const res = await api.get('/cafe-menus');
        menus.value = res.data.data || [];
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '메뉴 목록을 불러오는 데 실패했습니다.',
            life: 3000
        });
    }
});

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    menu.value = {
        cafeMenuName: ''
    };

    submitted.value = false;
    menuDialog.value = true;
}

function hideDialog() {
    menuDialog.value = false;
    submitted.value = false;
}

/* -----------------------------------------------------
    저장 (신규 등록만)
----------------------------------------------------- */
async function saveMenu() {
    submitted.value = true;

    if (!menu.value.cafeMenuName?.trim()) return;

    try {
        // 등록
        await api.post('/cafe-menus', menu.value);

        menus.value.push({ ...menu.value });

        toast.add({
            severity: 'success',
            summary: '등록 완료',
            detail: '메뉴가 성공적으로 등록되었습니다.',
            life: 3000
        });

        menuDialog.value = false;
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '메뉴 등록 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

/* -----------------------------------------------------
    삭제
----------------------------------------------------- */
function confirmDeleteMenu(m) {
    menu.value = m;
    deleteMenuDialog.value = true;
}

async function deleteMenu() {
    try {
        await api.delete('/cafe-menus', {
            params: { cafeMenuName: menu.value.cafeMenuName }
        });

        menus.value = menus.value.filter((m) => m.cafeMenuName !== menu.value.cafeMenuName);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '메뉴가 삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '삭제 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteMenuDialog.value = false;
}

/* -----------------------------------------------------
    선택 삭제
----------------------------------------------------- */
async function deleteSelectedMenus() {
    try {
        for (const m of selectedMenus.value) {
            await api.delete('/cafe-menus', {
                params: { cafeMenuName: m.cafeMenuName }
            });
        }

        menus.value = menus.value.filter((m) => !selectedMenus.value.includes(m));
        selectedMenus.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 메뉴가 모두 삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteManyDialog.value = false;
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="신규 등록" icon="pi pi-plus" class="mr-2" @click="openNew" />
                    <Button
                        label="선택 삭제"
                        icon="pi pi-trash"
                        severity="danger"
                        @click="deleteManyDialog = true"
                        :disabled="!selectedMenus || !selectedMenus.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedMenus"
                :value="menus"
                dataKey="cafeMenuName"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>메뉴 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="cafeMenuName" header="메뉴명" sortable />

                <Column style="min-width: 10rem">
                    <template #body="{ data }">
                        <Button
                            icon="pi pi-trash"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmDeleteMenu(data)"
                        />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 메뉴 등록 Dialog -->
        <Dialog v-model:visible="menuDialog" :style="{ width: '450px' }" header="메뉴 등록" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">메뉴명</label>
                    <InputText v-model="menu.cafeMenuName" required="true" autofocus fluid />
                    <small v-if="submitted && !menu.cafeMenuName" class="text-red-500"> 메뉴명은 필수 값입니다. </small>
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="등록" icon="pi pi-check" @click="saveMenu" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteMenuDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    메뉴 <b>{{ menu.cafeMenuName }}</b> 을(를) 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteMenuDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteMenu" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 메뉴를 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedMenus" />
            </template>
        </Dialog>
    </div>
</template>
