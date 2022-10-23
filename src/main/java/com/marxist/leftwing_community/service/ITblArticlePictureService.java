package com.marxist.leftwing_community.service;

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
    public List<TblArticlePicture> getPictureUrl(Long id);

    int addPic(TblArticlePicture articlePicture);

    void addPicByUrl(String url);
}
