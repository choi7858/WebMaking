<template>
    <div class="company-home">

        <!-- Hero 섹션 (회사 대표 이미지, 슬로건 등) -->
        <section class="hero-section">
            <!-- 배경 이미지나 간단한 배너 이미지가 있다면 여기에 삽입 -->
            <h1 class="hero-title">동영테크(주)</h1>
            <p class="hero-subtitle">"We Empower Your Business"</p>
        </section>

        <!-- 회사 소개 섹션 -->
        <!-- <section class="intro-section">
            <div class="section-content">
                <h2>회사 소개</h2>
                <p>
                    저희 동영테크(주)는 첨단 기술과 풍부한 경험을 바탕으로
                    고객에게 최적의 솔루션을 제공하는 종합 엔지니어링 기업입니다.
                    <br />
                    다년간 쌓아온 노하우와 우수한 인재를 통해
                    고객의 가치를 극대화하기 위해 노력하고 있습니다.
                </p>
            </div>
        </section> -->



        <!-- (옵션) 추가 섹션: 연혁, 조직도, 실적, 공지사항 등 -->

        <!-- 하단 안내/주소 섹션 (푸터 대신 여기에 포함 가능) -->


    </div>
</template>

<script setup>
// 특별한 로직이 없다면 script는 비워 두셔도 됩니다.
</script>

<style scoped>
/* 전체 영역 */
.company-home {
    background-color: #fff;
    /* 중앙 컨텐츠 흰 배경 */
    width: 100%;
    min-height: calc(100vh - 100px);
    /* 헤더/푸터 높이에 맞춰 조절 */
    display: flex;
    flex-direction: column;
    align-items: center;
}

/* Hero 섹션 */
.hero-section {
    width: 100%;
    padding: 80px 20px 60px;
    text-align: center;
    background: #f2f2f2;
    /* 연한 배경 (원하는 경우) */
}

.hero-title {
    font-size: 3rem;
    margin-bottom: 10px;
}

.hero-subtitle {
    font-size: 1.2rem;
    color: #666;
}

/* 공통 섹션 스타일 */
section {
    width: 100%;
    margin: 0 auto;
    padding: 40px 20px;
}

.section-content {
    max-width: 1200px;
    /* 중앙 고정 폭 */
    margin: 0 auto;
    text-align: center;
}

/* 회사 소개, 사업 분야 등 */
.intro-section,
.business-section {
    background-color: #fff;
}

.intro-section h2,
.business-section h2 {
    font-size: 2rem;
    margin-bottom: 20px;
}

.intro-section p,
.business-section p {
    font-size: 1rem;
    line-height: 1.6;
    color: #333;
}

/* 하단 안내 섹션 */
.info-section {
    width: 100%;
    padding: 30px 20px;
    background-color: #fafafa;
    border-top: 1px solid #eee;
    text-align: center;
}

.info-content {
    max-width: 1200px;
    margin: 0 auto;
    font-size: 0.9rem;
    color: #666;
}

.info-content p {
    margin: 5px 0;
}
</style>