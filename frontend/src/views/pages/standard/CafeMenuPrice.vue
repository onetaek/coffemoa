<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* =============================================================================
    1) 메뉴옵션 가격 상태
============================================================================= */
const menuPrices = ref([]);
const selectedPrices = ref(null);

const priceDialog = ref(false);
const deletePriceDialog = ref(false);
const deleteManyPriceDialog = ref(false);
const submitted = ref(false);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/** priceItem: CafeMenuPrice 저장/수정/삭제에 사용하는 객체 */
const initPriceItem = () => {
    return {
        id: null, // PK
        cafeMenuPrice: {
            id: '',
            name: ''
        },
        temperature: '',
        size: '',
        price: null
    };
};
const priceItem = ref(initPriceItem());

/* 메뉴 select */
const cafeMenuOptions = ref([]);

/* Enum Options */
const temperatures = [
    { label: 'Hot (H)', value: 'H' },
    { label: 'Ice (I)', value: 'I' }
];

const sizes = [
    { label: 'Regular (R)', value: 'R' },
    { label: 'Large (L)', value: 'L' }
];

function getTemperatureTypeSeverity(v) {
    return v === 'H' ? 'danger' : 'info';
}

function getSizeTypeSeverity(v) {
    return v === 'R' ? 'success' : 'warn';
}

/* =============================================================================
    2) 레시피 팝업 상태
============================================================================= */
const recipeDialog = ref(false);
const recipes = ref([]);
const selectedRecipes = ref(null);

const recipeFormDialog = ref(false);

/** MenuRecipeItem */
const recipeItem = ref({
    id: null,
    cafeMenuPrice: {
        id: null
    },
    material: {
        id: null,
        name: null
    },
    usageAmount: null,
    unitId: null,
    unit: {
        id: null,
        name: null
    }
});

const materialOptions = ref([]);
const unitOptions = ref([]);

/* =============================================================================
    3) 초기 로딩
============================================================================= */
onMounted(async () => {
    try {
        searchCafeMenuPrice();

        /* 메뉴 목록 */
        const menuRes = await api.get('/cafe-menus');
        cafeMenuOptions.value = menuRes.data.data.map((m) => ({
            label: m.name,
            value: m.id
        }));

        /* 원재료 목록 */
        const matRes = await api.get('/materials');
        materialOptions.value = matRes.data.data.map((m) => ({
            label: m.name,
            value: m.id
        }));

        /* 단위 목록 */
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
});

async function searchCafeMenuPrice() {
    /* 메뉴 옵션 가격 조회 */
    const res = await api.get('/cafe-menu-prices');
    menuPrices.value = res.data.data.map((item) => ({
        ...item,
        uniqueKey: `${item.id}`
    }));
}

/* =============================================================================
    4) 가격 Dialog
============================================================================= */
function openNew() {
    priceItem.value = initPriceItem();
    submitted.value = false;
    priceDialog.value = true;
}

function hidePriceDialog() {
    priceDialog.value = false;
    submitted.value = false;
}

function isPriceEdit() {
    return !!priceItem.value.id;
}

/* 저장 */
async function savePrice() {
    submitted.value = true;
    console.log('priceItem.value:', priceItem.value);

    if (!priceItem.value.cafeMenuPrice?.id || !priceItem.value.temperature || !priceItem.value.size) return;

    try {
        if (isPriceEdit()) {
            /* UPDATE */
            await api.post('/cafe-menu-prices/cud', [
                {
                    id: priceItem.value.id,
                    cafeMenuId: priceItem.value.cafeMenuPrice.id,
                    temperature: priceItem.value.temperature,
                    size: priceItem.value.size,
                    price: priceItem.value.price,
                    flag: 'U'
                }
            ]);

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '가격이 수정되었습니다.',
                life: 3000
            });

            searchCafeMenuPrice();
        } else {
            /* CREATE */
            await api.post('/cafe-menu-prices/cud', [
                {
                    cafeMenuId: priceItem.value.cafeMenuPrice.id,
                    temperature: priceItem.value.temperature,
                    size: priceItem.value.size,
                    price: priceItem.value.price,
                    flag: 'C'
                }
            ]);

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '가격이 등록되었습니다.',
                life: 3000
            });

            searchCafeMenuPrice();
        }

        priceDialog.value = false;
    } catch (e) {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '저장 실패',
            life: 3000
        });
    }
}

/* 삭제 */
function confirmDeletePrice(row) {
    priceItem.value = row;
    deletePriceDialog.value = true;
}

async function deletePrice() {
    try {
        await api.post('/cafe-menu-prices/cud', [
            {
                id: priceItem.value.id,
                flag: 'D'
            }
        ]);

        menuPrices.value = menuPrices.value.filter((v) => v.id !== priceItem.value.id);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '삭제 중 오류 발생',
            life: 3000
        });
    }

    deletePriceDialog.value = false;
}

