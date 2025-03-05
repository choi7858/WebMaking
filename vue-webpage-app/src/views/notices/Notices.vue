<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold text-center mb-8">공지사항</h1>
        <table class="w-full border-collapse border border-gray-300 shadow-lg">
            <thead>
                <tr class="bg-gray-200">
                    <th class="border p-2">No.</th>
                    <th class="border p-2">제목</th>
                    <th class="border p-2">작성자</th>
                    <th class="border p-2">작성일</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(notice, index) in notices" :key="notice.id" class="hover:bg-gray-100 transition-colors">
                    <td class="border p-2 text-center">{{ index + 1 }}</td>
                    <td class="border p-2">
                        <button @click="openPopup(notice)" class="text-blue-600 hover:underline font-medium">
                            {{ notice.title }}
                        </button>
                    </td>
                    <td class="border p-2 text-center">{{ notice.author }}</td>
                    <td class="border p-2 text-center">
                        {{ formatDate(notice.createdAt) }}
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- Modal Popup -->
        <div v-if="selectedNotice" class="fixed inset-0 flex items-center justify-center z-50">
            <!-- Overlay -->
            <div class="absolute inset-0 bg-black opacity-50" @click="closePopup"></div>
            <!-- Modal Content -->
            <div class="bg-white rounded-lg shadow-2xl z-50 max-w-lg w-full p-6 relative animate-fade-in">
                <button @click="closePopup" class="absolute top-2 right-2 text-gray-600 hover:text-gray-800 text-xl">
                    ✖
                </button>
                <h2 class="text-2xl font-bold mb-4">{{ selectedNotice.title }}</h2>
                <p class="mb-4 text-gray-700" v-html="selectedNotice.content"></p>
                <div class="text-sm text-gray-500">
                    작성자: <span class="font-medium">{{ selectedNotice.author }}</span>
                </div>
                <div class="text-sm text-gray-500">
                    작성일: <span class="font-medium">{{ formatDate(selectedNotice.createdAt) }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            notices: [],
            selectedNotice: null,
        };
    },
    mounted() {
        this.fetchNotices();
    },
    methods: {
        async fetchNotices() {
            try {
                const response = await axios.get("http://localhost:8080/api/notice/list");
                // API 응답이 Page 객체라면 content 배열을 사용하도록 처리합니다.
                this.notices = response.data.content || response.data;
            } catch (error) {
                console.error("공지사항 목록 불러오기 실패:", error);
            }
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString("ko-KR");
        },
        openPopup(notice) {
            this.selectedNotice = notice;
        },
        closePopup() {
            this.selectedNotice = null;
        },
    },
};
</script>

<style scoped>
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: scale(0.95);
    }

    to {
        opacity: 1;
        transform: scale(1);
    }
}

.animate-fade-in {
    animation: fadeIn 0.3s ease-out;
}
</style>