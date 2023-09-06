package team.aphroraletters.library.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.aphroraletters.library.pojo.entity.AuthorIndex;
import team.aphroraletters.library.pojo.entity.LibraryAuthor;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
@Component
public interface AuthorIndexMapper extends BaseMapper<AuthorIndex> {

    /**
     * 查询所有封装了LibraryAuthor对象的authorIndex对象
     *
     * @return
     */
    @Select("SELECT * FROM author_index WHERE is_effective = 1")//启用逻辑查询
    @Results({
            @Result(property = "articleId", column = "article_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "pdfUrl", column = "pdf_url"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "libraryAuthor",
                    column = "author_id",
                    javaType = LibraryAuthor.class,
                    many = @Many(select = "team.aphroraletters.library.dao.LibraryAuthorMapper.getAuthorByAuthorId")),
    })
    List<AuthorIndex> getAllAuthorIndex();

    /**
     * 分页查询：查询所有封装了LibraryAuthor对象的authorIndex对象
     *
     * @return
     */
    @Select("SELECT * FROM author_index WHERE is_effective = 1")//启用逻辑查询
    @Results({
            @Result(property = "articleId", column = "article_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "pdfUrl", column = "pdf_url"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "libraryAuthor",
                    column = "author_id",
                    javaType = LibraryAuthor.class,
                    many = @Many(select = "team.aphroraletters.library.dao.LibraryAuthorMapper.getAuthorByAuthorId")),
    })
    IPage<AuthorIndex> getAllAuthorIndexByPage(IPage<AuthorIndex> page);

    /**
     * 根据author_id查询所有索引作者对象
     * （同时为LibraryAuthorMapper提供接口方法）
     *
     * @param authorId 外键作者ID
     * @return AuthorIndex对象列表
     */
    @Select("SELECT * from author_index where author_id = #{author_id}")
    List<AuthorIndex> getByAuthorId(@Param(value = "author_id") Long authorId);

}
