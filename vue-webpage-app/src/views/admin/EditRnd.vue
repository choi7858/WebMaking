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

    // 캔버스의 실제 크기와 표시 크기의 비율 계산
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;

    // 실제 캔버스 좌표로 변환
    const canvasX = mouseX * scaleX;
    const canvasY = mouseY * scaleY;

    for (const node of nodeList.value) {
        if (canvasX >= node.x &&
            canvasX <= node.x + node.width &&
            canvasY >= node.y &&
            canvasY <= node.y + node.height) {

            if (node.route) {
                router.push(node.route);
            } else if (node.apiPath) {
                try {
                    const response = await axios.get(`http://localhost:8080${node.apiPath}`);
                    emit("open-nested", { type: node.type, fetchedData: response.data });
                } catch (error) {
                    console.error("데이터 불러오기 오류:", error);
                }
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

    // 캔버스 크기 계산
    const containerWidth = container.clientWidth - 40;
    const containerHeight = Math.max(container.clientHeight - 40, 900); // 최소 높이 증가

    canvas.width = containerWidth;
    canvas.height = containerHeight;

    // 캔버스 초기화
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.font = "16px sans-serif";
    ctx.fillStyle = "#000";
    ctx.strokeStyle = "#000";
    ctx.lineWidth = 1.5;

    // 노드 크기 및 간격 설정
    const mainNodeWidth = 160;
    const level1NodeWidth = 220;
    const level2NodeWidth = 280;
    const nodeHeight = 70; // 높이 증가
    const spacingY = 100; // 세로 간격 증가

    // 시작 위치 조정
    const startY = 150; // 시작 위치를 아래로 조정
    const mainNodeX = 80;
    const level1X = 350;
    const level2X = 700;

    // 메인 노드: "R&D"
    const mainNode = {
        x: mainNodeX,
        y: startY + 150, // 메인 노드 위치 조정
        width: mainNodeWidth,
        height: nodeHeight,
        label: "R&D"
    };
    drawRoundedRect(ctx, mainNode, 8); // 모서리 둥글기 증가
    drawCenteredText(ctx, mainNode, mainNode.label);

    // 노드 정보 초기화
    nodeList.value = [];

    // 하위 노드들 그리기
    let currentY = startY;

    submenus.value.forEach((menu) => {
        const nodeRect = {
            x: level1X,
            y: currentY,
            width: level1NodeWidth,
            height: nodeHeight,
            label: menu.label,
            type: menu.type,
            route: menu.route,
            apiPath: menu.apiPath
        };

        // 노드 그리기
        drawRoundedRect(ctx, nodeRect, 8);
        drawCenteredText(ctx, nodeRect, nodeRect.label);
        drawElbowLine(ctx,
            { x: mainNode.x + mainNode.width, y: mainNode.y + mainNode.height / 2 },
            { x: nodeRect.x, y: nodeRect.y + nodeRect.height / 2 }
        );

        nodeList.value.push(nodeRect);

        if (menu.children) {
            let childY = currentY;
            menu.children.forEach((child) => {
                const childNode = {
                    x: level2X,
                    y: childY,
                    width: level2NodeWidth,
                    height: nodeHeight,
                    label: child.label,
                    type: child.type,
                    apiPath: child.apiPath
                };

                drawRoundedRect(ctx, childNode, 8);
                drawCenteredText(ctx, childNode, childNode.label);
                drawElbowLine(ctx,
                    { x: nodeRect.x + nodeRect.width, y: nodeRect.y + nodeRect.height / 2 },
                    { x: childNode.x, y: childNode.y + childNode.height / 2 }
                );

                nodeList.value.push(childNode);

                childY += spacingY;
            });
            currentY = childY;
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

// 둥근 모서리 사각형 그리기 함수 추가
function drawRoundedRect(ctx, { x, y, width, height }, radius) {
    ctx.beginPath();
    ctx.moveTo(x + radius, y);
    ctx.lineTo(x + width - radius, y);
    ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
    ctx.lineTo(x + width, y + height - radius);
    ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
    ctx.lineTo(x + radius, y + height);
    ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
    ctx.lineTo(x, y + radius);
    ctx.quadraticCurveTo(x, y, x + radius, y);
    ctx.closePath();
    ctx.stroke();
}

// 텍스트 중앙 정렬 함수 수정
function drawCenteredText(ctx, { x, y, width, height }, text) {
    ctx.save();
    ctx.textBaseline = 'middle';
    ctx.textAlign = 'center';
    ctx.font = '18px sans-serif'; // 폰트 크기 증가
    const centerX = x + width / 2;
    const centerY = y + height / 2;
    ctx.fillText(text, centerX, centerY);
    ctx.restore();
}

// 엘보우 라인 그리기 함수 수정
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
    width: 90%;
    max-width: 1400px;
    height: 90vh;
    max-height: 1000px;
    min-height: 800px;
    border-radius: 8px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    position: relative;
    display: flex;
    flex-direction: column;
}

.popup-header {
    flex-shrink: 0;
    padding: 20px;
    border-bottom: 1px solid #eee;
    background: #fff;
    border-radius: 8px 8px 0 0;
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
    padding: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.close-button:hover {
    background-color: #f3f4f6;
    border-radius: 4px;
}

.diagram-canvas {
    flex: 1;
    margin: 20px;
    border: 1px solid #eee;
    border-radius: 8px;
    overflow: auto;
    background: #fff;
}

.back-button-container {
    flex-shrink: 0;
    padding: 20px;
    border-top: 1px solid #eee;
    background: #fff;
    border-radius: 0 0 8px 8px;
    display: flex;
    justify-content: center;
}

.back-button {
    padding: 10px 24px;
    background-color: #6b7280;
    color: white;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.2s ease;
}

.back-button:hover {
    background-color: #4b5563;
    transform: translateY(-1px);
}

/* 반응형 디자인 */
@media screen and (max-width: 768px) {
    .popup-container {
        width: 95%;
        height: 95vh;
    }

    .popup-header {
        padding: 15px;
    }

    .popup-title {
        font-size: 1.1rem;
    }

    .diagram-canvas {
        margin: 10px;
    }

    .back-button {
        padding: 8px 20px;
        font-size: 0.9rem;
    }
}

/* 작은 화면 높이 대응 */
@media screen and (max-height: 600px) {
    .popup-container {
        height: 98vh;
        min-height: 400px;
    }

    .popup-header,
    .back-button-container {
        padding: 10px;
    }
}
</style>