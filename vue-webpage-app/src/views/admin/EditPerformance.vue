<template>
    <Teleport to="body">
        <div v-if="visible" class="popup-overlay" @click.self="closePopup">
            <div class="popup-container" ref="diagramContainer">
                <div class="popup-header">
                    <h2 class="popup-title">실적현황 메뉴 편집</h2>
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
import axios from "axios";
import EditAutomaticControlSystem from "@/views/admin/EditAutomaticControlSystem.vue";
import EditElectricWorkStatus from "@/views/admin/EditElectricWorkStatus.vue";

// Props: 팝업 표시 여부 (부모에서 전달)
const props = defineProps({
    visible: { type: Boolean, default: false }
});
// Emit: close, open-nested 이벤트 전달
const emit = defineEmits(["close", "open-nested"]);

// 캔버스 및 컨테이너 ref
const canvasRef = ref(null);
const diagramContainer = ref(null);

// 노드 클릭 판별용 바운딩 박스 배열
const nodeList = ref([]);

// 하위 메뉴 배열 (실적현황 하위 항목들)
const submenus = ref([
    { label: "자동제어시스템", type: "automatic-control-system", apiPath: "/api/performance/automatic-control-system", component: EditAutomaticControlSystem },
    { label: "전기공사현황", type: "electric-work-status", apiPath: "/api/performance/electric-work-status", component: EditElectricWorkStatus }
]);

// 팝업 닫기 함수
function closePopup() {
    emit("close");
}

// 뒤로가기 함수
function goBack() {
    window.history.back();
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
            const menu = submenus.value.find(m => m.type === type);
            if (menu.apiPath) {
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

// 다이어그램 그리기 함수
function drawDiagram() {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    const container = diagramContainer.value;
    if (!container) return;

    // 컨테이너 크기에 맞춰 캔버스 크기 설정
    const containerWidth = container.clientWidth;
    const containerHeight = container.clientHeight;
    const spacingY = 60;
    const totalNodes = submenus.value.length;
    const totalHeightNeeded = 200 + totalNodes * spacingY;
    canvas.width = containerWidth;
    canvas.height = Math.max(containerHeight, totalHeightNeeded);

    // 캔버스 초기화
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 기본 스타일 설정
    ctx.font = "14px sans-serif";
    ctx.fillStyle = "#000";
    ctx.strokeStyle = "#000";

    // 메인 노드: "실적현황"
    const mainNode = { x: 20, y: 100, width: 120, height: 40, label: "실적현황" };
    drawRect(ctx, mainNode);
    drawCenteredText(ctx, mainNode, mainNode.label);

    // 노드 정보 초기화
    nodeList.value = [];

    // 하위 노드들 그리기
    const startX = 250;
    const startY = 50;
    submenus.value.forEach((menu, index) => {
        const nodeRect = {
            x: startX,
            y: startY + index * spacingY,
            width: 180,
            height: 40,
            label: menu.label,
            type: menu.type
        };
        drawRect(ctx, nodeRect);
        drawCenteredText(ctx, nodeRect, nodeRect.label);
        // 직각(엘보) 형태의 선 그리기: 메인 노드와 하위 노드 연결
        drawElbowLine(ctx,
            { x: mainNode.x + mainNode.width, y: mainNode.y + mainNode.height / 2 },
            { x: nodeRect.x, y: nodeRect.y + nodeRect.height / 2 }
        );
        nodeList.value.push(nodeRect);
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
    max-width: 800px;
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