package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.entity.Classification;
import team.aphroraletters.article.entity.TblArticleCategory;
import team.aphroraletters.article.entity.TblArticleInfo;

import java.util.List;

/**
 * <p>
 * tbl_article_info与classification多对多中间表 服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-11-21
 */
public interface IClassificationService extends IService<Classification> {

    List<TblArticleCategory> getArticleCategoryListByArticleId(Long articleId);

    IPage<TblArticleInfo> getArticleInfoByCategoryIdAndPage(Long categoryId, Long page);

    int insertArticleCategoryToArticle(Classification classification);

    int deleteCategory(Long articleId, Long categoryId);

    int deleteCategoryById(Long id);
}
