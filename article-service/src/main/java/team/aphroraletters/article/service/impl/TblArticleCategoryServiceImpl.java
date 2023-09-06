package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.TblArticleCategoryMapper;
import team.aphroraletters.article.pojo.entity.TblArticleCategory;
import team.aphroraletters.article.pojo.entity.TblArticleInfo;
import team.aphroraletters.article.service.ITblArticleCategoryService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Service
public class TblArticleCategoryServiceImpl extends ServiceImpl<TblArticleCategoryMapper, TblArticleCategory> implements ITblArticleCategoryService {
    @Autowired
    private TblArticleCategoryMapper articleCategoryMapper;

    @Override
    public TblArticleCategory getArticleCategoryById(Long categoryId) {
        return articleCategoryMapper.selectById(categoryId);
    }

    /**
     * 通过ID获取分类标签列表
     *
     * @param id
     * @return
     */
    @Override
    public List<TblArticleCategory> getCategoriesByArticleId(Long id) {

        return articleCategoryMapper.getCategoriesByArticleId(id);
    }

    /**
     * 通过分类标签名字封装文章对象列表
     *
     * @param categoryName
     * @return
     */
    @Override
    public List<TblArticleInfo> getInfoByCategory(String categoryName) {

        return articleCategoryMapper.getInfoByCategory(categoryName);
    }

    /**
     * 按文章标签搜索分类标签并返回封装对象
     *
     * @param category 分类标签名字
     * @return QueryWrapper
     */
    public List<TblArticleCategory> getCategoryByName(String category) {
        //查询封装对象
        QueryWrapper<TblArticleCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", category);

        return articleCategoryMapper.selectList(queryWrapper);
    }

    /**
     * 获取全部分类标签
     *
     * @return
     */
    @Override
    public List<TblArticleCategory> getAllCategory() {

        return articleCategoryMapper.selectList(null);
    }

    /**
     * 模糊查询标签集合
     *
     * @param categoryName
     * @return
     */
    @Override
    public List<TblArticleCategory> getRecommendCategory(String categoryName) {
        LambdaQueryWrapper<TblArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(TblArticleCategory::getCategoryName, categoryName);

        return articleCategoryMapper.selectList(queryWrapper);
    }

    /**
     * 添加文章类型标签数据并返回添加的值
     *
     * @param articleCategory
     * @return
     */
    @Override
    public Long insertCategory(TblArticleCategory articleCategory) {
        List<TblArticleCategory> selectList = articleCategoryMapper
                .selectList(new LambdaQueryWrapper<TblArticleCategory>().eq(TblArticleCategory::getCategoryName, articleCategory.getCategoryName()));
        if (selectList.isEmpty()) {
            articleCategoryMapper.insert(articleCategory);
            return articleCategory.getId();
        } else {
            return -1L; // 标签已存在
        }
    }

}
