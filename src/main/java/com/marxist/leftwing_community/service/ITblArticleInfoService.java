package com.marxist.leftwing_community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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

    IPage<TblArticleInfo> searchArticleInfoByPage(String contentLike, Integer page);

    int addInfo(TblArticleInfo articleInfo);

    int delArticle(Long id);
}
