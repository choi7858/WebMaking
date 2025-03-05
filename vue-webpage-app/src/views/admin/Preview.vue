<template>
    <div class="min-h-screen flex flex-col">
        <!-- 웹사이트 헤더 -->
        <header class="bg-blue-600 text-white p-4 shadow-md">
            <h1 class="text-2xl font-bold">{{ header }}</h1>
        </header>

        <!-- 메인 컨텐츠 -->
        <main class="flex-grow p-8">
            <p class="text-lg">{{ main }}</p>
        </main>

        <!-- 웹사이트 푸터 -->
        <footer class="bg-gray-800 text-white p-4 text-center">
            {{ footer }}
        </footer>

        <!-- 관리자 페이지로 돌아가기 -->
        <div class="flex justify-center mt-8">
            <router-link to="/admin/edit" class="px-4 py-2 bg-gray-600 text-white rounded">뒤로 가기</router-link>
        </div>
    </div>
</template>

<script setup>
import { useRoute } from "vue-router";

const route = useRoute();

const header = route.query.header || "기본 헤더";
const main = route.query.main || "기본 메인 컨텐츠";
const footer = route.query.footer || "기본 푸터";
</script>