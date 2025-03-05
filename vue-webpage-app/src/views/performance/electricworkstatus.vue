<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">전기공사현황</h1>

        <div v-if="electricWorkStatus.imageUrl" class="mt-4">
            <img :src="getImageUrl(electricWorkStatus.imageUrl)" alt="전기공사현황 이미지"
                class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <p class="mt-4 text-gray-700 text-lg">{{ electricWorkStatus.content }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            electricWorkStatus: {
                content: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/performance/electric-work-status");
            this.electricWorkStatus = response.data;
        } catch (error) {
            console.error("데이터를 불러오는 중 오류 발생:", error);
        }
    },
    methods: {
        getImageUrl(imageUrl) {
            return imageUrl ? `http://localhost:8080${imageUrl}` : "";
        }
    }
};
</script>
