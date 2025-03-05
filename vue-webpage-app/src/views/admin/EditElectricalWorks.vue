<template>
    <Teleport to="body">
        <div v-if="visible" class="popup-overlay" @click.self="closePopup">
            <div class="popup-container" ref="diagramContainer">
                <div class="popup-header">
                    <h2 class="popup-title">전기공사 편집</h2>
                    <button class="close-button" @click="closePopup">✖</button>
                </div>
                <!-- 캔버스 요소: 다이어그램 그리기 -->
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

// 하위 메뉴 배열 (예: "사업현황", "장비보유현황")
const submenus = ref([
    { label: "사업현황", type: "business-status" },
    { label: "장비보유현황", type: "equipment-status" }
]);

// 노드들의 바운딩 정보 저장 배열 (클릭 판별용)
const nodeList = ref([]);

/** 팝업 닫기 */
function closePopup() {
    emit("close");
}

/** 캔버스 클릭 이벤트 처리: 클릭한 좌표가 어느 노드에 해당하는지 판별 */
function handleCanvasClick(e) {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const rect = canvas.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    // nodeList 순회하며 클릭 판별
    for (const node of nodeList.value) {
        const { x, y, width, height, type } = node;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            // 해당 노드 클릭 시 open-nested 이벤트 발생
            emit("open-nested", type);
            break;
        }
    }
}

/** 다이어그램 그리기 함수 */
function drawDiagram() {
    const canvas = canvasRef.value;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    const container = diagramContainer.value;
    if (!container) return;

    // 컨테이너 크기에 맞게 캔버스 크기 설정
    const containerWidth = container.clientWidth;
    const containerHeight = container.clientHeight;
    const spacingY = 80; // 노드 간 수직 간격
    const totalNodes = submenus.value.length;
    const totalHeightNeeded = 200 + (totalNodes * spacingY);
    canvas.width = containerWidth;
    canvas.height = Math.max(containerHeight, totalHeightNeeded);

    // 캔버스 클리어
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 기본 폰트 및 선 스타일 설정
    ctx.font = "14px sans-serif";
    ctx.fillStyle = "#000";
    ctx.strokeStyle = "#000";

    // (1) 메인 노드: "전기공사"
    const mainNode = { x: 20, y: 100, width: 120, height: 40, label: "전기공사" };
    drawRect(ctx, mainNode);
    drawCenteredText(ctx, mainNode, mainNode.label);

    // nodeList 초기화
    nodeList.value = [];

    // (2) 하위 노드들: 예) "사업현황", "장비보유현황"
    const startX = 250;
    const startY = 80;
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
        // 엘보 형태의 선 그리기: 메인 노드와 하위 노드 연결
        drawElbowLine(ctx,
            { x: mainNode.x + mainNode.width, y: mainNode.y + mainNode.height / 2 },
            { x: nodeRect.x, y: nodeRect.y + nodeRect.height / 2 }
        );
        nodeList.value.push(nodeRect);
    });
}

/** 헬퍼 함수: 사각형 그리기 */
function drawRect(ctx, { x, y, width, height }) {
    ctx.beginPath();
    ctx.rect(x, y, width, height);
    ctx.stroke();
}

/** 헬퍼 함수: 사각형 중앙에 텍스트 그리기 */
function drawCenteredText(ctx, { x, y, width, height }, text) {
    const textWidth = ctx.measureText(text).width;
    const textX = x + (width - textWidth) / 2;
    const textY = y + height / 2 + 5;
    ctx.fillText(text, textX, textY);
}

/** 헬퍼 함수: 직각(엘보) 선 그리기 */
function drawElbowLine(ctx, start, end) {
    const midX = (start.x + end.x) / 2;
    ctx.beginPath();
    ctx.moveTo(start.x, start.y);
    ctx.lineTo(midX, start.y);
    ctx.lineTo(midX, end.y);
    ctx.lineTo(end.x, end.y);
    ctx.stroke();
}

/** 컴포넌트가 마운트되면 다이어그램 그리기 및 이벤트 등록 */
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