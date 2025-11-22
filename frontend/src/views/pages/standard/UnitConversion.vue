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

/* 복합키 객체 */
const conversion = ref({
    standardUnitId: '',
    conversionUnitId: '',
    conversionQuantity: null
});

/* 단위 목록 SelectBox 옵션 */
const unitOptions = ref([]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    단위환산 & 단위 목록 조회
----------------------------------------------------- */
onMounted(async () => {
    try {
        // 단위 환산 목록 조회
        const res = await api.get('/unit-conversions');
        conversions.value = res.data.data.map((c) => ({
            ...c,
            uniqueKey: `${c.standardUnitId}__${c.conversionUnitId}`
        }));

        // 단위 목록 조회 (SelectBox 용)
        const unitsRes = await api.get('/units');
        const units = unitsRes.data.data || [];

        unitOptions.value = units.map((u) => ({
            label: u.unitId,
            value: u.unitId
        }));
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '데이터 조회 중 오류가 발생했습니다.',
            life: 3000
        });
    }
});

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    conversion.value = {
        standardUnitId: '',
        conversionUnitId: '',
        conversionQuantity: null
    };
    submitted.value = false;
    conversionDialog.value = true;
}

function hideDialog() {
    conversionDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return conversions.value.some(
        (v) =>
            v.standardUnitId === conversion.value.standardUnitId &&
            v.conversionUnitId === conversion.value.conversionUnitId
    );
}

/* -----------------------------------------------------
    저장 (등록 / 수정)
----------------------------------------------------- */
async function saveConversion() {
    submitted.value = true;

    if (!conversion.value.standardUnitId || !conversion.value.conversionUnitId) {
        return;
    }

    try {
        if (isEditMode()) {
            // 🔥 PUT으로 수정
            await api.put('/unit-conversions', conversion.value);

            const idx = conversions.value.findIndex(
                (v) =>
                    v.standardUnitId === conversion.value.standardUnitId &&
                    v.conversionUnitId === conversion.value.conversionUnitId
            );

            conversions.value[idx] = { ...conversion.value };

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '단위 환산 정보가 수정되었습니다.',
                life: 3000
            });
        } else {
            // 🔥 신규 등록
            await api.post('/unit-conversions', conversion.value);

            conversions.value.push({ ...conversion.value });

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '단위 환산 정보가 등록되었습니다.',
                life: 3000
            });
        }

        conversionDialog.value = false;
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
    수정 Dialog 표시
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
        await api.delete('/unit-conversions', {
            params: {
                standardUnitId: conversion.value.standardUnitId,
                conversionUnitId: conversion.value.conversionUnitId
            }
        });

        conversions.value = conversions.value.filter(
            (v) =>
                !(
                    v.standardUnitId === conversion.value.standardUnitId &&
                    v.conversionUnitId === conversion.value.conversionUnitId
                )
        );

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '단위 환산 정보가 삭제되었습니다.',
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

    deleteDialog.value = false;
}

/* -----------------------------------------------------
    선택 삭제
----------------------------------------------------- */
async function deleteSelectedConversions() {
    try {
        for (const item of selectedConversions.value) {
            await api.delete('/unit-conversions', {
                params: {
                    standardUnitId: item.standardUnitId,
                    conversionUnitId: item.conversionUnitId
                }
            });
        }

        conversions.value = conversions.value.filter((v) => !selectedConversions.value.includes(v));

        selectedConversions.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 단위 환산 정보가 삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 처리 중 오류가 발생했습니다.',
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
                dataKey="uniqueKey"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>단위 환산 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어를 입력하세요..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="standardUnitId" header="기준 단위" sortable />
                <Column field="conversionUnitId" header="변환 단위" sortable />
                <Column field="conversionQuantity" header="환산 수량" sortable />

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
                        v-model="conversion.standardUnitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="기준 단위를 선택하세요"
                        :disabled="isEditMode()"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">변환 단위</label>
                    <Select
                        v-model="conversion.conversionUnitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="변환 단위를 선택하세요"
                        :disabled="isEditMode()"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">환산 수량</label>
                    <InputNumber
                        v-model="conversion.conversionQuantity"
                        :minFractionDigits="0"
                        :maxFractionDigits="6"
                        placeholder="환산 수량을 입력하세요"
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
                <span>
                    <b>{{ conversion.standardUnitId }}</b>
                    단위에서
                    <b>{{ conversion.conversionUnitId }}</b>
                    로의 단위 환산 정보를 삭제하시겠습니까?
                </span>
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
