<template>
    <div class="overlay">
        <div class="notice-container">
            <div class="header">
                <h3 class="title">공지사항 편집</h3>
                <button @click="closePopup" class="close-button">✖</button>
            </div>

            <form @submit.prevent="submitForm" class="form-container">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input v-model="formData.title" id="title" type="text" placeholder="제목 입력" required />
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea v-model="formData.content" id="content" placeholder="내용 입력" rows="5" required></textarea>
                </div>
                <div class="form-group">
                    <label for="author">작성자</label>
                    <input v-model="formData.author" id="author" type="text" placeholder="작성자 입력" required />
                </div>
                <button type="submit" class="submit-button">공지사항 등록</button>
            </form>

            <h3 class="title">공지사항 목록</h3>
            <table class="notice-table">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>제목</th>
                        <th>내용</th>
                        <th>작성자</th>
                        <th>작성일</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(notice, index) in notices" :key="notice.id">
                        <td>{{ index + 1 + (currentPage * pageSize) }}</td>
                        <td>{{ notice.title }}</td>
                        <td>{{ notice.content }}</td>
                        <td>{{ notice.author }}</td>
                        <td>{{ formatDate(notice.createdAt) }}</td>
                        <td>
                            <button @click="deleteNotice(notice.id)" class="delete-button">X</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="pagination">
                <button @click="prevPage" :disabled="currentPage === 0" class="page-btn">이전</button>
                <span>페이지 {{ currentPage + 1 }} / {{ totalPages }}</span>
                <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="page-btn">다음</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "EditNotices",
    data() {
        return {
            formData: {
                title: "",
                content: "",
                author: "",
            },
            notices: [],
            currentPage: 0,
            pageSize: 10,
            totalPages: 0,
        };
    },
    mounted() {
        this.fetchNotices();
    },
    methods: {
        async submitForm() {
            try {
                const response = await axios.post("http://localhost:8080/api/notice/upload", this.formData);
                if (response.status === 200) {
                    alert("업로드 성공!");
                    this.resetForm();
                    this.fetchNotices();
                }
            } catch (error) {
                console.error("업로드 실패:", error);
            }
        },
        async fetchNotices() {
            try {
                const response = await axios.get("http://localhost:8080/api/notice/list", {
                    params: {
                        page: this.currentPage,
                        size: this.pageSize,
                    },
                });
                this.notices = response.data.content;
                this.totalPages = response.data.totalPages;
            } catch (error) {
                console.error("데이터 불러오기 실패:", error);
            }
        },
        async deleteNotice(id) {
            if (!id) {
                console.error("삭제할 ID가 없습니다.");
                return;
            }
            if (confirm("정말로 삭제하시겠습니까?")) {
                try {
                    await axios.delete(`http://localhost:8080/api/notice/delete/${id}`);
                    alert("게시글이 삭제되었습니다.");
                    this.fetchNotices();
                } catch (error) {
                    console.error("삭제 실패:", error);
                }
            }
        },
        resetForm() {
            this.formData = {
                title: "",
                content: "",
                author: "",
            };
        },
        prevPage() {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.fetchNotices();
            }
        },
        nextPage() {
            if (this.currentPage < this.totalPages - 1) {
                this.currentPage++;
                this.fetchNotices();
            }
        },
        formatDate(date) {
            const options = { year: "numeric", month: "2-digit", day: "2-digit" };
            return new Date(date).toLocaleDateString("ko-KR", options);
        },
        closePopup() {
            this.$emit("close");
        },
    },
};
</script>

<style scoped>
/* 배경 오버레이 */
.overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

/* 공지사항 컨테이너 - 최대 높이를 지정하고 스크롤 추가 */
.notice-container {
    padding: 20px;
    width: 900px;
    max-height: 90vh;
    /* 화면의 90%를 최대 높이로 지정 */
    overflow-y: auto;
    /* 내용이 넘칠 경우 수직 스크롤 생성 */
    background: #fff;
    border-radius: 8px;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
    position: relative;
}

/* 헤더 스타일 */
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

/* 닫기 버튼 */
.close-button {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    color: #777;
}

.close-button:hover {
    color: #333;
}

/* 폼 스타일 */
.form-container {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.form-group label {
    font-weight: bold;
    display: block;
    margin-bottom: 5px;
}

.form-group input,
.form-group textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

/* 등록 버튼 */
.submit-button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.submit-button:hover {
    background-color: #0056b3;
}

/* 테이블 스타일 */
.notice-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

.notice-table th,
.notice-table td {
    border: 1px solid #ddd;
    padding: 10px;
    text-align: left;
}

.notice-table th {
    background-color: #f0f0f0;
}

/* 삭제 버튼 */
.delete-button {
    padding: 5px 10px;
    background-color: #ff4d4d;
    color: white;
    border: none;
    border-radius: 3px;
    cursor: pointer;
}

.delete-button:hover {
    background-color: #cc0000;
}

/* 페이지네이션 */
.pagination {
    margin-top: 20px;
    text-align: center;
}

.page-btn {
    margin: 0 5px;
    padding: 8px 12px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 3px;
}

.page-btn:hover {
    background-color: #0056b3;
}

.page-btn:disabled {
    background-color: #ccc;
}
</style>