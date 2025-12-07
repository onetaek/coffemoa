<script setup>
import api from '@/api/axios';
import { formatCurrency } from '@/utils/NumberUtil';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref, watch } from 'vue';

const toast = useToast();

/* -----------------------------------------------------
   데이터 상태
----------------------------------------------------- */
const records = ref([]);
const selectedRecords = ref([]);

const dialogVisible = ref(false);
const deleteDialog = ref(false);
const deleteManyDialog = ref(false);

/* 고정비 항목 */
const fixedCostOptions = ref([]);

/* 검색 조건 */
const search = ref({
    fixedCostId: 'ALL',
    fromValue: null,
    toValue: null
});

const selectedSearchPeriodType = ref(null);
const searchFromCalendar = ref(null);
const searchToCalendar = ref(null);

/* 등록/수정 Record */
const record = ref({
    id: null,
    fixedCostId: null,
    periodValue: null,
    amount: null
});

const selectedPeriodType = ref(null);
const calendarValue = ref(null);

const submitted = ref(false);

/* -----------------------------------------------------
   기간 포맷 변환
----------------------------------------------------- */
function formatPeriodValue(periodType, date) {
    if (!date) return null;

    let y = date.getFullYear();
    let m = String(date.getMonth() + 1).padStart(2, '0');
    let d = String(date.getDate()).padStart(2, '0');

    if (periodType === 'YEARLY') return `${y}`;
    if (periodType === 'MONTHLY') return `${y}-${m}`;
    if (periodType === 'DAILY') return `${y}-${m}-${d}`;
    return null;
}

function convertPeriodToDate(periodType, value) {
    if (!value) return null;
    if (periodType === 'YEARLY') return new Date(`${value}-01-01`);
    if (periodType === 'MONTHLY') return new Date(`${value}-01`);
    if (periodType === 'DAILY') return new Date(value);
    return null;
}

/* -----------------------------------------------------
   초기 데이터 로딩
----------------------------------------------------- */
async function loadData() {
    try {
        // 고정비 항목
        const fcRes = await api.get('/fixed-costs');
        fixedCostOptions.value = [
            { label: '전체', value: 'ALL', periodTypeCode: null },
            ...fcRes.data.data.map((f) => ({
                label: f.costName,
                value: f.id,
                periodTypeCode: f.periodTypeCode
            }))
        ];

        // 레코드 조회
        const params = {
            fixedCostId: search.value.fixedCostId === 'ALL' ? null : search.value.fixedCostId,
            fromValue: search.value.fromValue,
            toValue: search.value.toValue
        };

        const recRes = await api.get('/fixed-cost-records', { params });
        records.value = recRes.data.data;
    } catch (err) {
        toast.add({ severity: 'error', summary: '오류', detail: '조회 실패' });
    }
}

onMounted(loadData);

/* -----------------------------------------------------
   검색조건 → 유형 자동 선택
----------------------------------------------------- */
watch(
    () => search.value.fixedCostId,
    (id) => {
        if (id === 'ALL' || !id) {
            selectedSearchPeriodType.value = null;
            searchFromCalendar.value = null;
            searchToCalendar.value = null;
            search.value.fromValue = null;
            search.value.toValue = null;
            return;
        }

        const item = fixedCostOptions.value.find((f) => f.value === id);
        selectedSearchPeriodType.value = item?.periodTypeCode;
    }
);

/* -----------------------------------------------------
   검색용 Calendar → periodValue 자동 변환
----------------------------------------------------- */
watch(searchFromCalendar, (date) => {
    if (!selectedSearchPeriodType.value) return;

    search.value.fromValue = formatPeriodValue(selectedSearchPeriodType.value, date);

    if (!searchToCalendar.value) {
        searchToCalendar.value = new Date(date);
    }
});

watch(searchToCalendar, (date) => {
    if (!selectedSearchPeriodType.value) return;

    search.value.toValue = formatPeriodValue(selectedSearchPeriodType.value, date);
});

/* -----------------------------------------------------
   팝업: 항목 선택 → 기간 유형 결정
----------------------------------------------------- */
watch(
    () => record.value.fixedCostId,
    (id) => {
        const item = fixedCostOptions.value.find((f) => f.value === id);
        selectedPeriodType.value = item?.periodTypeCode || null;

        // 기존 값 초기화
        record.value.periodValue = null;
        calendarValue.value = null;
    }
);

