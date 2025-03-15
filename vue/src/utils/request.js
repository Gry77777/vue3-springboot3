import axios from "axios";
import { ElMessage } from "element-plus";

const request = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000
});

request.interceptors.request.use(config => {
    // 确保 headers 存在
    if (!config.headers) {
        config.headers = {};
    }
    config.headers['Content-Type'] = 'application/json;charset=utf-8'; // 设置 Content-Type
    return config;
}, error => {
    return Promise.reject(error);
});

request.interceptors.response.use(response => {
    let res = response.data;
    if (typeof res === 'string') {
        res = res ? JSON.parse(res) : res;
    }
    return res;
}, error => {
    if (error.response) {
        if (error.response.status === 404) {
            ElMessage.error('未找到接口');
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常');
        } else {
            console.error(error.message);
        }
    } else {
        // 如果没有 error.response，说明请求失败
        ElMessage.error('请求失败，请检查网络连接');
    }
    return Promise.reject(error);
});

export default request;
