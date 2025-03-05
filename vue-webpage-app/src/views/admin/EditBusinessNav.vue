<template>
    <Teleport to="body">
        <div v-if="visible" class="popup-overlay" @click.self="closePopup">
            <div class="popup-container">
                <div class="popup-header">
                    <h2 class="popup-title">사업분야</h2>
                    <button class="close-button" @click="closePopup">✖</button>
                </div>
                <div class="card-container">
                    <!-- 자동제어시스템 카드 -->
                    <div class="card" @click="openControlSystem">
                        <h3>자동제어시스템</h3>
                    </div>
                    <!-- 전기공사 카드 (예시) -->
                    <div class="card" @click="openElectricalWorks">
                        <h3>전기공사</h3>
                    </div>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
const props = defineProps({
    visible: { type: Boolean, default: false }
});
const emit = defineEmits(["close", "open-nested"]);

function closePopup() {
    emit("close");
}

function openControlSystem() {
    // 바로 open-nested 이벤트를 부모에 전달
    emit("open-nested", "control-system");
}

function openElectricalWorks() {
    emit("open-nested", "electrical-works");
}
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
    width: 500px;
    max-width: 90%;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.popup-title {
    margin: 0;
    font-size: 1.3rem;
    font-weight: bold;
    color: #333;
}

.close-button {
    background: transparent;
    border: none;
    font-size: 1.3rem;
    cursor: pointer;
    color: #666;
}

.close-button:hover {
    color: #000;
}

.card-container {
    display: flex;
    justify-content: space-around;
    gap: 20px;
    margin-top: 20px;
}

.card {
    width: 160px;
    background: #f5f5f5;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    cursor: pointer;
    transition: transform 0.2s ease, background 0.2s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.card:hover {
    transform: scale(1.03);
    background: #e8e8e8;
}

.card h3 {
    margin: 0;
    font-size: 1rem;
    color: #333;
}
</style>