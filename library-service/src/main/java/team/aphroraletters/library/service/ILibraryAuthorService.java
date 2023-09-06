package team.aphroraletters.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.library.entity.LibraryAuthor;

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

    int addLibraryAuthor(LibraryAuthor libraryAuthor);
}
