package com.marxist.leftwing_community.dao;

import com.marxist.leftwing_community.entity.AuthorIndex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marxist.leftwing_community.entity.LibraryAuthor;
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
public interface AuthorIndexMapper extends BaseMapper<AuthorIndex> {

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
