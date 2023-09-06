package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.entity.Admin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-20
 */
public interface IAdminService extends IService<Admin> {

    Admin adminLogin(Admin admin);

    IPage<Admin> getAdminListByPage(Long page);

    Admin getAdminById(Long id);

    int addAdmin(Admin admin);

    int delAdmin(Long userId);

    int updateAdminPassword(Admin admin, String oldPassword);
}
