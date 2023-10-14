package team.aphroraletters.gateway.service;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.aphroraletters.gateway.util.RedisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(redisUtils.get("admin:authority:id-" + loginId)); // 按登录用户id获取缓存权限

        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        ArrayList<String> roleList = new ArrayList<>();
        roleList.add(redisUtils.get("admin:authority:id-" + loginId));

        return roleList;
    }

}
