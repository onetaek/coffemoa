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
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* 단일 Material 객체 */
const material = ref({
    id: null,
    name: '',
    purchaseQuantity: null,
    purchaseUnitId: null,
    purchasePrice: null
});

/* SelectBox: 단위 목록 */
const unitOptions = ref([]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    🔥 초기 데이터 조회
----------------------------------------------------- */
async function loadData() {
    try {
        const res = await api.get('/materials');
        materials.value = res.data.data;

        const unitRes = await api.get('/units');
        unitOptions.value = unitRes.data.data.map((u) => ({
            label: u.name,
            value: u.id
        }));
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
    material.value = {
        id: null,
        name: '',
        purchaseQuantity: null,
        purchaseUnitId: null,
        purchasePrice: null
    };
    submitted.value = false;
    materialDialog.value = true;
}

function hideDialog() {
    materialDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return material.value.id != null;
}

/* -----------------------------------------------------
    🔥 저장 (등록 / 수정)
----------------------------------------------------- */
async function saveMaterial() {
    submitted.value = true;

    if (!material.value.name?.trim()) return;

    try {
        const payload = [];

        if (isEditMode()) {
            payload.push({
                id: material.value.id,
                name: material.value.name,
                purchaseQuantity: material.value.purchaseQuantity,
                purchaseUnitId: material.value.purchaseUnitId,
                purchasePrice: material.value.purchasePrice,
                flag: 'U'
            });
        } else {
            payload.push({
                name: material.value.name,
                purchaseQuantity: material.value.purchaseQuantity,
                purchaseUnitId: material.value.purchaseUnitId,
                purchasePrice: material.value.purchasePrice,
                flag: 'C'
            });
        }

        await api.post('/materials/cud', payload);

        toast.add({
            severity: 'success',
            summary: '성공',
            detail: isEditMode() ? '수정되었습니다.' : '등록되었습니다.',
            life: 2500
        });

        materialDialog.value = false;
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
function editMaterial(m) {
    material.value = { ...m };
    materialDialog.value = true;
}

/* -----------------------------------------------------
    🔥 단건 삭제
----------------------------------------------------- */
function confirmDeleteMaterial(data) {
    material.value = data;
    deleteDialog.value = true;
}

async function deleteMaterial() {
    try {
        const payload = [
            {
                id: material.value.id,
                flag: 'D'
            }
        ];

        await api.post('/materials/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '원자재가 삭제되었습니다.',
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
async function deleteSelectedMaterials() {
    try {
        const payload = selectedMaterials.value.map((m) => ({
            id: m.id,
            flag: 'D'
        }));

        await api.post('/materials/cud', payload);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 원자재가 삭제되었습니다.',
            life: 2500
        });

        await loadData();
        selectedMaterials.value = null;
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
                        :disabled="!selectedMaterials || !selectedMaterials.length"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedMaterials"
                :value="materials"
                dataKey="id"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>원자재 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />

                <Column field="name" header="원자재명" sortable />
                <Column field="purchaseQuantity" header="수량" sortable />
                <Column field="purchaseUnit.name" header="단위" sortable />
                <Column field="purchasePrice" header="가격(원)" sortable />

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
        <Dialog v-model:visible="materialDialog" :style="{ width: '450px' }" header="원자재 등록 / 수정" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">원자재명</label>
                    <InputText v-model="material.name" required fluid />
                    <small v-if="submitted && !material.name" class="text-red-500">원자재명은 필수입니다.</small>
                </div>

                <div>
                    <label class="block font-bold mb-2">수량</label>
                    <InputNumber
                        v-model="material.purchaseQuantity"
                        :minFractionDigits="0"
                        :maxFractionDigits="4"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">단위</label>
                    <Select
                        v-model="material.purchaseUnitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="단위를 선택하세요"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">가격</label>
                    <InputNumber v-model="material.purchasePrice" :minFractionDigits="0" :maxFractionDigits="0" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="saveMaterial" />
            </template>
        </Dialog>

        <!-- 단건 삭제 Dialog -->
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span
                    >원자재 <b>{{ material.name }}</b
                    >을(를) 삭제하시겠습니까?</span
                >
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteDialog = false" />
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
