import type {ApiResult} from "../models/ApiResult";
import type {Announcement} from "../models/Announcement";

enum AnnouncementApiUrl {
    getAnnouncements = "/api/article/getAnnouncements",
    getAnnouncementList = "/api/article/getAnnouncementList",
    getAnnouncementById = "/api/article/getAnnouncementById",
}

// 获取部分广告栏展示对象列表
const getAnnouncements = async (): Promise<ApiResult<Announcement[]>> => {
    return await $fetch(AnnouncementApiUrl.getAnnouncements).catch(error => {
        openErrorNotification(`${error.data.status}`, "数据获取失败");
    }) as Promise<ApiResult<Announcement[]>>;
};

// 获取全部广告栏展示对象列表
const getAnnouncementList = async (): Promise<ApiResult<Announcement[]>> => {
    return await $fetch(AnnouncementApiUrl.getAnnouncementList).catch(error => {
        openErrorNotification(`${error.data.status}`, "数据获取失败");
    }) as Promise<ApiResult<Announcement[]>>;
};

// 获取全部广告栏展示对象列表
const getAnnouncementById = async (announcementId: number): Promise<ApiResult<Announcement>> => {
    return await $fetch(AnnouncementApiUrl.getAnnouncementById + "/" + announcementId).catch(error => {
        openErrorNotification(`${error.data.status}`, "数据获取失败");
    }) as Promise<ApiResult<Announcement>>;
};

export {
    getAnnouncements,
    getAnnouncementList,
    getAnnouncementById,
};
