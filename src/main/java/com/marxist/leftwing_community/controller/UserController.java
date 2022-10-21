package com.marxist.leftwing_community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.User;
import com.marxist.leftwing_community.service.IUserService;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-20
 */
@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 表单登录
     * @param user 账户对象
     * @param session Session对象
     * @return 重定向
     */
    @OperateLog(operateDesc = "登录请求")
    @RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")
    public String userLogin(User user, HttpSession session) {
        System.err.println("表单发送的请求用户:\n账号:" + user.getUserAccount() + "\n密码:" + user.getPassword());
        User account = userService.userLogin(user);
        if (account!=null) {
            session.setAttribute("adminName", account.getUserAccount());
            String url = "home";

            return "redirect:/admin/starter?url=" + url;
        } else {
            boolean flag = true;//登录反馈

            return "redirect:/admin/login_page?flag=" + flag;
        }

    }

}
