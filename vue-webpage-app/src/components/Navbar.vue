<template>
    <header>
        <nav>
            <ul>

                <li>
                    <router-link to="/">
                        <img src="@/assets/logo.png" alt="회사 로고" class="w-25 h-15 mr-2" />
                    </router-link>
                </li>


                <li class="relative group">
                    <router-link to="/company/ceo-message">회사소개</router-link>
                    <ul class="submenu">
                        <li><router-link to="/company/ceo-message">CEO 인사말</router-link></li>
                        <li><router-link to="/company/ci">CI</router-link></li>
                        <li><router-link to="/company/esg">ESG 경영</router-link></li>
                        <li><router-link to="/company/history">회사연혁</router-link></li>
                        <li><router-link to="/company/organization">조직도</router-link></li>
                        <li><router-link to="/company/certifications">보유면허 및 인증서</router-link></li>
                        <li><router-link to="/company/location">찾아오시는 길</router-link></li>
                    </ul>
                </li>


                <li class="relative group">
                    <router-link to="/business/water-system">사업분야</router-link>
                    <ul class="submenu">
                        <li class="relative group">
                            <router-link to="/business/water-system">자동제어시스템</router-link>
                            <ul class="submenu-right">
                                <li><router-link to="/business/water-system">상·하수처리 시스템</router-link></li>
                                <li><router-link to="/business/building-automation">빌딩자동제어</router-link></li>
                                <li><router-link to="/business/tunnel-monitoring">터널원격 감시제어</router-link></li>
                                <li><router-link to="/business/lighting-control">경관조명 자동감시제어</router-link></li>
                                <li><router-link to="/business/remote-monitoring">원방감시 자동제어</router-link></li>
                                <li><router-link to="/business/power-systems">수·배전반, 신재생 에너지</router-link></li>
                                <li><router-link to="/business/khnp">한국수력원자력</router-link></li>
                            </ul>
                        </li>
                        <li class="relative group">
                            <router-link to="/business/business-status">전기공사</router-link>
                            <ul class="submenu-right">
                                <li><router-link to="/business/business-status">사업현황</router-link></li>
                                <li><router-link to="/business/equipment-status">장비보유현황</router-link></li>
                            </ul>
                        </li>
                    </ul>
                </li>


                <li class="relative group">
                    <router-link to="/rnd/researchintroduction">R&D</router-link>
                    <ul class="submenu">
                        <li class="relative group">
                            <router-link to="/rnd/researchintroduction">연구소 소개</router-link>
                        </li>
                        <li class="relative group">
                            <router-link to="/rnd/electronicprotection">연구분야</router-link>
                            <ul class="submenu-right">
                                <li><router-link to="/rnd/electronicprotection">전자식 방호장치</router-link></li>
                                <li><router-link to="/rnd/electricprotection">전기식 방호장치</router-link></li>
                                <li><router-link to="/rnd/coil-lift">코일리프트</router-link></li>
                                <li><router-link to="/rnd/ssils">SSILS 전원공급기</router-link></li>
                                <li><router-link to="/rnd/calibrator">다기능 캘리브레이터</router-link></li>
                                <li><router-link to="/rnd/scada">SCADA HMI 일체형 PLC</router-link></li>
                            </ul>
                        </li>
                        <li class="relative group">
                            <router-link to="/rnd/research-cooperation">연구협력</router-link>
                        </li>
                    </ul>
                </li>

                <li class="relative group">
                    <router-link to="/performance/automaticcontrolsystem">실적현황</router-link>
                    <ul class="submenu">
                        <li><router-link to="/performance/automaticcontrolsystem">자동제어시스템</router-link></li>
                        <li><router-link to="/performance/electricworkstatus">전기공사현황</router-link></li>
                    </ul>
                </li>

                <li><router-link to="/notices/Notices">공지사항</router-link></li>

            </ul>
        </nav>
    </header>
</template>
<script>
export default {
    name: 'HeaderComponent',
    data() {
        return {
            logoUrl: '', // 동적으로 설정
        };
    },
    created() {
        this.setLogoUrl();
    },
    methods: {
        setLogoUrl() {
            const hostname = window.location.hostname;
            this.logoUrl = `@/assets/logo.png`;
        },
    },
}
</script>

<style scoped>
/* 네비게이션 바 스타일 */
nav {
    background-color: white;
    padding: 10px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 100%;
}

ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

ul li {
    position: relative;
    display: inline-block;
    vertical-align: top;
}

ul li a {
    color: black;
    padding: 15px 25px;
    text-decoration: none;
    font-size: 16px;
    font-weight: normal;
    transition: background-color 0.3s ease;
    border-bottom: 2px solid transparent;
    white-space: nowrap;
    display: block;
}

ul li a:hover {
    border-bottom: 2px solid #007BFF;
}

/* 드롭다운 스타일 */
.submenu {
    display: none;
    position: absolute;
    background-color: white;
    border: 1px solid #ccc;
    min-width: 180px;
    z-index: 1000;
    padding: 0;
    top: 100%;
}

ul li:hover>.submenu {
    display: block;
}

.submenu li {
    display: block;
    text-align: left;
    width: 100%;
}

.submenu li a {
    padding: 10px 20px;
    background-color: white;
    color: black;
    font-size: 14px;
    border-bottom: 1px solid #eee;
}

.submenu li a:hover {
    background-color: #f1f1f1;
    color: #007BFF;
}

/* 하위 메뉴를 오른쪽에 표시 */
.submenu-right {
    left: 100%;
    top: 0;
    display: none;
    position: absolute;
    background-color: white;
    border: 1px solid #ccc;
    min-width: 180px;
    z-index: 1000;
}

ul li:hover>.submenu-right {
    display: block;
}

/* 로고 스타일 */
.logo {
    height: 30px;
    margin-right: 100px;
}
</style>