<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold text-center mb-8">헤더 편집</h1>

        <div class="grid grid-cols-2 gap-6">
            <!-- 로고 편집 -->
            <div class="border p-6 rounded-lg shadow-md cursor-pointer hover:shadow-lg">
                <h2 class="text-xl font-bold">로고 편집</h2>
                <input type="file" @change="handleLogoUpload" class="mt-4" />
                <img v-if="logoUrl" :src="logoUrl" alt="로고 미리보기" class="w-32 h-32 mt-4 object-contain border" />
            </div>

            <!-- 네비게이션 바 편집 (클릭 시 네비게이션 편집 페이지로 이동) -->
            <div @click="goToNavEdit" class="border p-6 rounded-lg shadow-md cursor-pointer hover:shadow-lg">
                <h2 class="text-xl font-bold">네비게이션 바 편집</h2>
                <p class="text-gray-600 mt-2">메뉴 이름 및 내용을 수정할 수 있습니다.</p>
            </div>
        </div>

        <div class="flex justify-center mt-8">
            <button @click="saveChanges"
                class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700">저장</button>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const logoUrl = ref(null);

const handleLogoUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
        logoUrl.value = URL.createObjectURL(file);
    }
};

const goToNavEdit = () => {
    router.push("/admin/edit/header/nav");
};

const saveChanges = () => {
    console.log("저장된 데이터:", { logo: logoUrl.value });
    alert("헤더 변경사항이 저장되었습니다!");
    router.push("/admin/edit?type=company");
};
</script>