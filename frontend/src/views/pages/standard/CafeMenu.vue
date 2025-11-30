<!-- CafeMenu.vue (Refactored for CUD API) -->
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
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* 메뉴 객체 */
const menu = ref({
    id: null,
    name: ''
});

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    초기 조회
----------------------------------------------------- */
async function loadData() {
    try {
        const res = await api.get('/cafe-menus');
        menus.value = res.data.data;
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '메뉴 목록 조회 실패',
            life: 3000
        });
    }
}

onMounted(loadData);

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    menu.value = { id: null, name: '' };
    submitted.value = false;
    menuDialog.value = true;
}

function hideDialog() {
    menuDialog.value = false;
    submitted.value = false;
}

/* 수정 모드 판단 */
function isEditMode() {
    return menu.value.id != null;
}

/* -----------------------------------------------------
    저장 (등록 / 수정)
----------------------------------------------------- */
async function saveMenu() {
    submitted.value = true;
    if (!menu.value.name?.trim()) return;

    try {
        const payload = [];

        if (isEditMode()) {
            payload.push({
                id: menu.value.id,
                name: menu.value.name,
                flag: 'U'
            });
        } else {
            payload.push({
                name: menu.value.name,
                flag: 'C'
            });
        }

        await api.post('/cafe-menus/cud', payload);

        toast.add({
            severity: 'success',
            summary: '성공',
            detail: isEditMode() ? '수정 완료' : '등록 완료',
            life: 2500
        });

        menuDialog.value = false;
        await loadData();
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '저장 중 오류 발생',
            life: 3000
        });
    }
}

/* -----------------------------------------------------
    수정 버튼
----------------------------------------------------- */
function editMenu(m) {
    menu.value = { ...m };
    menuDialog.value = true;
}

/* -----------------------------------------------------
    단건 삭제
----------------------------------------------------- */
function confirmDeleteMenu(m) {
    menu.value = m;
    deleteDialog.value = true;
}

async function deleteMenu() {
    try {
        const payload = [
            {
                id: menu.value.id,
                flag: 'D'
            }
        ];

        await api.post('/cafe-menus/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '메뉴가 삭제되었습니다.',
            life: 2500
        });

        await loadData();
        selectedMenus.value = null;
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '삭제 중 오류 발생',
            life: 3000
        });
    }

    deleteDialog.value = false;
}

/* -----------------------------------------------------
    선택 삭제
----------------------------------------------------- */
async function deleteSelectedMenus() {
    try {
        const payload = selectedMenus.value.map((m) => ({
            id: m.id,
            flag: 'D'
        }));

        await api.post('/cafe-menus/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 메뉴가 삭제되었습니다.',
            life: 2500
        });

        await loadData();
        selectedMenus.value = null;
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 중 오류 발생',
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
                        :disabled="!selectedMenus || !selectedMenus.length"
                        @click="deleteManyDialog = true"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedMenus"
                :value="menus"
                dataKey="id"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>카페 메뉴 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />

                <Column field="name" header="메뉴명" sortable />

                <Column style="min-width: 9rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editMenu(data)" />
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

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="menuDialog" :style="{ width: '450px' }" header="메뉴 등록 / 수정" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="font-bold block mb-2">메뉴명</label>
                    <InputText v-model="menu.name" required fluid autofocus />
                    <small v-if="submitted && !menu.name" class="text-red-500"> 메뉴명은 필수입니다. </small>
                </div>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveMenu" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl"></i>
                <span
                    >메뉴 <b>{{ menu.name }}</b> 을(를) 삭제하시겠습니까?</span
                >
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="deleteDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteMenu" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl"></i>
                <span>선택한 메뉴를 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedMenus" />
            </template>
        </Dialog>
    </div>
</template>
