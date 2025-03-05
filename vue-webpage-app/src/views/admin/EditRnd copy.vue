<template>
    <Teleport to="body">
        <div v-if="visible" class="popup-overlay" @click.self="closePopup">
            <div class="popup-container" ref="diagramContainer">
                <div class="popup-header">
                    <h2 class="popup-title">R&D 메뉴 편집</h2>
                    <button class="close-button" @click="closePopup">✖</button>
                </div>
                <!-- 다이어그램이 그려질 canvas -->
                <canvas ref="canvasRef" class="diagram-canvas" @click="handleCanvasClick"></canvas>
                <!-- 뒤로가기 버튼 -->
                <div class="back-button-container">
                    <button class="back-button" @click="goBack">뒤로가기</button>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import EditResearchLab from "@/views/admin/EditResearchLab.vue";
import EditResearchCooperation from "@/views/admin/EditResearchCooperation.vue";
import EditElectronicProtection from "@/views/admin/EditElectronicProtection.vue";
import EditElectricProtection from "@/views/admin/EditElectricProtection.vue";
import EditCoilLift from "@/views/admin/EditCoilLift.vue";
import EditSSILS from "@/views/admin/EditSSILS.vue";
import EditCalibrator from "@/views/admin/EditCalibrator.vue";
import EditSCADA from "@/views/admin/EditSCADA.vue";

// Props: 팝업 표시 여부 (부모에서 전달)
const props = defineProps({
    visible: { type: Boolean, default: false }
});
// Emit: close, open-nested 이벤트 전달
const emit = defineEmits(["close", "open-nested"]);

const router = useRouter();

// 캔버스 및 컨테이너 ref
const canvasRef = ref(null);
const diagramContainer = ref(null);

// 노드 클릭 판별용 바운딩 박스 배열
const nodeList = ref([]);

// 하위 메뉴 배열 (R&D 하위 항목들 + 연구분야 하위 메뉴 포함)
const submenus = ref([
    { label: "연구소 소개", type: "research-lab", apiPath: "/api/rnd/research-lab", component: EditResearchLab },
    {
        label: "연구분야 >", type: "research-fields", route: "/admin/edit/header/rnd/research-fields",
        children: [
            { label: "전자식 방호장치", type: "electronic-protection", apiPath: "/api/rnd/electronic-protection", component: EditElectronicProtection },
            { label: "전기식 방호장치", type: "electric-protection", apiPath: "/api/rnd/electric-protection", component: EditElectricProtection },
            { label: "코일리프트", type: "coil-lift", apiPath: "/api/rnd/coil-lift", component: EditCoilLift },
            { label: "SSILS 전원공급기", type: "ssils", apiPath: "/api/rnd/ssils", component: EditSSILS },
            { label: "다기능 캘리브레이터", type: "calibrator", apiPath: "/api/rnd/calibrator", component: EditCalibrator },
            { label: "SCADA HMI 일체형 PLC", type: "scada", apiPath: "/api/rnd/scada", component: EditSCADA },
        ]
    },
    { label: "연구협력", type: "research-cooperation", apiPath: "/api/rnd/research-cooperation", component: EditResearchCooperation }
]);

// 팝업 닫기 함수
function closePopup() {
    emit("close");
}

// 뒤로가기 함수
function goBack() {
    router.push("/admin/edit/header/nav");
}

// 노드 클릭 판별: canvas 클릭 이벤트 처리
async function handleCanvasClick(e) {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const rect = canvas.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    for (const node of nodeList.value) {
        const { x, y, width, height, type } = node;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            const menu = findMenuByType(type);
            if (menu.route) {
                router.push(menu.route); // 연구분야 페이지 이동
            } else if (menu.apiPath) {
                // 팝업 열기 (API 호출 후 데이터 전달)
                let fetchedData = null;
                try {
                    const response = await axios.get(`http://localhost:8080${menu.apiPath}`);
                    fetchedData = response.data;
                } catch (error) {
                    console.error("데이터 불러오기 오류:", error);
                    fetchedData = null;
                }
                emit("open-nested", { type: menu.type, fetchedData });
            }
            break;
        }
    }
}

// type으로 메뉴 찾기 (하위 메뉴 포함)
function findMenuByType(type) {
    for (const menu of submenus.value) {
        if (menu.type === type) return menu;
        if (menu.children) {
            const child = menu.children.find(c => c.type === type);
            if (child) return child;
        }
    }
    return null;
}

