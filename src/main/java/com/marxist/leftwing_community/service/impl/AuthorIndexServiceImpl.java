package com.marxist.leftwing_community.service.impl;

import com.marxist.leftwing_community.entity.AuthorIndex;
import com.marxist.leftwing_community.dao.AuthorIndexMapper;
import com.marxist.leftwing_community.service.IAuthorIndexService;
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
public class AuthorIndexServiceImpl extends ServiceImpl<AuthorIndexMapper, AuthorIndex> implements IAuthorIndexService {

    @Autowired
    private AuthorIndexMapper authorIndexMapper;

    @Override
    public List<AuthorIndex> getListByAuthorId(Long authorId) {

        return authorIndexMapper.getByAuthorId(authorId);
    }
}
