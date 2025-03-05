<template>
    <div class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
        <div class="bg-white w-1/3 max-h-[500px] p-6 rounded-lg shadow-lg relative overflow-hidden flex flex-col">
            <button @click="closePopup"
                class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">✖</button>

            <h2 class="text-xl font-bold text-center mb-4">SSILS 전원공급기 편집</h2>

            <div class="flex-grow overflow-y-auto px-2">
                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700">이미지 업로드</label>
                    <input type="file" @change="handleFileUpload" class="mt-1 w-full border p-2 rounded-md" />
                    <div v-if="previewImage" class="mt-2 flex justify-center">
                        <img :src="previewImage" alt="미리보기"
                            class="max-w-full max-h-40 rounded-md shadow object-contain" />
                    </div>
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700">설명</label>
                    <textarea v-model="ssilsData.description" rows="4"
                        class="w-full border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                </div>
            </div>

            <div class="flex justify-end mt-4">
                <button @click="saveSSILS"
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
            ssilsData: {
                description: "",
                imageUrl: "",
            },
            file: null,
            previewImage: "",
        };
    },
    watch: {
        existingData: {
            immediate: true,
            handler(newValue) {
                if (newValue) {
                    this.ssilsData = { ...newValue };
                    this.previewImage = newValue.imageUrl ? `http://localhost:8080${newValue.imageUrl}` : "";
                }
            },
        },
    },
    methods: {
        handleFileUpload(event) {
            const file = event.target.files[0];
            if (!file) return;

            this.file = file;
            this.previewImage = URL.createObjectURL(file);
        },

        async saveSSILS() {
            const formData = new FormData();
            formData.append("description", this.ssilsData.description || "");

            if (this.file) {
                formData.append("file", this.file);
            }

            try {
                await axios.post("http://localhost:8080/api/rnd/ssils/save", formData, {
                    headers: { "Content-Type": "multipart/form-data" },
                });

                alert("저장 완료!");
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
