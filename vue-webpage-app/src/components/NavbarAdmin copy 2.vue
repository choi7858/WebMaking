<template>
    <header class="admin-header">
        <div class="container">
            <!-- 로고 영역 -->
            <div class="logo-section">
                <input type="file" ref="logoInput" class="hidden" @change="uploadLogo" accept="image/*" />
                <img :src="logoUrl" alt="회사 로고" class="logo-img" @click="triggerFileUpload" />
            </div>
            <!-- 네비게이션 바 -->
            <nav class="nav-menu">
                <ul>
                    <li @click="openPopup('company')">회사소개</li>
                    <li @click="openPopup('business')">사업분야</li>
                    <li @click="openPopup('rnd')">R&D</li>
                    <li @click="openPopup('performance')">실적현황</li>
                    <li @click="openPopup('notices')">공지사항</li>
                </ul>
            </nav>
        </div>

        <!-- 팝업 렌더링 (Teleport + 동적 컴포넌트) -->
        <Teleport to="body">
            <component :is="selectedComponent" v-if="isPopupOpen" :visible="true" :existingData="fetchedData"
                @close="closePopup" @open-nested="handleOpenNested" />
        </Teleport>
    </header>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// 상위 메뉴 팝업 컴포넌트들
import EditCompanyNav from "@/views/admin/EditCompanyNav.vue";
import EditBusinessNav from "@/views/admin/EditBusinessNav.vue";
import EditRnd from "@/views/admin/EditRnd.vue"; // 수정된 EditRnd.vue 사용
import EditPerformance from "@/views/admin/EditPerformance.vue";
import EditNotices from "@/views/admin/EditNotices.vue";

// 하위 팝업 컴포넌트들 (기존과 동일)
import EditControlSystem from "@/views/admin/EditControlSystem.vue";
import EditWaterSystem from "@/views/admin/EditWaterSystem.vue";
import EditElectricalWorks from "@/views/admin/EditElectricalWorks.vue";
import EditBuildingAutomation from "@/views/admin/EditBuildingAutomation.vue";
import EditTunnelMonitoring from "@/views/admin/EditTunnelMonitoring.vue";
import EditLightingControl from "@/views/admin/EditLightingControl.vue";
import EditRemoteMonitoring from "@/views/admin/EditRemoteMonitoring.vue";
import EditPowerSystems from "@/views/admin/EditPowerSystems.vue";
import EditKHNP from "@/views/admin/EditKHNP.vue";
import EditBusinessStatus from "@/views/admin/EditBusinessStatus.vue";
import EditEquipmentStatus from "@/views/admin/EditEquipmentStatus.vue";

// 회사소개 하위 팝업 컴포넌트들 (기존과 동일)
import EditCeoMessage from "@/views/admin/EditCeoMessage.vue";
import EditCI from "@/views/admin/EditCI.vue";
import EditEsg from "@/views/admin/EditEsg.vue";
import EditHistory from "@/views/admin/EditHistory.vue";
import EditOrganization from "@/views/admin/EditOrganization.vue";
import EditCertifications from "@/views/admin/EditCertifications.vue";
import EditLocation from "@/views/admin/EditLocation.vue";

// R&D 하위 팝업 컴포넌트들
import EditResearchLab from "@/views/admin/EditResearchLab.vue";
import EditResearchCooperation from "@/views/admin/EditResearchCooperation.vue";

// 로고 관련
const logoUrl = ref("/assets/default-logo.png");
const logoInput = ref(null);

// 팝업 상태 관리 및 DB 데이터 저장
const isPopupOpen = ref(false);
const selectedComponent = ref(null);
const fetchedData = ref(null);

// 상위 메뉴 매핑 객체
const topMenuMapping = {
    company: EditCompanyNav,
    business: EditBusinessNav,
    rnd: EditRnd, // 이미 매핑되어 있음
    performance: EditPerformance,
    notices: EditNotices,
};

function openPopup(menuType) {
    if (topMenuMapping[menuType]) {
        selectedComponent.value = topMenuMapping[menuType];
        isPopupOpen.value = true;
        fetchedData.value = null; // 초기 데이터는 null로 설정 (필요 시 API 호출 추가 가능)
    }
}

function closePopup() {
    isPopupOpen.value = false;
    selectedComponent.value = null;
    fetchedData.value = null;
}

