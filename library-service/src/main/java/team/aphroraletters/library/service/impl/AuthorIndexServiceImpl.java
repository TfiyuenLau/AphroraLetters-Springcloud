package team.aphroraletters.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.library.dao.AuthorIndexMapper;
import team.aphroraletters.library.pojo.entity.AuthorIndex;
import team.aphroraletters.library.service.IAuthorIndexService;

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

    /**
     * @return 返回所有封装了LibraryAuthor对象的authorIndex对象
     */
    @Override
    public List<AuthorIndex> getAllAuthorIndex() {

        return authorIndexMapper.getAllAuthorIndex();
    }

    /**
     * @return 返回所有封装了LibraryAuthor对象的authorIndex分页对象
     */
    @Override
    public IPage<AuthorIndex> getAllAuthorIndexByPage(Long page) {
        Page<AuthorIndex> authorIndexPage = new Page<>(page, 10);

        return authorIndexMapper.getAllAuthorIndexByPage(authorIndexPage);
    }

    /**
     * 根据authorId查询全部AuthorIndex对象
     *
     * @param authorId
     * @return
     */
    @Override
    public List<AuthorIndex> getListByAuthorId(Long authorId) {

        return authorIndexMapper.getByAuthorId(authorId);
    }

    /**
     * 插入一个authorIndex对象
     *
     * @param authorIndex
     * @return
     */
    @Override
    public int insertAuthorIndex(AuthorIndex authorIndex) {

        return authorIndexMapper.insert(authorIndex);
    }

    @Override
    public int updateAuthorIndexById(AuthorIndex authorIndex) {
        return authorIndexMapper.updateById(authorIndex);
    }

    /**
     * 逻辑删除文库对应articleId的文章
     *
     * @param articleId
     * @return
     */
    @Override
    public int deleteAuthorIndex(Long articleId) {
        LambdaQueryWrapper<AuthorIndex> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AuthorIndex::getArticleId, articleId);

        return authorIndexMapper.delete(lambdaQueryWrapper);
    }

}
