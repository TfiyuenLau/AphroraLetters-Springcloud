import type {ApiResult} from "../models/ApiResult";
import type {VersionLog} from "../models/VersionLog";

enum VersionLogApi {
    getVersionLogs = "/api/article/getVersionLogs",
    getVersionLogList = "/api/article/getVersionLogList",
}

// 获取最近的版本日志列表
const getVersionLogs = async ():Promise<ApiResult<VersionLog[]>> => {
    return await $fetch(VersionLogApi.getVersionLogs).catch(error => {
        openErrorNotification(`${error.data.status}`, "数据获取失败");
    }) as Promise<ApiResult<VersionLog[]>>;
}

// 获取最近的版本日志列表
const getVersionLogList = async ():Promise<ApiResult<VersionLog[]>> => {
    return await $fetch(VersionLogApi.getVersionLogList).catch(error => {
        openErrorNotification(`${error.data.status}`, "数据获取失败");
    }) as Promise<ApiResult<VersionLog[]>>;
}

export {
    getVersionLogs,
    getVersionLogList,
}
