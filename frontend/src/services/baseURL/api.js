import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/school-equipment-lending-portal",
  withCredentials: true,
});

export default API;
