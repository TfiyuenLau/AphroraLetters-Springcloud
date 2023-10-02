package team.aphroraletters.article.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.aphroraletters.article.pojo.entity.*;
import team.aphroraletters.article.pojo.request.ArticleParams;
import team.aphroraletters.article.pojo.response.ArticleInfoListVO;
import team.aphroraletters.article.pojo.response.ArticleVO;
import team.aphroraletters.article.pojo.response.ResultVO;
import team.aphroraletters.article.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.Transient;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
public class ArticleController {
    @Autowired
    private ITblArticleInfoService articleInfoService;

    @Autowired
    private ITblArticleContentService articleContentService;

    @Autowired
    private ITblArticleCommentService articleCommentService;

    @Autowired
    private ITblArticleCategoryService articleCategoryService;

    @Autowired
    private IClassificationService classificationService;

    @ApiOperation("获取推荐文章")
    @GetMapping("/getRecommendArticles")
    public ResultVO getRecommendArticles() {
        List<TblArticleInfo> recommendArticle = articleInfoService.getRecommendArticle();
        if (recommendArticle.isEmpty()) {
            return ResultVO.errorMsg("暂无推荐文章");
        }

        return ResultVO.ok(recommendArticle);
    }

    @ApiOperation("通过page获取文章信息列表")
    @GetMapping("/getArticleListByPage/{page}")
    public ResultVO getArticleInfoListByPage(@PathVariable("page") Long page) {
        IPage<TblArticleInfo> articleInfoIPage = articleInfoService.getArticleByPage(page);
        if (articleInfoIPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("无效的页码");
        }

        // 封装新的分页对象：ArticleInfoListVO
        IPage<ArticleInfoListVO> articleListResponseIPage = new Page<>();
        List<ArticleInfoListVO> articleInfoListVOS = new ArrayList<>();
        for (TblArticleInfo articleInfo : articleInfoIPage.getRecords()) {
            ArticleInfoListVO articleInfoListVO = new ArticleInfoListVO();
            BeanUtils.copyProperties(articleInfo, articleInfoListVO); // 拷贝数据至response对象中
            // 封装分类标签
            articleInfoListVO.setCategoryList(classificationService.getArticleCategoryListByArticleId(articleInfo.getId()));
            articleInfoListVOS.add(articleInfoListVO);
        }
        BeanUtils.copyProperties(articleInfoIPage, articleListResponseIPage); // 拷贝分页对象其他参数
        articleListResponseIPage.setRecords(articleInfoListVOS);

        return ResultVO.ok(articleListResponseIPage);
    }

    @ApiOperation("通过id获取文章视图对象")
    @GetMapping("/getArticleById/{id}")
    public ResultVO getArticleById(@PathVariable("id") Long articleId) {
        // 通过id获取文章Info对象
        TblArticleInfo articleInfo = articleInfoService.getArticleById(articleId);
        if (articleInfo == null) {
            return ResultVO.errorMsg("没有对应找到id为{" + articleId + "}的文章");
        }

        // 封装视图对象
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(articleInfo, articleVO);
        try {
            articleVO.setContent(articleContentService.getArticleContentByArticleId(articleId).getContent()); // 封装文章md内容
            articleVO.setCategoryList(classificationService.getArticleCategoryListByArticleId(articleId)); // 封装分类标签
            articleVO.setArticleCommentList(articleCommentService.getComment(articleId)); // 封装评论集合
        } catch (Exception e) {
            return ResultVO.errorException("Error:" + e.getMessage());
        }

        return ResultVO.ok(articleVO);
    }

    @ApiOperation("搜索文章内容获取相关内容")
    @GetMapping("/searchArticleByContent")
    public ResultVO searchArticleByContent(@RequestParam("content") String content, @RequestParam("page") Integer page) {
        IPage<TblArticleContent> articleContentIPage = articleContentService.searchContent(content, page);
        if (articleContentIPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("找不到含有字段【" + content + "】的文章");
        }

        // 封装文章信息分页列表对象
        IPage<ArticleInfoListVO> infoListVOPage = new Page<>();
        BeanUtils.copyProperties(articleContentIPage, infoListVOPage);
        ArrayList<ArticleInfoListVO> infoListVOS = new ArrayList<>();
        for (TblArticleContent record : articleContentIPage.getRecords()) {
            TblArticleInfo articleInfo = articleInfoService.getArticleById(record.getId());
            ArticleInfoListVO articleInfoListVO = new ArticleInfoListVO();
            BeanUtils.copyProperties(articleInfo, articleInfoListVO);
            articleInfoListVO.setCategoryList(articleCategoryService.getCategoriesByArticleId(articleInfoListVO.getId()));
            infoListVOS.add(articleInfoListVO);
        }
        infoListVOPage.setRecords(infoListVOS);

        return ResultVO.ok(infoListVOPage);
    }

    @ApiOperation("添加一条文章评论")
    @PostMapping("/insertArticleComment")
    public ResultVO insertArticleComment(@RequestBody TblArticleComment articleComment) {
        boolean flag;
        try {
            flag = articleCommentService.insertComment(articleComment);
        } catch (RuntimeException e) {
            return ResultVO.errorException("Error:" + e);
        }

        return ResultVO.ok(flag);
    }

