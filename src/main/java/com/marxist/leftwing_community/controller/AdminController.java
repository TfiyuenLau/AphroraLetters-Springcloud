package com.marxist.leftwing_community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.*;
import com.marxist.leftwing_community.service.*;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private ILibraryAuthorService libraryAuthorService;

    @Autowired
    private IAuthorIndexService authorIndexService;

    @Autowired
    private ITblArticleCategoryService articleCategoryService;

    @Autowired
    private IClassificationService classificationService;

    /**
     * 表单登录
     *
     * @param user    账户对象
     * @param session Session对象
     * @return 重定向
     */
    @OperateLog(operateDesc = "登录请求")
    @RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")
    public String userLogin(User user, HttpSession session) {
//        System.err.println("表单发送的请求用户:\n账号:" + user.getUserAccount() + "\n密码:" + user.getPassword());
        User account = userService.userLogin(user);
        if (account != null) {
            session.setAttribute("adminName", account.getUserAccount());
            session.setAttribute("login_status", "login");//存储登录状态
            String url = "home";

            return "redirect:/admin/starter?url=" + url;
        } else {
            boolean flag = true;//登录反馈

            return "redirect:/admin/login_page?flag=" + flag;
        }

    }

    /**
     * 登出方法
     *
     * @param session
     * @return
     */
    @OperateLog(operateDesc = "登出请求")
    @RequestMapping(value = "/logout", produces = "text/html;charset=UTF-8")
    public String userLogout(HttpSession session) {
        String loginStatus = (String) session.getAttribute("login_status");
        if (loginStatus.equals("login")) {
            session.invalidate();
        }

        return "redirect:/admin/login_page";
    }

    /**
     * iframe跳转至管理面板的默认页面
     *
     * @return
     */
    @OperateLog(operateDesc = "进入默认管理面板")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toHome() {

        return "adminLTE/home";
    }

    /**
     * iframe跳转至admin_list
     *
     * @param page
     * @param session
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "查看管理员列表")
    @RequestMapping(value = "/admin_list", method = RequestMethod.GET)
    public String getAdminList(@RequestParam(value = "page", defaultValue = "1") Long page, HttpSession session, Model model) {
        //获取列表
        IPage<User> userList = userService.getUserListByPage(page);
        //获取当前登录的账户,将非当前账户密码匿名
        String adminName = (String) session.getAttribute("adminName");
        for (User user : userList.getRecords()) {
            if (!user.getUserAccount().equals(adminName)) {
                user.setPassword("********");
            }
        }

        model.addAttribute("userList", userList.getRecords());

        return "adminLTE/admin_list";
    }

    /**
     * 添加账户至数据库
     *
     * @param user
     * @return
     */
    @OperateLog(operateDesc = "添加管理员")
    @RequestMapping(value = "/add_admin", method = RequestMethod.POST)
    public String addAdmin(User user) {
        userService.addAdmin(user);

        return "redirect:/admin/admin_list";
    }

    /**
     * 逻辑删除管理员
     *
     * @param userId
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "删除管理员")
    @RequestMapping("/del_admin")
    public String delAdmin(@RequestParam("user_id") Long userId, HttpSession session, HttpServletResponse response) throws IOException {
        //若非本人账号则无法删除管理员
        String adminName = (String) session.getAttribute("adminName");
        if (!"tfiyuenlau@foxmail.com".equals(adminName)) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("<h1 align='center'>权限不足！您无法删除该管理员</h1><script>setTimeout(function(){window.history.go(-1);},3000);</script>");
            return null;
        }

        userService.delAdmin(userId);

        return "redirect:/adminLTE/admin_list";
    }

    /**
     * 更新管理员密码
     *
     * @param user
     * @param oldPassword
     * @param response
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "修改管理员密码")
    @RequestMapping(value = "/update_admin_password", method = RequestMethod.POST)
    public String updateAdminPassword(User user, String oldPassword, HttpServletResponse response) throws IOException {
        System.err.println(user.getUserId() + "." + user.getPassword() + "\nold_password:" + oldPassword);
        int flag = userService.updateAdminPassword(user, oldPassword);
        if (flag == -1) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("<h1 align='center'>旧密码错误！修改失败</h1><script>setTimeout(function(){window.history.go(-1);},3000);</script>");
            return null;
        }

        return "redirect:/admin/admin_list";
    }

    /**
     * iframe跳转至日志表单
     *
     * @param page
     * @param flag
     * @param model
     * @return
     */
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

    /**
     * 逻辑删除日志
     *
     * @param id
     * @return
     */
    @OperateLog(operateDesc = "删除日志")
    @RequestMapping("/del_log")
    public String delLog(@RequestParam(value = "id") Long id) {
        int flag = logService.delLog(id);

        return "redirect:/admin/log_table";
    }

    /**
     * 获取文章信息列表
     *
     * @param page
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "后台查询文章列表")
    @RequestMapping(value = "/article_list")
    public String getArticleInfoList(@RequestParam(value = "page", defaultValue = "1") Long page, Model model) {
        //分页查询文章基本信息
        IPage<TblArticleInfo> articleByPage = articleInfoService.getArticleByPage(page);
        model.addAttribute("articleInfo", articleByPage.getRecords());

        //传输文章与分类标签
        List<TblArticleCategory> allCategory = articleCategoryService.getAllCategory();
        List<TblArticleInfo> allArticleInfo = articleInfoService.getAllArticleInfo();
        model.addAttribute("allArticleInfo", allArticleInfo);
        model.addAttribute("allCategory", allCategory);

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) articleByPage.getPages());//初始化集合
        for (int i = 1; i <= articleByPage.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", articleByPage.getCurrent());//当前页码

        return "adminLTE/blog_management";
    }

    /**
     * 实现文件上传文章
     *
     * @param file          md文章文件
     * @param picture       文章题图文件
     * @param articleImages 文章素材图片
     * @param summary       文章概述
     * @return 基本信息
     * @throws IOException 基本错误
     */
    @OperateLog(operateDesc = "文章文件上传")
    @RequestMapping(value = "/upload_article_file", method = RequestMethod.POST)
    @ResponseBody
    public String uploadArticle(@RequestParam("file") MultipartFile file, @RequestParam("picture") MultipartFile picture, MultipartFile[] articleImages, String summary) throws IOException {
        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对mywebproject\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //文章文件放入/static/md/目录下
        String pathContent = absolutePath + "/static/md/";
        //图片文件放入/static/img/目录下
        String pathPic = absolutePath + "/static/img/";

        //不存在则创建文件
        File targetContentFile = new File(pathContent, Objects.requireNonNull(file.getOriginalFilename()));
        if (!targetContentFile.exists()) {
            targetContentFile.mkdirs();
        }
        File targetPicFile = new File(pathPic, Objects.requireNonNull(picture.getOriginalFilename()));
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

        //保存所有上传的文章素材图片
        for (MultipartFile articleImage : articleImages) {
            File targetImgFile = new File(pathPic, Objects.requireNonNull(articleImage.getOriginalFilename()));
            if (!targetImgFile.exists()) {
                targetImgFile.mkdirs();
            }

            try {
                articleImage.transferTo(targetImgFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //复制target文件到/static/md&img/(仅idea开发时使用)
        /*
        try {
            Files.copy(new File(targetContentFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\md\\" + file.getOriginalFilename()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\" + picture.getOriginalFilename()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        try {
            //将文件内容添加至数据库
            Long articleId = articleContentService.addContentByFile(targetContentFile, summary);
            //将图片地址添加至数据库
            articlePictureService.addPicByUrl("img/" + picture.getOriginalFilename());

            //将数据库内容转换为html保存
            articleContentService.toHtmlArticleContent(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败" + e.toString();
        }

        return "<h1 align='center'>文件上传成功,已放置于 " + targetContentFile.getAbsolutePath() + " !</h1>" + "<script>setTimeout(function(){window.history.go(-1);},3000);</script>";
    }

    /**
     * 添加文章分类标签
     *
     * @param categoryName 文章分类标签名称
     * @return
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public String addCategory(String categoryName) {
        TblArticleCategory articleCategory = new TblArticleCategory();
        articleCategory.setCategoryName(categoryName);

        articleCategoryService.addCategory(articleCategory);

        return "redirect:/admin/starter?url=article_list";
    }

    /**
     * 选择文章添加指定分类标签
     *
     * @param articleId  文章外键
     * @param categoryId 分类标签外键
     * @return
     */
    @RequestMapping(value = "/add_category_to_article", method = RequestMethod.POST)
    @ResponseBody
    public String addCategoryToArticle(Long articleId, Long categoryId) {
        //封装标签对象
        Classification classification = new Classification();
        classification.setArticleId(articleId);
        classification.setCategoryId(categoryId);

        //添加标签至中间表
        try {
            classificationService.addCategoryToArticle(classification);
        } catch (Exception e) {
            return "<h1 align='center'>标签添加失败!</h1>\n" + e.getMessage() + "<script>setTimeout(function(){window.history.go(-1);},3000);</script>";
        }

        return "<h1 align='center'>标签添加成功!</h1><script>setTimeout(function(){window.history.go(-1);},3000);</script>";
    }

    /**
     * 逻辑删除文章
     *
     * @param id       文章ID
     * @param response
     * @return
     */
    @OperateLog(operateDesc = "删除文章")
    @RequestMapping("/del_article")
    public String delArticle(@RequestParam(value = "id") Long id, HttpServletResponse response) throws IOException {
        try {
            articleInfoService.delArticle(id);//逻辑删除文章信息
            articlePictureService.delPic(id);//逻辑删除题图数据以解决删除文章后题图不对应bug
            articleContentService.delContent(id);//逻辑删除文章内容
        } catch (Exception e) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("<h1 align='center'>删除出现问题！</h1>\n" + e.getMessage() + "<script>setTimeout(function(){window.history.go(-1);},3000);</script>");
            return null;
        }

        return "redirect:/admin/article_list";
    }

    /**
     * 进入文库管理页
     *
     * @param page
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "进入文库管理页面")
    @RequestMapping("/library_operation")
    public String getLibraryOperation(@RequestParam("page") Long page, Model model) {
        //文库文章列分页表
//        List<AuthorIndex> authorIndices = authorIndexService.getAllAuthorIndex();
        IPage<AuthorIndex> allAuthorIndexByPage = authorIndexService.getAllAuthorIndexByPage(page);
        List<AuthorIndex> authorIndices = allAuthorIndexByPage.getRecords();
        model.addAttribute("authorIndices", authorIndices);

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) allAuthorIndexByPage.getPages());//初始化集合
        for (int i = 1; i <= allAuthorIndexByPage.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", allAuthorIndexByPage.getCurrent());//当前页码

        //为选择框传输内容
        List<LibraryAuthor> libraryAuthors = libraryAuthorService.getAllLibraryAuthor();
        model.addAttribute("libraryAuthors", libraryAuthors);

        return "adminLTE/library_operation";
    }

    /**
     * 添加人物至文库
     *
     * @param picFile       作者图片
     * @param libraryAuthor 封装作者对象
     * @param model         视图
     * @return 基本信息
     * @throws IOException 基本错误
     */
    @OperateLog(operateDesc = "添加人物至文库")
    @RequestMapping(value = "/add_library_author", method = RequestMethod.POST)
    @ResponseBody
    public String addLibraryAuthor(@RequestParam(value = "pic_file") MultipartFile picFile, LibraryAuthor libraryAuthor, Model model) throws IOException {
        //设置头像路径
        libraryAuthor.setPicUrl("img/" + picFile.getOriginalFilename());

        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对mywebproject\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //图片文件放入/static/img/目录下
        String pathPic = absolutePath + "/static/img/";
        //不存在则创建文件
        File targetPicFile = new File(pathPic, Objects.requireNonNull(picFile.getOriginalFilename()));
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        //保存文件至target并上传至数据库
        try {
            picFile.transferTo(targetPicFile);
            libraryAuthorService.addLibraryAuthor(libraryAuthor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        //复制target文件到/static/img/(仅idea开发时使用)
        try {
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\" + picFile.getOriginalFilename()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        return "<h1 align='center'>传入的作者名是：" + libraryAuthor.getCharacterName() + ",其简介为：\n" + libraryAuthor.getIntroduction() + "\n作者肖像路径为：" + libraryAuthor.getPicUrl() + "</h1><script>setTimeout(function(){window.history.go(-1);},3000);</script>";
    }

    /**
     * 添加文章至数据库
     *
     * @param authorIndex   作者索引对象
     * @param pdfFile       pdf文件
     * @param characterName 作者名
     * @param model         视图
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "添加文库文章至数据库")
    @RequestMapping(value = "/add_author_index", method = RequestMethod.POST)
    @ResponseBody
    public String addAuthorIndex(AuthorIndex authorIndex, @RequestParam("pdf_file") MultipartFile pdfFile, @RequestParam(value = "character_name") String characterName, Model model) throws IOException {
        //初始化authorId的值
        LibraryAuthor author = libraryAuthorService.getAuthorByCharacterName(characterName);
        authorIndex.setAuthorId(author.getId());
        //初始化pdfUrl的值
        authorIndex.setPdfUrl("/literature?url=" + "http://8.130.39.9:8080/" + "pdf/" + pdfFile.getOriginalFilename());

        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对mywebproject\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //图片文件放入/static/pdf/目录下
        String pathPdf = absolutePath + "/static/pdf/";
        //不存在则创建文件
        File targetPicFile = new File(pathPdf, Objects.requireNonNull(pdfFile.getOriginalFilename()));
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        //保存文件至target并上传至数据库
        try {
            pdfFile.transferTo(targetPicFile);
            authorIndexService.addAuthorIndex(authorIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        //复制target文件到/static/pdf/(仅idea开发时使用)
        try {
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\pdf\\" + pdfFile.getOriginalFilename()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        return "<h1 align='center'>传入成功!\n文章为：" + authorIndex.getTitle() + "</h1><script>setTimeout(function(){window.history.go(-1);},5000);</script>";
    }

    /**
     * 逻辑删除按article_id文章
     *
     * @param articleId
     * @return
     */
    @OperateLog(operateDesc = "删除文库文章")
    @RequestMapping("/del_article_index")
    public String deleteArticleIndex(@RequestParam("article_id") Long articleId) {
        authorIndexService.deleteAuthorIndex(articleId);

        return "redirect:/admin/library_operation";
    }

    /**
     * 访问日历
     *
     * @return
     */
    @OperateLog(operateDesc = "访问日历")
    @RequestMapping(value = "/calendar")
    public String toCalendar() {

        return "adminLTE/calendar";
    }

    /**
     * 测试文章，解析得html静态资源上传至服务器
     *
     * @param id
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "测试博客文章生成html")
    @ResponseBody
    @RequestMapping(value = "/testArticle/{id}", method = RequestMethod.GET)
    public String testArticle(@PathVariable Long id) throws IOException {
        //转换为html保存在本地
        articleContentService.toHtmlArticleContent(id);

        String articleTitle = articleInfoService.getArticleTitle(id);
        String url = "../static/page/" + articleTitle;
        url += ".html";

        return "<h1 align='center'>" + url + "测试成功!</h1>";
    }

}
