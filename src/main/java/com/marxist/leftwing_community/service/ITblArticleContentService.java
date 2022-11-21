package com.marxist.leftwing_community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.TblArticleContent;
import com.baomidou.mybatisplus.extension.service.IService;

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
    public void toHtmlArticleContent(Long id) throws IOException;

    public IPage<TblArticleContent> searchContent(String contentLike, Integer page);

    Long addContentByFile(File file, String summary) throws IOException;

    int delContent(Long id);
}
