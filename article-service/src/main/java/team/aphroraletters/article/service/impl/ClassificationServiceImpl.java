package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.ClassificationMapper;
import team.aphroraletters.article.pojo.entity.Classification;
import team.aphroraletters.article.pojo.entity.TblArticleCategory;
import team.aphroraletters.article.pojo.entity.TblArticleInfo;
import team.aphroraletters.article.service.IClassificationService;
import team.aphroraletters.article.service.ITblArticleCategoryService;
import team.aphroraletters.article.service.ITblArticleInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * tbl_article_info与classification多对多中间表 服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2022-11-21
 */
@Service
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements IClassificationService {

    @Autowired
    private ClassificationMapper classificationMapper;

    @Autowired
    private ITblArticleInfoService articleInfoService;

    @Autowired
    private ITblArticleCategoryService articleCategoryService;

    /**
     * 通过文章ID获取对应的标签集合
     *
     * @param articleId
     * @return
     */
    @Override
    public List<TblArticleCategory> getArticleCategoryListByArticleId(Long articleId) {
        LambdaQueryWrapper<Classification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Classification::getArticleId, articleId);
        List<Classification> classificationList = classificationMapper.selectList(lambdaQueryWrapper);

        ArrayList<TblArticleCategory> categories = new ArrayList<>();
        for (Classification classification : classificationList) {
            categories.add(articleCategoryService.getArticleCategoryById(classification.getCategoryId()));
        }

        return categories;
    }

    /**
     * 通过标签分类ID和page查询分页对象
     *
     * @param categoryId
     * @param page
     * @return
     */
    @Override
    public IPage<TblArticleInfo> getArticleInfoByCategoryIdAndPage(Long categoryId, Long page) {
        IPage<TblArticleInfo> articleInfoIPage = new Page<>(page, 10);
        IPage<Classification> classificationIPage = new Page<>(page, 10);

        // 封装查询条件对象
        LambdaQueryWrapper<Classification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Classification::getCategoryId, categoryId);
        classificationIPage = classificationMapper.selectPage(classificationIPage, lambdaQueryWrapper);

        BeanUtils.copyProperties(classificationIPage, articleInfoIPage);

        // 遍历标签分类信息列表，封装文章信息列表
        ArrayList<TblArticleInfo> articleInfos = new ArrayList<>();
        for (Classification classification : classificationIPage.getRecords()) {
            articleInfos.add(articleInfoService.getArticleById(classification.getArticleId()));
        }
        articleInfoIPage.setRecords(articleInfos);

        return articleInfoIPage;
    }

    /**
     * 添加文章分类标签
     *
     * @param classification
     * @return
     */
    @Override
    public int insertArticleCategoryToArticle(Classification classification) {

        return classificationMapper.insert(classification);
    }

    @Override
    public int deleteCategory(Long articleId, Long categoryId) {
        LambdaUpdateWrapper<Classification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Classification::getArticleId, articleId)
                .eq(Classification::getCategoryId, categoryId);

        return classificationMapper.delete(updateWrapper);
    }

    @Override
    public int deleteCategoryById(Long id) {
        return classificationMapper.deleteById(id);
    }

}
