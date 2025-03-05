import axios from "axios";

const API_URL = "http://localhost:8080/api/business/remote-monitoring";

// 데이터 가져오기
export const fetchRemoteMonitoring = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// 데이터 저장
export const saveRemoteMonitoring = async (description, file) => {
  const formData = new FormData();
  formData.append("description", description);
  if (file) {
    formData.append("file", file);
  }

  const response = await axios.post(`${API_URL}/save`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
  return response.data;
};