// 하위 메뉴 매핑 객체 (API 엔드포인트와 컴포넌트)
const nestedMapping = {
    // 기존 하위 메뉴들
    "control-system": { endpoint: "/api/business/control-system", component: EditControlSystem },
    "water-system": { endpoint: "/api/business/water-system", component: EditWaterSystem },
    "electrical-works": { endpoint: "/api/business/electrical-works", component: EditElectricalWorks },
    "building-automation": { endpoint: "/api/business/building-automation", component: EditBuildingAutomation },
    "tunnel-monitoring": { endpoint: "/api/business/tunnel-monitoring", component: EditTunnelMonitoring },
    "lighting-control": { endpoint: "/api/business/lighting-control", component: EditLightingControl },
    "remote-monitoring": { endpoint: "/api/business/remote-monitoring", component: EditRemoteMonitoring },
    "power-systems": { endpoint: "/api/business/power-systems", component: EditPowerSystems },
    "khnp": { endpoint: "/api/business/khnp", component: EditKHNP },
    "business-status": { endpoint: "/api/business/business-status", component: EditBusinessStatus },
    "equipment-status": { endpoint: "/api/business/equipment-status", component: EditEquipmentStatus },
    "ceo-message": { endpoint: "/api/ceo-message", component: EditCeoMessage },
    "ci": { endpoint: "/api/ci", component: EditCI },
    "esg": { endpoint: "/api/esg", component: EditEsg },
    "history": { endpoint: "/api/history", component: EditHistory },
    "organization": { endpoint: "/api/organization", component: EditOrganization },
    "certifications": { endpoint: "/api/certifications", component: EditCertifications },
    "location": { endpoint: "/api/location", component: EditLocation },
    // R&D 하위 메뉴 추가
    "research-lab": { endpoint: "/api/rnd/research-lab", component: EditResearchLab },
    "research-cooperation": { endpoint: "/api/rnd/research-cooperation", component: EditResearchCooperation },

};

async function handleOpenNested(nestedData) {
    let mapping = null;
    if (typeof nestedData === "string") {
        mapping = nestedMapping[nestedData];
    } else if (typeof nestedData === "object" && nestedData !== null) {
        mapping = nestedMapping[nestedData.type];
        if (nestedData.fetchedData) {
            fetchedData.value = nestedData.fetchedData;
        }
    }
    if (mapping) {
        if (mapping.endpoint) {
            try {
                const { data } = await axios.get(`http://localhost:8080${mapping.endpoint}`);
                fetchedData.value = data;
                console.log("DB fetched data =>", data);
            } catch (error) {
                console.error("handleOpenNested DB fetch error:", error);
                fetchedData.value = null;
            }
        } else {
            fetchedData.value = null;
        }
        selectedComponent.value = mapping.component;
    } else {
        selectedComponent.value = null;
        fetchedData.value = null;
    }
    isPopupOpen.value = true;
}

function triggerFileUpload() {
    logoInput.value.click();
}

async function uploadLogo(event) {
    const file = event.target.files[0];
    if (!file) return;
    const formData = new FormData();
    formData.append("file", file);
    try {
        const response = await axios.post("http://localhost:8080/api/logo/upload", formData);
        logoUrl.value = response.data.imageUrl;
    } catch (error) {
        console.error("로고 업로드 실패:", error);
    }
}

async function fetchLogo() {
    try {
        const response = await axios.get("http://localhost:8080/api/logo");
        if (response.data.imageUrl) {
            logoUrl.value = response.data.imageUrl;
        }
    } catch (error) {
        console.error("로고 불러오기 실패:", error);
    }
}

onMounted(fetchLogo);
</script>

<style scoped>
.admin-header {
    background: white;
    padding: 10px 0;
    border-bottom: 2px solid #e5e5e5;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
}

.container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 90%;
    max-width: 1400px;
}

.logo-section {
    cursor: pointer;
    display: flex;
    align-items: center;
}

.logo-img {
    width: 160px;
    height: auto;
}

.nav-menu ul {
    display: flex;
    list-style: none;
    gap: 40px;
}

.nav-menu ul li {
    cursor: pointer;
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
    transition: color 0.3s ease;
}

.nav-menu ul li:hover {
    color: #007bff;
}

.hidden {
    display: none;
}
</style>