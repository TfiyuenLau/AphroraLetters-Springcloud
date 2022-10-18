package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marxist.leftwing_community.entity.TblArticleComment;
import com.marxist.leftwing_community.dao.TblArticleCommentMapper;
import com.marxist.leftwing_community.service.ITblArticleCommentService;
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
public class TblArticleCommentServiceImpl extends ServiceImpl<TblArticleCommentMapper, TblArticleComment> implements ITblArticleCommentService {
    @Autowired
    private TblArticleCommentMapper articleCommentMapper;

    @Override
    public List<TblArticleComment> getComment(Long article_id) {
        QueryWrapper<TblArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", article_id).orderByDesc("id");//查询并按id降序排序

        return articleCommentMapper.selectList(queryWrapper);
    }

    public boolean insertComment(TblArticleComment articleComment) {
        articleCommentMapper.insert(articleComment);

        return true;
    }

}
