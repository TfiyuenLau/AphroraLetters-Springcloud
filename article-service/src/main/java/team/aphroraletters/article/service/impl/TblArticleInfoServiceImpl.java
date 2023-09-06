package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.TblArticleInfoMapper;
import team.aphroraletters.article.pojo.entity.TblArticleContent;
import team.aphroraletters.article.pojo.entity.TblArticleInfo;
import team.aphroraletters.article.service.ITblArticleInfoService;

import java.util.ArrayList;
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
public class TblArticleInfoServiceImpl extends ServiceImpl<TblArticleInfoMapper, TblArticleInfo> implements ITblArticleInfoService {
    @Autowired
    private TblArticleInfoMapper articleInfoMapper;

    /**
     * 获取所有文章信息
     *
     * @return 封装了所有文章信息的List对象
     */
    @Override
    public List<TblArticleInfo> getAllArticleInfo() {

        return articleInfoMapper.selectList(null);
    }

    /**
     * 通过id查询文章数据
     *
     * @param id 文章id
     * @return 一条文章数据
     */
    @Override
    public TblArticleInfo getArticleById(Long id) {
        return articleInfoMapper.selectById(id);
    }

    /**
     * 按id获取文章标题
     *
     * @param id 文章di
     * @return 文章标题
     */
    @Override
    public String getArticleTitle(Long id) {
        TblArticleInfo articleInfo = articleInfoMapper.selectById(id);

        return articleInfo.getTitle();
    }

    /**
     * 分页查询文章
     *
     * @param page 页码
     * @return IPage
     */
    @Override
    public IPage<TblArticleInfo> getArticleByPage(Long page) {
        IPage<TblArticleInfo> infoIPage = new Page<>(page, 10);//第page页，每页10条数据
        articleInfoMapper.selectPage(
                infoIPage,
                new QueryWrapper<TblArticleInfo>().orderByDesc("id")//倒叙查询
        );

        return infoIPage;
    }

    /**
     * 通过content查询返回分页的Info对象集合
     *
     * @param contentIPage 搜索字段
     * @param page 页码
     * @return infoIPage
     */
    @Override
    public IPage<TblArticleInfo> searchArticleInfoByPage(IPage<TblArticleContent> contentIPage, Integer page) {
        //获取Content对象集合后封装id
        List<TblArticleContent> articleContents = contentIPage.getRecords();
        List<Long> articleIds = new ArrayList<>();
        for (TblArticleContent articleContent : articleContents) {
            articleIds.add(articleContent.getArticleId());
        }

        //按Content对象的article_id查询获取Info分页对象集合并返回
        IPage<TblArticleInfo> infoIPage = new Page<>(1, 10);//查询时已经设定了当前页码page，故固定为1
        QueryWrapper<TblArticleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", articleIds);
        try {
            articleInfoMapper.selectPage(infoIPage, queryWrapper);
        } catch (Exception e) {
            //异常处理：没有查找出文章
            TblArticleInfo errorArticleInfo = new TblArticleInfo();
            errorArticleInfo.setId(1L);
            errorArticleInfo.setTitle("很抱歉，没有您要找的关键词...");
            errorArticleInfo.setSummary("null");

            ArrayList<TblArticleInfo> arrayList = new ArrayList<>();
            arrayList.add(errorArticleInfo);
            return new Page<TblArticleInfo>(page, 10).setRecords(arrayList);
        }

        return infoIPage;
    }

    @Override
    public int insertArticleInfo(TblArticleInfo articleInfo) {

        return articleInfoMapper.insert(articleInfo);
    }

    /**
     * 部分更新文章信息数据
     *
     * @param id 文章id
     * @param title 文章标题
     * @param summary 文章综述
     * @return 修改数据条数
     */
    @Override
    public int updateArticleInfo(Long id, String title, String summary) {
        // 封装对象
        TblArticleInfo articleInfo = new TblArticleInfo();
        articleInfo.setId(id);
        articleInfo.setTitle(title);
        articleInfo.setSummary(summary); // 为null则不更新

        return articleInfoMapper.updateById(articleInfo);
    }

    /**
     * 直接修改
     *
     * @param articleInfo
     * @return
     */
    @Override
    public int updateArticleInfo(TblArticleInfo articleInfo) {
        return articleInfoMapper.updateById(articleInfo);
    }

    /**
     * 逻辑删除文章
     *
     * @param id 文章id
     * @return 删除数据条数
     */
    @Override
    public int deleteArticleInfoById(Long id) {

        return articleInfoMapper.deleteById(id);
    }

    /**
     * 获取推荐文章
     *
     * @return List<TblArticleInfo>
     */
    @Override
    public List<TblArticleInfo> getRecommendArticle() {
        //推荐倒数的8篇文章
        IPage<TblArticleInfo> infoPage = new Page<>(1, 8);

        return articleInfoMapper.selectPage(infoPage, new QueryWrapper<TblArticleInfo>().orderByDesc("id")).getRecords();
    }

}
