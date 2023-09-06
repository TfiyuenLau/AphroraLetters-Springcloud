package team.aphroraletters.library.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import team.aphroraletters.library.entity.*;
import team.aphroraletters.library.service.*;
import team.aphroraletters.library.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ILibraryAuthorService libraryAuthorService;

    @Autowired
    private IAuthorIndexService authorIndexService;

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
        authorIndex.setPdfUrl("/literature?url=" + "http://8.130.39.9:8080/" + "static/pdf/" + pdfFile.getOriginalFilename());

        //保存文件至target并上传至数据库
        try {
            uploadFile(pdfFile, "static/pdf");//上传文件至/static/pdf/目录
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

}
