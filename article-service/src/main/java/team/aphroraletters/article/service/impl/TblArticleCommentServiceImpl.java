package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.TblArticleCommentMapper;
import team.aphroraletters.article.pojo.entity.TblArticleComment;
import team.aphroraletters.article.service.ITblArticleCommentService;

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

    @Override
    public boolean insertComment(TblArticleComment articleComment) {
        articleCommentMapper.insert(articleComment);

        return true;
    }

}
