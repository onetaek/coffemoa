<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const toast = useToast();
const dt = ref();

/* 목록 */
const menuPrices = ref([]);
const selectedPrices = ref(null);

/* Dialogs */
const priceDialog = ref(false);
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);
const submitted = ref(false);

/* 폼 데이터 */
const priceItem = ref({
    cafeMenuName: '',
    temperature: '',
    size: '',
    price: null
});

/* 메뉴 목록 (select용) */
const cafeMenuOptions = ref([]);

/* 기본값 */
const temperatures = [
    { label: 'Hot (H)', value: 'H' },
    { label: 'Ice (I)', value: 'I' }
];

const sizes = [
    { label: 'Regular (R)', value: 'R' },
    { label: 'Large (L)', value: 'L' }
];

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

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

/* ----------------------------------------------------------------
   1) 초기 조회
---------------------------------------------------------------- */
onMounted(async () => {
    try {
        const res = await api.get('/cafe-menu-prices');
        menuPrices.value = res.data.data.map((item) => ({
            ...item,
            uniqueKey: `${item.cafeMenuName}_${item.temperature}_${item.size}`
        }));

        const menuRes = await api.get('/cafe-menus');
        cafeMenuOptions.value = menuRes.data.data.map((m) => ({
            label: m.cafeMenuName,
            value: m.cafeMenuName
        }));
    } catch {
        toast.add({
            severity: 'error',
            summary: '오류',
            detail: '메뉴 가격 목록을 불러오는 데 실패했습니다.',
            life: 3000
        });
    }
});

/* ----------------------------------------------------------------
   Dialog 관련
---------------------------------------------------------------- */
function openNew() {
    priceItem.value = {
        cafeMenuName: '',
        temperature: '',
        size: '',
        price: null
    };
    submitted.value = false;
    priceDialog.value = true;
}

function hideDialog() {
    priceDialog.value = false;
    submitted.value = false;
}

function isEditMode() {
    return menuPrices.value.some(
        (v) =>
            v.cafeMenuName === priceItem.value.cafeMenuName &&
            v.temperature === priceItem.value.temperature &&
            v.size === priceItem.value.size
    );
}

/* ----------------------------------------------------------------
   저장 (등록 및 수정)
---------------------------------------------------------------- */
async function savePrice() {
    submitted.value = true;

    if (!priceItem.value.cafeMenuName || !priceItem.value.temperature || !priceItem.value.size) {
        return;
    }

    try {
        if (isEditMode()) {
            /* 수정 */
            await api.put('/cafe-menu-prices', priceItem.value);

            const idx = menuPrices.value.findIndex(
                (v) =>
                    v.cafeMenuName === priceItem.value.cafeMenuName &&
                    v.temperature === priceItem.value.temperature &&
                    v.size === priceItem.value.size
            );

            menuPrices.value[idx] = { ...priceItem.value };
            menuPrices.value[idx].uniqueKey =
                `${priceItem.value.cafeMenuName}_${priceItem.value.temperature}_${priceItem.value.size}`;

            toast.add({ severity: 'success', summary: '수정 완료', detail: '가격이 수정되었습니다.', life: 3000 });
        } else {
            /* 신규 등록 */
            await api.post('/cafe-menu-prices', priceItem.value);

            const added = { ...priceItem.value };
            added.uniqueKey = `${added.cafeMenuName}_${added.temperature}_${added.size}`;
            menuPrices.value.push(added);

            toast.add({ severity: 'success', summary: '등록 완료', detail: '가격이 등록되었습니다.', life: 3000 });
        }

        priceDialog.value = false;
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '저장 중 오류가 발생했습니다.', life: 3000 });
    }
}

/* ----------------------------------------------------------------
   삭제
---------------------------------------------------------------- */
function confirmDelete(p) {
    priceItem.value = p;
    deleteDialog.value = true;
}

