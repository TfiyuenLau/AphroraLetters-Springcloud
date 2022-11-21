package com.marxist.leftwing_community.dao;

import com.marxist.leftwing_community.entity.Classification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * tbl_article_info与classification多对多中间表 Mapper 接口
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-11-21
 */
@Component
public interface ClassificationMapper extends BaseMapper<Classification> {

}
