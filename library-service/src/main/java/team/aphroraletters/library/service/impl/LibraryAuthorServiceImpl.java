package team.aphroraletters.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.library.dao.LibraryAuthorMapper;
import team.aphroraletters.library.pojo.entity.LibraryAuthor;
import team.aphroraletters.library.service.ILibraryAuthorService;

import java.util.List;

/**
 * <p>
 * 服务实现类
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
     *
     * @param id 主键，与AuthorIndex外键相等
     * @return 返回一个封装了AuthorIndex集合的LibraryAuthor对象
     */
    @Override
    public LibraryAuthor getAuthorById(Long id) {

        return libraryAuthorMapper.getIndexByArticleId(id);
    }

    /**
     * @return 返回LibraryAuthor集合
     */
    @Override
    public List<LibraryAuthor> getAllLibraryAuthor() {

        return libraryAuthorMapper.selectList(null);
    }

    /**
     * 通过characterName查询一个LibraryAuthor对象
     *
     * @param characterName 作者名
     * @return LibraryAuthor对象
     */
    @Override
    public LibraryAuthor getAuthorByCharacterName(String characterName) {

        return libraryAuthorMapper.selectOne(
                new QueryWrapper<LibraryAuthor>().eq("character_name", characterName)
        );
    }

    /**
     * 增加一个对象至数据库
     *
     * @param libraryAuthor 作者对象
     */
    @Override
    public void insertLibraryAuthor(LibraryAuthor libraryAuthor) {
        libraryAuthorMapper.insert(libraryAuthor);
    }

    @Override
    public void updateLibraryAuthorById(LibraryAuthor libraryAuthor) {
        libraryAuthorMapper.updateById(libraryAuthor);
    }

    /**
     * 逻辑删除文库作者
     *
     * @param id
     */
    @Override
    public void deleteLibraryAuthorById(Long id) {
        LambdaQueryWrapper<LibraryAuthor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LibraryAuthor::getId, id);

        libraryAuthorMapper.delete(queryWrapper);
    }

}
