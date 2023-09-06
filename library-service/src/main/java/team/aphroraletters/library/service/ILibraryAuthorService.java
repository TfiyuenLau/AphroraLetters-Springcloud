package team.aphroraletters.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.library.pojo.entity.LibraryAuthor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
public interface ILibraryAuthorService extends IService<LibraryAuthor> {

    List<LibraryAuthor> getAllAuthorIndex();

    LibraryAuthor getAuthorById(Long id);

    List<LibraryAuthor> getAllLibraryAuthor();

    LibraryAuthor getAuthorByCharacterName(String characterName);

    void insertLibraryAuthor(LibraryAuthor libraryAuthor);

    void updateLibraryAuthorById(LibraryAuthor libraryAuthor);

    void deleteLibraryAuthorById(Long id);
}
