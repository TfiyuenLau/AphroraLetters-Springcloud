/**
 * 后端API通用封装视图对象
 */
export interface ApiResult<T> {
    status: number,
    msg: string,
    data: T,
}

/**
 * 后端MBP分页视图对象
 */
export interface Pagination<T> {
    records: T[];
    total: number;
    size: number;
    current: number;
    orders: any[];
    optimizeCountSql: boolean;
    searchCount: boolean;
    maxLimit: number
    countId: number;
    pages: number;
}
