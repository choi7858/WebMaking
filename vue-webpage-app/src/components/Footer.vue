<template>
    <footer class="footer">
        <div class="footer-content">
            <!-- 로고 -->
            <div class="footer-logo">
                <img src="@/assets/logo.png" alt="동영테크원 로고" />
            </div>

            <!-- 회사 정보 및 링크 -->
            <div class="footer-info">
                <div class="footer-links">
                    <a href="#">찾아오시는 길</a> |
                    <a href="#">개인정보처리방침</a> |
                    <a href="#">관리자 로그인</a>
                </div>
                <div class="footer-address">
                    주소: 부산시 해운대구 반송로 513번길 66-33 &nbsp; 대표번호: 051-320-0500 &nbsp;
                    FAX: 051-320-0509 &nbsp; E-MAIL: <a href="mailto:dlyauto1@dytechone.kr">dlyauto1@dytechone.kr</a>
                </div>
                <div class="footer-copyright">
                    Copyright © 2022 동영테크원 All Rights Reserved.
                </div>
            </div>
        </div>
    </footer>
</template>

<style scoped>
.footer {
    width: 100%;
    background-color: #fff;
    border-top: 1px solid #ddd;
    padding: 20px 0;
    position: relative;
    bottom: 0;
    left: 0;
}

.footer-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.footer-logo img {
    width: 150px;
}

.footer-info {
    text-align: right;
    font-size: 14px;
    color: #333;
}

.footer-links a {
    color: #333;
    text-decoration: none;
    margin: 0 5px;
}

.footer-links a:hover {
    text-decoration: underline;
}

.footer-address {
    margin-top: 5px;
    color: #555;
}

.footer-copyright {
    margin-top: 5px;
    color: #888;
}
</style>
