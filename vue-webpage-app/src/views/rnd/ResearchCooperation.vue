<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">연구협력</h1>

        <!-- 이미지 표시 -->
        <div v-if="researchCooperation.imageUrl" class="mt-4">
            <img :src="getImageUrl(researchCooperation.imageUrl)" alt="연구협력 이미지"
                class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <!-- 텍스트 표시 -->
        <p class="mt-4 text-gray-700 text-lg">{{ researchCooperation.content }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            researchCooperation: {
                content: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/rnd/research-cooperation");
            this.researchCooperation = response.data; // DB 데이터 가져오기
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
