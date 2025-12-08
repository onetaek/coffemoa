<script setup>
import api from '@/api/axios';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import * as XLSX from 'xlsx';

// FullCalendar import
import koLocale from '@fullcalendar/core/locales/ko';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import FullCalendar from '@fullcalendar/vue3';

const toast = useToast();

/* ======================================================
 * 1) 달력에 표시할 이벤트 리스트
 * ====================================================== */
const calendarEvents = ref([]);

const currentYear = ref(new Date().getFullYear());
const currentMonth = ref(new Date().getMonth() + 1);

/* ======================================================
 * 2) 특정 월의 업로드 현황 API 호출
 * ====================================================== */
async function loadCalendarSummary() {
    try {
        const res = await api.get('/receipt/uploaded-summary', {
            params: {
                year: currentYear.value,
                month: currentMonth.value
            }
        });

        const dailyList = res.data.data;

        // FullCalendar 이벤트로 변환
        calendarEvents.value = dailyList.map((d) => ({
            title: `${d.count}건`,
            start: d.date,
            allDay: true,
            backgroundColor: '#e3f2fd', // 연한 파랑
            borderColor: '#90caf9',
            textColor: '#0d47a1'
        }));
        console.log('calendarEvents.value:', calendarEvents.value);
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '달력 로딩 실패',
            detail: '업로드 현황 데이터를 불러오는 중 오류가 발생했습니다.',
            life: 2500
        });
    }
}

/* ======================================================
 * 3) FullCalendar - 월 변경 시 호출
 * ====================================================== */
function onMonthChange(info) {
    currentYear.value = info.start.getFullYear();
    currentMonth.value = info.start.getMonth() + 1;

    loadCalendarSummary();
}

function fetchEvents(fetchInfo, successCallback) {
    successCallback([...calendarEvents.value]);
}

/* ======================================================
 * 4) 엑셀 업로드 기존 기능
 * ====================================================== */
const result = ref({
    date: '',
    receiptList: []
});

function handleFileUpload(event) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (e) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });

        const sheetName = workbook.SheetNames[0];
        const sheet = workbook.Sheets[sheetName];

        const rawJson = XLSX.utils.sheet_to_json(sheet, {
            defval: '',
            raw: false
        });

        const parsed = parseReceiptExcel(rawJson);

        result.value = parsed;
    };

    reader.readAsArrayBuffer(file);
}

async function saveToServer() {
    try {
        const requestBody = {
            date: result.value.date,
            receiptList: result.value.receiptList.map((item) => ({
                posNumber: item.posNo,
                receiptNumber: item.receiptNo,
                category: item.category,
                orderTime: item.orderTime,
                payTime: item.payTime,
                productCode: item.productCode,
                productName: item.productName,
                quantity: item.quantity,
                totalPrice: item.totalPrice,
                discountPrice: item.discountAmount,
                actualPrice: item.netSales,
                cost: item.cost,
                vat: item.vat
            }))
        };

        await api.put('/receipt/upload', requestBody);

        toast.add({
            severity: 'success',
            summary: '저장 완료',
            detail: `${result.value.date} 일자의 영수증이 저장되었습니다.`,
            life: 5000
        });

        // 저장 후 달력 갱신
        loadCalendarSummary();
    } catch (e) {
        console.error(e);
        toast.add({
            severity: 'error',
            summary: '저장 실패',
            detail: '데이터 저장 중 오류가 발생했습니다.',
            life: 3000
        });
    }
}

function parseReceiptExcel(jsonData) {
    const dateLine = jsonData[0]['영수증별상세현황'];
    const date = dateLine.split('조회일자 : ')[1].split(',')[0].trim();

    const rows = jsonData.slice(2, jsonData.length - 1);

    const receiptList = rows
        .filter((r) => r['__EMPTY'] && r['__EMPTY'] !== '')
        .map((r) => {
            return {
                posNo: r['영수증별상세현황']?.trim() || '',
                receiptNo: r['__EMPTY']?.trim() || '',
                category: r['__EMPTY_1']?.trim() || '',
                orderTime: r['__EMPTY_3']?.trim() || '',
                payTime: r['__EMPTY_4']?.trim() || '',
                productCode: r['__EMPTY_5']?.trim() || '',
                productName: r['__EMPTY_7']?.trim() || '',
                quantity: Number(r['__EMPTY_8']?.replace(/\D/g, '') || 0),
                totalPrice: Number(r['__EMPTY_9']?.replace(/[^0-9]/g, '') || 0),
                discountAmount: Number(r['__EMPTY_12']?.replace(/[^0-9]/g, '') || 0),
                netSales: Number(r['__EMPTY_14']?.replace(/[^0-9]/g, '') || 0),
                cost: Number(r['__EMPTY_15']?.replace(/[^0-9]/g, '') || 0),
                vat: Number(r['__EMPTY_16']?.replace(/[^0-9]/g, '') || 0)
            };
        });

    return { date, receiptList };
}

/* ======================================================
 * 초기 로딩
 * ====================================================== */
onMounted(() => {
    loadCalendarSummary();
});
</script>

<template>
    <div class="card">
        <!-- ======================== -->
        <!--        업로드 달력        -->
        <!-- ======================== -->
        <h2 class="text-xl font-bold mb-4">📅 영수증 업로드 현황</h2>

        <FullCalendar
            :options="{
                plugins: [dayGridPlugin, interactionPlugin],
                initialView: 'dayGridMonth',
                locale: koLocale, // ⭐ 한글 적용!
                events: fetchEvents,
                datesSet: onMonthChange,
                height: 'auto'
            }"
        />

        <!-- ======================== -->
        <!--     엑셀 업로드 영역     -->
        <!-- ======================== -->
        <h2 class="text-xl font-bold mt-8 mb-4">📄 영수증 상세 업로드</h2>

        <input type="file" accept=".xlsx,.xls" @change="handleFileUpload" />

        <div v-if="result.date" class="mt-4 text-lg font-semibold">조회일자: {{ result.date }}</div>

        <DataTable
            v-if="result.receiptList.length > 0"
            :value="result.receiptList"
            class="mt-4"
            paginator
            :rows="10"
            responsiveLayout="scroll"
        >
            <Column field="posNo" header="포스번호" />
            <Column field="receiptNo" header="영수증번호" />
            <Column field="category" header="구분" />
            <Column field="orderTime" header="주문시각" />
            <Column field="payTime" header="결제시각" />
            <Column field="productCode" header="상품코드" />
            <Column field="productName" header="상품명" />
            <Column field="quantity" header="수량" />
            <Column field="totalPrice" header="매출액" />
            <Column field="discountAmount" header="할인액" />
            <Column field="netSales" header="실매출액" />
            <Column field="cost" header="가액" />
            <Column field="vat" header="부가세" />
        </DataTable>

        <Button label="DB 저장하기" class="mt-4" @click="saveToServer" :disabled="result.receiptList.length === 0" />
    </div>
</template>

<style>
/* 오늘 날짜 하이라이트 */
.fc .fc-daygrid-day.fc-day-today {
    background-color: #fff8e1 !important;
}
</style>