    @ApiOperation("管理员通过page获取文章信息列表")
    @GetMapping("/admin/getArticleListByPage/{page}")
    public ResultVO getArticleListByPage(@PathVariable("page") Long page) {
        IPage<TblArticleInfo> articleInfoIPage = articleInfoService.getArticleByPage(page);
        if (articleInfoIPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("无效的页码");
        }

        // 封装新的分页对象：ArticleVOIPage
        IPage<ArticleVO> articleListResponseIPage = new Page<>();
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (TblArticleInfo articleInfo : articleInfoIPage.getRecords()) {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(articleInfo, articleVO); // 拷贝数据至response对象中
            // 封装分类标签
            articleVO.setContent(articleContentService.getArticleContentByArticleId(articleInfo.getId()).getContent());
            articleVO.setCategoryList(classificationService.getArticleCategoryListByArticleId(articleInfo.getId()));
            articleVOList.add(articleVO);
        }
        BeanUtils.copyProperties(articleInfoIPage, articleListResponseIPage); // 拷贝分页对象其他参数
        articleListResponseIPage.setRecords(articleVOList);

        return ResultVO.ok(articleListResponseIPage);
    }

    @ApiOperation("新增一篇文章")
    @PostMapping("/admin/insertArticle")
    @Transient // 开启MySql事务
    public ResultVO insertArticle(@RequestBody ArticleParams articleParams) {
        TblArticleInfo articleInfo = new TblArticleInfo();
        BeanUtils.copyProperties(articleParams, articleInfo);

        TblArticleContent articleContent = new TblArticleContent();
        BeanUtils.copyProperties(articleParams, articleContent);

        articleInfoService.insertArticleInfo(articleInfo);
        articleContentService.insertArticleContent(articleContent);

        return ResultVO.ok("添加成功");
    }

    @ApiOperation("上传文章图片")
    @PostMapping("/admin/uploadArticleImage")
    public ResultVO uploadArticleImage(@RequestParam("image") MultipartFile imageFile) {
        File uploadFile;
        try {
            uploadFile = uploadFile(imageFile, "img");
        } catch (FileNotFoundException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(uploadFile.getName());
    }

    @ApiOperation("更新文章信息数据")
    @PutMapping("/admin/updateArticleInfo")
    public ResultVO updateArticleInfo(@RequestBody TblArticleInfo articleInfo) {
        try {
            articleInfoService.updateArticleInfo(articleInfo);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("修改文章基本信息成功");
    }

    @ApiOperation("更新文章题图")
    @PostMapping("/admin/updateArticleImage")
    public ResultVO updateArticleImage(@RequestParam("image") MultipartFile imageFile, @RequestParam("id") Long id) {
        File uploadFile;
        try {
            uploadFile = uploadFile(imageFile, "img");

            TblArticleInfo articleInfo = new TblArticleInfo();
            articleInfo.setId(id);
            articleInfo.setPictureUrl("img/" + uploadFile.getName());
            articleInfoService.updateArticleInfo(articleInfo);
        } catch (FileNotFoundException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(uploadFile.getName());
    }

    @ApiOperation("更新文章内容数据")
    @PutMapping("/admin/updateArticleContent")
    public ResultVO updateArticleContent(@RequestBody TblArticleContent articleContent) {
        try {
            articleContentService.updateArticleContent(articleContent);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("修改文章内容成功");
    }

    @ApiOperation("管理员通过id删除文章信息数据")
    @DeleteMapping("/admin/deleteArticleInfoById/{id}")
    public ResultVO deleteArticleInfoById(@PathVariable("id") Long id) {
        try {
            articleInfoService.deleteArticleInfoById(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("删除文章成功");
    }

    /**
     * 上传文件通用方法
     *
     * @param file         MultipartFile 文件对象
     * @param filePathName 静态资源文件目录——img、md、page、pdf
     * @return 创建的targetPicFile对象
     * @throws FileNotFoundException 抛出找不到文件异常
     */
    private File uploadFile(MultipartFile file, String filePathName) throws FileNotFoundException {
        // 获取上传文件的地址
        File newFile = new File(ResourceUtils.getURL("classpath:").getPath());
        // 项目路径绝对my_web_project\target\classes
        String absolutePath = newFile.getAbsolutePath();
        // 文件放入/static/#/目录下
        String pathPic = absolutePath + "/static/" + filePathName + "/";
        // 获取文件后缀
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        // 生成图片路径和唯一标识
        File targetPicFile = new File(pathPic, UUID.randomUUID().toString().replace("-", "") + fileSuffix);
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        // 保存文件至target
        try {
            file.transferTo(targetPicFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 复制target文件到/static/*/(仅Win开发时使用)
        try {
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(),
                    new File(System.getProperty("user.dir") + "\\article-service\\src\\main\\resources\\static\\" + filePathName + "\\" + targetPicFile.getName()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return targetPicFile; // 返回创建的目标文件对象
    }

}
