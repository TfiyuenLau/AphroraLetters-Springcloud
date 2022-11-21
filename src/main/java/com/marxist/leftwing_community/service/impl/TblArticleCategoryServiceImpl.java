package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marxist.leftwing_community.entity.TblArticleCategory;
import com.marxist.leftwing_community.dao.TblArticleCategoryMapper;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.service.ITblArticleCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 添加文章类型标签数据
     *
     * @param articleCategory
     * @return
     */
    @Override
    public int addCategory(TblArticleCategory articleCategory) {

        return articleCategoryMapper.insert(articleCategory);
    }

}
