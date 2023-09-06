package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.entity.TblArticleComment;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
public interface ITblArticleCommentService extends IService<TblArticleComment> {

    List<TblArticleComment> getComment(Long article_id);

    boolean insertComment(TblArticleComment articleComment);
}
