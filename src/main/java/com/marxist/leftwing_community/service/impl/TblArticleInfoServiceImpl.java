package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.entity.TblArticleContent;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.dao.TblArticleInfoMapper;
import com.marxist.leftwing_community.service.ITblArticleInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private TblArticleContentServiceImpl articleContentService;

    /**
     * 获取所有文章信息
     *
     * @return
     */
    @Override
    public List<TblArticleInfo> getAllArticleInfo() {

        return articleInfoMapper.selectList(null);
    }

    @Override
    public TblArticleInfo getArticleById(Long id) {
        return articleInfoMapper.selectById(id);
    }

    /**
     * 按id获取文章标题
     *
     * @param id
     * @return
     */
    @Override
    public String getArticleTitle(Long id) {
        TblArticleInfo articleInfo = articleInfoMapper.selectById(id);

        return articleInfo.getTitle();
    }

    /**
     * 分页插件，返回分页后的数据
     *
     * @param page
     * @return IPage
     */
    @Override
    public IPage<TblArticleInfo> getArticleByPage(Long page) {
        IPage<TblArticleInfo> infoIPage = new Page<>(page, 10);//第page页，每页10条数据
        articleInfoMapper.selectPage(infoIPage, null);

        return infoIPage;
    }

    /**
     * 通过content查询返回分页的Info对象集合
     *
     * @param contentLike
     * @param page
     * @return
     */
    @Override
    public IPage<TblArticleInfo> searchArticleInfoByPage(String contentLike, Integer page) {
        //模糊查询获取Content对象集合
        List<TblArticleContent> articleContents = articleContentService.searchContentByPage(contentLike, page).getRecords();
        List<Long> articleIds = new ArrayList<>();
        for (TblArticleContent articleContent : articleContents) {
            articleIds.add(articleContent.getArticleId());
        }

        //按Content对象的article_id查询获取Info分页对象集合并返回
        IPage<TblArticleInfo> infoIPage = new Page<>(page, 10);
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
    public int addInfo(TblArticleInfo articleInfo) {

        return articleInfoMapper.insert(articleInfo);
    }

    /**
     * 逻辑删除文章
     * @param id 文章id
     * @return
     */
    @Override
    public int delArticle(Long id) {

        return articleInfoMapper.deleteById(id);
    }

}
