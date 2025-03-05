<template>
  <div class="flex flex-col min-h-screen">
    <!-- 헤더 (admin 메인 페이지에서는 숨김) -->
    <header v-if="showHeader" class="bg-white shadow-md">
      <div class="container mx-auto flex justify-between items-center p-4">
        <NavbarAdmin v-if="isAdminEditPage" />
        <Navbar v-else />
      </div>
    </header>

    <!-- 페이지 컨텐츠 -->
    <main class="flex-grow p-8">
      <RouterView />
    </main>

    <!-- 푸터 (admin 메인 페이지에서는 숨김) -->
    <footer v-if="showFooter" class="bg-gray-800 text-white p-4 text-center">
      <AdminFooter v-if="isAdminEditPage" />
      <UserFooter v-else />
    </footer>
  </div>
</template>

<script setup>
import Navbar from "@/components/Navbar.vue";
import NavbarAdmin from "@/components/NavbarAdmin.vue";
import AdminFooter from "@/components/AdminFooter.vue";
import UserFooter from "@/components/Footer.vue";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

// `admin` 페이지에서는 헤더/푸터 숨기고, `admin/edit` 페이지부터 보이게 설정
const isAdminPage = computed(() => route.path.startsWith("/admin"));
const isAdminEditPage = computed(() => route.path.startsWith("/admin/edit"));
const showHeader = computed(() => !route.path.startsWith("/admin") || isAdminEditPage.value);
const showFooter = computed(() => !route.path.startsWith("/admin") || isAdminEditPage.value);
</script>
