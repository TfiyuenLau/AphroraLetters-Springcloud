package com.marxist.leftwing_community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.AuthorIndex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
public interface IAuthorIndexService extends IService<AuthorIndex> {

    List<AuthorIndex> getAllAuthorIndex();

    IPage<AuthorIndex> getAllAuthorIndexByPage(Long page);

    List<AuthorIndex> getListByAuthorId(Long authorId);

    int addAuthorIndex(AuthorIndex authorIndex);

    int deleteAuthorIndex(Long articleId);
}
