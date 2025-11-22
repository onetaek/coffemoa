<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* -----------------------------------------------------
    상태값
----------------------------------------------------- */
const materials = ref([]);
const selectedMaterials = ref(null);
const materialDialog = ref(false);
const deleteMaterialDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* PK: materialName */
const material = ref({
    materialName: '',
    quantity: null,
    unitId: '',
    price: null
});

/* 단위 SelectBox */
const unitOptions = ref([]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    조회 (목록 + 단위)
----------------------------------------------------- */
onMounted(async () => {
    try {
        const res = await api.get('/materials');
        materials.value = res.data.data || [];

        const unitRes = await api.get('/units');
        unitOptions.value = unitRes.data.data.map((u) => ({
            label: u.unitId,
            value: u.unitId
        }));
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '데이터를 불러오는 데 실패했습니다.',
            life: 3000
        });
    }
});

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    material.value = {
        materialName: '',
        quantity: null,
        unitId: '',
        price: null
    };

    submitted.value = false;
    materialDialog.value = true;
}

function hideDialog() {
    materialDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return materials.value.some((m) => m.materialName === material.value.materialName);
}

/* -----------------------------------------------------
    저장 (등록 / 수정)
----------------------------------------------------- */
async function saveMaterial() {
    submitted.value = true;

    if (!material.value.materialName?.trim()) return;

    try {
        if (isEditMode()) {
            // 수정
            await api.put('/materials', material.value);

            const idx = materials.value.findIndex((m) => m.materialName === material.value.materialName);

            materials.value[idx] = { ...material.value };

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '원자재 정보가 수정되었습니다.',
                life: 3000
            });
        } else {
            // 등록
            await api.post('/materials', material.value);

            materials.value.push({ ...material.value });

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '원자재가 등록되었습니다.',
                life: 3000
            });
        }

        materialDialog.value = false;
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '저장 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

/* -----------------------------------------------------
    수정
----------------------------------------------------- */
function editMaterial(m) {
    material.value = { ...m };
    materialDialog.value = true;
}

/* -----------------------------------------------------
    단건 삭제
----------------------------------------------------- */
function confirmDeleteMaterial(m) {
    material.value = m;
    deleteMaterialDialog.value = true;
}

async function deleteMaterial() {
    try {
        await api.delete('/materials', {
            params: { materialName: material.value.materialName }
        });

        materials.value = materials.value.filter((m) => m.materialName !== material.value.materialName);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '원자재가 삭제되었습니다.',
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

    deleteMaterialDialog.value = false;
}

/* -----------------------------------------------------
    선택 삭제
----------------------------------------------------- */
async function deleteSelectedMaterials() {
    try {
        for (const m of selectedMaterials.value) {
            await api.delete('/materials', {
                params: { materialName: m.materialName }
            });
        }

        materials.value = materials.value.filter((m) => !selectedMaterials.value.includes(m));
        selectedMaterials.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 원자재가 삭제되었습니다.',
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
                        :disabled="!selectedMaterials || !selectedMaterials.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedMaterials"
                :value="materials"
                dataKey="materialName"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>원자재 자격 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어를 입력하세요..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="materialName" header="원자재명" sortable />
                <Column field="quantity" header="수량" sortable />
                <Column field="unitId" header="단위" sortable />
                <Column field="price" header="가격" sortable />

                <Column style="min-width: 10rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editMaterial(data)" />
                        <Button
                            icon="pi pi-trash"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmDeleteMaterial(data)"
                        />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="materialDialog" :style="{ width: '450px' }" header="원자재 정보" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">원자재명</label>
                    <InputText
                        v-model="material.materialName"
                        required="true"
                        autofocus
                        fluid
                        :disabled="isEditMode()"
                    />
                    <small v-if="submitted && !material.materialName" class="text-red-500">
                        원자재명은 필수 값입니다.
                    </small>
                </div>

                <div>
                    <label class="block font-bold mb-2">수량</label>
                    <InputNumber
                        v-model="material.quantity"
                        :minFractionDigits="0"
                        :maxFractionDigits="4"
                        placeholder="예: 1000"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">단위</label>
                    <Select
                        v-model="material.unitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="단위를 선택하세요"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">가격</label>
                    <InputNumber
                        v-model="material.price"
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                        placeholder="예: 5000"
                        fluid
                    />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveMaterial" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteMaterialDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    원자재 <b>{{ material.materialName }}</b> 을(를) 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteMaterialDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteMaterial" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 원자재를 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedMaterials" />
            </template>
        </Dialog>
    </div>
</template>
