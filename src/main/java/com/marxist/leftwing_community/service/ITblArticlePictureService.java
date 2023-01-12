package com.marxist.leftwing_community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.TblArticleContent;
import com.marxist.leftwing_community.entity.TblArticlePicture;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 这张表用来保存题图url，每一篇文章都应该有题图 服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
public interface ITblArticlePictureService extends IService<TblArticlePicture> {
    public List<String> getAllPictureUrl();
    public TblArticlePicture getPictureUrl(Long id);

    IPage<TblArticlePicture> searchArticlePicByPage(IPage<TblArticleContent> contentIPage, Integer page);

    int addPic(TblArticlePicture articlePicture);

    void addPicByUrl(String url);

    int delPic(Long id);

    int updatePicById(Long id, String url);
}
