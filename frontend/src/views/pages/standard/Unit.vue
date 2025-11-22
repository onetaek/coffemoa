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

// 🔥 unitId = PK
const unit = ref({
    unitId: '',
    description: ''
});

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    🔥 1) 조회 API
----------------------------------------------------- */
onMounted(async () => {
    try {
        const res = await api.get('/units');
        units.value = res.data.data || [];
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '단위 목록을 불러오는 데 실패했습니다.',
            life: 3000
        });
    }
});

/* -----------------------------------------------------
    다이얼로그
----------------------------------------------------- */
function openNew() {
    unit.value = { unitId: '', description: '' };
    submitted.value = false;
    unitDialog.value = true;
}

function hideDialog() {
    unitDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return units.value.some((u) => u.unitId === unit.value.unitId);
}

/* -----------------------------------------------------
    🔥 2) 저장 (등록 / 수정)
----------------------------------------------------- */
async function saveUnit() {
    submitted.value = true;

    if (!unit.value.unitId?.trim()) return;

    try {
        if (isEditMode()) {
            // 수정
            await api.put('/units', unit.value);

            const idx = units.value.findIndex((u) => u.unitId === unit.value.unitId);
            units.value[idx] = { ...unit.value };

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '단위 정보가 수정되었습니다.',
                life: 3000
            });
        } else {
            // 등록
            await api.post('/units', unit.value);

            units.value.push({ ...unit.value });

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '단위가 성공적으로 등록되었습니다.',
                life: 3000
            });
        }

        unitDialog.value = false;
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

/* -----------------------------------------------------
    수정 / 삭제
----------------------------------------------------- */
function editUnit(u) {
    unit.value = { ...u };
    unitDialog.value = true;
}

function confirmDeleteUnit(u) {
    unit.value = u;
    deleteUnitDialog.value = true;
}

/* -----------------------------------------------------
    🔥 3) 단건 삭제
----------------------------------------------------- */
async function deleteUnit() {
    try {
        await api.delete('/units', {
            params: { unitId: unit.value.unitId }
        });

        units.value = units.value.filter((u) => u.unitId !== unit.value.unitId);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '단위가 삭제되었습니다.',
            life: 3000
        });
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '단위를 삭제하는 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteUnitDialog.value = false;
    unit.value = {};
}

/* -----------------------------------------------------
    🔥 4) 선택 삭제
----------------------------------------------------- */
async function deleteSelectedUnits() {
    try {
        for (const u of selectedUnits.value) {
            await api.delete('/units', {
                params: { unitId: u.unitId }
            });
        }

        units.value = units.value.filter((u) => !selectedUnits.value.includes(u));
        selectedUnits.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 단위가 모두 삭제되었습니다.',
            life: 3000
        });
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 처리 중 오류가 발생했습니다.',
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
                dataKey="unitId"
                :paginator="true"
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>단위 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어를 입력하세요" />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="unitId" header="단위 ID" sortable style="min-width: 12rem" />
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

        <!-- 단위 등록/수정 Dialog -->
        <Dialog v-model:visible="unitDialog" :style="{ width: '450px' }" header="단위 등록 / 수정" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">단위 ID</label>
                    <InputText v-model="unit.unitId" required="true" autofocus fluid :disabled="isEditMode()" />
                    <small v-if="submitted && !unit.unitId" class="text-red-500"> 단위 ID는 필수 값입니다. </small>
                </div>

                <div>
                    <label class="block font-bold mb-2">설명</label>
                    <InputText fluid v-model="unit.description" />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveUnit" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteUnitDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ unit?.unitId }}</b> 단위를 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="아니오" icon="pi pi-times" text @click="deleteUnitDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteUnit" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteUnitsDialog" :style="{ width: '400px' }" header="선택 삭제 확인" :modal="true">
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
