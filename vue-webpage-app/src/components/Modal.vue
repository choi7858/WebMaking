<template>
    <div class="modal-overlay" @click.self="close">
        <div class="modal-content">
            <slot></slot>
            <!-- 선택 사항: 닫기 버튼 (필요 시) -->
            <button class="modal-close" @click="close">Close</button>
        </div>
    </div>
</template>

<script setup>
const emit = defineEmits(['close'])
function close() {
    emit('close')
}
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 10000;
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    width: 500px;
    max-width: 90%;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    position: relative;
}

.modal-close {
    position: absolute;
    top: 10px;
    right: 10px;
    background: transparent;
    border: none;
    font-size: 1.3rem;
    cursor: pointer;
    color: #000;
}
</style>