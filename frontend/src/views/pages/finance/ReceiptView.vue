<script setup>
import api from '@/api/axios';
import { today } from '@/utils/DateUtil';
import { formatCurrency } from '@/utils/NumberUtil';
import { FilterMatchMode } from '@primevue/core/api';
import Chart from 'primevue/chart';
import { useToast } from 'primevue/usetoast';
import { computed, ref } from 'vue';

const toast = useToast();

/* -----------------------------
   📌 날짜 검색
------------------------------ */
const fromDate = ref(today());
const toDate = ref(today());

/* -----------------------------
   📌 API 데이터
------------------------------ */
const detailList = ref([]);
const fixedCostList = ref([]);

const summary = ref({
    totalPrice: 0,
    discountPrice: 0,
    costPrice: 0,
    profitPrice: 0,
    fixedTotal: 0
});

const loading = ref(false);

/* -----------------------------
   📌 검색 필터
------------------------------ */
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
});

/* =====================================================
   📊 Doughnut Chart (고정비 상세 포함)
===================================================== */
const doughnutData = computed(() => {
    const labels = [];
    const data = [];
    const colors = [];

    // 재료비
    labels.push('재료비');
    data.push(summary.value.costPrice);
    colors.push('#fb923c');

    // 할인액
    labels.push('할인액');
    data.push(summary.value.discountPrice);
    colors.push('#60a5fa');

    // 고정비 상세
    const fixedCostColors = ['#ef4444', '#f87171', '#dc2626', '#b91c1c'];
    fixedCostList.value.forEach((fc, idx) => {
        labels.push(`고정비 - ${fc.costName}`);
        data.push(fc.appliedAmount ?? 0);
        colors.push(fixedCostColors[idx % fixedCostColors.length]);
    });

    // 순수익
    labels.push('순수익');
    data.push(summary.value.profitPrice);
    colors.push('#22c55e');

    return {
        labels,
        datasets: [
            {
                data,
                backgroundColor: colors
            }
        ]
    };
});

const doughnutOptions = {
    cutout: '65%',
    plugins: {
        legend: {
            position: 'bottom',
            labels: {
                usePointStyle: true
            }
        },
        tooltip: {
            callbacks: {
                label: (ctx) => {
                    const value = ctx.raw ?? 0;
                    return `${ctx.label}: ${formatCurrency(value)}`;
                }
            }
        }
    }
};

/* =====================================================
   📋 Doughnut 옆 요약 테이블 데이터
===================================================== */
const donutTableRows = computed(() => {
    const rows = [];

    const total =
        summary.value.costPrice + summary.value.discountPrice + summary.value.fixedTotal + summary.value.profitPrice;

    rows.push({
        name: '재료비',
        amount: summary.value.costPrice,
        ratio: total > 0 ? (summary.value.costPrice / total) * 100 : 0
    });

    rows.push({
        name: '할인액',
        amount: summary.value.discountPrice,
        ratio: total > 0 ? (summary.value.discountPrice / total) * 100 : 0
    });

    fixedCostList.value.forEach((fc) => {
        rows.push({
            name: `고정비 - ${fc.costName}`,
            amount: fc.appliedAmount ?? 0,
            ratio: total > 0 ? ((fc.appliedAmount ?? 0) / total) * 100 : 0
        });
    });

    rows.push({
        name: '순수익',
        amount: summary.value.profitPrice,
        ratio: total > 0 ? (summary.value.profitPrice / total) * 100 : 0
    });

    return rows;
});

