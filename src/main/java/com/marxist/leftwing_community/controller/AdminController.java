package com.marxist.leftwing_community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.SysLog;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.entity.User;
import com.marxist.leftwing_community.service.*;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISysLogService logService;

    @Autowired
    private ITblArticleInfoService articleInfoService;

    @Autowired
    private ITblArticleContentService articleContentService;

    @Autowired
    private ITblArticlePictureService articlePictureService;

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
    public String getLogList(@RequestParam(value = "page", defaultValue = "1") Long page, Integer flag, Model model) {
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

        //删除反馈
        model.addAttribute("flag", flag);

        return "adminLTE/log_table";
    }

    //逻辑删除日志
    @OperateLog(operateDesc = "删除日志")
    @RequestMapping("/del_log")
    public String delLog(@RequestParam(value = "id") Long id) {
        int flag = logService.delLog(id);

        return "redirect:/admin/log_table";
    }

    //获取文章信息列表
    @OperateLog(operateDesc = "后台查询文章列表")
    @RequestMapping(value = "/article_list")
    public String getArticleInfoList(@RequestParam(value = "page", defaultValue = "1") Long page, Model model) {
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

    //实现文件上传文章
    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    @ResponseBody
    public String uploadArticle(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(value = "picture", required = true) MultipartFile picture, @RequestParam() String title, HttpServletRequest request) throws IOException {
        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对mywebproject\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //文章文件放入/static/md/目录下
        String pathContent = absolutePath + "\\static\\md\\";
        //图片文件放入/static/md/目录下
        String picContent = absolutePath + "\\static\\img\\";

        //不存在则创建文件夹
        File targetContentFile = new File(pathContent, Objects.requireNonNull(file.getOriginalFilename()));
        if (!targetContentFile.exists()) {
            targetContentFile.mkdirs();
        }
        File targetPicFile = new File(picContent, Objects.requireNonNull(picture.getOriginalFilename()));
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        //保存文件至target
        try {
            file.transferTo(targetContentFile);
            picture.transferTo(targetPicFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //复制target文件到/static/md&img/(仅idea开发时使用)
        Files.copy(new File(targetContentFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\md\\" + file.getOriginalFilename()).toPath());
        Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\" + picture.getOriginalFilename()).toPath());

        //将文件内容添加至数据库
        Long articleId = articleContentService.addContent(targetContentFile);
        //将图片地址添加至数据库
        articlePictureService.addPicByUrl("img/" + picture.getOriginalFilename());

        //将数据库内容转换为html保存
        articleContentService.toHtmlArticleContent(articleId);

        return "<h1 align='center'>文件上传成功,已放置于 " + targetContentFile.getAbsolutePath() + " !</h1>";
    }

    //逻辑删除文章
    @RequestMapping("/del_article")
    public String delArticle(@RequestParam(value = "id") Long id) {
        int flag = articleInfoService.delArticle(id);

        return "redirect:/admin/article_list";
    }

    //访问日历
    @OperateLog(operateDesc = "访问日历")
    @RequestMapping(value = "/calendar")
    public String toCalendar() {

        return "adminLTE/calendar";
    }

}
