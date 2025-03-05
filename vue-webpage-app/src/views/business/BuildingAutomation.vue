<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">빌딩자동제어</h1>

        <!-- 이미지 표시 -->
        <div v-if="buildingAutomation.imageUrl" class="mt-4">
            <img :src="getImageUrl(buildingAutomation.imageUrl)" alt="빌딩자동제어 이미지"
                class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <!-- 텍스트 표시 -->
        <p class="mt-4 text-gray-700 text-lg">{{ buildingAutomation.description }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            buildingAutomation: {
                description: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/business/building-automation");
            this.buildingAutomation = response.data; // DB 데이터 가져오기
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
