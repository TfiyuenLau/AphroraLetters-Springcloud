package com.marxist.leftwing_community.dao;

import com.marxist.leftwing_community.entity.LibraryAuthor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

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
public interface LibraryAuthorMapper extends BaseMapper<LibraryAuthor> {

    /**
     * 查询全部文库作者对象；多表查询，将AuthorIndex集合封装至LibraryAuthor对象的authorIndices中
     * 其中LibraryAuthor对象的id与AuthorIndex外键author_id相等
     *
     * @return 返回封装了AuthorIndex集合的LibraryAuthor对象集合
     */
    @Select("SELECT * FROM library_author WHERE is_effective = 1")//启用逻辑查询
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "characterName",column = "character_name"),
            @Result(property = "picUrl",column = "pic_url"),
            @Result(property = "introduction",column = "introduction"),
            @Result(property = "authorIndices",
                    column = "id",
                    javaType = List.class,
                    many = @Many(select = "com.marxist.leftwing_community.dao.AuthorIndexMapper.getByAuthorId"))
    })
    List<LibraryAuthor> getAllAuthorIndex();

    /**
     * 查询单个文库作者对象；多表查询，将AuthorIndex集合封装至LibraryAuthor对象的authorIndices中
     * 其中LibraryAuthor对象的id与AuthorIndex外键author_id相等
     *
     * @param id 主键，与AuthorIndex外键相等
     * @return 返回一个封装了AuthorIndex集合的LibraryAuthor对象
     */
    @Select("SELECT * FROM library_author WHERE id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "characterName",column = "character_name"),
            @Result(property = "picUrl",column = "pic_url"),
            @Result(property = "introduction",column = "introduction"),
            @Result(property = "authorIndices",
                    column = "id",
                    javaType = List.class,
                    many = @Many(select = "com.marxist.leftwing_community.dao.AuthorIndexMapper.getByAuthorId"))
    })
    LibraryAuthor getIndexByArticleId(@Param(value = "id") Long id);

    /**
     * 根据author_id查询author
     *
     * @param authorId
     * @return
     */
    @Select("SELECT * FROM library_author WHERE id = #{author_id}")
    LibraryAuthor getAuthorByAuthorId(@Param("author_id") Long authorId);

}
