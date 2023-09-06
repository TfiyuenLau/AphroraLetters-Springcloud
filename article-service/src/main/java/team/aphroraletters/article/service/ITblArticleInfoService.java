package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.entity.TblArticleContent;
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
public interface ITblArticleInfoService extends IService<TblArticleInfo> {
    //获取所有文章
    public List<TblArticleInfo> getAllArticleInfo();

    TblArticleInfo getArticleById(Long id);

    public String getArticleTitle(Long id);

    public IPage<TblArticleInfo> getArticleByPage(Long page);

    IPage<TblArticleInfo> searchArticleInfoByPage(IPage<TblArticleContent> contentIPage, Integer page);

    int insertArticleInfo(TblArticleInfo articleInfo);

    int deleteArticleInfoById(Long id);

    int updateArticleInfo(Long id, String title, String summary);

    int updateArticleInfo(TblArticleInfo articleInfo);

    List<TblArticleInfo> getRecommendArticle();
}