/* Calendar → periodValue */
watch(calendarValue, (date) => {
    if (!selectedPeriodType.value) return;
    record.value.periodValue = formatPeriodValue(selectedPeriodType.value, date);
});

/* -----------------------------------------------------
   CRUD Dialog
----------------------------------------------------- */
function openNew() {
    record.value = { id: null, fixedCostId: null, periodValue: null, amount: null };
    selectedPeriodType.value = null;
    calendarValue.value = null;
    submitted.value = false;
    dialogVisible.value = true;
}

function editRow(row) {
    record.value = { ...row };
    selectedPeriodType.value = row.periodTypeCode;
    calendarValue.value = convertPeriodToDate(row.periodTypeCode, row.periodValue);
    dialogVisible.value = true;
}

function hideDialog() {
    dialogVisible.value = false;
}

const filters = ref({
    global: { value: null, matchMode: 'contains' }
});

/* -----------------------------------------------------
   저장(CUD)
----------------------------------------------------- */
async function saveRecord() {
    submitted.value = true;

    if (!record.value.fixedCostId || !record.value.periodValue || !record.value.amount) {
        return;
    }

    const payload = [
        {
            id: record.value.id,
            fixedCostId: record.value.fixedCostId,
            periodValue: record.value.periodValue,
            amount: record.value.amount,
            flag: record.value.id ? 'U' : 'C'
        }
    ];

    try {
        await api.post('/fixed-cost-records/cud', payload);
        toast.add({ severity: 'success', summary: '저장 완료' });
        dialogVisible.value = false;
        await loadData();
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '저장 실패' });
    }
}

/* -----------------------------------------------------
   삭제
----------------------------------------------------- */
function confirmDeleteRow(row) {
    record.value = row;
    deleteDialog.value = true;
}

async function deleteRecord() {
    const payload = [{ id: record.value.id, flag: 'D' }];

    try {
        await api.post('/fixed-cost-records/cud', payload);
        toast.add({ severity: 'success', summary: '삭제 완료' });
        await loadData();
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '삭제 실패' });
    }

    deleteDialog.value = false;
}

/* 선택 삭제 */
async function deleteSelectedRecords() {
    const payload = selectedRecords.value.map((r) => ({ id: r.id, flag: 'D' }));

    try {
        await api.post('/fixed-cost-records/cud', payload);
        toast.add({ severity: 'success', summary: '삭제 완료' });
        await loadData();
        selectedRecords.value = [];
    } catch {
        toast.add({ severity: 'error', summary: '오류', detail: '삭제 실패' });
    }

    deleteManyDialog.value = false;
}

/* -----------------------------------------------------
   화면 표기를 위해 periodValue 변환
----------------------------------------------------- */
function displayPeriod(row) {
    const v = row.periodValue;
    if (!v) return '';

    if (row.periodTypeCode === 'YEARLY') return `${v}년`;
    if (row.periodTypeCode === 'MONTHLY') {
        const [y, m] = v.split('-');
        return `${y}년 ${m}월`;
    }
    if (row.periodTypeCode === 'DAILY') {
        const [y, m, d] = v.split('-');
        return `${y}년 ${m}월 ${d}일`;
    }
    return v;
}
</script>

