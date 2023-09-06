package team.aphroraletters.library.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.library.pojo.entity.AuthorIndex;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
public interface IAuthorIndexService extends IService<AuthorIndex> {

    List<AuthorIndex> getAllAuthorIndex();

    IPage<AuthorIndex> getAllAuthorIndexByPage(Long page);

    List<AuthorIndex> getListByAuthorId(Long authorId);

    int insertAuthorIndex(AuthorIndex authorIndex);

    int updateAuthorIndexById(AuthorIndex authorIndex);

    int deleteAuthorIndex(Long articleId);
}
