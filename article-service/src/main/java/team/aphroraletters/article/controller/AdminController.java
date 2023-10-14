package team.aphroraletters.article.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.aphroraletters.article.pojo.entity.*;
import team.aphroraletters.article.pojo.request.LoginParams;
import team.aphroraletters.article.pojo.response.ResultVO;
import team.aphroraletters.article.service.*;
import team.aphroraletters.article.util.RedisUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成图像验证码
     *
     * @param response response请求对象
     */
    @ApiOperation(value = "生成图像验证码")
    @GetMapping("/generateValidateCode")
    public void generateValidateCode(HttpServletResponse response) throws IOException {
        // 设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 把图形验证码凭证放入cookie中
        String tokenId = "CAPTCHA_TOKEN" + UUID.randomUUID();
        Cookie cookie = new Cookie("image_code_token", tokenId);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 创建扭曲干扰验证码，定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(170, 40, 4, 4);

        // 把凭证对应的验证码信息保存到redis
        redisUtils.set(tokenId, captcha.getCode(), 60);

        // 输出浏览器
        OutputStream stream = response.getOutputStream();
        captcha.write(stream);
        stream.flush();
        stream.close();
    }

    @ApiOperation("登录请求")
    @PostMapping(value = "/login")
    public SaResult login(@RequestBody LoginParams loginParams, @CookieValue("image_code_token") String tokenId) {
        // 图形码验证
        String codeValue = redisUtils.get(tokenId);
        if (codeValue == null) {
            return SaResult.error("验证码已过期，请重新输入");
        }
        if (!codeValue.equals(loginParams.getCode())) {
            return SaResult.error("验证码错误");
        }

        // 密码MD5脱敏
        loginParams.setPassword(DigestUtils.md5DigestAsHex(loginParams.getPassword().getBytes()));

        // 账号密码验证
        Admin admin = new Admin();
        BeanUtils.copyProperties(loginParams, admin);
        Admin account = adminService.adminLogin(admin);
        if (account == null) {
            return SaResult.error("登陆失败，账号或密码错误");
        }

        redisUtils.set("admin:authority:id-" + account.getId(), account.getAuthority());
        StpUtil.login(account.getId());
        account.setRoles(Lists.newArrayList(account.getAuthority())); // 封装权限列表

        return SaResult.ok("登陆成功").setData(account);
    }

    @ApiOperation("登出请求")
    @PostMapping(value = "/logout")
    public SaResult logout() {
        try {
            StpUtil.logout();
        } catch (Exception e) {
            return SaResult.error(e.getMessage());
        }

        return SaResult.ok("登出成功");
    }

    @ApiOperation("获取id对应的权限列表")
    @GetMapping("/getAuthorityById/{id}")
    public ResultVO getAuthorityById(@PathVariable("id") Long id) {
        List<String> roles = Collections.singletonList(adminService.getAdminById(id).getAuthority());

        return ResultVO.ok(roles);
    }

    @ApiOperation("获取登陆的管理员基本信息")
    @GetMapping("/getLoginAdmin")
    public ResultVO getAdminById() {
        long adminId = StpUtil.getLoginIdAsLong();
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResultVO.errorMsg("没有对应的管理员账户");
        }

        return ResultVO.ok(admin);
    }

    @ApiOperation("按page获取管理员名单")
    @GetMapping("/getAdminListByPage/{page}")
    public ResultVO getAdminListByPage(@PathVariable("page") Long page) {
        IPage<Admin> userListByPage = adminService.getAdminListByPage(page);
        if (userListByPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("数据为空");
        }

        return ResultVO.ok(userListByPage);
    }

    @ApiOperation("新增一个管理员")
    @PostMapping("/insertAdmin")
    public ResultVO insertAdmin(@RequestBody Admin admin) {
        try {
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes())); // MD5加密脱敏
            adminService.addAdmin(admin);
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("新增成功");
    }

    @ApiOperation("通过adminId更新管理员资料")
    @PutMapping("/updateAdminById")
    public ResultVO updateAdminById(@RequestBody Admin admin) {
        try {
            if (admin.getPassword() != null) {
                admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
            }
            adminService.updateById(admin);
        } catch (Exception e) {
            return ResultVO.errorMsg(e.getMessage());
        }

        return ResultVO.ok("当前管理员资料更新成功");
    }

    @ApiOperation("通过id删除管理员")
    @DeleteMapping("/deleteAdminById/{id}")
    public ResultVO deleteAdminById(@PathVariable("id") Long id) {
        try {
            adminService.delAdmin(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(e.getMessage());
        }

        return ResultVO.ok();
    }

}
