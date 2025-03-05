<template>
    <Teleport to="body">
        <!-- 팝업 오버레이 (회색 반투명 배경) -->
        <div v-if="visible" class="popup-overlay" @click.self="closePopup">
            <!-- 팝업 컨테이너 -->
            <div class="popup-container" ref="diagramContainer">
                <!-- 헤더 영역 -->
                <div class="popup-header">
                    <h2 class="popup-title">사업분야 - 자동제어시스템</h2>
                    <button class="close-button" @click="closePopup">✖</button>
                </div>
                <!-- 실제 다이어그램이 그려질 canvas -->
                <canvas ref="canvasRef" class="diagram-canvas"></canvas>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";

const props = defineProps({
    visible: { type: Boolean, default: false }
});
const emit = defineEmits(["close", "open-nested"]);

const canvasRef = ref(null);
const diagramContainer = ref(null);

// 하위 메뉴 데이터 (노드 목록)
const submenus = ref([
    { label: "상하수처리 시스템", type: "water-system" },
    { label: "빌딩자동제어", type: "building-automation" },
    { label: "터널원격 감시제어", type: "tunnel-monitoring" },
    { label: "경관조명 자동감시제어", type: "lighting-control" },
    { label: "원방감시 자동제어", type: "remote-monitoring" },
    { label: "수·배전반/신재생 에너지", type: "power-systems" },
    { label: "한국수력원자력", type: "khnp" }
]);

// 그려진 노드들의 바운딩 박스 정보(클릭 판별용)
const nodeList = ref([]);

/** 팝업 닫기 */
function closePopup() {
    emit("close");
}

/** 캔버스 클릭 이벤트: 노드 클릭 판별 */
function handleCanvasClick(e) {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const rect = canvas.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    for (const node of nodeList.value) {
        const { x, y, width, height, type } = node;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            openSubmenu(type);
            break;
        }
    }
}

/** 하위 팝업 열기 (노드 클릭 시) */
function openSubmenu(nestedType) {
    emit("open-nested", nestedType);
}

/** 다이어그램 그리기 로직 */
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

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.font = "14px sans-serif";
    ctx.fillStyle = "#000";
    ctx.strokeStyle = "#000";

    // 메인 노드 ("자동제어시스템")
    const mainNode = { x: 20, y: 100, width: 120, height: 40, label: "자동제어시스템" };
    drawRect(ctx, mainNode);
    drawCenteredText(ctx, mainNode, mainNode.label);

    nodeList.value = []; // 기존 노드 정보 초기화

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
        // 직각(엘보) 형태의 선 연결 그리기
        drawElbowLine(ctx,
            { x: mainNode.x + mainNode.width, y: mainNode.y + mainNode.height / 2 },
            { x: nodeRect.x, y: nodeRect.y + nodeRect.height / 2 }
        );
        nodeList.value.push(nodeRect);
    });
}

/** 사각형 그리기 */
function drawRect(ctx, { x, y, width, height }) {
    ctx.beginPath();
    ctx.rect(x, y, width, height);
    ctx.stroke();
}

/** 사각형 내부에 텍스트 중앙 배치 */
function drawCenteredText(ctx, { x, y, width, height }, text) {
    const textWidth = ctx.measureText(text).width;
    const textX = x + (width - textWidth) / 2;
    const textY = y + height / 2 + 5;
    ctx.fillText(text, textX, textY);
}

/** 직각(엘보) 선 그리기: 수평 -> 수직 -> 수평 */
function drawElbowLine(ctx, start, end) {
    const midX = (start.x + end.x) / 2;
    ctx.beginPath();
    ctx.moveTo(start.x, start.y);
    ctx.lineTo(midX, start.y);
    ctx.lineTo(midX, end.y);
    ctx.lineTo(end.x, end.y);
    ctx.stroke();
}

/** 마운트 시 다이어그램 그리기 및 이벤트 등록 */
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
</style>