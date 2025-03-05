import axios from "axios";

const API_URL = "http://localhost:8080/api/rnd/electronic-protection";

// 전자식 방호장치 데이터 가져오기
export const fetchElectronicProtection = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// 전자식 방호장치 데이터 저장
export const saveElectronicProtection = async (description, file) => {
  const formData = new FormData();
  formData.append("description", description || "");

  if (file) {
    formData.append("file", file);
  }

  const response = await axios.post(`${API_URL}/save`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return response.data;
};