async function deletePrice() {
    try {
        await api.delete('/cafe-menu-prices', {
            params: {
                cafeMenuName: priceItem.value.cafeMenuName,
                temperature: priceItem.value.temperature,
                size: priceItem.value.size
            }
        });

        menuPrices.value = menuPrices.value.filter((v) => v.uniqueKey !== priceItem.value.uniqueKey);

        toast.add({ severity: 'success', summary: '삭제 완료', detail: '삭제되었습니다.', life: 3000 });
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '삭제 중 오류 발생', life: 3000 });
    }

    deleteDialog.value = false;
}

/* ----------------------------------------------------------------
   선택 삭제
---------------------------------------------------------------- */
async function deleteSelected() {
    try {
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

        toast.add({ severity: 'success', summary: '삭제 완료', detail: '선택 항목이 삭제되었습니다.', life: 3000 });
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '선택 삭제 처리 중 오류 발생', life: 3000 });
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
                        :disabled="!selectedPrices || !selectedPrices.length"
                        @click="deleteManyDialog = true"
                    />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedPrices"
                :value="menuPrices"
                dataKey="uniqueKey"
                paginator
                :rows="10"
                :filters="filters"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>메뉴 옵션 / 가격 관리</h4>
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

                <Column style="min-width: 10rem">
                    <template #body="{ data }">
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
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDelete(data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- 등록 & 수정 Dialog -->
        <Dialog v-model:visible="priceDialog" :style="{ width: '480px' }" header="메뉴 옵션 가격" :modal="true">
            <div class="flex flex-col gap-6">
                <!-- 메뉴명 -->
                <div>
                    <label class="block font-bold mb-2">메뉴명</label>
                    <Select
                        v-model="priceItem.cafeMenuName"
                        :options="cafeMenuOptions"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="메뉴 선택"
                        :disabled="isEditMode()"
                        fluid
                    />
                </div>

                <!-- 온도 RadioButtons -->
                <div>
                    <label class="block font-bold mb-2">온도 (Temperature)</label>

                    <div class="grid grid-cols-12 gap-4">
                        <div class="flex items-center gap-2 col-span-6" v-for="t in temperatures" :key="t.value">
                            <RadioButton
                                :inputId="'temp_' + t.value"
                                v-model="priceItem.temperature"
                                name="temperature"
                                :value="t.value"
                                :disabled="isEditMode()"
                            />
                            <label :for="'temp_' + t.value">{{ t.label }}</label>
                        </div>
                    </div>
                </div>

                <!-- 사이즈 RadioButtons -->
                <div>
                    <label class="block font-bold mb-2">사이즈 (Size)</label>

                    <div class="grid grid-cols-12 gap-4">
                        <div class="flex items-center gap-2 col-span-6" v-for="s in sizes" :key="s.value">
                            <RadioButton
                                :inputId="'size_' + s.value"
                                v-model="priceItem.size"
                                name="size"
                                :value="s.value"
                                :disabled="isEditMode()"
                            />
                            <label :for="'size_' + s.value">{{ s.label }}</label>
                        </div>
                    </div>
                </div>

                <!-- 가격 -->
                <div>
                    <label class="block font-bold mb-2">가격</label>
                    <InputNumber
                        v-model="priceItem.price"
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                        placeholder="가격 입력"
                        fluid
                    />
                </div>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="hideDialog" />
                <Button label="저장" icon="pi pi-check" @click="savePrice" />
            </template>
        </Dialog>

        <!-- 단건 삭제 -->
        <Dialog v-model:visible="deleteDialog" :style="{ width: '400px' }" header="삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>
                    <b>{{ priceItem.cafeMenuName }}</b> - {{ priceItem.temperature }}/{{ priceItem.size }}
                    을(를) 삭제하시겠습니까?
                </span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deletePrice" />
            </template>
        </Dialog>

        <!-- 선택 삭제 -->
        <Dialog v-model:visible="deleteManyDialog" :style="{ width: '400px' }" header="선택 삭제 확인" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle text-3xl" />
                <span>선택한 메뉴 가격 정보들을 삭제하시겠습니까?</span>
            </div>

            <template #footer>
                <Button label="취소" icon="pi pi-times" text @click="deleteManyDialog = false" />
                <Button label="삭제" icon="pi pi-check" severity="danger" @click="deleteSelected" />
            </template>
        </Dialog>
    </div>
</template>
