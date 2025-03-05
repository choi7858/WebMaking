<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">회사연혁</h1>

        <!-- 연혁 이미지 표시 -->
        <div v-if="historyData.imageUrl" class="mt-4">
            <img :src="getImageUrl(historyData.imageUrl)" alt="회사연혁 이미지" class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <!-- 연혁 설명 표시 -->
        <p class="mt-4 text-gray-700 text-lg">{{ historyData.description }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            historyData: {
                description: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/history");
            this.historyData = response.data; // DB 데이터 가져오기
        } catch (error) {
            console.error("데이터를 불러오는 중 오류 발생:", error);
        }
    },
    methods: {
        getImageUrl(imageUrl) {
            if (!imageUrl) return ""; // 이미지가 없으면 빈 값 반환
            return `http://localhost:8080${imageUrl}`;
        }
    }
};
</script>
