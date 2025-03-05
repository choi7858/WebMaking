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
                <h2 class="text-xl font-bold text-center mb-4">빌딩자동제어 편집</h2>

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
                        <textarea v-model="buildingAutomationData.description" rows="4"
                            class="w-full border p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                    </div>
                </div>

                <!-- 하단 저장 버튼 -->
                <div class="flex justify-end mt-4">
                    <button @click="saveBuildingAutomation"
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

/**
 * 부모(AdminNavbar.vue)에서 :visible="true" :existingData="buildingAutomationItem"
 * 예) <EditBuildingAutomation
 *       :visible="isPopupOpen"
 *       :existingData="fetchedData"
 *       @close="closePopup"
 *     />
 */
const props = defineProps({
    // 팝업 표시 여부
    visible: { type: Boolean, default: false },
    // DB에서 불러온 기존 데이터 (예: { description: "", imageUrl: "/uploads/xx.jpg" })
    existingData: { type: Object, default: null },
});

// 팝업 닫기를 부모에게 알리는 이벤트
const emit = defineEmits(["close"]);

// (1) 내부에서 관리할 빌딩자동제어 데이터
const buildingAutomationData = reactive({
    description: "",
    imageUrl: "",
});

// 업로드할 파일과 미리보기 URL
const file = ref(null);
const previewImage = ref("");

/**
 * (2) 부모로부터 existingData가 들어오면, 로컬 상태를 업데이트
 *     ex) { description: "...", imageUrl: "/uploads/building_auto.jpg" }
 */
watch(
    () => props.existingData,
    (newVal) => {
        console.log("EditBuildingAutomation.vue: existingData changed:", newVal);
        if (newVal) {
            buildingAutomationData.description = newVal.description || "";
            buildingAutomationData.imageUrl = newVal.imageUrl || "";
            // DB 이미지가 "/uploads/xxx.jpg"라면 최종 URL => "http://localhost:8080/uploads/xxx.jpg"
            previewImage.value = newVal.imageUrl
                ? "http://localhost:8080" + newVal.imageUrl
                : "";
            console.log(
                "EditBuildingAutomation.vue: previewImage =>",
                previewImage.value
            );
        }
    },
    { immediate: true, deep: true }
);

/**
 * (3) 파일 업로드 시, 새 미리보기
 */
function handleFileUpload(event) {
    const uploadedFile = event.target.files[0];
    if (!uploadedFile) return;
    file.value = uploadedFile;
    previewImage.value = URL.createObjectURL(uploadedFile);
    console.log("EditBuildingAutomation.vue: File selected:", uploadedFile);
}

/**
 * (4) DB 저장 로직
 */
async function saveBuildingAutomation() {
    const formData = new FormData();
    formData.append("description", buildingAutomationData.description || "");
    if (file.value) {
        formData.append("file", file.value);
    }

    try {
        // 실제 API 경로 예: http://localhost:8080/api/business/building-automation/save
        const response = await axios.post(
            "http://localhost:8080/api/business/building-automation/save",
            formData,
            { headers: { "Content-Type": "multipart/form-data" } }
        );
        console.log("saveBuildingAutomation => response:", response.data);
        alert("저장 완료!");
        // 저장 후 팝업 닫기
        emit("close");
    } catch (error) {
        console.error("저장 중 오류 발생:", error);
        alert("저장 중 오류가 발생했습니다.");
    }
}

/**
 * (5) 팝업 닫기
 */
function closePopup() {
    emit("close");
}
</script>

<style scoped>
/* 팝업 스타일 (상하수처리와 동일) */
.popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 9999;
    display: flex;
    align-items: center;
    justify-content: center;
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