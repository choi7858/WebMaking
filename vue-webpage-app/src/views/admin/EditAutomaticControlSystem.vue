<template>
    <div class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
        <div class="bg-white w-3/4 max-h-[80vh] p-6 rounded-lg shadow-lg relative overflow-hidden flex flex-col">
            <button @click="closePopup"
                class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">✖</button>

            <h2 class="text-xl font-bold text-center mb-4 text-gray-800">자동제어시스템 편집</h2>

            <div class="flex-grow overflow-y-auto px-2">
                <!-- 입력 폼 -->
                <form @submit.prevent="submitForm" class="bg-white p-5 rounded-lg shadow-md mb-6">
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700 mb-1">분류</label>
                        <input v-model="formData.type" type="text" placeholder="분류 입력 (e.g., 상,하수처리 설비)" required
                            class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700 mb-1">계약명</label>
                        <input v-model="formData.contractName" type="text" placeholder="계약명 입력" required
                            class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700 mb-1">제작 및 설치기간</label>
                        <input v-model="formData.manufactureYear" type="text" placeholder="설치기간 입력 (e.g., 1년)" required
                            class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700 mb-1">계약처 및 수요처</label>
                        <input v-model="formData.client" type="text" placeholder="계약처 입력" required
                            class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <button type="submit"
                        class="w-full py-3 text-lg font-semibold text-white bg-blue-600 rounded-md hover:bg-blue-700 transition-colors duration-200">업로드</button>
                </form>

                <!-- 필터 버튼 -->
                <h3 class="text-lg font-semibold text-center mb-4 text-gray-800">분류별 보기</h3>
                <div class="mb-6 flex flex-wrap justify-center gap-2.5">
                    <button v-for="type in filterTypes" :key="type" @click="filterByType(type)"
                        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-200">
                        {{ type }}
                    </button>
                </div>

                <!-- 게시글 목록 -->
                <h3 class="text-lg font-semibold text-center mb-4 text-gray-800">게시글 목록</h3>
                <table class="w-full mb-6 border-collapse">
                    <thead>
                        <tr class="bg-gray-100">
                            <th class="p-3 border border-gray-300 text-left">No.</th>
                            <th class="p-3 border border-gray-300 text-left">분류</th>
                            <th class="p-3 border border-gray-300 text-left">계약명</th>
                            <th class="p-3 border border-gray-300 text-left">제작 및 설치기간</th>
                            <th class="p-3 border border-gray-300 text-left">계약처 및 수요처</th>
                            <th class="p-3 border border-gray-300 text-left">수정</th>
                            <th class="p-3 border border-gray-300 text-left">삭제</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-if="filteredSystems.length === 0" class="text-center">
                            <td colspan="7" class="p-4 text-gray-500">데이터가 없습니다.</td>
                        </tr>
                        <tr v-for="(system, index) in filteredSystems" :key="system.id"
                            class="border-b border-gray-200">
                            <td class="p-3 border border-gray-300">{{ index + 1 + currentPage * pageSize }}</td>
                            <td class="p-3 border border-gray-300">{{ system.type || "미분류" }}</td>
                            <td class="p-3 border border-gray-300">{{ system.contractName }}</td>
                            <td class="p-3 border border-gray-300">{{ system.manufactureYear }}</td>
                            <td class="p-3 border border-gray-300">{{ system.client }}</td>
                            <td class="p-3 border border-gray-300">
                                <button @click="editSystem(system)"
                                    class="px-3 py-1.5 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors duration-200">수정</button>
                            </td>
                            <td class="p-3 border border-gray-300">
                                <button @click="deleteSystem(system.id)"
                                    class="px-3 py-1.5 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors duration-200">삭제</button>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!-- 수정 폼 (팝업) -->
                <div v-if="isEditOpen" class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
                    <div class="bg-white w-1/2 max-w-md p-6 rounded-lg shadow-lg relative">
                        <button @click="isEditOpen = false"
                            class="absolute top-3 right-3 text-gray-500 hover:text-red-600 text-lg">✖</button>
                        <h3 class="text-lg font-bold text-center mb-4 text-gray-800">데이터 수정</h3>
                        <form @submit.prevent="updateSystem" class="space-y-4">
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">분류</label>
                                <input v-model="editFormData.type" type="text" required
                                    class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">계약명</label>
                                <input v-model="editFormData.contractName" type="text" required
                                    class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">제작 및 설치기간</label>
                                <input v-model="editFormData.manufactureYear" type="text" required
                                    class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">계약처 및 수요처</label>
                                <input v-model="editFormData.client" type="text" required
                                    class="w-full border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <button type="submit"
                                class="w-full py-3 text-lg font-semibold text-white bg-blue-600 rounded-md hover:bg-blue-700 transition-colors duration-200">수정
                                완료</button>
                        </form>
                    </div>
                </div>

                <!-- 페이지네이션 -->
                <div class="flex justify-center items-center gap-4 mb-6">
                    <button @click="prevPage" :disabled="currentPage === 0"
                        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-300 transition-colors duration-200">이전</button>
                    <span class="text-sm text-gray-700">페이지 {{ currentPage + 1 }} / {{ totalPages }}</span>
                    <button @click="nextPage" :disabled="currentPage >= totalPages - 1"
                        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-300 transition-colors duration-200">다음</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios"; // axios 직접 사용

const props = defineProps({
    existingData: {
        type: Object,
        default: null,
    },
});

const emit = defineEmits(["close"]);

// 폼 데이터
const formData = ref({
    type: "",
    contractName: "",
    manufactureYear: "",
    client: "",
});

