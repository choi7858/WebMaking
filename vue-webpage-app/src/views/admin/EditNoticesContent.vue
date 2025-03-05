<template>
    <div class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
        <div class="bg-white w-1/3 max-h-[500px] p-6 rounded-lg shadow-lg relative overflow-hidden flex flex-col">
            <button @click="closePopup"
                class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">✖</button>

            <h2 class="text-xl font-bold text-center mb-4">공지사항 편집</h2>

            <div class="flex-grow overflow-y-auto px-2">
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700">공지사항 제목</label>
                    <input v-model="notices.title" type="text" class="border p-2 w-full rounded-md" />
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700">공지사항 내용</label>
                    <textarea v-model="notices.content" rows="4"
                        class="w-full border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                </div>
            </div>

            <div class="flex justify-end mt-4">
                <button @click="saveNotices"
                    class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">저장</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    props: {
        existingData: Object,
    },
    data() {
        return {
            notices: {
                title: "",
                content: "",
            },
        };
    },
    watch: {
        existingData: {
            immediate: true,
            handler(newValue) {
                if (newValue) {
                    this.notices = { ...newValue };
                }
            },
        },
    },
    methods: {
        async saveNotices() {
            try {
                await axios.post("http://localhost:8080/api/notices/save", this.notices);
                alert("공지사항이 저장되었습니다.");
                this.$emit("close");
            } catch (error) {
                console.error("저장 중 오류 발생:", error);
                alert("저장 중 오류가 발생했습니다.");
            }
        },

        closePopup() {
            this.$emit("close");
        },
    },
};
</script>
