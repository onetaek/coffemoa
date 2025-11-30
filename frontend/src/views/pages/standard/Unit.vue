<!-- Unit.vue (Refactored for CUD API) -->
<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

const units = ref([]);
const selectedUnits = ref(null);
const unitDialog = ref(false);
const deleteUnitDialog = ref(false);
const deleteUnitsDialog = ref(false);
const submitted = ref(false);

const unit = ref({
    id: null,
    name: '',
    description: ''
});

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

async function loadUnits() {
    try {
        const res = await api.get('/units');
        units.value = res.data.data;
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '단위 목록을 불러오는 데 실패했습니다.',
            life: 3000
        });
    }
}

onMounted(loadUnits);

function openNew() {
    unit.value = { id: null, name: '', description: '' };
    submitted.value = false;
    unitDialog.value = true;
}

function hideDialog() {
    unitDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return unit.value.id != null;
}

async function saveUnit() {
    submitted.value = true;

    if (!unit.value.name.trim()) return;

    try {
        const payload = [];

        if (isEditMode()) {
            payload.push({
                id: unit.value.id,
                name: unit.value.name,
                description: unit.value.description,
                flag: 'U'
            });
        } else {
            payload.push({
                name: unit.value.name,
                description: unit.value.description,
                flag: 'C'
            });
        }

        await api.post('/units/cud', payload);

        toast.add({
            severity: 'success',
            summary: '성공',
            detail: isEditMode() ? '수정되었습니다.' : '등록되었습니다.',
            life: 3000
        });

        unitDialog.value = false;
        await loadUnits();
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '단위를 저장하는 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

function editUnit(u) {
    unit.value = { ...u };
    unitDialog.value = true;
}

function confirmDeleteUnit(u) {
    unit.value = u;
    deleteUnitDialog.value = true;
}

async function deleteUnit() {
    try {
        const payload = [
            {
                id: unit.value.id,
                flag: 'D'
            }
        ];

        await api.post('/units/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '단위가 삭제되었습니다.',
            life: 3000
        });

        await loadUnits();
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '삭제 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteUnitDialog.value = false;
    unit.value = {};
}

async function deleteSelectedUnits() {
    try {
        const payload = selectedUnits.value.map((u) => ({
            id: u.id,
            flag: 'D'
        }));

        await api.post('/units/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 단위가 삭제되었습니다.',
            life: 3000
        });

        await loadUnits();
        selectedUnits.value = null;
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteUnitsDialog.value = false;
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
                        @click="deleteUnitsDialog = true"
                        :disabled="!selectedUnits || !selectedUnits.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedUnits"
                :value="units"
                dataKey="id"
                :paginator="true"
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>단위 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력" />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="name" header="단위명" sortable style="min-width: 12rem" />
                <Column field="description" header="설명" sortable style="min-width: 16rem" />

                <Column :exportable="false" style="min-width: 10rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editUnit(data)" />
                        <Button
                            icon="pi pi-trash"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmDeleteUnit(data)"
                        />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="unitDialog" header="단위 등록 / 수정" :modal="true" :style="{ width: '450px' }">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">단위명</label>
                    <InputText v-model="unit.name" required autofocus fluid />
                    <small v-if="submitted && !unit.name" class="text-red-500"> 단위명은 필수 값입니다. </small>
                </div>

                <div>
                    <label class="block font-bold mb-2">설명</label>
                    <InputText v-model="unit.description" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveUnit" />
            </template>
        </Dialog>

        <!-- 단건 삭제 -->
        <Dialog v-model:visible="deleteUnitDialog" header="삭제 확인" :modal="true" :style="{ width: '400px' }">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ unit?.name }}</b> 단위를 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="아니오" icon="pi pi-times" text @click="deleteUnitDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteUnit" />
            </template>
        </Dialog>

        <!-- 선택 삭제 -->
        <Dialog v-model:visible="deleteUnitsDialog" header="선택 삭제 확인" :modal="true" :style="{ width: '400px' }">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 단위를 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="아니오" icon="pi pi-times" text @click="deleteUnitsDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedUnits" />
            </template>
        </Dialog>
    </div>
</template>
