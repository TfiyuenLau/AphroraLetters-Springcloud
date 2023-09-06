package team.aphroraletters.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.aphroraletters.article.pojo.entity.TblArticleCategory;
import team.aphroraletters.article.pojo.entity.TblArticleInfo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Component
public interface TblArticleCategoryMapper extends BaseMapper<TblArticleCategory> {

    /**
     * 通过文章id获取所有标签并封装
     *
     * @param id 文章ID
     * @return 标签列表
     */
    @Select("SELECT\n" +
            "tbl_article_category.id, tbl_article_category.category_name\n" +
            "from\n" +
            "tbl_article_info ,tbl_article_category, classification\n" +
            "WHERE\n" +
            "classification.article_id = tbl_article_info.id and classification.category_id = tbl_article_category.id and tbl_article_info.id = #{id} and classification.is_effective = 1")
    List<TblArticleCategory> getCategoriesByArticleId(@Param("id")Long id);

    /**
     * 通过分类标签名字封装文章对象列表
     *
     * @param categoryName
     * @return
     */
    @Select("SELECT\n" +
            "\ttbl_article_info.id,tbl_article_info.title,tbl_article_info.summary,tbl_article_info.modified_by\n" +
            "FROM\n" +
            "\ttbl_article_info ,tbl_article_category, classification\n" +
            "WHERE\n" +
            "\tclassification.article_id = tbl_article_info.id and classification.category_id = tbl_article_category.id and tbl_article_category.category_name = #{category_name} and tbl_article_info.is_effective = 1")
    List<TblArticleInfo> getInfoByCategory(@Param("category_name") String categoryName);

    /**
     * 插入一条articleCategory数据并返回自增ID
     *
     * @param articleCategory
     * @return
     */
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TblArticleCategory articleCategory);

}
