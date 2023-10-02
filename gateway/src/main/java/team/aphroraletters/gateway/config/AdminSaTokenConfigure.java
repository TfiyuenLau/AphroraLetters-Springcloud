package team.aphroraletters.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证]
 * 管理面板权限认证配置类
 *
 * @author TfiyuenLau
 */
@Configuration
public class AdminSaTokenConfigure {
    // 注册管理面板过滤器
    @Bean
    public SaReactorFilter getAdminReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/article/admin/**", "/library/admin/**") // 拦截地址
                .addExclude("/article/admin/login", "/article/admin/generateValidateCode") // 开放登录接口
                .setAuth(obj -> { // 鉴权方法
                    // 登录校验
                    SaRouter.match("/**", r -> StpUtil.checkLogin());

                    // 权限认证 -- 不同模块, 校验不同权限
                    SaRouter.match("/**", r -> StpUtil.checkRoleOr("super-admin", "admin"));
                }).setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}
