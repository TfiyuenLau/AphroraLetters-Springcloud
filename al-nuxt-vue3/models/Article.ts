export interface ArticleInfo {
    id: number;
    title: string;
    pictureUrl: string,
    summary: string;
    isTop: boolean;
    traffic: number;
    createBy: string;
    modifiedBy: string;
    isEffective: boolean;
    categoryList: null | string[];
    time: string;
}

export interface Article {
    id: number;
    title: string;
    pictureUrl: string;
    summary: string;
    content: string;
    createBy: string;
    modifiedBy: string;
    categoryList: Category[];
    articleCommentList: ArticleComment[];
    time: string;
}

export interface Category {
    id: number;
    categoryName: string;
    createBy: string | null;
    modifiedBy: string | null;
    articleInfoList: Article[] | null;
    articleCategoryList: Category[] | null;
}

export interface ArticleComment {
    id: number | null;
    articleId: number;
    email: string;
    comment: string;
    createBy: string | null;
    isEffective: boolean;
}
