import type {ApiResult} from "../models/ApiResult";
import type {AuthorIndex, LibraryAuthor} from "../models/Library";

enum LibraryApiUrl {
    "getAuthorList" = "/api/library/getAuthorList",
    "getAuthorByAuthorId" = "/api/library/getAuthorByAuthorId",
    "getAuthorIndexListByAuthorId" = "/api/library/getAuthorIndexListByAuthorId",
}

// 获取文库收录的作者
const getAuthorList = async (): Promise<ApiResult<LibraryAuthor[]>> => {
    return await $fetch(LibraryApiUrl.getAuthorList).catch(error => {
        openErrorNotification(`${error.data.status}`, "作者列表获取失败");
    }) as Promise<ApiResult<LibraryAuthor[]>>;
}

// 通过作者id获取文库作者信息
const getAuthorByAuthorId = async (authorId: number): Promise<ApiResult<LibraryAuthor>> => {
    return await $fetch(LibraryApiUrl.getAuthorByAuthorId + "/" + authorId).catch(error => {
        openErrorNotification(`${error.data.status}`, "作者信息获取失败");
    }) as Promise<ApiResult<LibraryAuthor>>;
}

// 通过作者id获取其文章列表
const getAuthorIndexListByAuthorId = async (authorId: number): Promise<ApiResult<AuthorIndex[]>> => {
    return await $fetch(LibraryApiUrl.getAuthorIndexListByAuthorId + "/" + authorId).catch(error => {
        openErrorNotification(`${error.data.status}`, "作者文章列表获取失败");
    }) as Promise<ApiResult<AuthorIndex[]>>;
}

export {
    getAuthorList,
    getAuthorByAuthorId,
    getAuthorIndexListByAuthorId,
}
