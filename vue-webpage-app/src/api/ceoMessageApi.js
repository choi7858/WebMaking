import axios from "axios";

const API_URL = "http://localhost:8080/api/ceo-message";

// CEO 인사말 데이터 가져오기
export const fetchCeoMessage = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// CEO 인사말 저장
export const saveCeoMessage = async (message, file) => {
  const formData = new FormData();
  formData.append("message", message);
  if (file) {
    formData.append("file", file);
  }

  const response = await axios.post(`${API_URL}/save`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
  return response.data;
};
