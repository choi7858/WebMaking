<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">보유면허 · 인증서</h1>

        <div v-if="certificationsData.imageUrl" class="mt-4">
            <img :src="getImageUrl(certificationsData.imageUrl)" alt="보유면허 · 인증서 이미지"
                class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <p class="mt-4 text-gray-700 text-lg">{{ certificationsData.description }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            certificationsData: {
                description: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/certifications");
            this.certificationsData = response.data;
        } catch (error) {
            console.error("데이터 불러오기 오류:", error);
        }
    },
    methods: {
        getImageUrl(imageUrl) {
            if (!imageUrl) return "";
            return `http://localhost:8080${imageUrl}`;
        }
    }
};
</script>
