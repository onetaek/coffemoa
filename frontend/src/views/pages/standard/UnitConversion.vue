<!-- UnitConversion.vue (Refactored for CUD API) -->
<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* -----------------------------------------------------
    상태값
----------------------------------------------------- */
const conversions = ref([]);
const selectedConversions = ref(null);
const conversionDialog = ref(false);
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* 단일 객체 */
const conversion = ref({
    id: null,
    baseUnitId: null,
    targetUnitId: null,
    baseUnit: {
        id: null,
        name: null
    },
    targetUnit: {
        id: null,
        name: null
    },
    ratio: null
});

/* 단위 목록 SelectBox 옵션 */
const unitOptions = ref([]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    단위환산 & 단위 목록 조회
----------------------------------------------------- */
async function loadData() {
    try {
        const res = await api.get('/unit-conversions');
        conversions.value = res.data.data;

        const unitsRes = await api.get('/units');
        const units = unitsRes.data.data;

        unitOptions.value = units.map((u) => ({
            label: u.name,
            value: u.id
        }));
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '데이터 조회 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

onMounted(loadData);

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    conversion.value = {
        id: null,
        baseUnitId: null,
        targetUnitId: null,
        ratio: null
    };
    submitted.value = false;
    conversionDialog.value = true;
}

function hideDialog() {
    conversionDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return conversion.value.id != null;
}

/* -----------------------------------------------------
    저장 (등록 / 수정)
----------------------------------------------------- */
async function saveConversion() {
    submitted.value = true;

    if (!conversion.value.baseUnitId || !conversion.value.targetUnitId) return;

    try {
        const payload = [];

        if (isEditMode()) {
            payload.push({
                id: conversion.value.id,
                baseUnitId: conversion.value.baseUnitId,
                targetUnitId: conversion.value.targetUnitId,
                ratio: conversion.value.ratio,
                flag: 'U'
            });
        } else {
            payload.push({
                baseUnitId: conversion.value.baseUnitId,
                targetUnitId: conversion.value.targetUnitId,
                ratio: conversion.value.ratio,
                flag: 'C'
            });
        }

        await api.post('/unit-conversions/cud', payload);

        toast.add({
            severity: 'success',
            summary: '성공',
            detail: isEditMode() ? '수정되었습니다.' : '등록되었습니다.',
            life: 2500
        });

        conversionDialog.value = false;
        await loadData();
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '저장 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

/* -----------------------------------------------------
    수정 Dialog
----------------------------------------------------- */
function editConversion(data) {
    conversion.value = { ...data };
    conversionDialog.value = true;
}

/* -----------------------------------------------------
    단건 삭제
----------------------------------------------------- */
function confirmDeleteConversion(data) {
    conversion.value = data;
    deleteDialog.value = true;
}

async function deleteConversion() {
    try {
        const payload = [
            {
                id: conversion.value.id,
                flag: 'D'
            }
        ];

        await api.post('/unit-conversions/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '단위 환산 정보가 삭제되었습니다.',
            life: 2500
        });

        await loadData();
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '삭제 중 오류가 발생했습니다.',
            life: 3000
        });
    }

    deleteDialog.value = false;
}

/* -----------------------------------------------------
    선택 삭제
----------------------------------------------------- */
async function deleteSelectedConversions() {
    try {
        const payload = selectedConversions.value.map((item) => ({
            id: item.id,
            flag: 'D'
        }));

        await api.post('/unit-conversions/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 항목이 삭제되었습니다.',
            life: 2500
        });

        await loadData();
        selectedConversions.value = null;
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 오류가 발생했습니다.',
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
                        :disabled="!selectedConversions || !selectedConversions.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedConversions"
                :value="conversions"
                dataKey="id"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>단위 환산 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력" />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="baseUnit.name" header="기준 단위" sortable />
                <Column field="targetUnit.name" header="변환 단위" sortable />
                <Column field="ratio" header="환산 비" sortable />

                <Column style="min-width: 8rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editConversion(data)" />
                        <Button
                            icon="pi pi-trash"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmDeleteConversion(data)"
                        />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="conversionDialog" :style="{ width: '450px' }" header="단위 환산 정보" :modal="true">
            <div class="flex flex-col gap-4">
                <div>
                    <label class="block font-bold mb-2">기준 단위</label>
                    <Select
                        v-model="conversion.baseUnitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="기준 단위를 선택하세요"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">변환 단위</label>
                    <Select
                        v-model="conversion.targetUnitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="변환 단위를 선택하세요"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">환산 비</label>
                    <InputNumber
                        v-model="conversion.ratio"
                        :minFractionDigits="0"
                        :maxFractionDigits="6"
                        placeholder="예: 10 (1샷 → 10g)"
                        fluid
                    />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveConversion" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>정말 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="아니오" icon="pi pi-times" text @click="deleteDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteConversion" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 단위 환산 정보를 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="아니오" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedConversions" />
            </template>
        </Dialog>
    </div>
</template>