// 다이어그램 그리기 함수
function drawDiagram() {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    const container = diagramContainer.value;
    if (!container) return;

    // 캔버스 크기 계산
    const containerWidth = container.clientWidth;
    const spacingY = 60;
    const totalNodes = submenus.value.reduce((acc, menu) => acc + (menu.children ? menu.children.length : 1), 0);
    const totalHeightNeeded = 200 + totalNodes * spacingY;
    canvas.width = containerWidth;
    canvas.height = Math.max(container.clientHeight, totalHeightNeeded);

    // 캔버스 초기화
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 기본 스타일 설정
    ctx.font = "14px sans-serif";
    ctx.fillStyle = "#000";
    ctx.strokeStyle = "#000";

    // 메인 노드: "R&D"
    const mainNode = { x: 20, y: 100, width: 120, height: 40, label: "R&D" };
    drawRect(ctx, mainNode);
    drawCenteredText(ctx, mainNode, mainNode.label);

    // 노드 정보 초기화
    nodeList.value = [];

    // 하위 노드들 그리기
    const startXLevel1 = 250; // 1단계 노드 X 위치
    const startXLevel2 = 450; // 2단계 노드 X 위치 (연구분야 하위)
    let currentY = 50;

    submenus.value.forEach((menu) => {
        const nodeRect = {
            x: startXLevel1,
            y: currentY,
            width: 180,
            height: 40,
            label: menu.label,
            type: menu.type
        };
        drawRect(ctx, nodeRect);
        drawCenteredText(ctx, nodeRect, nodeRect.label);
        drawElbowLine(ctx,
            { x: mainNode.x + mainNode.width, y: mainNode.y + mainNode.height / 2 },
            { x: nodeRect.x, y: nodeRect.y + nodeRect.height / 2 }
        );
        nodeList.value.push(nodeRect);

        // 연구분야 하위 메뉴 그리기
        if (menu.children) {
            let childY = currentY;
            menu.children.forEach((child) => {
                const childNode = {
                    x: startXLevel2,
                    y: childY,
                    width: 220,
                    height: 40,
                    label: child.label,
                    type: child.type
                };
                drawRect(ctx, childNode);
                drawCenteredText(ctx, childNode, childNode.label);
                drawElbowLine(ctx,
                    { x: nodeRect.x + nodeRect.width, y: nodeRect.y + nodeRect.height / 2 },
                    { x: childNode.x, y: childNode.y + childNode.height / 2 }
                );
                nodeList.value.push(childNode);
                childY += spacingY;
            });
            currentY = childY; // 하위 메뉴 이후 다음 노드 위치 조정
        } else {
            currentY += spacingY;
        }
    });
}

// 헬퍼 함수: 사각형 그리기
function drawRect(ctx, { x, y, width, height }) {
    ctx.beginPath();
    ctx.rect(x, y, width, height);
    ctx.stroke();
}

// 헬퍼 함수: 사각형 중앙에 텍스트 그리기
function drawCenteredText(ctx, { x, y, width, height }, text) {
    const textWidth = ctx.measureText(text).width;
    const textX = x + (width - textWidth) / 2;
    const textY = y + height / 2 + 5;
    ctx.fillText(text, textX, textY);
}

// 헬퍼 함수: 직각(엘보) 선 그리기 (수평 -> 수직 -> 수평)
function drawElbowLine(ctx, start, end) {
    const midX = (start.x + end.x) / 2;
    ctx.beginPath();
    ctx.moveTo(start.x, start.y);
    ctx.lineTo(midX, start.y);
    ctx.lineTo(midX, end.y);
    ctx.lineTo(end.x, end.y);
    ctx.stroke();
}

// 컴포넌트 마운트 시 다이어그램 그리기 및 이벤트 등록
onMounted(() => {
    nextTick(() => {
        drawDiagram();
    });
    window.addEventListener("resize", drawDiagram);
    const canvas = canvasRef.value;
    if (canvas) {
        canvas.addEventListener("click", handleCanvasClick);
    }
});
</script>

<style scoped>
.popup-overlay {
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
    width: 80%;
    max-width: 1000px;
    /* 연구분야 하위 메뉴를 위해 너비 증가 */
    min-height: 400px;
    border-radius: 8px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    position: relative;
    overflow: auto;
    display: flex;
    flex-direction: column;
}

.popup-header {
    position: relative;
    z-index: 10;
    padding: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
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
    z-index: 11;
}

.diagram-canvas {
    border: 1px solid #ccc;
    display: block;
    flex-grow: 1;
}

.back-button-container {
    padding: 20px;
    display: flex;
    justify-content: center;
}

.back-button {
    padding: 10px 20px;
    background-color: #6b7280;
    /* gray-600 */
    color: white;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
}

.back-button:hover {
    background-color: #4b5563;
    /* gray-700 */
}
</style>