<template>
    <div>
        <!-- ------------------ 검색 + 버튼 ------------------ -->
        <div class="card mb-4">
            <div class="flex justify-between items-center mb-3">
                <div>
                    <Button label="신규 등록" icon="pi pi-plus" @click="openNew" class="mr-2" />
                    <Button
                        label="선택 삭제"
                        icon="pi pi-trash"
                        severity="danger"
                        :disabled="!selectedRecords.length"
                        @click="deleteManyDialog = true"
                    />
                </div>

                <div class="flex gap-2 items-end">
                    <!-- 고정비 항목 선택 -->
                    <Select
                        v-model="search.fixedCostId"
                        :options="fixedCostOptions"
                        optionLabel="label"
                        optionValue="value"
                        class="w-32"
                    />

                    <!-- 기간 선택 UI (유형에 따라 변경) -->
                    <template v-if="selectedSearchPeriodType === 'YEARLY'">
                        <Calendar v-model="searchFromCalendar" view="year" dateFormat="yy" class="w-32" />
                        <Calendar v-model="searchToCalendar" view="year" dateFormat="yy" class="w-32" />
                    </template>

                    <template v-if="selectedSearchPeriodType === 'MONTHLY'">
                        <Calendar v-model="searchFromCalendar" view="month" dateFormat="yy-mm" class="w-32" />
                        <Calendar v-model="searchToCalendar" view="month" dateFormat="yy-mm" class="w-32" />
                    </template>

                    <template v-if="selectedSearchPeriodType === 'DAILY'">
                        <Calendar v-model="searchFromCalendar" dateFormat="yy-mm-dd" class="w-32" />
                        <Calendar v-model="searchToCalendar" dateFormat="yy-mm-dd" class="w-32" />
                    </template>

                    <Button label="조회" icon="pi pi-search" @click="loadData" />
                </div>
            </div>

            <!-- ------------------ 테이블 ------------------ -->
            <DataTable
                :value="records"
                :filters="filters"
                v-model:selection="selectedRecords"
                dataKey="id"
                paginator
                :rows="10"
            >
                <template #header>
                    <div class="flex justify-between items-center">
                        <h4>고정비 금액 이력 관리</h4>
                        <IconField>
                            <InputIcon><i class="pi pi-search" /></InputIcon>
                            <InputText v-model="filters.global.value" placeholder="검색어 입력..." />
                        </IconField>
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem" />
                <Column field="fixedCostName" header="항목명" sortable />

                <Column field="periodTypeName" header="유형" sortable />

                <Column header="기간" sortable>
                    <template #body="{ data }">
                        {{ displayPeriod(data) }}
                    </template>
                </Column>

                <Column field="amount" header="금액" sortable>
                    <template #body="{ data }">
                        {{ formatCurrency(data.amount) }}
                    </template>
                </Column>

                <Column style="width: 9rem">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editRow(data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteRow(data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- ------------------ Dialog ------------------ -->
        <Dialog v-model:visible="dialogVisible" header="고정비 금액 이력 등록 / 수정" modal :style="{ width: '430px' }">
            <div class="flex flex-col gap-4">
                <!-- 고정비 항목 -->
                <div>
                    <label class="block font-bold mb-2">고정비 항목</label>
                    <Select
                        v-model="record.fixedCostId"
                        :options="fixedCostOptions.filter((f) => f.value !== 'ALL')"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="항목 선택"
                        fluid
                    />
                </div>

                <!-- 기간 Calendar -->
                <div>
                    <label class="block font-bold mb-2">기간</label>

                    <Calendar
                        v-if="selectedPeriodType === 'YEARLY'"
                        v-model="calendarValue"
                        view="year"
                        dateFormat="yy"
                        fluid
                    />

                    <Calendar
                        v-if="selectedPeriodType === 'MONTHLY'"
                        v-model="calendarValue"
                        view="month"
                        dateFormat="yy-mm"
                        fluid
                    />

                    <Calendar
                        v-if="selectedPeriodType === 'DAILY'"
                        v-model="calendarValue"
                        dateFormat="yy-mm-dd"
                        fluid
                    />
                </div>

                <!-- 금액 -->
                <div>
                    <label class="block font-bold mb-2">금액</label>
                    <InputNumber v-model="record.amount" inputClass="w-full" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="취소" @click="dialogVisible = false" text />
                <Button label="저장" @click="saveRecord" class="p-button-success" />
            </template>
        </Dialog>

        <!-- ------------------ 단건 삭제 ------------------ -->
        <Dialog v-model:visible="deleteDialog" header="삭제 확인" :style="{ width: '350px' }">
            <p>정말 삭제하시겠습니까?</p>

            <template #footer>
                <Button label="취소" text @click="deleteDialog = false" />
                <Button label="삭제" severity="danger" @click="deleteRecord" />
            </template>
        </Dialog>

        <!-- ------------------ 선택 삭제 ------------------ -->
        <Dialog v-model:visible="deleteManyDialog" header="선택 삭제" :style="{ width: '350px' }">
            <p>선택한 항목을 삭제하시겠습니까?</p>

            <template #footer>
                <Button label="취소" text @click="deleteManyDialog = false" />
                <Button label="삭제" severity="danger" @click="deleteSelectedRecords" />
            </template>
        </Dialog>
    </div>
</template>
