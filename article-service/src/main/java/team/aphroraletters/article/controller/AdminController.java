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
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.aphroraletters.article.pojo.entity.*;
import team.aphroraletters.article.pojo.request.LoginParams;
import team.aphroraletters.article.pojo.response.ResultVO;
import team.aphroraletters.article.service.*;
import team.aphroraletters.article.util.OperateLog;
import team.aphroraletters.article.util.RedisUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @Autowired
    private ITblArticleInfoService articleInfoService;

    @Autowired
    private ITblArticleContentService articleContentService;

    @Autowired
    private ITblArticlePictureService articlePictureService;

    @Autowired
    private ITblArticleCategoryService articleCategoryService;

    @Autowired
    private IAnnouncementService announcementService;

    @Autowired
    private IVersionLogService versionLogService;

    @Autowired
    private ISysLogService logService;

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
        IPage<Admin> userList = adminService.getAdminListByPage(page);
        //获取当前登录的账户,将非当前账户密码匿名
        String adminName = (String) session.getAttribute("adminName");
        for (Admin admin : userList.getRecords()) {
            if (!admin.getUsername().equals(adminName)) {
                admin.setPassword("********");
            }
        }

        model.addAttribute("userList", userList.getRecords());

        return "adminLTE/admin_list";
    }

    /**
     * 添加账户至数据库
     *
     * @param admin
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "添加管理员")
    @RequestMapping(value = "/add_admin", method = RequestMethod.POST)
    public String addAdmin(Admin admin, Model model) {
        //添加数据
        adminService.addAdmin(admin);

        //数据反馈
        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "添加管理员成功");
        feedbackMap.put("info", "已添加一名管理员：" + admin.getUsername() + "！您的加入将丰富我们的管理员团队。");
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
        adminService.delAdmin(userId);

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
     * @param admin
     * @param oldPassword
     * @param model
     * @return
     * @throws IOException
     */
    @OperateLog(operateDesc = "修改管理员密码")
    @RequestMapping(value = "/update_admin_password", method = RequestMethod.POST)
    public String updateAdminPassword(Admin admin, String oldPassword, Model model) throws IOException {
        System.err.println(admin.getId() + "." + admin.getPassword() + "\nold_password:" + oldPassword);
        int flag = adminService.updateAdminPassword(admin, oldPassword);
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

        //传输文章、分类标签与图片列表
        List<TblArticleInfo> allArticleInfo = articleInfoService.getAllArticleInfo();
        List<TblArticleCategory> allCategory = articleCategoryService.getAllCategory();
        List<String> allPictureUrl = articlePictureService.getAllPictureUrl();
        model.addAttribute("allPictureUrl", allPictureUrl);
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
    public String uploadArticle(@RequestParam("file") MultipartFile contentFile,
                                @RequestParam("picture") MultipartFile picture,
                                MultipartFile[] articleImages,
                                String summary,
                                Model model) throws IOException {
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
        articleCategoryService.insertCategory(articleCategory);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "新增标签成功");
        feedbackMap.put("info", "标签——“" + categoryName + "”创建成功！您现在可以为指定文章添加该标签了");
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
            articleInfoService.deleteArticleInfoById(id);//逻辑删除文章信息
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
     * 修改文章信息
     *
     * @param articlePic
     * @param articleId
     * @param articleTitle
     * @param articleSummary
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "修改文章信息")
    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    public String updateArticle(MultipartFile articlePic,
                                Long articleId,
                                String articleTitle,
                                String articleSummary,
                                Model model) {
        //创建反馈图
        HashMap<String, Object> feedbackMap = new HashMap<>();

        //判断是否需要修改该部分数据
        if (!Objects.equals(articlePic.getOriginalFilename(), "")) {
            try {
                //上传新图片
                File uploadImg = uploadFile(articlePic, "img");
                //更新数据库
                articlePictureService.updatePicById(articleId, "img/" + uploadImg.getName());
            } catch (FileNotFoundException e) {
                feedbackMap.put("flag", false);
                feedbackMap.put("title", "文章修改失败！");
                feedbackMap.put("info", "文章题图信息修改时出现错误....详细请见：\n" + new RuntimeException(e).getMessage());
                model.addAttribute("feedbackMap", feedbackMap);

                return "adminLTE/feedback";
            }
        }

        //不为空则部分更新数据
        try {
            if (!articleTitle.equals("")) {
                articleInfoService.updateArticleInfo(articleId, articleTitle, null);
            }
            if (!articleSummary.equals("")) {
                articleInfoService.updateArticleInfo(articleId, null, articleSummary);
            }
        } catch (Exception e) {
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "文章修改失败！");
            feedbackMap.put("info", "文章部分信息修改时出现错误....详细请见：\n" + new RuntimeException(e).getMessage());
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        //修改成功
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "文章修改成功！");
        feedbackMap.put("info", "文章的部分信息已完成修改，请查看...");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    @OperateLog(operateDesc = "查询公告栏")
    @RequestMapping("/announcement_admin")
    public String announcement(Long page, Model model) {
        //获取分页对象
        IPage<Announcement> announcementIPage = announcementService.getAnnouncementByPage(page);
        model.addAttribute("announcementList", announcementIPage.getRecords());

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) announcementIPage.getPages());//初始化集合
        for (int i = 1; i <= announcementIPage.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", announcementIPage.getCurrent());//当前页码

        return "adminLTE/announcement_admin";
    }

    /**
     * 添加公告栏通知消息
     *
     * @param publisher
     * @param title
     * @param content
     * @param imgFiles
     * @param model
     * @return
     */
    @RequestMapping("/addAnnouncement")
    public String addAnnouncement(@RequestParam(value = "publisher", required = false, defaultValue = "Leftwing Community") String publisher,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @RequestParam(value = "imgFile", required = false) MultipartFile[] imgFiles,
                                  Model model) {
        //创建反馈图
        HashMap<String, Object> feedbackMap = new HashMap<>();

        //判断是否需要上传题图
        if (imgFiles.length != 0) {
            try {
                for (MultipartFile file : imgFiles) {
                    uploadFile(file, "img");
                }
            } catch (FileNotFoundException e) {
                feedbackMap.put("flag", false);
                feedbackMap.put("title", "题图上传失败！");
                feedbackMap.put("info", "文章题图上传时出现错误，可能超出了最大16mb的限制....详细请见：\n" + new RuntimeException(e).getMessage());
                model.addAttribute("feedbackMap", feedbackMap);

                return "adminLTE/feedback";
            }
        }

        try {
            //封装公告对象并添加至数据库
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setPublisher(publisher);
            announcement.setContent(content);
            announcementService.insertAnnouncement(announcement);
        } catch (Exception e) {
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "公告添加失败！");
            feedbackMap.put("info", "这篇公告在添加的过程中出现了意料之外的错误，详细信息请见：\n" + new RuntimeException(e).getMessage());
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        feedbackMap.put("flag", true);
        feedbackMap.put("title", "公告添加成功！");
        feedbackMap.put("info", "现在这篇公告已被添加至了数据库！");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 删除一则公告
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/delAnnouncement")
    public String delAnnouncement(Long id, Model model) {
        announcementService.delAnnouncement(id);

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("flag", true);
        feedbackMap.put("title", "公告删除成功！");
        feedbackMap.put("info", "现在这篇公告消息已无法被查看。若该删除操作是一个意外，请立刻联系管理员！");
        model.addAttribute("feedbackMap", feedbackMap);

        return "adminLTE/feedback";
    }

    /**
     * 添加版本日志
     *
     * @param version
     * @param content
     * @param time
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "添加版本日志")
    @RequestMapping("/addVersionLog")
    public String addVersionLog(String version, String content, String time, Model model) {
        //创建反馈图
        HashMap<String, Object> feedbackMap = new HashMap<>();
        //创建VersionLog对象
        VersionLog versionLog = new VersionLog();

        //封装对象
        try {
            if (time != null && !"".equals(time)) {
                versionLog.setCreateBy(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
            }
            versionLog.setVersion(version);
            versionLog.setLog(content);

            versionLogService.insertVersionLog(versionLog);
        } catch (Exception e) {
            feedbackMap.put("flag", false);
            feedbackMap.put("title", "版本日志添加失败！");
            feedbackMap.put("info", "现在这篇日志未记载在了数据库中！详情请见：\n" + new RuntimeException(e).getMessage());
            model.addAttribute("feedbackMap", feedbackMap);

            return "adminLTE/feedback";
        }

        feedbackMap.put("flag", true);
        feedbackMap.put("title", "版本日志添加成功！");
        feedbackMap.put("info", "现在这篇日志已记载在了数据库中！");
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
