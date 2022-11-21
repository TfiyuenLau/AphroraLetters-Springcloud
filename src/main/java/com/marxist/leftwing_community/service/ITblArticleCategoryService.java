package com.marxist.leftwing_community.service;

import com.marxist.leftwing_community.entity.TblArticleCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.marxist.leftwing_community.entity.TblArticleInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
public interface ITblArticleCategoryService extends IService<TblArticleCategory> {

    List<TblArticleCategory> getCategoriesByArticleId(Long id);

    List<TblArticleInfo> getInfoByCategory(String categoryName);

    List<TblArticleCategory> getAllCategory();

    int addCategory(TblArticleCategory articleCategory);
}
