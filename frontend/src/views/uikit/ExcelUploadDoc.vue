<script setup>
import { ref } from 'vue';
import * as XLSX from 'xlsx';

const jsonData = ref([]);

function handleFileUpload(event) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (e) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });

        // 첫 번째 시트 선택
        const sheetName = workbook.SheetNames[0];
        const sheet = workbook.Sheets[sheetName];

        // 시트 → JSON 변환
        jsonData.value = XLSX.utils.sheet_to_json(sheet, {
            defval: '', // 빈 셀은 "" 로 처리
            raw: false // 날짜 등을 파싱해서 일반 문자열로 변환
        });
    };

    reader.readAsArrayBuffer(file);
}
</script>

<template>
    <div>
        <h2>Excel 업로드</h2>
        <input type="file" accept=".xlsx,.xls" @change="handleFileUpload" />

        <pre>{{ jsonData }}</pre>
    </div>
</template>
