<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold text-center mb-8">네비게이션 바 편집</h1>

        <div class="grid grid-cols-3 gap-6">
            <div v-for="(menu, index) in menus" :key="index" class="border p-6 rounded-lg shadow-md relative">
                <div class="flex justify-between items-center">
                    <h2 class="text-xl font-bold cursor-pointer" @click="handleMenuClick(menu)">
                        {{ menu.label }}
                    </h2>
                    <button @click="openEditPopup(index)" class="text-gray-500 hover:text-blue-600">
                        ✏️
                    </button>
                </div>
            </div>
        </div>

        <div class="flex justify-center mt-8">
            <button @click="goBack" class="px-6 py-3 bg-gray-600 text-white rounded-lg hover:bg-gray-700">
                뒤로가기
            </button>
        </div>

        <!-- 메뉴 이름 변경 팝업 -->
        <div v-if="isPopupOpen" class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 class="text-xl font-bold mb-4">메뉴 이름 수정</h2>
                <input v-model="menuToEdit.label" class="border p-2 w-full rounded-md" />
                <div class="flex justify-end mt-4">
                    <button @click="saveEdit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                        저장
                    </button>
                    <button @click="closePopup"
                        class="px-4 py-2 bg-gray-500 text-white rounded-lg ml-2 hover:bg-gray-600">
                        취소
                    </button>
                </div>
            </div>
        </div>

        <!-- 공지사항 편집 팝업 -->
        <Teleport to="#app">
            <div v-if="isEditNoticesOpen"
                class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
                <EditNotices @close="closeEditNoticesPopup" />
            </div>
        </Teleport>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import EditNotices from "@/views/admin/EditNotices.vue";

const router = useRouter();

const menus = ref([
    { label: "회사소개", route: "/admin/edit/header/company" },
    { label: "사업분야", route: "/admin/edit/header/business" },
    { label: "R&D", route: "/admin/edit/header/rnd" },
    { label: "실적현황", route: "/admin/edit/header/performance" },
    { label: "공지사항", route: "popup" }, // 공지사항은 팝업으로 띄우기
]);

const isPopupOpen = ref(false);
const menuToEdit = ref({});
const editIndex = ref(null);
const isEditNoticesOpen = ref(false);

const openEditPopup = (index) => {
    editIndex.value = index;
    menuToEdit.value = { ...menus.value[index] };
    isPopupOpen.value = true;
};

const saveEdit = () => {
    menus.value[editIndex.value].label = menuToEdit.value.label;
    isPopupOpen.value = false;
};

const closePopup = () => {
    isPopupOpen.value = false;
};

// 🔹 공지사항 클릭 시 팝업 창 열기
const handleMenuClick = (menu) => {
    if (menu.route === "popup") {
        isEditNoticesOpen.value = true;
    } else {
        router.push(menu.route);
    }
};

// 🔹 팝업 닫기 기능
const closeEditNoticesPopup = () => {
    isEditNoticesOpen.value = false;
};

// 🔹 뒤로가기
const goBack = () => {
    router.push("/admin/edit/header");
};
</script>
