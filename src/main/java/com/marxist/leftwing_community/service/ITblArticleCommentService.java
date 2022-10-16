package com.marxist.leftwing_community.service;

import com.marxist.leftwing_community.entity.TblArticleComment;
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
public interface ITblArticleCommentService extends IService<TblArticleComment> {

    List<TblArticleComment> getComment(Long article_id);
}
