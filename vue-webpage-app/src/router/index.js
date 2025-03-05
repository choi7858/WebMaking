import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import Admin from "@/views/admin/Admin.vue"; // ✅ 관리자 페이지 추가
import Templates from "@/views/admin/Templates.vue"; // ✅ 템플릿 선택 화면
import Edit from "@/views/admin/Edit.vue"; // ✅ 편집 화면
import Preview from "@/views/admin/Preview.vue"; // ✅ 미리보기 화면
import EditHeader from "@/views/admin/EditHeader.vue"; // 헤더 상세 편집 페이지 추가
import EditNav from "@/views/admin/EditNav.vue"; // 헤더 상세 편집 페이지 추가
import EditCompanyNav from "@/views/admin/EditCompanyNav.vue";
import EditBusinessNav from "@/views/admin/EditBusinessNav.vue";
import EditControlSystem from "@/views/admin/EditControlSystem.vue";
import EditElectricalWorks from "@/views/admin/EditElectricalWorks.vue";
import EditPerformance from "@/views/admin/EditPerformance.vue";
import EditNotices from "@/views/admin/EditNotices.vue";
import EditCeoMessage from "@/views/admin/EditCeoMessage.vue";
// R&D 편집 페이지 추가
import EditRnd from "@/views/admin/EditRnd.vue"; // R&D 편집 메인 페이지
// import EditResearchLab from "@/views/admin/EditResearchLab.vue"; // 연구소 소개 편집 페이지
import EditResearchFields from "@/views/admin/EditResearchFields.vue"; // 연구분야 편집 페이지
// import EditResearchCooperation from "@/views/admin/EditResearchCooperation.vue"; // 연구협력 편집 페이지

// 회사소개
import CEOMessage from "@/views/company/CEOMessage.vue";
import CI from "@/views/company/CI.vue";
import ESG from "@/views/company/ESG.vue";
import History from "@/views/company/History.vue";
import Organization from "@/views/company/Organization.vue";
import Certifications from "@/views/company/Certifications.vue";
import Location from "@/views/company/Location.vue";

// 사업분야 - 자동제어시스템
import ControlSystem from "@/views/business/ControlSystem.vue"; // 자동제어시스템 메인
import WaterSystem from "@/views/business/WaterSystem.vue";
import BuildingAutomation from "@/views/business/BuildingAutomation.vue";
import TunnelMonitoring from "@/views/business/TunnelMonitoring.vue";
import LightingControl from "@/views/business/LightingControl.vue";
import RemoteMonitoring from "@/views/business/RemoteMonitoring.vue";
import PowerSystems from "@/views/business/PowerSystems.vue";
import KHNP from "@/views/business/KHNP.vue";

// 사업분야 - 전기공사
import BusinessStatus from "@/views/business/BusinessStatus.vue"; // 사업현황
import EquipmentStatus from "@/views/business/EquipmentStatus.vue"; // 장비보유현황

// R&D
import ResearchIntroduction from "@/views/rnd/ResearchIntroduction.vue";
import ElectronicProtection from "@/views/rnd/ElectronicProtection.vue";
import ElectricProtection from "@/views/rnd/ElectricProtection.vue";
import CoilLift from "@/views/rnd/CoilLift.vue";
import SSILS from "@/views/rnd/SSILS.vue";
import Calibrator from "@/views/rnd/Calibrator.vue";
import SCADA from "@/views/rnd/SCADA.vue";
import ResearchCooperation from "@/views/rnd/ResearchCooperation.vue";

// 실적현황
import automaticcontrolsystem from "@/views/performance/automaticcontrolsystem.vue";
import electricworkstatus from "@/views/performance/electricworkstatus.vue";

// 공지사항
import Notices from "@/views/notices/Notices.vue";

const routes = [
  { path: "/admin/edit/header/company/ceo-message", component: EditCeoMessage },
  { path: "/", component: Home },
  { path: "/admin", component: Admin }, // ✅ 관리자 페이지 경로 추가
  { path: "/admin/templates", component: Templates },
  { path: "/admin/edit", component: Edit },
  { path: "/admin/preview", component: Preview },
  { path: "/admin/edit/header", component: EditHeader },
  { path: "/admin/edit/header/nav", component: EditNav },
  { path: "/admin/edit/header/company", component: EditCompanyNav },
  { path: "/admin/edit/header/business", component: EditBusinessNav },
  {
    path: "/admin/edit/header/business/control-system",
    component: EditControlSystem,
  },
  {
    path: "/admin/edit/header/business/electrical-works",
    component: EditElectricalWorks,
  },
  { path: "/admin/edit/header/rnd", component: EditRnd },
  // { path: "/views/admin/EditResearchLab.vue", component: EditResearchLab },
  {
    path: "/admin/edit/header/rnd/research-fields",
    component: EditResearchFields,
  },
  // {
  //   path: "/views/admin/EditResearchCooperation.vue",
  //   component: EditResearchCooperation,
  // },

  { path: "/admin/edit/header/performance", component: EditPerformance },
  { path: "/admin/edit/header/notices", component: EditNotices },

  // 회사소개
  { path: "/company/ceo-message", component: CEOMessage },
  { path: "/company/ci", component: CI },
  { path: "/company/esg", component: ESG },
  { path: "/company/history", component: History },
  { path: "/company/organization", component: Organization },
  { path: "/company/certifications", component: Certifications },
  { path: "/company/location", component: Location },

  // 자동제어시스템
  { path: "/business/control-system", component: ControlSystem }, // 자동제어시스템 클릭 시 이동
  { path: "/business/water-system", component: WaterSystem },
  { path: "/business/building-automation", component: BuildingAutomation },
  { path: "/business/tunnel-monitoring", component: TunnelMonitoring },
  { path: "/business/lighting-control", component: LightingControl },
  { path: "/business/remote-monitoring", component: RemoteMonitoring },
  { path: "/business/power-systems", component: PowerSystems },
  { path: "/business/khnp", component: KHNP },

  // 전기공사
  { path: "/business/business-status", component: BusinessStatus },
  { path: "/business/equipment-status", component: EquipmentStatus },

  // R&D
  {
    path: "/rnd/researchintroduction",
    component: ResearchIntroduction,
  },
  { path: "/rnd/electronicprotection", component: ElectronicProtection },
  { path: "/rnd/electricprotection", component: ElectricProtection },
  { path: "/rnd/coil-lift", component: CoilLift },
  { path: "/rnd/ssils", component: SSILS },
  { path: "/rnd/calibrator", component: Calibrator },
  { path: "/rnd/scada", component: SCADA },
  { path: "/rnd/research-cooperation", component: ResearchCooperation },

  // 실적현황
  {
    path: "/performance/automaticcontrolsystem",
    component: automaticcontrolsystem,
  },
  {
    path: "/performance/electricworkstatus",
    component: electricworkstatus,
  },

  // 공지사항
  { path: "/notices/notices", component: Notices },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
