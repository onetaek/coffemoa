<script setup>
import api from '@/api/axios';
import { today } from '@/utils/DateUtil';
import { FilterMatchMode } from '@primevue/core/api';
import { useToast } from 'primevue/usetoast';
import { ref } from 'vue';

const toast = useToast();

// 날짜 검색
const fromDate = ref(today());
const toDate = ref(today());

// 테이블 데이터
const receipts = ref([]);
const loading = ref(false);

const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
});

// 금액 포맷터
function formatCurrency(value) {
    return value?.toLocaleString() || '0';
}

// 검색 버튼 클릭 시
async function search() {
    if (!fromDate.value || !toDate.value) {
        toast.add({
            severity: 'warn',
            summary: '입력 필요',
            detail: '조회 시작일과 종료일을 입력하세요.',
            life: 2500
        });
        return;
    }

    try {
        loading.value = true;
        const res = await api.get('/receipt', {
            params: {
                fromDate: fromDate.value,
                toDate: toDate.value
            }
        });
        console.log('res:', res);

        receipts.value = res.data.data ?? [];

        toast.add({
            severity: 'success',
            summary: '조회 완료',
            detail: `총 ${receipts.value?.length || 0}건의 영수증 항목을 조회했습니다.`,
            life: 2500
        });
    } catch (err) {
        console.error(err);
        toast.add({
            severity: 'error',
            summary: '조회 오류',
            detail: '조회 중 오류가 발생했습니다.',
            life: 2500
        });
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div class="card">
        <!-- Search Area -->
        <div class="flex flex-wrap gap-3 items-end mb-4">
            <div>
                <label class="font-semibold block mb-1">조회 시작일</label>
                <InputText v-model="fromDate" type="date" />
            </div>

            <div>
                <label class="font-semibold block mb-1">조회 종료일</label>
                <InputText v-model="toDate" type="date" />
            </div>

            <div>
                <Button label="조회" icon="pi pi-search" @click="search" />
            </div>
        </div>

        <!-- DataTable -->
        <DataTable
            :value="receipts"
            :loading="loading"
            :filters="filters"
            paginator
            :rows="20"
            responsiveLayout="scroll"
            class="mt-6"
        >
            <template #header>
                <div class="flex flex-wrap gap-2 items-center justify-between">
                    <h4 class="m-0">영수증 상세 조회</h4>

                    <IconField>
                        <InputIcon>
                            <i class="pi pi-search" />
                        </InputIcon>
                        <InputText v-model="filters['global'].value" placeholder="Search..." />
                    </IconField>
                </div>
            </template>

            <Column field="salesDate" header="일자" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ data.salesDate }}
                </template>
            </Column>

            <Column field="posNumber" header="POS" sortable style="min-width: 80px" />

            <Column field="receiptNumber" header="영수증번호" sortable style="min-width: 120px" />

            <Column field="category" header="구분" sortable style="min-width: 100px">
                <template #body="{ data }">
                    <Tag :value="data.category" severity="info" />
                </template>
            </Column>

            <Column field="orderTime" header="주문시각" sortable style="min-width: 120px" />
            <Column field="payTime" header="결제시각" sortable style="min-width: 120px" />

            <Column field="productCode" header="상품코드" sortable style="min-width: 100px" />
            <Column field="productName" header="상품명" sortable style="min-width: 180px" />

            <Column field="quantity" header="수량" sortable style="min-width: 80px" />

            <Column field="totalPrice" header="총매출" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ formatCurrency(data.totalPrice) }}
                </template>
            </Column>

            <Column field="discountPrice" header="할인액" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ formatCurrency(data.discountPrice) }}
                </template>
            </Column>

            <Column field="actualPrice" header="실매출" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ formatCurrency(data.actualPrice) }}
                </template>
            </Column>

            <Column field="cost" header="가액" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ formatCurrency(data.cost) }}
                </template>
            </Column>

            <Column field="vat" header="부가세" sortable style="min-width: 120px">
                <template #body="{ data }">
                    {{ formatCurrency(data.vat) }}
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<style scoped>
:deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
