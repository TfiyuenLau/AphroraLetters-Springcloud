package com.marxist.leftwing_community.service;

import com.marxist.leftwing_community.entity.LibraryAuthor;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
