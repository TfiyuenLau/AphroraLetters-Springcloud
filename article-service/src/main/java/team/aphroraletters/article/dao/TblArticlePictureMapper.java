package team.aphroraletters.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import team.aphroraletters.article.pojo.entity.TblArticlePicture;

/**
 * <p>
 * 这张表用来保存题图url，每一篇文章都应该有题图 Mapper 接口
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Deprecated
@Component
public interface TblArticlePictureMapper extends BaseMapper<TblArticlePicture> {

}
