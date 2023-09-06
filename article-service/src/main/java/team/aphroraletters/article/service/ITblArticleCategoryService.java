package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.entity.TblArticleCategory;
import team.aphroraletters.article.entity.TblArticleInfo;

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

    TblArticleCategory getArticleCategoryById(Long categoryId);

    List<TblArticleCategory> getCategoriesByArticleId(Long id);

    List<TblArticleInfo> getInfoByCategory(String categoryName);

    List<TblArticleCategory> getAllCategory();

    List<TblArticleCategory> getRecommendCategory(String categoryName);

    Long insertCategory(TblArticleCategory articleCategory);
}
