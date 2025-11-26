<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();

/* ===========================================
   1) 메뉴옵션/가격 관리 상태
=========================================== */
const dt = ref();

const menuPrices = ref([]);
const selectedPrices = ref(null);

const priceDialog = ref(false);
const deletePriceDialog = ref(false);
const deleteManyPriceDialog = ref(false);
const submitted = ref(false);

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

const priceItem = ref({
    cafeMenuName: '',
    temperature: '',
    size: '',
    price: null
});

/* 메뉴 select */
const cafeMenuOptions = ref([]);

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

/* ===========================================
   2) 레시피 팝업 상태
=========================================== */
const recipeDialog = ref(false);
const recipes = ref([]);
const selectedRecipes = ref(null);

/* 레시피 등록/수정 Dialog */
const recipeFormDialog = ref(false);

const recipeItem = ref({
    cafeMenuName: '',
    temperature: '',
    size: '',
    materialName: '',
    quantity: null,
    unitId: ''
});

/* 원재료 select */
const materialOptions = ref([]);
const unitOptions = ref([]);

/* ===========================================
   3) 초기 로딩
=========================================== */
onMounted(async () => {
    /* 메뉴 옵션 가격 조회 */
    const res = await api.get('/cafe-menu-prices');
    menuPrices.value = res.data.data.map((item) => ({
        ...item,
        uniqueKey: `${item.cafeMenuName}_${item.temperature}_${item.size}`
    }));

    /* 메뉴 목록 */
    const menuRes = await api.get('/cafe-menus');
    cafeMenuOptions.value = menuRes.data.data.map((m) => ({
        label: m.cafeMenuName,
        value: m.cafeMenuName
    }));

    /* 원재료 목록 (레시피용) */
    const matRes = await api.get('/materials');
    materialOptions.value = matRes.data.data.map((v) => ({
        label: v.materialName,
        value: v.materialName
    }));

    /* 단위 목록 */
    const unitRes = await api.get('/units');
    unitOptions.value = unitRes.data.data.map((v) => ({
        label: v.unitId,
        value: v.unitId
    }));
});

/* ===========================================
   가격 Dialog
=========================================== */
function openNew() {
    priceItem.value = { cafeMenuName: '', temperature: '', size: '', price: null };
    submitted.value = false;
    priceDialog.value = true;
}

function hidePriceDialog() {
    priceDialog.value = false;
    submitted.value = false;
}

function isPriceEdit() {
    return menuPrices.value.some(
        (v) =>
            v.cafeMenuName === priceItem.value.cafeMenuName &&
            v.temperature === priceItem.value.temperature &&
            v.size === priceItem.value.size
    );
}

/* 저장 */
async function savePrice() {
    submitted.value = true;
    if (!priceItem.value.cafeMenuName || !priceItem.value.temperature || !priceItem.value.size) return;

    if (isPriceEdit()) {
        await api.put('/cafe-menu-prices', priceItem.value);

        const idx = menuPrices.value.findIndex(
            (v) =>
                v.cafeMenuName === priceItem.value.cafeMenuName &&
                v.temperature === priceItem.value.temperature &&
                v.size === priceItem.value.size
        );

        menuPrices.value[idx] = {
            ...priceItem.value,
            uniqueKey: `${priceItem.value.cafeMenuName}_${priceItem.value.temperature}_${priceItem.value.size}`
        };

        toast.add({ severity: 'success', summary: '수정 완료', detail: '가격이 수정되었습니다', life: 3000 });
    } else {
        await api.post('/cafe-menu-prices', priceItem.value);

        const added = { ...priceItem.value };
        added.uniqueKey = `${added.cafeMenuName}_${added.temperature}_${added.size}`;
        menuPrices.value.push(added);

        toast.add({ severity: 'success', summary: '등록 완료', detail: '가격이 등록되었습니다', life: 3000 });
    }

    priceDialog.value = false;
}

/* 삭제 */
function confirmDeletePrice(row) {
    priceItem.value = row;
    deletePriceDialog.value = true;
}

async function deletePrice() {
    await api.delete('/cafe-menu-prices', {
        params: {
            cafeMenuName: priceItem.value.cafeMenuName,
            temperature: priceItem.value.temperature,
            size: priceItem.value.size
        }
    });

    menuPrices.value = menuPrices.value.filter((v) => v.uniqueKey !== priceItem.value.uniqueKey);

    toast.add({ severity: 'success', summary: '삭제 완료', detail: '삭제되었습니다', life: 3000 });
    deletePriceDialog.value = false;
}

/* 선택 삭제 */
async function deleteSelectedPrices() {
    for (const p of selectedPrices.value) {
        await api.delete('/cafe-menu-prices', {
            params: {
                cafeMenuName: p.cafeMenuName,
                temperature: p.temperature,
                size: p.size
            }
        });
    }

    menuPrices.value = menuPrices.value.filter((v) => !selectedPrices.value.includes(v));
    selectedPrices.value = null;

    deleteManyPriceDialog.value = false;
    toast.add({ severity: 'success', summary: '삭제 완료', detail: '선택한 항목이 삭제되었습니다', life: 3000 });
}

/* ===========================================
   레시피 팝업 관리
=========================================== */
async function openRecipePopup(row) {
    recipeItem.value.cafeMenuName = row.cafeMenuName;
    recipeItem.value.temperature = row.temperature;
    recipeItem.value.size = row.size;

    const res = await api.get('/cafe-menu-materials', {
        params: {
            cafeMenuName: row.cafeMenuName,
            temperature: row.temperature,
            size: row.size
        }
    });

    recipes.value = res.data.data.map((m) => ({
        ...m,
        uniqueKey: `${m.cafeMenuName}_${m.temperature}_${m.size}_${m.materialName}`
    }));

    recipeDialog.value = true;
}