/* -----------------------------
   🔍 검색 실행
------------------------------ */
async function search() {
    if (!fromDate.value || !toDate.value) {
        toast.add({
            severity: 'warn',
            summary: '입력 필요',
            detail: '조회 시작일과 종료일을 입력하세요.',
            life: 3000
        });
        return;
    }

    try {
        loading.value = true;

        const res = await api.get('/receipt/cost', {
            params: {
                fromDate: fromDate.value,
                toDate: toDate.value
            }
        });

        const data = res.data.data;

        detailList.value = data.detailList ?? [];
        fixedCostList.value = data.fixedCostList ?? [];

        const fixedTotal = fixedCostList.value.reduce((sum, fc) => sum + (fc.appliedAmount ?? 0), 0);

        summary.value = {
            totalPrice: data.totalPrice ?? 0,
            discountPrice: data.discountPrice ?? 0,
            costPrice: data.costPrice ?? 0,
            profitPrice: data.profitPrice ?? 0,
            fixedTotal
        };

        toast.add({
            severity: 'success',
            summary: '조회 완료',
            detail: `총 ${detailList.value.length}건의 영수증 내역을 조회했습니다.`,
            life: 2500
        });
    } catch (err) {
        console.error(err);
        toast.add({
            severity: 'error',
            summary: '조회 오류',
            detail: '조회 중 오류가 발생했습니다.',
            life: 3000
        });
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div class="card">
        <!-- 🔍 검색 영역 -->
        <div class="flex flex-wrap gap-3 items-end mb-4">
            <div>
                <label class="font-semibold block mb-1">조회 시작일</label>
                <InputText type="date" v-model="fromDate" />
            </div>

            <div>
                <label class="font-semibold block mb-1">조회 종료일</label>
                <InputText type="date" v-model="toDate" />
            </div>

            <div>
                <Button label="조회" icon="pi pi-search" @click="search" />
            </div>
        </div>

        <!-- 📊 도넛 차트 + 테이블 -->
        <Card class="mb-6">
            <template #title>기간별 재무 요약</template>

            <template #content>
                <!-- 
                  grid-cols-1  : 모바일 → 세로
                  md:grid-cols-2 : PC → 좌/우
                -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6 items-center">
                    <!-- 도넛 차트 -->
                    <div class="flex justify-center">
                        <Chart
                            type="doughnut"
                            :data="doughnutData"
                            :options="doughnutOptions"
                            class="w-full max-w-[360px]"
                        />
                    </div>

                    <!-- 요약 테이블 -->
                    <div class="mx-auto w-full max-w-[420px]">
                        <table class="w-full text-sm border-collapse">
                            <thead>
                                <tr class="border-b">
                                    <th class="text-left py-2">항목명</th>
                                    <th class="text-right py-2">금액</th>
                                    <th class="text-right py-2">비율</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="row in donutTableRows" :key="row.name" class="border-b last:border-0">
                                    <td class="py-2">
                                        {{ row.name }}
                                    </td>
                                    <td class="py-2 text-right font-medium">
                                        {{ formatCurrency(row.amount) }}
                                    </td>
                                    <td class="py-2 text-right text-gray-600">{{ row.ratio.toFixed(1) }}%</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </template>
        </Card>

        <!-- 📄 영수증 상세 테이블 -->
        <DataTable
            :value="detailList"
            :loading="loading"
            :filters="filters"
            paginator
            :rows="20"
            responsiveLayout="scroll"
        >
            <template #header>
                <div class="flex justify-between items-center">
                    <h4 class="m-0">영수증 상세 조회</h4>

                    <IconField>
                        <InputIcon>
                            <i class="pi pi-search" />
                        </InputIcon>
                        <InputText v-model="filters['global'].value" placeholder="Search..." />
                    </IconField>
                </div>
            </template>

            <Column field="salesDate" header="일자" sortable style="min-width: 120px" />
            <Column field="receiptNumber" header="영수증번호" sortable />
            <Column field="orderTime" header="주문시각" sortable />
            <Column field="productCode" header="상품코드" sortable />
            <Column field="productName" header="상품명" sortable />

            <Column field="quantity" header="수량" sortable />

            <Column field="totalPrice" header="총매출" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.totalPrice) }}</template>
            </Column>

            <Column field="discountPrice" header="할인액" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.discountPrice) }}</template>
            </Column>

            <Column field="actualPrice" header="실매출" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.actualPrice) }}</template>
            </Column>

            <Column field="costPrice" header="재료비" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.costPrice) }}</template>
            </Column>

            <Column field="profitPrice" header="수익금" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.profitPrice) }}</template>
            </Column>
        </DataTable>
    </div>
</template>

<style scoped></style>
