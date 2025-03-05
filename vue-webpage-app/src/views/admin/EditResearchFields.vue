<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold text-center mb-8">R&D → 연구분야 편집</h1>

        <div class="grid grid-cols-2 gap-6">
            <div v-for="(sub, index) in submenus" :key="index" class="border p-6 rounded-lg shadow-md relative">
                <div class="flex justify-between items-center">
                    <h2 class="text-xl font-bold cursor-pointer" @click="openEditContent(sub)">
                        {{ sub.label }}
                    </h2>
                </div>
            </div>
        </div>

        <div class="flex justify-center mt-8">
            <button @click="goBack" class="px-6 py-3 bg-gray-600 text-white rounded-lg hover:bg-gray-700">뒤로가기</button>
        </div>

        <!-- 편집 팝업 -->
        <Teleport to="body">
            <div v-if="isEditContentOpen"
                class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
                <component :is="selectedComponent" :existingData="selectedMenuData" @close="closeEditContent" />
            </div>
        </Teleport>
    </div>
</template>

<script setup>
import { ref, markRaw } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import EditElectronicProtection from "@/views/admin/EditElectronicProtection.vue";
import EditElectricProtection from "@/views/admin/EditElectricProtection.vue";
import EditCoilLift from "@/views/admin/EditCoilLift.vue";
import EditSSILS from "@/views/admin/EditSSILS.vue";
import EditCalibrator from "@/views/admin/EditCalibrator.vue";
import EditSCADA from "@/views/admin/EditSCADA.vue";

const router = useRouter();

const submenus = ref([
    { label: "전자식 방호장치", apiPath: "/api/rnd/electronic-protection", component: markRaw(EditElectronicProtection) },
    { label: "전기식 방호장치", apiPath: "/api/rnd/electric-protection", component: markRaw(EditElectricProtection) },
    { label: "코일리프트", apiPath: "/api/rnd/coil-lift", component: markRaw(EditCoilLift) },
    { label: "SSILS 전원공급기", apiPath: "/api/rnd/ssils", component: markRaw(EditSSILS) },
    { label: "다기능 캘리브레이터", apiPath: "/api/rnd/calibrator", component: markRaw(EditCalibrator) },
    { label: "SCADA HMI 일체형 PLC", apiPath: "/api/rnd/scada", component: markRaw(EditSCADA) },
]);

const isEditContentOpen = ref(false);
const selectedMenuData = ref({});
const selectedComponent = ref(null);

const openEditContent = async (menu) => {
    if (menu.apiPath) {
        try {
            const response = await axios.get(`http://localhost:8080${menu.apiPath}`);
            selectedMenuData.value = response.data;
        } catch (error) {
            console.error("데이터 불러오기 오류:", error);
            alert("데이터를 불러오는 중 오류가 발생했습니다.");
        }
        selectedComponent.value = menu.component;
        isEditContentOpen.value = true;
    } else {
        alert("편집할 내용이 없습니다.");
    }
};

const closeEditContent = () => {
    isEditContentOpen.value = false;
};

const goBack = () => {
    router.push("/admin/edit/header/rnd");
};
</script>
