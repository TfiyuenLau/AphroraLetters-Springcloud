package com.marxist.leftwing_community.service.impl;

import com.marxist.leftwing_community.entity.Classification;
import com.marxist.leftwing_community.dao.ClassificationMapper;
import com.marxist.leftwing_community.service.IClassificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * tbl_article_info与classification多对多中间表 服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-11-21
 */
@Service
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements IClassificationService {
    @Autowired
    private ClassificationMapper classificationMapper;

    /**
     * 添加文章分类标签
     *
     * @param classification
     * @return
     */
    @Override
    public int addCategoryToArticle(Classification classification) {

        return classificationMapper.insert(classification);
    }

}
