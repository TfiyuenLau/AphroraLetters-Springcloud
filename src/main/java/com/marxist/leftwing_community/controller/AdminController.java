package com.marxist.leftwing_community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.SysLog;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.entity.User;
import com.marxist.leftwing_community.service.ISysLogService;
import com.marxist.leftwing_community.service.ITblArticleInfoService;
import com.marxist.leftwing_community.service.IUserService;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISysLogService logService;

    @Autowired
    private ITblArticleInfoService articleInfoService;

    //iframe跳转至管理面板的默认页面
    @OperateLog(operateDesc = "进入默认管理面板")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toHome() {

        return "adminLTE/home";
    }

    //iframe跳转至admin_list
    @OperateLog(operateDesc = "查看管理员列表")
    @RequestMapping(value = "/admin_list", method = RequestMethod.GET)
    public String getAdminList(@RequestParam(value = "page", defaultValue = "1") Long page, Model model) {
        IPage<User> userList = userService.getUserListByPage(page);
        model.addAttribute("userList", userList.getRecords());

        return "adminLTE/admin_list";
    }

    //iframe跳转至日志表单
    @OperateLog(operateDesc = "查看日志列表")
    @RequestMapping(value = "/log_table", method = RequestMethod.GET)
    public String getLogList(@RequestParam(value = "page", defaultValue = "1") Long page, Model model) {
        //查询分页日志对象
        IPage<SysLog> logList = logService.getLogByPage(page);
        model.addAttribute("logList", logList.getRecords());

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) logList.getPages());//初始化集合
        for (int i = 1; i <= logList.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", logList.getCurrent());//当前页码

        return "adminLTE/log_table";
    }

    //获取文章信息列表
    @OperateLog(operateDesc = "后台查询文章列表")
    @RequestMapping(value = "/article_list")
    public String getArticleInfoList(@RequestParam(value = "page", defaultValue = "1") Long page,Model model) {
        IPage<TblArticleInfo> articleByPage = articleInfoService.getArticleByPage(page);
        model.addAttribute("articleInfo", articleByPage.getRecords());

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) articleByPage.getPages());//初始化集合
        for (int i = 1; i <= articleByPage.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", articleByPage.getCurrent());//当前页码

        return "adminLTE/blog_management";
    }

    //访问日历
    @OperateLog(operateDesc = "访问日历")
    @RequestMapping(value = "/calendar")
    public String toCalendar() {

        return "adminLTE/calendar";
    }

}
