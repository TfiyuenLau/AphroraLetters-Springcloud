package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.entity.User;
import com.marxist.leftwing_community.dao.UserMapper;
import com.marxist.leftwing_community.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录方法
     * @param user 表单提交的用户对象
     * @return
     */
    @Override
    public User userLogin(User user) {
        //查询账户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account" ,user.getUserAccount());
        User account = userMapper.selectOne(queryWrapper);

        if (account==null) {
            return null;
        }

        if (account.getPassword().equals(user.getPassword())) {
            return account;
        } else {
            return null;
        }

    }

    /**
     * 按页码获取用户列表
     * @param page 页码
     * @return 页码查询对象
     */
    @Override
    public IPage<User> getUserListByPage(Long page) {
        IPage<User> iPage = new Page<>(page, 10);

        return userMapper.selectPage(iPage, null);
    }

    /**
     * 通过id获取用户
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {

        return userMapper.selectById(id);
    }

    /**
     * 向数据库添加一个管理员
     *
     * @param user
     * @return
     */
    @Override
    public int addAdmin(User user) {

        return userMapper.insert(user);
    }

    /**
     * 按user_id删除一个管理员
     *
     * @param userId
     * @return
     */
    @Override
    public int delAdmin(Long userId) {

        return userMapper.deleteById(userId);
    }

    /**
     * 更新管理员密码
     *
     * @param user
     * @return
     */
    @Override
    public int updateAdminPassword(User user, String oldPassword) {
        //判断旧密码是否正确
        User trueUser = userMapper.selectById(user.getUserId());
        if (trueUser.getPassword().equals(oldPassword)) {
            //创建updateWrapper对象，实现条件修改数据
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", user.getUserId()).set("password", user.getPassword());

            return userMapper.update(user, updateWrapper);
        }

        return -1;//旧密码错误
    }

}
