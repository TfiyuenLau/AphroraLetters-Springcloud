package com.marxist.leftwing_community.service;

import com.marxist.leftwing_community.entity.Classification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * tbl_article_info与classification多对多中间表 服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-11-21
 */
public interface IClassificationService extends IService<Classification> {

    int addCategoryToArticle(Classification classification);
}
