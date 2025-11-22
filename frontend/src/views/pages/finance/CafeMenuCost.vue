<script setup>
import api from '@/api/axios';
import { FilterMatchMode } from '@primevue/core/api';
import { onBeforeMount, ref } from 'vue';

const 카페메뉴원가목록 = ref(null);

const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
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

function getCostRateSeverity(value) {
    const number = Number(value);

    if (number >= 40) {
        return 'danger'; // 빨강
    }
    if (number >= 30) {
        return 'warn'; // 주황
    }
    if (number >= 20) {
        return 'success'; // 초록
    }
    return 'secondary'; // 회색
}

onBeforeMount(async () => {
    const response = await api.get('/cafe-menu-costs');
    카페메뉴원가목록.value = response?.data?.data || [];
});
</script>

<template>
    <div class="card">
        <DataTable :value="카페메뉴원가목록" :filters="filters" class="mt-6">
            <template #header>
                <div class="flex flex-wrap gap-2 items-center justify-between">
                    <h4 class="m-0">카페메뉴원가목록</h4>
                    <IconField>
                        <InputIcon>
                            <i class="pi pi-search" />
                        </InputIcon>
                        <InputText v-model="filters['global'].value" placeholder="Search..." />
                    </IconField>
                </div>
            </template>
            <Column field="menuName" header="메뉴" sortable style="min-width: 180px" frozen class="font-bold"></Column>
            <Column field="temperatureType" header="온도" sortable style="min-width: 80px">
                <template #body="{ data }">
                    <Tag :value="data.temperatureType" :severity="getTemperatureTypeSeverity(data.temperatureType)" />
                </template>
            </Column>
            <Column field="sizeType" header="사이즈" sortable style="min-width: 80px">
                <template #body="{ data }">
                    <Tag :value="data.sizeType" :severity="getSizeTypeSeverity(data.sizeType)" />
                </template>
            </Column>
            <Column field="recipe" header="레시피" sortable style="min-width: 250px"></Column>
            <Column field="totalCost" header="판매가" sortable style="min-width: 150px"></Column>
            <Column field="price" header="가격" sortable style="min-width: 150px"></Column>
            <Column field="costRate" header="원가율(%)" sortable style="min-width: 100px">
                <template #body="{ data }">
                    <Tag :value="data.costRate" :severity="getCostRateSeverity(data.costRate)" />
                </template>
            </Column>
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
