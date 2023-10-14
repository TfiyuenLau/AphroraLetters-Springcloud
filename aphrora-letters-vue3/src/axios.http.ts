import type { AxiosInstance, AxiosRequestConfig, AxiosRequestHeaders } from "axios";
import axios from "axios";
import {openErrorNotification} from "@/utils/notification";

/**
 * axios封装类
 */
class AxiosHttp {
    private readonly options: AxiosRequestConfig;
    private axiosInstance: AxiosInstance;

    // 构造函数 参数 options
    constructor(params: AxiosRequestConfig) {
        this.options = params;
        this.axiosInstance = axios.create(params); // 生成实例
        this.setupInterceptors();
    }

    private setupInterceptors() {
        this.axiosInstance.defaults.baseURL = "/";
        this.axiosInstance.defaults.headers.post["Content-Type"] =
            "application/json";
        this.axiosInstance.interceptors.request.use(
            (config) => {
                config.headers = {} as AxiosRequestHeaders; // explicitly define headers as empty AxiosRequestHeaders object
                // config.headers.Authorization = CSRF_TOKEN;
                return config;
            },
            () => {
                return Promise.reject({
                    code: 1,
                    message: "请求错误，请联系管理员",
                });
            }
        );

        this.axiosInstance.interceptors.response.use(
            (response) => {
                return Promise.resolve(response);
            },
            (error) => {
                let message = "";
                if (error.response) {
                    switch (error.response.status) {
                        case 400:
                            message = "请求错误，请重试";
                            break;
                        case 404:
                            message = "请求错误，没有找到服务器";
                            break;
                        case 429:
                            message = "请求过于频繁，请在一分钟后重试";
                            openErrorNotification("429", message);
                            break;
                        default:
                            message = "未知错误，请联系管理员";
                            break;
                    }
                } else {
                    if (error.code && error.code == "ECONNABORTED") {
                        message = "请求超时，请检查网是否正常";
                    } else {
                        message = "未知错误，请稍后再试";
                    }
                }
                return Promise.reject({
                    code: -1,
                    message: message,
                });
            }
        );
    }

    /**
     * AxiosHttp get
     * @param url 请求路径
     * @param config 配置信息
     * @returns Promise
     */
    get(url: string, config?: any): Promise<any> {
        return new Promise((resolve, reject) => {
            this.axiosInstance
                .get(url, config)
                .then((response) => {
                    resolve(response.data);
                })
                .catch((error) => {
                    reject(error);
                });
        });
    }

    /**
     * AxiosHttp post
     * @param url 请求路径
     * @param data 请求数据
     * @param config 配置
     * @returns Promise
     */
    post(url: string, data?: any, config?: any): Promise<any> {
        return new Promise((resolve, reject) => {
            this.axiosInstance
                .post(url, data, config)
                .then((response) => {
                    resolve(response.data);
                })
                .catch((error) => {
                    reject(error);
                });
        });
    }
}
const http = new AxiosHttp({
    timeout: 1000 * 5,
});
export default http;