// 수정 폼 데이터
const isEditOpen = ref(false);
const editFormData = ref({
    id: null,
    type: "",
    contractName: "",
    manufactureYear: "",
    client: "",
});

// 시스템 목록 및 필터
const systems = ref([]);
const filter = ref("전체");
const filterTypes = ref([
    "상,하수처리 설비",
    "방재설비 배수,홍수,지하차도",
    "철도 및 도로시설 터널,지하철,교량,철도",
    "빌딩자동제어",
    "기타 플랜트 설비",
    "전체",
]);
const currentPage = ref(0);
const pageSize = ref(10);
const totalPages = ref(0);

const filteredSystems = computed(() => {
    if (!systems.value || systems.value.length === 0) return [];
    if (filter.value === "전체") return systems.value.filter(system => system.type || system.type === "미분류");
    return systems.value.filter((system) => system.type === filter.value);
});

onMounted(() => {
    loadSystems();
});

async function loadSystems() {
    const API_URL = "http://localhost:8080/api/performance/automatic-control-system";
    try {
        console.log("Fetching systems from:", `${API_URL}/list?page=${currentPage.value}&size=${pageSize.value}`);
        const response = await axios.get(`${API_URL}/list`, {
            params: { page: currentPage.value, size: pageSize.value },
            headers: { "Origin": "http://localhost:5173" },
        });
        console.log("API Response:", response.data);
        if (response.data && Array.isArray(response.data.content)) {
            systems.value = response.data.content.map(system => ({
                ...system,
                type: system.type || "미분류"
            }));
            totalPages.value = response.data.totalPages || 1;
        } else {
            systems.value = [];
            totalPages.value = 1;
            console.warn("No content or invalid response structure:", response.data);
        }
    } catch (error) {
        console.error("데이터 불러오기 실패:", error.response?.data || error.message || error);
        systems.value = [];
        totalPages.value = 1;
    }
}

async function submitForm() {
    const API_URL = "http://localhost:8080/api/performance/automatic-control-system";
    try {
        const response = await axios.post(`${API_URL}/upload`, formData.value, {
            headers: { "Content-Type": "application/json", "Origin": "http://localhost:5173" },
        });
        alert("업로드 성공!");
        loadSystems();
        resetForm();
    } catch (error) {
        console.error("업로드 실패:", error.response?.data || error.message || error);
        alert("업로드 중 오류가 발생했습니다: " + (error.response?.data?.message || error.message));
    }
}

async function deleteSystem(id) {
    const API_URL = "http://localhost:8080/api/performance/automatic-control-system";
    if (!id) {
        console.error("삭제할 ID가 없습니다.");
        alert("삭제할 데이터 ID가 유효하지 않습니다.");
        return;
    }
    if (confirm("정말로 삭제하시겠습니까?")) {
        try {
            const response = await axios.delete(`${API_URL}/delete/${id}`, {
                headers: { "Origin": "http://localhost:5173" },
            });
            console.log("Delete response:", response.data);
            alert(response.data || "삭제되었습니다."); // 백엔드 응답 메시지 표시
            loadSystems(); // 데이터 새로고침
        } catch (error) {
            console.error("삭제 실패:", error.response?.data || error.message || error);
            alert("삭제 중 오류가 발생했습니다: " + (error.response?.data?.message || error.message || "알 수 없는 오류"));
        }
    }
}

async function updateSystem() {
    if (!editFormData.value.id) {
        console.error("수정할 데이터 ID가 없습니다.");
        alert("수정할 데이터 ID가 유효하지 않습니다.");
        return;
    }
    const API_URL = "http://localhost:8080/api/performance/automatic-control-system";
    try {
        const response = await axios.put(`${API_URL}/update/${editFormData.value.id}`, editFormData.value, {
            headers: { "Content-Type": "application/json", "Origin": "http://localhost:5173" },
        });
        console.log("Update response:", response.data);
        alert("수정 완료!");
        loadSystems(); // 데이터 새로고침
        isEditOpen.value = false;
    } catch (error) {
        console.error("수정 실패:", error.response?.data || error.message || error);
        alert("수정 중 오류가 발생했습니다: " + (error.response?.data?.message || error.message || "알 수 없는 오류"));
    }
}

function resetForm() {
    formData.value = { type: "", contractName: "", manufactureYear: "", client: "" };
}

function filterByType(type) {
    filter.value = type;
    currentPage.value = 0; // 필터 변경 시 페이지 초기화
    loadSystems();
}

function prevPage() {
    if (currentPage.value > 0) {
        currentPage.value--;
        loadSystems();
    }
}

function nextPage() {
    if (currentPage.value < totalPages.value - 1) {
        currentPage.value++;
        loadSystems();
    }
}

function closePopup() {
    emit("close");
}

function editSystem(system) {
    editFormData.value = { ...system };
    isEditOpen.value = true;
}
</script>

<style scoped>
.form-container {
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
}

.mb-4 {
    margin-bottom: 1rem;
}

.primary-button {
    width: 100%;
    padding: 12px;
    font-size: 16px;
    font-weight: bold;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.primary-button:hover {
    background-color: #0056b3;
}

.filter-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    justify-content: center;
}

.custom-table {
    width: 100%;
    border-collapse: collapse;
}

.custom-table th,
.custom-table td {
    padding: 12px 15px;
    text-align: left;
    border: 1px solid #ddd;
}

.custom-table th {
    background-color: #f8f9fa;
    font-weight: bold;
}

.delete-button {
    padding: 6px 12px;
    background-color: #dc3545;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.delete-button:hover {
    background-color: #c82333;
}

.edit-button {
    padding: 6px 12px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.edit-button:hover {
    background-color: #218838;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 15px;
}
</style>