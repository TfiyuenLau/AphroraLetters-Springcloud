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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "添加管理员")
    @RequestMapping(value = "/add_admin", method = RequestMethod.POST)
    public String addAdmin(User user, Model model) {
        //添加数据
        userService.addAdmin(user);

        //数据反馈
        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "添加管理员成功");
        feedbackMap.put("info", "已添加一名管理员：" + user.getUserAccount() + "！您的加入将丰富我们的管理员团队。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 逻辑删除管理员
     *
     * @param userId
     * @param session
     * @param model
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "删除管理员")
    @RequestMapping("/del_admin")
    public String delAdmin(@RequestParam("user_id") Long userId, HttpSession session, Model model) throws IOException {
        //若非超级管理员账号则无法删除管理员
        String adminName = (String) session.getAttribute("adminName");
        if (!"tfiyuenlau@foxmail.com".equals(adminName)) {
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "删除管理员失败！");
            feedbackMap.put("info", "出现一个错误！权限不足，您无法删除该管理员！");
            model.addAttribute("feedbackMap", feedbackMap);
            return "adminLTE/feedback";
        }

        //删除管理员
        userService.delAdmin(userId);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "删除管理员成功！");
        feedbackMap.put("info", "已删除管理员：" + userId + "！");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 更新管理员密码
     *
     * @param user
     * @param oldPassword
     * @param model
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "修改管理员密码")
    @RequestMapping(value = "/update_admin_password", method = RequestMethod.POST)
    public String updateAdminPassword(User user, String oldPassword, Model model) throws IOException {
        System.err.println(user.getUserId() + "." + user.getPassword() + "\nold_password:" + oldPassword);
        int flag = userService.updateAdminPassword(user, oldPassword);
        if (flag == -1) {
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "密码修改失败！");
            feedbackMap.put("info", "密码未正确更新。原因很可能在于旧密码错误，请稍后重试或联系开发人员！");
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "密码修改成功！");
        feedbackMap.put("info", "密码已完成更新，请将新密码妥善保管。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
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
     * @param contentFile   md文章文件
     * @param picture       文章题图文件
     * @param articleImages 文章素材图片
     * @param summary       文章概述
     * @param model         反馈对象
     * @return 基本信息
     * @throws IOException 基本错误
     */
    @OperateLog(operateDesc = "添加文章数据")
    @RequestMapping(value = "/upload_article_file", method = RequestMethod.POST)
    public String uploadArticle(@RequestParam("file") MultipartFile contentFile, @RequestParam("picture") MultipartFile picture, MultipartFile[] articleImages, String summary, Model model) throws IOException {
        File targetContentFile;
        File targetPictureFile;
        try {
            //上传内容markdown文件至/static/md/目录下
            targetContentFile = uploadFile(contentFile, "md");
            //上传题图文件至/static/img/目录下
            targetPictureFile = uploadFile(picture, "img");
            //上传所有图片素材文件至/static/img目录下
            for (MultipartFile articleImage : articleImages) {
                uploadFile(articleImage, "img");
            }

            //将文件内容添加至数据库
            Long articleId = articleContentService.addContentByFile(targetContentFile, summary);
            //将图片地址添加至数据库
            articlePictureService.addPicByUrl("img/" + picture.getOriginalFilename());

            //将数据库内容转换为html保存
            articleContentService.toHtmlArticleContent(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "添加文章失败");
            feedbackMap.put("info", "文章上传失败！请检查后重试，或尝试联系开发者。");
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "添加文章成功");
        feedbackMap.put("info", "文章——" + targetContentFile.getName() + "上传成功！文件已存放至服务器。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 添加文章分类标签
     *
     * @param categoryName 文章分类标签名称
     * @return 重定向
     */
    @OperateLog(operateDesc = "新增标签")
    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public String addCategory(String categoryName, Model model) {
        //创建标签实体对象
        TblArticleCategory articleCategory = new TblArticleCategory();
        articleCategory.setCategoryName(categoryName);

        //新增标签
        articleCategoryService.addCategory(articleCategory);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "新增标签成功");
        feedbackMap.put("info", "标签——“" + categoryName + "”创建成功！您现在可以为指定文章添加该标签了");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 选择文章添加指定分类标签
     *
     * @param articleId  文章外键
     * @param categoryId 分类标签外键
     * @return
     */
    @OperateLog(operateDesc = "为文章添加标签")
    @RequestMapping(value = "/add_category_to_article", method = RequestMethod.POST)
    public String addCategoryToArticle(Long articleId, Long categoryId, Model model) {
        //封装标签对象
        Classification classification = new Classification();
        classification.setArticleId(articleId);
        classification.setCategoryId(categoryId);

        //添加标签至中间表
        try {
            classificationService.addCategoryToArticle(classification);
        } catch (Exception e) {
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "文章标签添加失败！");
            feedbackMap.put("info", "标签添加失败了！详细消息请见：\n" + e.getMessage());
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "文章标签添加成功！");
        feedbackMap.put("info", "标签添加成功了！现在您可以在文章标签栏看到它。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 逻辑删除文章
     *
     * @param id    文章ID
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "删除文章")
    @RequestMapping("/del_article")
    public String delArticle(@RequestParam(value = "id") Long id, Model model) throws IOException {
        try {
            articleInfoService.delArticle(id);//逻辑删除文章信息
            articlePictureService.delPic(id);//逻辑删除题图数据以解决删除文章后题图不对应bug
//            articleContentService.delContent(id);//逻辑删除文章内容
        } catch (Exception e) {
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "文章删除失败！");
            feedbackMap.put("info", "删除文章的过程中可能发送了意想不到的错误，请联系管理员：\n" + e.getMessage());
            model.addAttribute("feedbackMap", feedbackMap);
            return "adminLTE/feedback";
        }

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "文章删除成功！");
        feedbackMap.put("info", "现在这篇文章已无法被查看。若该删除操作是一个意外，请联系管理员！");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
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
    public String addLibraryAuthor(@RequestParam(value = "pic_file") MultipartFile picFile, LibraryAuthor libraryAuthor, Model model) throws IOException {
        //设置头像路径
        libraryAuthor.setPicUrl("img/" + picFile.getOriginalFilename());

        //上传文件：存放至/static/img/目录
        uploadFile(picFile, "img");
        //添加至数据库
        libraryAuthorService.addLibraryAuthor(libraryAuthor);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "人物添加成功！");
        feedbackMap.put("info", "文库人物添加成功了！现在您可以在文库的索引页中发现他。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 添加文库文章
     *
     * @param authorIndex   作者索引对象
     * @param pdfFile       pdf文件
     * @param characterName 作者名
     * @param model         视图
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "添加文库文章")
    @RequestMapping(value = "/add_author_index", method = RequestMethod.POST)
    public String addAuthorIndex(AuthorIndex authorIndex, @RequestParam("pdf_file") MultipartFile pdfFile, @RequestParam(value = "character_name") String characterName, Model model) throws IOException {
        //初始化authorId的值
        LibraryAuthor author = libraryAuthorService.getAuthorByCharacterName(characterName);
        authorIndex.setAuthorId(author.getId());
        //初始化pdfUrl的值
        authorIndex.setPdfUrl("/literature?url=" + "http://8.130.39.9:8080/" + "pdf/" + pdfFile.getOriginalFilename());

        //保存文件至target并上传至数据库
        try {
            //pdfFile.transferTo(targetPicFile);
            uploadFile(pdfFile, "pdf");//上传文件至/static/pdf/目录
            authorIndexService.addAuthorIndex(authorIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "文库文章添加成功！");
        feedbackMap.put("info", "文库文章添加成功了！现在您可以在人物的索引页中发现它。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 逻辑删除按article_id文章
     *
     * @param articleId
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "删除文库文章")
    @RequestMapping("/del_article_index")
    public String deleteArticleIndex(@RequestParam("article_id") Long articleId, Model model) {
        authorIndexService.deleteAuthorIndex(articleId);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "文库文章删除成功！");
        feedbackMap.put("info", "现在这篇文库文章已无法被查看。若该删除操作是一个意外，请立刻联系管理员！");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
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
     * 上传文件通用方法
     *
     * @param file     MultipartFile 文件对象
     * @param filePath 静态资源文件目录——img、md、page、pdf
     * @return 创建的targetPicFile对象
     * @throws FileNotFoundException 抛出找不到文件异常
     */
    private File uploadFile(MultipartFile file, String filePath) throws FileNotFoundException {
        //获取上传文件的地址
        File newFile = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对my_web_project\target\classes
        String absolutePath = newFile.getAbsolutePath();
        //图片文件放入/static/#/目录下
        String pathPic = absolutePath + "/static/" + filePath + "/";
        //不存在则创建文件
        File targetPicFile = new File(pathPic, Objects.requireNonNull(file.getOriginalFilename()));
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        //保存文件至target
        try {
            file.transferTo(targetPicFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        //复制target文件到/static/#/(仅idea测试时使用)
        try {
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + filePath +"\\" + file.getOriginalFilename()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        return targetPicFile;//返回创建的目标文件对象
    }

    /**
     * 测试文章，解析得html静态资源上传至服务器
     *
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "测试博客文章生成html")
    @RequestMapping(value = "/testArticle/{id}", method = RequestMethod.GET)
    public String testArticle(@PathVariable Long id, Model model) throws IOException {
        //转换为html保存在本地
        articleContentService.toHtmlArticleContent(id);

        String articleTitle = articleInfoService.getArticleTitle(id);
        String url = "../static/page/" + articleTitle;
        url += ".html";

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "测试完成！");
        feedbackMap.put("info", "现在这篇文章已被生成，请在静态资源目录中去查看。");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

}
