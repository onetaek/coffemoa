<script setup>
import api from '@/api/axios';
import { onBeforeMount, ref } from 'vue';

const 카페메뉴원가목록 = ref(null);
onBeforeMount(async () => {
    const response = await api.get('/cafe-menu-costs');
    카페메뉴원가목록.value = response?.data?.data || [];
});
</script>

<template>
    <div class="card">
        <div class="font-semibold text-xl mb-4">카페메뉴원가목록</div>

        <DataTable :value="카페메뉴원가목록" class="mt-6">
            <Column field="menuName" header="메뉴" style="min-width: 180px" frozen class="font-bold"></Column>
            <Column field="temperatureType" header="온도" style="min-width: 80px"></Column>
            <Column field="sizeType" header="사이즈" style="min-width: 80px"></Column>
            <Column field="recipe" header="레시피" style="min-width: 250px"></Column>
            <Column field="totalCost" header="판매가" style="min-width: 150px"></Column>
            <Column field="price" header="가격" style="min-width: 150px"></Column>
            <Column field="costRate" header="원가율(%)" style="min-width: 100px"></Column>
        </DataTable>
    </div>
</template>

<style scoped lang="scss">
:deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
