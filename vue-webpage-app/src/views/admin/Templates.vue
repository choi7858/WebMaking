<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold text-center mb-8">웹사이트 템플릿 선택</h1>
        <div class="grid grid-cols-3 gap-8">
            <TemplateCard v-for="template in templates" :key="template.id" :template="template"
                @select="selectTemplate" />
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import TemplateCard from "@/components/TemplateCard.vue";
import { useRouter } from "vue-router";

const router = useRouter();

const templates = ref([
    { id: 1, name: "회사 홈페이지", description: "회사 소개 및 사업 페이지", route: "/admin/edit?type=company" },
    { id: 2, name: "홈쇼핑", description: "상품 판매를 위한 쇼핑몰", route: "/admin/edit?type=shopping" },
    { id: 3, name: "여행사", description: "여행 상품 및 예약 서비스", route: "/admin/edit?type=travel" },
    { id: 4, name: "블로그", description: "개인 및 기업 블로그", route: "/admin/edit?type=blog" },
]);

const selectTemplate = (template) => {
    router.push(template.route);
};
</script>