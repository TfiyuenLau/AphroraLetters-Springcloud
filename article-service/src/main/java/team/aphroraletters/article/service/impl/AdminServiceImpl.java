package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.AdminMapper;
import team.aphroraletters.article.entity.Admin;
import team.aphroraletters.article.service.IAdminService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-20
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 登录方法
     *
     * @param admin 表单提交的用户对象
     * @return
     */
    @Override
    public Admin adminLogin(Admin admin) {
        //查询账户信息
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, admin.getUsername());
        Admin account = adminMapper.selectOne(queryWrapper);

        if (account == null) {
            return null;
        }

        if (account.getPassword().equals(admin.getPassword())) {
            return account;
        } else {
            return null;
        }

    }

    /**
     * 按页码获取用户列表
     *
     * @param page 页码
     * @return 页码查询对象
     */
    @Override
    public IPage<Admin> getAdminListByPage(Long page) {
        IPage<Admin> iPage = new Page<>(page, 10);

        return adminMapper.selectPage(iPage, null);
    }

    /**
     * 通过id获取用户
     *
     * @param id
     * @return
     */
    @Override
    public Admin getAdminById(Long id) {

        return adminMapper.selectById(id);
    }

    /**
     * 向数据库添加一个管理员
     *
     * @param admin
     * @return
     */
    @Override
    public int addAdmin(Admin admin) {

        return adminMapper.insert(admin);
    }

    /**
     * 按user_id删除一个管理员
     *
     * @param userId
     * @return
     */
    @Override
    public int delAdmin(Long userId) {

        return adminMapper.deleteById(userId);
    }

    /**
     * 更新管理员密码
     *
     * @param admin
     * @return
     */
    @Override
    public int updateAdminPassword(Admin admin, String oldPassword) {
        //判断旧密码是否正确
        Admin trueAdmin = adminMapper.selectById(admin.getId());
        if (trueAdmin.getPassword().equals(oldPassword)) {
            //创建updateWrapper对象，实现条件修改数据
            UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", admin.getId()).set("password", admin.getPassword());

            return adminMapper.update(admin, updateWrapper);
        }

        return -1;//旧密码错误
    }

}
