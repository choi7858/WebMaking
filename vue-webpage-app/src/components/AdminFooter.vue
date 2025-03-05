<template>
    <footer class="admin-footer">
        <div class="footer-content">
            <!-- 로고 (헤더와 동일 DB 로고) -->
            <div class="footer-logo">
                <img :src="logoUrlWithVersion" alt="동영테크원 로고" @click="triggerLogoUpload" />
                <input type="file" ref="logoInput" class="hidden" @change="uploadLogo" accept="image/*" />
            </div>

            <!-- 하단 링크 / 정보 -->
            <div class="footer-info">
                <div class="footer-links">
                    <a href="#">찾아오시는 길</a>
                    <span class="divider">|</span>
                    <a href="#">개인정보처리방침</a>
                    <span class="divider">|</span>
                    <a href="#">관리자 설정</a>
                </div>
                <div class="footer-address">
                    주소: 부산시 해운대구 반송로 513번길 66-33 &nbsp;
                    대표번호: 051-320-0500 &nbsp;
                    FAX: 051-320-0509 &nbsp;
                    E-MAIL:
                    <a href="mailto:dlyauto1@dytechone.kr">dlyauto1@dytechone.kr</a>
                </div>
                <div class="footer-copy">
                    © 2023 동영테크원. All Rights Reserved.
                </div>
            </div>
        </div>
    </footer>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";

// 헤더와 동일하게 DB에서 로고를 가져오기 위해 ref 선언
const logoUrl = ref("http://localhost:8080/assets/default-logo.png");
const logoInput = ref(null);
const version = ref(Date.now());

// 캐싱 방지용 쿼리 파라미터
const logoUrlWithVersion = computed(() => `${logoUrl.value}?v=${version.value}`);

// 파일 선택 창 열기
function triggerLogoUpload() {
    logoInput.value.click();
}

// 로고 업로드 (헤더와 동일 로직)
async function uploadLogo(event) {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
        // 헤더와 동일하게 /api/logo/upload 등을 호출
        const response = await axios.post("http://localhost:8080/api/logo/upload", formData);
        logoUrl.value = response.data.imageUrl;
        version.value = Date.now(); // 캐시 무효화
    } catch (error) {
        console.error("로고 업로드 실패:", error);
    }
}

// DB에서 로고 가져오기 (헤더와 동일 로직)
async function fetchLogo() {
    try {
        const response = await axios.get("http://localhost:8080/api/logo");
        if (response.data.imageUrl) {
            logoUrl.value = response.data.imageUrl;
            version.value = Date.now();
        }
    } catch (error) {
        console.error("로고 불러오기 실패:", error);
    }
}

// 컴포넌트 마운트 시 로고 불러오기
onMounted(() => {
    fetchLogo();
});
</script>

<style scoped>
.admin-footer {
    width: 100%;
    /* 검은색 테두리를 없애고, 필요 시 부드러운 배경색 사용 */
    background-color: #e9f0f9;
    /* 예: 밝은 하늘색 느낌 */
    padding: 20px 0;
    position: relative;
}

/* 내부 컨테이너 */
.footer-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
}

/* 로고 */
.footer-logo img {
    height: 45px;
    /* 높이만 고정, 가로 비율 자동 */
    width: auto;
    cursor: pointer;
    border: none;
    /* 검은색 테두리 제거 */
    transition: transform 0.3s ease;
}

.footer-logo img:hover {
    transform: scale(1.05);
}

/* 오른쪽 정보 영역 */
.footer-info {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    font-size: 14px;
    color: #333;
    line-height: 1.6;
}

/* 링크 영역 */
.footer-links {
    margin-bottom: 5px;
}

.footer-links a {
    color: #333;
    text-decoration: none;
    margin: 0 5px;
    transition: color 0.3s ease;
}

.footer-links a:hover {
    color: #007bff;
}

.divider {
    color: #ccc;
    margin: 0 2px;
}

/* 주소 */
.footer-address {
    color: #555;
    margin-bottom: 5px;
}

/* 저작권 */
.footer-copy {
    color: #888;
}

/* 숨겨진 파일 입력 */
.hidden {
    display: none;
}
</style>