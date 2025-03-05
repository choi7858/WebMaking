import axios from "axios";

const API_URL = "http://localhost:8080/api/ci";

// CI 데이터 가져오기
export const fetchCiData = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// CI 데이터 저장
export const saveCiData = async (ciData) => {
  const response = await axios.post(`${API_URL}/save`, ciData);
  return response.data;
};

// CI 이미지 업로드
export const uploadCiImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await axios.post(`${API_URL}/save`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return response.data;
};
