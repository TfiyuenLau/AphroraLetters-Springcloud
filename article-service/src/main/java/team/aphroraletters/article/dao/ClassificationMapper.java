package team.aphroraletters.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import team.aphroraletters.article.entity.Classification;

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
