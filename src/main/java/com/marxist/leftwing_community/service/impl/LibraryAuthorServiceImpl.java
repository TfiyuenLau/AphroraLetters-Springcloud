package com.marxist.leftwing_community.service.impl;

import com.marxist.leftwing_community.entity.LibraryAuthor;
import com.marxist.leftwing_community.dao.LibraryAuthorMapper;
import com.marxist.leftwing_community.service.ILibraryAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
@Service
public class LibraryAuthorServiceImpl extends ServiceImpl<LibraryAuthorMapper, LibraryAuthor> implements ILibraryAuthorService {
    @Autowired
    private LibraryAuthorMapper libraryAuthorMapper;

    /**
     * 获取所有封装后的LibraryAuthor对象
     *
     * @return 返回封装了AuthorIndex集合的LibraryAuthor对象集合
     */
    @Override
    public List<LibraryAuthor> getAllAuthorIndex() {

        return libraryAuthorMapper.getAllAuthorIndex();
    }

    /**
     * 通过id获取一个封装后的LibraryAuthor对象
     * @param id 主键，与AuthorIndex外键相等
     * @return 返回一个封装了AuthorIndex集合的LibraryAuthor对象
     */
    @Override
    public LibraryAuthor getAuthorById(Long id) {

        return libraryAuthorMapper.getIndexByArticleId(id);
    }

    /**
     *
     * @return 返回LibraryAuthor集合
     */
    @Override
    public List<LibraryAuthor> getAllLibraryAuthor() {

        return libraryAuthorMapper.selectList(null);
    }

}
