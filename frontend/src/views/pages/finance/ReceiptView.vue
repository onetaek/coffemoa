<script setup>
import api from '@/api/axios';
import { today } from '@/utils/DateUtil';
import { formatCurrency } from '@/utils/NumberUtil';
import { FilterMatchMode } from '@primevue/core/api';
import { useToast } from 'primevue/usetoast';
import { ref } from 'vue';

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

        /* 상세 목록 */
        detailList.value = data.detailList ?? [];

        /* 고정비 목록 */
        fixedCostList.value = data.fixedCostList ?? [];

        /* Summary */
        const fixedTotal = fixedCostList.value.reduce((sum, fc) => sum + (fc.appliedAmount ?? 0), 0);

        summary.value = {
            totalPrice: data.totalPrice ?? 0,
            discountPrice: data.discountPrice ?? 0,
            costPrice: data.costPrice ?? 0,
            profitPrice: data.profitPrice ?? 0,
            fixedTotal: fixedTotal
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

        <!-- 📊 Summary Cards -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
            <Card>
                <template #title>총 매출액</template>
                <template #content>
                    <div class="text-2xl font-bold text-green-600">{{ formatCurrency(summary.totalPrice) }}원</div>
                </template>
            </Card>

            <Card>
                <template #title>총 할인액</template>
                <template #content>
                    <div class="text-2xl font-bold text-blue-600">{{ formatCurrency(summary.discountPrice) }}원</div>
                </template>
            </Card>

            <Card>
                <template #title>총 원가(재료비)</template>
                <template #content>
                    <div class="text-2xl font-bold text-orange-600">{{ formatCurrency(summary.costPrice) }}원</div>
                </template>
            </Card>

            <Card>
                <template #title>총 수익금</template>
                <template #content>
                    <div class="text-2xl font-bold text-red-600">{{ formatCurrency(summary.profitPrice) }}원</div>
                </template>
            </Card>
        </div>

        <!-- 🧾 고정비 Summary -->
        <div v-if="fixedCostList.length > 0" class="mb-6">
            <h3 class="text-lg font-semibold mb-2">고정비</h3>

            <!-- 총 고정비 카드 -->
            <Card class="mb-4 bg-red-50">
                <template #title>총 고정비</template>
                <template #content>
                    <div class="text-2xl font-bold text-red-600">{{ formatCurrency(summary.fixedTotal) }}원</div>
                </template>
            </Card>

            <!-- 개별 고정비 카드들 -->
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                <Card v-for="fc in fixedCostList" :key="fc.fixedCostId" class="border border-gray-200">
                    <template #title>{{ fc.costName }}</template>

                    <template #content>
                        <div
                            class="text-xl font-semibold"
                            :class="fc.appliedAmount > 0 ? 'text-blue-600' : 'text-gray-400'"
                        >
                            {{ formatCurrency(fc.appliedAmount) }}원
                        </div>

                        <div class="text-xs text-gray-400 mt-1">
                            {{ fc.periodValue }}
                        </div>
                    </template>
                </Card>
            </div>
        </div>

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
                <template #body="{ data }"> {{ formatCurrency(data.totalPrice) }}원 </template>
            </Column>

            <Column field="discountPrice" header="할인액" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.discountPrice) }}원 </template>
            </Column>

            <Column field="actualPrice" header="실매출" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.actualPrice) }}원 </template>
            </Column>

            <Column field="costPrice" header="재료비" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.costPrice) }}원 </template>
            </Column>

            <Column field="profitPrice" header="수익금" sortable>
                <template #body="{ data }"> {{ formatCurrency(data.profitPrice) }}원 </template>
            </Column>
        </DataTable>
    </div>
</template>

<style scoped></style>
