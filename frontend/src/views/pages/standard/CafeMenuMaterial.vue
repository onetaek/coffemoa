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

/* 등록/수정 모델 */
const material = ref({
    cafeMenuName: '',
    temperature: '',
    size: '',
    materialName: '',
    quantity: null,
    unitId: ''
});

/* 메뉴 SelectBox */
const cafeMenuOptions = ref([]);

/* 온도 */
const temperatures = [
    { label: 'Hot (H)', value: 'H' },
    { label: 'Ice (I)', value: 'I' }
];

/* 사이즈 */
const sizes = [
    { label: 'Regular (R)', value: 'R' },
    { label: 'Large (L)', value: 'L' }
];

function getTemperatureTypeSeverity(value) {
    switch (value) {
        case 'H':
            return 'danger';

        case 'I':
            return 'info';

        case 'renewal':
            return null;
    }
}

function getSizeTypeSeverity(value) {
    switch (value) {
        case 'R':
            return 'success';

        case 'L':
            return 'warn';

        case 'renewal':
            return null;
    }
}

/* 원재료 SelectBox */
const materialOptions = ref([]);

/* 단위 SelectBox */
const unitOptions = ref([]);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
    1) 조회
----------------------------------------------------- */
onMounted(async () => {
    try {
        // 레시피 조회
        const res = await api.get('/cafe-menu-materials');
        materials.value = (res.data.data || []).map((m) => ({
            ...m,
            uniqueKey: `${m.cafeMenuName}_${m.temperature}_${m.size}_${m.materialName}`
        }));

        // 메뉴 목록 조회
        const menuRes = await api.get('/cafe-menus');
        cafeMenuOptions.value = menuRes.data.data.map((m) => ({
            label: m.cafeMenuName,
            value: m.cafeMenuName
        }));

        // 원재료 목록
        const matRes = await api.get('/materials');
        materialOptions.value = matRes.data.data.map((el) => ({
            label: el.materialName,
            value: el.materialName
        }));

        // 단위 목록
        const unitRes = await api.get('/units');
        unitOptions.value = unitRes.data.data.map((el) => ({
            label: el.unitId,
            value: el.unitId
        }));
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '레시피 목록 조회 실패',
            life: 3000
        });
    }
});

/* -----------------------------------------------------
    Dialog
----------------------------------------------------- */
function openNew() {
    material.value = {
        cafeMenuName: '',
        temperature: '',
        size: '',
        materialName: '',
        quantity: null,
        unitId: ''
    };

    submitted.value = false;
    materialDialog.value = true;
}

function hideDialog() {
    materialDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return materials.value.some(
        (v) =>
            v.cafeMenuName === material.value.cafeMenuName &&
            v.temperature === material.value.temperature &&
            v.size === material.value.size &&
            v.materialName === material.value.materialName
    );
}

/* -----------------------------------------------------
    저장 (POST / PUT)
----------------------------------------------------- */
async function saveMaterial() {
    submitted.value = true;

    if (
        !material.value.cafeMenuName ||
        !material.value.temperature ||
        !material.value.size ||
        !material.value.materialName
    ) {
        return;
    }

    try {
        if (isEditMode()) {
            // 수정
            await api.put('/cafe-menu-materials', material.value);

            const idx = materials.value.findIndex(
                (v) =>
                    v.cafeMenuName === material.value.cafeMenuName &&
                    v.temperature === material.value.temperature &&
                    v.size === material.value.size &&
                    v.materialName === material.value.materialName
            );

            materials.value[idx] = {
                ...material.value,
                uniqueKey: `${material.value.cafeMenuName}_${material.value.temperature}_${material.value.size}_${material.value.materialName}`
            };

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '레시피가 수정되었습니다.',
                life: 3000
            });
        } else {
            // 신규 등록
            await api.post('/cafe-menu-materials', material.value);

            const added = { ...material.value };
            added.uniqueKey = `${added.cafeMenuName}_${added.temperature}_${added.size}_${added.materialName}`;

            materials.value.push(added);

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '레시피가 등록되었습니다.',
                life: 3000
            });
        }

        materialDialog.value = false;
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '저장 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

/* -----------------------------------------------------
    수정 버튼 클릭
----------------------------------------------------- */
function editMaterial(row) {
    material.value = { ...row };
    materialDialog.value = true;
}

/* -----------------------------------------------------
    삭제
----------------------------------------------------- */
function confirmDelete(row) {
    material.value = { ...row };
    deleteDialog.value = true;
}

