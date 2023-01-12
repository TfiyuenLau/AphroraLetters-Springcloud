package com.marxist.leftwing_community.controller;

import com.marxist.leftwing_community.entity.Announcement;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.entity.VersionLog;
import com.marxist.leftwing_community.service.IAnnouncementService;
import com.marxist.leftwing_community.service.ITblArticleInfoService;
import com.marxist.leftwing_community.service.IVersionLogService;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ITblArticleInfoService articleInfoService;

    @Autowired private IAnnouncementService announcementService;

    @Autowired private IVersionLogService versionLogService;

    //访问导航页面
    @OperateLog(operateDesc = "导航页")
    @RequestMapping("/index")
    public String index(){

        return "index";
    }

    //访问首页
    @OperateLog(operateDesc = "访问首页")
    @RequestMapping("/home")
    public String home(Model model) {
        //获取推荐文章列表
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        //获取公告栏中的最新消息列表
        List<Announcement> announcements = announcementService.getAnnouncements();
        model.addAttribute("announcements", announcements);

        //获取公告侧边栏全部消息列表
        List<Announcement> announcementList = announcementService.getAnnouncementList();
        model.addAttribute("announcementList", announcementList);

        //获取公告栏中最新的版本日志
        List<VersionLog> versionLogs = versionLogService.getVersionLogs();
        model.addAttribute("versionLogs", versionLogs);

        //获取公告侧边栏全部版本日志
        List<VersionLog> versionLogList = versionLogService.getVersionLogList();
        model.addAttribute("versionLogList", versionLogList);

        return "home";
    }

    //访问公告消息
    @OperateLog(operateDesc = "访问公告消息")
    @RequestMapping("/announcement")
    public String announcement(@RequestParam(value = "id", required = false, defaultValue = "1") Long id, Model model) {
        //获取announcement对象
        Announcement announcement = announcementService.getAnnouncementById(id);
        model.addAttribute("announcement", announcement);

        return "announcement";
    }

    //访问assistance页面
    @OperateLog(operateDesc = "访问支持我们页面")
    @RequestMapping("/assistance")
    public String assistance(Model model) {
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "assistance";
    }

    //访问about页面
    @OperateLog(operateDesc = "访问关于")
    @RequestMapping("/about")
    public String about(Model model) {
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "about";
    }

    //error页面
    @OperateLog(operateDesc = "发生错误")
    @RequestMapping("/error_page")
    public String error(Model model) {
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "error";
    }

    //登录界面
    @OperateLog(operateDesc = "进入后台登录页面")
    @RequestMapping("/admin/login_page")
    public String enterLogin(boolean flag, Model model) {
        //默认为false,登录错误后传参为true
        model.addAttribute("flag", flag);

        return "adminLTE/login";
    }

    //后台管理面板
    //@OperateLog(operateDesc = "进入后台管理面板")//多余记录
    @RequestMapping(value = "/admin/starter")
    public String starter(String url, HttpSession session, Model model) {
        //添加后台当前账户名
        model.addAttribute("adminName", (String)session.getAttribute("adminName"));
        //添加Controller请求地址/admin/starter?url=${url}
        model.addAttribute("url", url);

        return "adminLTE/starter";
    }

}
