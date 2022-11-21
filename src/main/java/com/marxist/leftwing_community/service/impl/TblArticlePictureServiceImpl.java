package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.entity.TblArticleContent;
import com.marxist.leftwing_community.entity.TblArticlePicture;
import com.marxist.leftwing_community.dao.TblArticlePictureMapper;
import com.marxist.leftwing_community.service.ITblArticlePictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 这张表用来保存题图url，每一篇文章都应该有题图 服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Service
public class TblArticlePictureServiceImpl extends ServiceImpl<TblArticlePictureMapper, TblArticlePicture> implements ITblArticlePictureService {
    @Autowired
    private TblArticlePictureMapper articlePictureMapper;

    @Autowired
    private TblArticleContentServiceImpl articleContentService;

    /**
     * 获取所有图片url
     *
     * @return
     */
    @Override
    public List<String> getAllPictureUrl() {
        List<TblArticlePicture> articlePictures = articlePictureMapper.selectList(null);

        ArrayList<String> urlList = new ArrayList<>();
        for (TblArticlePicture articlePicture : articlePictures) {
            urlList.add(articlePicture.getPictureUrl());
        }

        return urlList;
    }

    /**
     * 按文章id查询图片对象返回列表
     *
     * @param id
     * @return
     */
    @Override
    public TblArticlePicture getPictureUrl(Long id) {
        //按id查询文章图片返回列表
        QueryWrapper<TblArticlePicture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", id);

        return articlePictureMapper.selectOne(queryWrapper);
    }

    /**
     * 通过content查询返回分页的Picture对象集合
     *
     * @param contentIPage 搜索后的字段页码
     * @param page        页码
     * @return
     */
    @Override
    public IPage<TblArticlePicture> searchArticlePicByPage(IPage<TblArticleContent> contentIPage, Integer page) {
        //模糊查询获取Content对象集合
        List<TblArticleContent> articleContents = contentIPage.getRecords();
        List<Long> articleIds = new ArrayList<>();
        for (TblArticleContent articleContent : articleContents) {
            articleIds.add(articleContent.getArticleId());
        }

        //按Content对象的article_id查询获取Picture分页对象集合并返回
        IPage<TblArticlePicture> pictureIPage = new Page<>(page, 10);
        QueryWrapper<TblArticlePicture> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", articleIds);
        try {
            articlePictureMapper.selectPage(pictureIPage, queryWrapper);
        } catch (Exception e) {
            //异常处理：没有查找出文章
            TblArticlePicture errorArticlePic = new TblArticlePicture();
            errorArticlePic.setId(1L);
            errorArticlePic.setArticleId(1L);
            errorArticlePic.setPictureUrl("img/维护中.png");

            ArrayList<TblArticlePicture> arrayList = new ArrayList<>();
            arrayList.add(errorArticlePic);
            return new Page<TblArticlePicture>(page, 10).setRecords(arrayList);
        }

        return pictureIPage;
    }

    /**
     * 添加图片
     *
     * @param articlePicture
     * @return
     */
    @Override
    public int addPic(TblArticlePicture articlePicture) {

        return articlePictureMapper.insert(articlePicture);
    }

    /**
     * 通过图片地址添加图片对象
     *
     * @param url 图片地址
     */
    @Override
    public void addPicByUrl(String url) {
        TblArticlePicture articlePicture = new TblArticlePicture();
        articlePicture.setArticleId(articlePictureMapper.selectList(new QueryWrapper<TblArticlePicture>().orderByDesc("id")).get(0).getArticleId() + 1);
        articlePicture.setPictureUrl(url);

        addPic(articlePicture);
    }

    /**
     * 通过id删除图片
     *
     * @param id
     * @return
     */
    @Override
    public int delPic(Long id) {

        return articlePictureMapper.deleteById(id);
    }

}