/* ===========================================
   레시피 등록/수정
=========================================== */
function openNewRecipe() {
    recipeItem.value = {
        cafeMenuName: recipeItem.value.cafeMenuName,
        temperature: recipeItem.value.temperature,
        size: recipeItem.value.size,
        materialName: '',
        quantity: null,
        unitId: ''
    };
    recipeFormDialog.value = true;
}

function editRecipe(row) {
    recipeItem.value = { ...row };
    recipeFormDialog.value = true;
}

async function saveRecipe() {
    if (!recipeItem.value.materialName) return;

    const isEdit = recipes.value.some(
        (v) =>
            v.materialName === recipeItem.value.materialName &&
            v.cafeMenuName === recipeItem.value.cafeMenuName &&
            v.temperature === recipeItem.value.temperature &&
            v.size === recipeItem.value.size
    );

    if (isEdit) {
        await api.put('/cafe-menu-materials', recipeItem.value);

        const idx = recipes.value.findIndex(
            (v) =>
                v.uniqueKey ===
                `${recipeItem.value.cafeMenuName}_${recipeItem.value.temperature}_${recipeItem.value.size}_${recipeItem.value.materialName}`
        );

        recipes.value[idx] = {
            ...recipeItem.value,
            uniqueKey: `${recipeItem.value.cafeMenuName}_${recipeItem.value.temperature}_${recipeItem.value.size}_${recipeItem.value.materialName}`
        };

        toast.add({ severity: 'success', summary: '수정 완료', detail: '레시피가 수정되었습니다', life: 3000 });
    } else {
        await api.post('/cafe-menu-materials', recipeItem.value);

        const added = { ...recipeItem.value };
        added.uniqueKey = `${added.cafeMenuName}_${added.temperature}_${added.size}_${added.materialName}`;
        recipes.value.push(added);

        toast.add({ severity: 'success', summary: '등록 완료', detail: '레시피가 등록되었습니다', life: 3000 });
    }

    recipeFormDialog.value = false;
}

/* 레시피 삭제 */
async function deleteRecipe(row) {
    await api.delete('/cafe-menu-materials', {
        params: {
            cafeMenuName: row.cafeMenuName,
            temperature: row.temperature,
            size: row.size,
            materialName: row.materialName
        }
    });

    recipes.value = recipes.value.filter((v) => v.uniqueKey !== row.uniqueKey);

    toast.add({ severity: 'success', summary: '삭제 완료', detail: '레시피가 삭제되었습니다', life: 3000 });
}
</script>

<template>
    <div>
        <!-- =======================
             1) 메뉴 옵션 / 가격 관리
        ======================== -->
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

        <!-- =======================
             2) 가격 등록 Dialog
        ======================== -->
        <Dialog v-model:visible="priceDialog" header="메뉴 옵션 가격" :style="{ width: '480px' }" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">메뉴명</label>
                    <Select
                        v-model="priceItem.cafeMenuName"
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
                                :disabled="isPriceEdit()"
                            />
                            <label :for="'temp_' + t.value">{{ t.label }}</label>
                        </div>
                    </div>
                </div>

                <div>
                    <label class="block font-bold mb-2">사이즈</label>
                    <div class="flex gap-6">
                        <div class="flex items-center gap-2" v-for="s in sizes" :key="s.value">
                            <RadioButton
                                :inputId="'size_' + s.value"
                                v-model="priceItem.size"
                                :value="s.value"
                                :disabled="isPriceEdit()"
                            />
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

        <!-- 가격 삭제 Dialog -->
        <Dialog v-model:visible="deletePriceDialog" header="삭제 확인" :style="{ width: '400px' }" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ priceItem.cafeMenuName }}</b> ({{ priceItem.temperature }}/{{ priceItem.size }}) 옵션을
                    삭제하시겠습니까?
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

        <!-- =======================
             3) 레시피 팝업
        ======================== -->
        <Dialog v-model:visible="recipeDialog" header="레시피 관리" :style="{ width: '900px' }" :modal="true">
            <!-- 선택된 메뉴 옵션 요약 (readonly) -->
            <div class="mb-4 p-4 bg-gray-50 border rounded-lg">
                <p class="text-lg font-bold mb-2">선택한 메뉴 옵션</p>

                <p><b>메뉴명:</b> {{ recipeItem.cafeMenuName }}</p>
                <p><b>온도:</b> {{ recipeItem.temperature }}</p>
                <p><b>사이즈:</b> {{ recipeItem.size }}</p>
            </div>

            <!-- 레시피 테이블 -->
            <DataTable :value="recipes" v-model:selection="selectedRecipes" dataKey="uniqueKey" paginator :rows="8">
                <Column selectionMode="multiple" style="width: 3rem" />

                <Column field="materialName" header="원재료" sortable />
                <Column field="quantity" header="수량" sortable />
                <Column field="unitId" header="단위" sortable />

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

        <!-- 레시피 등록/수정 Dialog -->
        <Dialog v-model:visible="recipeFormDialog" header="레시피 등록/수정" :style="{ width: '450px' }" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label class="block font-bold mb-2">원재료</label>
                    <Select
                        v-model="recipeItem.materialName"
                        :options="materialOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="원재료 선택"
                        fluid
                    />
                </div>

                <div>
                    <label class="block font-bold mb-2">수량</label>
                    <InputNumber v-model="recipeItem.quantity" :minFractionDigits="0" :maxFractionDigits="4" fluid />
                </div>

                <div>
                    <label class="block font-bold mb-2">단위</label>
                    <Select
                        v-model="recipeItem.unitId"
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
