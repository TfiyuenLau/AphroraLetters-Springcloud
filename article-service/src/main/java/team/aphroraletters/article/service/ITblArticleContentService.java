package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.pojo.entity.TblArticleContent;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
public interface ITblArticleContentService extends IService<TblArticleContent> {
    TblArticleContent getArticleContentById(Long articleId);

    TblArticleContent getArticleContentByArticleId(Long articleId);

    public void toHtmlArticleContent(Long id) throws IOException;

    public IPage<TblArticleContent> searchContent(String contentLike, Integer page);

    Long addContentByFile(File file, String summary) throws IOException;

    int insertArticleContent(TblArticleContent articleContent);

    int delContent(Long id);

    int updateArticleContent(TblArticleContent articleContent);
}
