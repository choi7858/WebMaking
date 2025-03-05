<template>
    <Teleport to="body">
        <div v-if="visible" class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50"
            @click.self="closePopup">
            <div class="bg-white w-1/3 max-h-[500px] p-6 rounded-lg shadow-lg relative overflow-hidden flex flex-col">
                <button @click="closePopup" class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">
                    ✖
                </button>
                <h2 class="text-xl font-bold text-center mb-4">
                    사업현황 편집
                </h2>
                <div class="flex-grow overflow-y-auto px-2">
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">
                            이미지 업로드
                        </label>
                        <input type="file" @change="handleFileUpload" class="mt-1 w-full border p-2 rounded-md" />
                        <div v-if="previewImage" class="mt-2 flex justify-center">
                            <img :src="previewImage" alt="미리보기"
                                class="max-w-full max-h-40 rounded-md shadow object-contain" />
                        </div>
                    </div>
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">
                            사업현황 설명
                        </label>
                        <textarea v-model="businessStatusData.description" rows="4"
                            class="w-full border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                    </div>
                </div>
                <div class="flex justify-end mt-4">
                    <button @click="saveBusinessStatus"
                        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                        저장
                    </button>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import axios from "axios";

const props = defineProps({
    visible: { type: Boolean, default: false },
    existingData: { type: Object, default: null },
});
const emit = defineEmits(["close"]);

// 로컬 상태: 사업현황 데이터
const businessStatusData = reactive({
    description: "",
    imageUrl: "",
});
const file = ref(null);
const previewImage = ref("");

// 부모로부터 받은 existingData를 watch하여 업데이트
watch(
    () => props.existingData,
    (newValue) => {
        if (newValue) {
            businessStatusData.description = newValue.description || "";
            businessStatusData.imageUrl = newValue.imageUrl || "";
            previewImage.value = newValue.imageUrl
                ? `http://localhost:8080${newValue.imageUrl}`
                : "";
            console.log("EditBusinessStatus.vue: previewImage =>", previewImage.value);
        }
    },
    { immediate: true, deep: true }
);

// 파일 업로드 핸들러
function handleFileUpload(event) {
    const uploadedFile = event.target.files[0];
    if (!uploadedFile) return;
    file.value = uploadedFile;
    previewImage.value = URL.createObjectURL(uploadedFile);
    console.log("EditBusinessStatus.vue: File selected:", uploadedFile);
}

// 저장 함수: API 호출
async function saveBusinessStatus() {
    const formData = new FormData();
    formData.append("description", businessStatusData.description || "");
    if (file.value) {
        formData.append("file", file.value);
    }
    try {
        const response = await axios.post(
            "http://localhost:8080/api/business/business-status/save",
            formData,
            { headers: { "Content-Type": "multipart/form-data" } }
        );
        console.log("EditBusinessStatus.vue: save response:", response.data);
        alert("저장 완료!");
        emit("close");
    } catch (error) {
        console.error("저장 중 오류 발생:", error);
        alert("저장 중 오류가 발생했습니다.");
    }
}

function closePopup() {
    emit("close");
}
</script>

<style scoped>
.fixed {
    /* Tailwind CSS를 참고한 오버레이 스타일 */
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 9999;
    display: flex;
    justify-content: center;
    align-items: center;
}



.popup-container {
    background: #fff;
    width: 600px;
    max-width: 90%;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    position: relative;
}

.popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.popup-title {
    margin: 0;
    font-size: 1.25rem;
    font-weight: bold;
    color: #333;
}

.close-button {
    background: transparent;
    border: none;
    font-size: 1.3rem;
    cursor: pointer;
    color: #000;
}
</style>