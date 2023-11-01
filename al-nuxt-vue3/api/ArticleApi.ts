import type {ApiResult, Pagination} from "../models/ApiResult";
import type {Article, ArticleComment, ArticleInfo} from "../models/Article";
import type {Category} from "../models/Article";

/**
 * Article请求URl枚举
 */
enum ArticleApiUrl {
    getRecommendArticles = "/api/article/getRecommendArticles",
    getArticleListByPage = "/api/article/getArticleListByPage",
    getArticleById = "/api/article/getArticleById",
    getArticleCategoryById = "/api/article/getArticleCategoryById",
    getArticleListByCategoryIdAndPage = "/api/article/getArticleListByCategoryIdAndPage",
    searchArticleByContent = "/api/article/searchArticleByContent",
    insertArticleComment = "/api/article/insertArticleComment",
}

// 获取推荐文章列表
const getRecommendArticles = async (): Promise<ApiResult<ArticleInfo[]>> => {
    return await $fetch(ArticleApiUrl.getRecommendArticles).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "获取推荐文章失败");
    }) as Promise<ApiResult<ArticleInfo[]>>;
};

// 获取分页文章列表
const getArticleListByPage = async (page: number): Promise<ApiResult<Pagination<ArticleInfo>>> => {
    return await $fetch(ArticleApiUrl.getArticleListByPage + "/" + page).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "获取文章列表失败");
    }) as Promise<ApiResult<Pagination<ArticleInfo>>>;
};

// 根据ID获取文章内容
const getArticleById = async (articleId: number): Promise<ApiResult<Article>> => {
    return await $fetch(ArticleApiUrl.getArticleById + "/" + articleId).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "文章内容获取失败");
    }) as Promise<ApiResult<Article>>;
};

// 根据标签ID获取文章标签
const getArticleCategoryById = async (categoryId: number): Promise<ApiResult<Category>> => {
    return await $fetch(ArticleApiUrl.getArticleCategoryById + "/" + categoryId).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "无法获取ID[" + categoryId + "]对应的文章标签");
    }) as Promise<ApiResult<Category>>;
};

// 通过页码和标签ID获取分页标签列表
const getArticleListByCategoryIdAndPage = async (page: number, categoryId: number): Promise<ApiResult<Pagination<ArticleInfo>>> => {
    return await $fetch(ArticleApiUrl.getArticleListByCategoryIdAndPage + "/" + categoryId + "/" + page).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "无法获取ID[" + categoryId + "]对应的文章列表");
    }) as Promise<ApiResult<Pagination<ArticleInfo>>>;
};

// 通过页码和标签ID获取分页标签列表
const searchArticleByContent = async (content: string, page: number): Promise<ApiResult<Pagination<ArticleInfo>>> => {
    return await $fetch(ArticleApiUrl.searchArticleByContent + "?content=" + content + '&page=' + page).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "没有找到[" + content + "]对应的文章");
    }) as Promise<ApiResult<Pagination<ArticleInfo>>>;
};

// 发送文章评论
const sendArticleComment = async (commentData: ArticleComment): Promise<ApiResult<any>> => {
    return await $fetch(ArticleApiUrl.insertArticleComment, {
        method: "post",
        body: commentData,
    }).catch(error => {
        openErrorNotification(`${error.data ? error.data.status : '请求发生错误'}`, "评论失败，请稍后重试");
    }) as Promise<ApiResult<any>>;
};

export {
    getRecommendArticles,
    getArticleListByPage,
    getArticleById,
    getArticleCategoryById,
    getArticleListByCategoryIdAndPage,
    searchArticleByContent,
    sendArticleComment,
};
