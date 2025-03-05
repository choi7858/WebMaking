<template>
    <Teleport to="body">
        <!-- visible이 true일 때만 팝업 표시 -->
        <div v-if="visible" class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50"
            @click.self="closePopup">
            <!-- 팝업 박스 -->
            <div class="bg-white w-1/3 max-h-[500px] p-6 rounded-lg shadow-lg relative overflow-hidden flex flex-col">
                <!-- 닫기 버튼 -->
                <button @click="closePopup" class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">
                    ✖
                </button>

                <!-- 팝업 타이틀 -->
                <h2 class="text-xl font-bold text-center mb-4">
                    터널원격 감시제어 편집
                </h2>

                <!-- 본문: 이미지 업로드, 설명 입력 -->
                <div class="flex-grow overflow-y-auto px-2">
                    <!-- 이미지 업로드 영역 -->
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

                    <!-- 설명 입력 영역 -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">
                            설명
                        </label>
                        <textarea v-model="tunnelMonitoringData.description" rows="4"
                            class="w-full border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                    </div>
                </div>

                <!-- 하단 저장 버튼 -->
                <div class="flex justify-end mt-4">
                    <button @click="saveTunnelMonitoring"
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

const tunnelMonitoringData = reactive({
    description: "",
    imageUrl: "",
});
const file = ref(null);
const previewImage = ref("");

// 부모로부터 전달받은 기존 데이터가 있으면 로컬 상태 업데이트
watch(
    () => props.existingData,
    (newVal) => {
        console.log("EditTunnelMonitoring.vue: existingData changed:", newVal);
        if (newVal) {
            tunnelMonitoringData.description = newVal.description || "";
            tunnelMonitoringData.imageUrl = newVal.imageUrl || "";
            previewImage.value = newVal.imageUrl
                ? `http://localhost:8080${newVal.imageUrl}`
                : "";
            console.log("EditTunnelMonitoring.vue: previewImage =>", previewImage.value);
        }
    },
    { immediate: true, deep: true }
);

function handleFileUpload(event) {
    const uploadedFile = event.target.files[0];
    if (!uploadedFile) return;
    file.value = uploadedFile;
    previewImage.value = URL.createObjectURL(uploadedFile);
    console.log("EditTunnelMonitoring.vue: File selected:", uploadedFile);
}

async function saveTunnelMonitoring() {
    const formData = new FormData();
    formData.append("description", tunnelMonitoringData.description || "");
    if (file.value) {
        formData.append("file", file.value);
    }
    try {
        const response = await axios.post(
            "http://localhost:8080/api/business/tunnel-monitoring/save",
            formData,
            { headers: { "Content-Type": "multipart/form-data" } }
        );
        console.log("EditTunnelMonitoring.vue: save response:", response.data);
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
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
}

.flex.items-center {
    display: flex;
    align-items: center;
}

.bg-gray-900.bg-opacity-50 {
    background-color: rgba(0, 0, 0, 0.5);
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
</style>