/* 선택 삭제 */
async function deleteSelectedPrices() {
    try {
        for (const p of selectedPrices.value) {
            await api.delete('/cafe-menu-prices', { params: { id: p.id } });
        }

        menuPrices.value = menuPrices.value.filter((v) => !selectedPrices.value.includes(v));
        selectedPrices.value = null;

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '선택한 항목이 삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '선택 삭제 오류',
            life: 3000
        });
    }

    deleteManyPriceDialog.value = false;
}

/* =============================================================================
    5) 레시피 팝업
============================================================================= */
async function openRecipePopup(row) {
    recipeItem.value.cafeMenuPrice.id = row.id;
    searchRecipe();
    recipeDialog.value = true;
}

async function searchRecipe() {
    const res = await api.get('/menu-recipes', {
        params: { cafeMenuPriceId: recipeItem.value.cafeMenuPrice.id }
    });

    recipes.value = res.data.data.map((m) => ({
        ...m,
        uniqueKey: `${m.id}`
    }));
}

/* =============================================================================
    6) 레시피 등록/수정
============================================================================= */
function openNewRecipe() {
    recipeItem.value = {
        id: null,
        cafeMenuPrice: {
            id: recipeItem.value.cafeMenuPrice?.id
        },
        material: {
            id: null,
            name: null
        },
        unit: {
            id: null,
            name: null
        },
        usageAmount: 0,
        unitId: null
    };
    recipeFormDialog.value = true;
}

function editRecipe(row) {
    recipeItem.value = { ...row };
    recipeFormDialog.value = true;
}

async function saveRecipe() {
    if (!recipeItem.value.material?.id) return;

    const isEdit = !!recipeItem.value.id;

    try {
        if (isEdit) {
            await api.post('/menu-recipes/cud', [
                {
                    ...recipeItem.value,
                    cafeMenuPriceId: recipeItem.value.cafeMenuPrice.id,
                    materialId: recipeItem.value.material.id,
                    unitId: recipeItem.value.unit.id,
                    usageAmount: recipeItem.value.usageAmount,
                    flag: 'U'
                }
            ]);

            const idx = recipes.value.findIndex((v) => v.id === recipeItem.value.id);
            recipes.value[idx] = { ...recipeItem.value, uniqueKey: `${recipeItem.value.id}` };

            toast.add({
                severity: 'success',
                summary: '수정 완료',
                detail: '레시피가 수정되었습니다.',
                life: 3000
            });

            searchRecipe();
        } else {
            await api.post('/menu-recipes/cud', [
                {
                    ...recipeItem.value,
                    cafeMenuPriceId: recipeItem.value.cafeMenuPrice.id,
                    materialId: recipeItem.value.material.id,
                    unitId: recipeItem.value.unit.id,
                    usageAmount: recipeItem.value.usageAmount,
                    flag: 'C'
                }
            ]);

            toast.add({
                severity: 'success',
                summary: '등록 완료',
                detail: '레시피가 등록되었습니다.',
                life: 3000
            });

            searchRecipe();
        }

        recipeFormDialog.value = false;
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '레시피 저장 실패',
            life: 3000
        });
    }
}

/* 레시피 삭제 */
async function deleteRecipe(row) {
    console.log('row:', row);
    try {
        await api.post('/menu-recipes/cud', [
            {
                id: row.id,
                flag: 'D'
            }
        ]);

        recipes.value = recipes.value.filter((v) => v.id !== row.id);

        toast.add({
            severity: 'success',
            summary: '삭제 완료',
            detail: '레시피가 삭제되었습니다.',
            life: 3000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '레시피 삭제 실패',
            life: 3000
        });
    }
}
</script>

