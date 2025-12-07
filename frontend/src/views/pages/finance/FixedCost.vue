<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* -----------------------------------------------------
    상태값
----------------------------------------------------- */
const fixedCosts = ref([]);
const selectedFixedCosts = ref(null);
const dialogVisible = ref(false);
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* 단일 FixedCost 객체 */
const fixedCost = ref({
    id: null,
    costName: '',
    periodTypeCode: null,
    remark: ''
});

/* SelectBox: enum 목록 */
const periodTypeOptions = ref([
    { label: '연 단위', value: 'YEARLY' },
    { label: '월 단위', value: 'MONTHLY' },
    { label: '주 단위', value: 'WEEKLY' },
    { label: '일 단위', value: 'DAILY' }
]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    🔥 데이터 조회
----------------------------------------------------- */
async function loadData() {
    try {
        const res = await api.get('/fixed-costs');
        fixedCosts.value = res.data.data;
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '데이터 조회 실패',
            life: 3000
        });
    }
}

onMounted(loadData);

/* -----------------------------------------------------
    Dialog 제어
----------------------------------------------------- */
function openNew() {
    fixedCost.value = {
        id: null,
        costName: '',
        periodTypeCode: null,
        remark: ''
    };
    submitted.value = false;
    dialogVisible.value = true;
}

function hideDialog() {
    dialogVisible.value = false;
    submitted.value = false;
}

function isEditMode() {
    return fixedCost.value.id != null;
}

/* -----------------------------------------------------
    🔥 저장 (등록 / 수정)
----------------------------------------------------- */
async function saveData() {
    submitted.value = true;

    if (!fixedCost.value.costName?.trim()) return;

    try {
        const payload = [];

        if (isEditMode()) {
            payload.push({
                id: fixedCost.value.id,
                costName: fixedCost.value.costName,
                periodTypeCode: fixedCost.value.periodTypeCode,
                remark: fixedCost.value.remark,
                flag: 'U'
            });
        } else {
            payload.push({
                costName: fixedCost.value.costName,
                periodTypeCode: fixedCost.value.periodTypeCode,
                remark: fixedCost.value.remark,
                flag: 'C'
            });
        }

        await api.post('/fixed-costs/cud', payload);

        toast.add({
            severity: 'success',
            summary: '성공',
            detail: isEditMode() ? '수정되었습니다.' : '등록되었습니다.',
            life: 2500
        });

        dialogVisible.value = false;
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
    수정 버튼 클릭
----------------------------------------------------- */
function editRow(row) {
    fixedCost.value = { ...row };
    dialogVisible.value = true;
}

/* -----------------------------------------------------
    🔥 단건 삭제
----------------------------------------------------- */
function confirmDeleteRow(row) {
    fixedCost.value = row;
    deleteDialog.value = true;
}

async function deleteRow() {
    try {
        const payload = [{ id: fixedCost.value.id, flag: 'D' }];

        await api.post('/fixed-costs/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '항목이 삭제되었습니다.',
            life: 2500
        });

        await loadData();
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
    🔥 선택 삭제
----------------------------------------------------- */
async function deleteSelectedRows() {
    try {
        const payload = selectedFixedCosts.value.map((m) => ({
            id: m.id,
            flag: 'D'
        }));

        await api.post('/fixed-costs/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 항목이 삭제되었습니다.',
            life: 2500
        });

        await loadData();
        selectedFixedCosts.value = null;
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
                        @click="deleteManyDialog = true"
                        :disabled="!selectedFixedCosts || !selectedFixedCosts.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedFixedCosts"
                :value="fixedCosts"
                dataKey="id"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>고정비 항목 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />

                <Column field="costName" header="항목명" sortable />
                <Column field="periodTypeName" header="기간 유형" sortable />
                <Column field="remark" header="비고" sortable />

                <Column style="min-width: 10rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editRow(data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteRow(data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="dialogVisible" :style="{ width: '450px' }" header="고정비 등록 / 수정" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">항목명</label>
                    <InputText v-model="fixedCost.costName" required fluid />
                    <small v-if="submitted && !fixedCost.costName" class="text-red-500">필수 항목입니다.</small>
                </div>

                <div>
                    <label class="block font-bold mb-2">기간 유형</label>
                    <Select
                        v-model="fixedCost.periodTypeCode"
                        :options="periodTypeOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="기간을 선택하세요"
                        fluid
                    />
                    <small v-if="submitted && !fixedCost.periodTypeCode" class="text-red-500">필수 항목입니다.</small>
                </div>

                <div>
                    <label class="block font-bold mb-2">비고</label>
                    <InputText v-model="fixedCost.remark" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveData" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span
                    >항목 <b>{{ fixedCost.costName }}</b> 을(를) 삭제하시겠습니까?</span
                >
            </div>

            <template #footer>
                <Button label="취소" icon="pi.pi-times" text @click="deleteDialog = false" />
                <Button label="삭제" icon="pi.pi-check" severity="danger" @click="deleteRow" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 고정비 항목을 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedRows" />
            </template>
        </Dialog>
    </div>
</template>
