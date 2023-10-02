package team.aphroraletters.gateway.service;

import cn.dev33.satoken.stp.StpInterface;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.aphroraletters.fegin.client.AdminClient;
import team.aphroraletters.fegin.domain.Admin;
import team.aphroraletters.fegin.domain.response.ResultVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        ResultVO authorizeVO = adminClient.getAuthorityById(Long.valueOf((String) loginId));
        List<String> permissionList = objectMapper.convertValue(authorizeVO.getData(), new TypeReference<List<String>>() {
        });

        return new ArrayList<>(permissionList);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        ResultVO authorizeVO = adminClient.getAuthorityById(Long.valueOf((String) loginId));
        List<String> roleList = objectMapper.convertValue(authorizeVO.getData(), new TypeReference<List<String>>() {
        });

        return new ArrayList<>(roleList);
    }

}