<template>
    <div>
        <!-- =====================================================================
         1) 메뉴 옵션 / 가격 관리
    ====================================================================== -->
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="신규 등록" icon="pi pi-plus" class="mr-2" @click="openNew" />
                    <Button
                        label="선택 삭제"
                        icon="pi pi-trash"
                        severity="danger"
                        :disabled="!selectedPrices || !selectedPrices.length"
                        @click="deleteManyPriceDialog = true"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedPrices"
                :value="menuPrices"
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

                <Column field="cafeMenuPrice.name" header="메뉴명" sortable>
                    <template #body="{ data }">
                        {{ data.cafeMenuPrice.name }}
                    </template>
                </Column>

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

                <Column field="price" header="가격" sortable />

                <Column header="관리" style="min-width: 12rem">
                    <template #body="{ data }">
                        <Button
                            icon="pi pi-list"
                            outlined
                            rounded
                            class="mr-2"
                            severity="info"
                            @click="openRecipePopup(data)"
                        />

                        <Button
                            icon="pi pi-pencil"
                            outlined
                            rounded
                            class="mr-2"
                            @click="
                                priceItem = { ...data };
                                priceDialog = true;
                            "
                        />

                        <Button
                            icon="pi pi-trash"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmDeletePrice(data)"
                        />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- =====================================================================
         2) 가격 등록 Dialog
    ====================================================================== -->
        <Dialog v-model:visible="priceDialog" header="메뉴 옵션 가격" :style="{ width: '480px' }" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">메뉴명</label>
                    <Select
                        v-model="priceItem.cafeMenuPrice.id"
                        :options="cafeMenuOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="메뉴 선택"
                        :disabled="isPriceEdit()"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">온도</label>
                    <div class="flex gap-6">
                        <div class="flex items-center gap-2" v-for="t in temperatures" :key="t.value">
                            <RadioButton
                                :inputId="'temp_' + t.value"
                                v-model="priceItem.temperature"
                                :value="t.value"
                            />
                            <label :for="'temp_' + t.value">{{ t.label }}</label>
                        </div>
                    </div>
                </div>

                <div>
                    <label class="block font-bold mb-2">사이즈</label>
                    <div class="flex gap-6">
                        <div class="flex items-center gap-2" v-for="s in sizes" :key="s.value">
                            <RadioButton :inputId="'size_' + s.value" v-model="priceItem.size" :value="s.value" />
                            <label :for="'size_' + s.value">{{ s.label }}</label>
                        </div>
                    </div>
                </div>

                <div>
                    <label class="block font-bold mb-2">가격</label>
                    <InputNumber v-model="priceItem.price" :minFractionDigits="0" :maxFractionDigits="0" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="hidePriceDialog" />
                <Button label="저장" icon="pi pi-check" @click="savePrice" />
            </template>
        </Dialog>

        <!-- 삭제 Dialog -->
        <Dialog v-model:visible="deletePriceDialog" header="삭제 확인" :style="{ width: '400px' }" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ priceItem.cafeMenu?.name }}</b>
                    ({{ priceItem.temperature }}/{{ priceItem.size }}) 옵션을 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="deletePriceDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deletePrice" />
            </template>
        </Dialog>

        <!-- 선택 삭제 Dialog -->
        <Dialog v-model:visible="deleteManyPriceDialog" header="선택 삭제" :style="{ width: '400px' }" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 옵션들을 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="deleteManyPriceDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelectedPrices" />
            </template>
        </Dialog>

        <!-- =====================================================================
         3) 레시피 팝업
    ====================================================================== -->
        <Dialog v-model:visible="recipeDialog" header="레시피 관리" :style="{ width: '900px' }" :modal="true">
            <div class="mb-4 p-4 bg-gray-50 border rounded-lg">
                <p>
                    <b>메뉴명:</b>
                    {{ menuPrices.find((p) => p.id === recipeItem.cafeMenuPrice.id)?.cafeMenuPrice?.name }}
                </p>
                <p><b>온도:</b> {{ menuPrices.find((p) => p.id === recipeItem.cafeMenuPrice.id)?.temperature }}</p>
                <p><b>사이즈:</b> {{ menuPrices.find((p) => p.id === recipeItem.cafeMenuPrice.id)?.size }}</p>
            </div>

            <!-- 레시피 테이블 -->
            <DataTable :value="recipes" v-model:selection="selectedRecipes" dataKey="id" paginator :rows="8">
                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="material.name" header="원재료" sortable>
                    <template #body="{ data }">
                        {{ data.material.name }}
                    </template>
                </Column>
                <Column field="usageAmount" header="수량" sortable />
                <Column field="unit.name" header="단위" sortable>
                    <template #body="{ data }">
                        {{ data.unit.name }}
                    </template>
                </Column>

                <Column header="관리">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editRecipe(data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="deleteRecipe(data)" />
                    </template>
                </Column>
            </DataTable>

            <template #footer>
                <Button label="재료 추가" icon="pi pi-plus" @click="openNewRecipe" />
                <Button label="닫기" icon="pi pi-times" text @click="recipeDialog = false" />
            </template>
        </Dialog>

        <!-- 레시피 등록/수정 -->
        <Dialog v-model:visible="recipeFormDialog" header="레시피 등록/수정" :style="{ width: '450px' }" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">원재료</label>
                    <Select
                        v-model="recipeItem.material.id"
                        :options="materialOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="원재료 선택"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">수량</label>
                    <InputNumber v-model="recipeItem.usageAmount" :minFractionDigits="0" :maxFractionDigits="4" fluid />
                </div>

                <div>
                    <label class="block font-bold mb-2">단위</label>
                    <Select
                        v-model="recipeItem.unit.id"
                        :options="unitOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="단위 선택"
                        fluid
                    />
                </div>
            </div>

            <template #footer>
                <Button label="취소" text icon="pi pi-times" @click="recipeFormDialog = false" />
                <Button label="저장" icon="pi pi-check" @click="saveRecipe" />
            </template>
        </Dialog>
    </div>
</template>