async function deleteMaterial() {
    try {
        await api.delete('/cafe-menu-materials', {
            params: {
                cafeMenuName: material.value.cafeMenuName,
                temperature: material.value.temperature,
                size: material.value.size,
                materialName: material.value.materialName
            }
        });

        materials.value = materials.value.filter((v) => v.uniqueKey !== material.value.uniqueKey);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '레시피가 삭제되었습니다.',
            life: 3000
        });
    } catch (e) {
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
async function deleteSelected() {
    try {
        for (const m of selectedMaterials.value) {
            await api.delete('/cafe-menu-materials', {
                params: {
                    cafeMenuName: m.cafeMenuName,
                    temperature: m.temperature,
                    size: m.size,
                    materialName: m.materialName
                }
            });
        }

        materials.value = materials.value.filter((v) => !selectedMaterials.value.includes(v));

        selectedMaterials.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택 항목이 삭제되었습니다.',
            life: 3000
        });
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 처리 중 오류 발생',
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
                dataKey="uniqueKey"
                paginator
                :rows="12"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>카페 메뉴 레시피 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />

                <Column field="cafeMenuName" header="메뉴명" sortable />
                <Column field="temperature" header="온도" sortable>
                    <template #body="{ data }">
                        <Tag :value="data.temperature" :severity="getTemperatureTypeSeverity(data.temperature)" />
                    </template>
                </Column>
                <Column field="size" header="사이즈" sortable>
                    <template #body="{ data }">
                        <Tag :value="data.size" :severity="getSizeTypeSeverity(data.size)" />
                    </template>
                </Column>
                <Column field="materialName" header="원재료" sortable />
                <Column field="quantity" header="수량" sortable />
                <Column field="unitId" header="단위" sortable />

                <Column style="min-width: 10rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editMaterial(data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDelete(data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록/수정 Dialog -->
        <Dialog v-model:visible="materialDialog" :style="{ width: '480px' }" header="레시피 정보" :modal="true">
            <div class="flex flex-col gap-6">
                <!-- 메뉴 -->
                <div>
                    <label class="block font-bold mb-2">메뉴명</label>
                    <Select
                        v-model="material.cafeMenuName"
                        :options="cafeMenuOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="메뉴 선택"
                        :disabled="isEditMode()"
                        fluid
                    />
                </div>

                <!-- 온도 -->
                <div>
                    <label class="block font-bold mb-2">온도</label>

                    <div class="flex gap-6 items-center">
                        <div class="flex items-center gap-2" v-for="t in temperatures" :key="t.value">
                            <RadioButton
                                :inputId="'temp_' + t.value"
                                v-model="material.temperature"
                                name="temperature"
                                :value="t.value"
                                :disabled="isEditMode()"
                            />
                            <label :for="'temp_' + t.value">{{ t.label }}</label>
                        </div>
                    </div>
                </div>

                <!-- 사이즈 -->
                <div>
                    <label class="block font-bold mb-2">사이즈</label>

                    <div class="flex gap-6 items-center">
                        <div class="flex items-center gap-2" v-for="s in sizes" :key="s.value">
                            <RadioButton
                                :inputId="'size_' + s.value"
                                v-model="material.size"
                                :value="s.value"
                                :disabled="isEditMode()"
                            />
                            <label :for="'size_' + s.value">{{ s.label }}</label>
                        </div>
                    </div>
                </div>

                <!-- 원재료 -->
                <div>
                    <label class="block font-bold mb-2">원재료</label>
                    <Select
                        v-model="material.materialName"
                        :options="materialOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="원재료 선택"
                        :disabled="isEditMode()"
                        fluid
                    />
                </div>

                <!-- 수량 -->
                <div>
                    <label class="block font-bold mb-2">수량</label>
                    <InputNumber
                        v-model="material.quantity"
                        :minFractionDigits="0"
                        :maxFractionDigits="4"
                        placeholder="수량 입력"
                        fluid
                    />
                </div>

                <!-- 단위 -->
                <div>
                    <label class="block font-bold mb-2">단위</label>
                    <Select
                        v-model="material.unitId"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="단위 선택"
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
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ material.cafeMenuName }}</b> ({{ material.temperature }}/{{ material.size }}) 원재료
                    <b>{{ material.materialName }}</b> 을(를) 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteMaterial" />
            </template>
        </Dialog>
        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '450px' }" header="선택 삭제 확인" :modal="true">
            <div class="flex items-start gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    선택한 <b>{{ selectedMaterials?.length }}</b
                    >개의 레시피를 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelected" />
            </template>
        </Dialog>
    </div>
</template>
