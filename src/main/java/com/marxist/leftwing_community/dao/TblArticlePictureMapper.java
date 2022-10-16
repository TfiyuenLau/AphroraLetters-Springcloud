package com.marxist.leftwing_community.dao;

import com.marxist.leftwing_community.entity.TblArticlePicture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 这张表用来保存题图url，每一篇文章都应该有题图 Mapper 接口
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Component
public interface TblArticlePictureMapper extends BaseMapper<TblArticlePicture> {

}
