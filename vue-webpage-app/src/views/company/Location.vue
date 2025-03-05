<template>
    <div class="p-8">
        <h1 class="text-3xl font-bold">찾아오시는 길</h1>

        <!-- 이미지 표시 -->
        <div v-if="locationData.imageUrl" class="mt-4">
            <img :src="getImageUrl(locationData.imageUrl)" alt="위치 이미지" class="w-1/2 mx-auto rounded-lg shadow-md" />
        </div>

        <!-- 텍스트 표시 -->
        <p class="mt-4 text-gray-700 text-lg">{{ locationData.description }}</p>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            locationData: {
                description: "",
                imageUrl: "",
            },
        };
    },
    async created() {
        try {
            const response = await axios.get("http://localhost:8080/api/location");
            this.locationData = response.data;
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
