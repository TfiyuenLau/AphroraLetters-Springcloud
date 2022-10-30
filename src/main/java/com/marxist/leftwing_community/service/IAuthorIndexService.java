package com.marxist.leftwing_community.service;

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

    List<AuthorIndex> getListByAuthorId(Long authorId);
}
