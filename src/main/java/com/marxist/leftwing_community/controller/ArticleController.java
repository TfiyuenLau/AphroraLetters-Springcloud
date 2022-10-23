package com.marxist.leftwing_community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.TblArticleComment;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.entity.TblArticlePicture;
import com.marxist.leftwing_community.service.impl.TblArticleCommentServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticleInfoServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticlePictureServiceImpl;
import com.marxist.leftwing_community.util.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private TblArticleInfoServiceImpl articleInfoService;

    @Autowired
    private TblArticlePictureServiceImpl articlePictureService;

    @Autowired
    private TblArticleCommentServiceImpl articleCommentService;

    /**
     * 获取所有文章传递后形成列表
     *
     * @param page 文章列表页码
     * @param model
     * @return Template
     */
    @OperateLog(operateDesc = "查询文章列表")
    @RequestMapping("/article_list")
    public String getAllArticleInfo(@RequestParam(value = "page", required = false, defaultValue = "1") Long page, Model model) {
        //分页查询文章列表
        IPage<TblArticleInfo> infoPage = articleInfoService.getArticleByPage(page);
        model.addAttribute("aPageArticleInfo", infoPage.getRecords());

        //底部分页导航栏
        ArrayList<Integer> pageCount = new ArrayList<>((int) infoPage.getPages());//初始化集合
        for (int i = 1; i <= infoPage.getPages(); i++) {
            pageCount.add(i);
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", infoPage.getCurrent());//当前页码

        //图片列表
        List<String> allPictureUrl = articlePictureService.getAllPictureUrl();
        model.addAttribute("allPictureUrl", allPictureUrl);

        //获取推荐文章
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "article_list";
    }

    /**
     * 按id获取文章并返回页面
     *
     * @param id 文章id
     * @param token 标识是否发送评论及其是否成功
     * @param model
     * @return Template
     */
    @OperateLog(operateDesc = "查询文章")
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public String getArticle(@RequestParam(value = "id", defaultValue = "1") Long id, String token, Model model) {
        //获取文章信息
        TblArticleInfo article = articleInfoService.getArticleById(id);
        model.addAttribute("articleTitle", article.getTitle());
        model.addAttribute("url", article.getTitle() + ".html");
        model.addAttribute("article_id", id);

        //获取文章评论对象集合
        List<TblArticleComment> articleComments = articleCommentService.getComment(id);
        model.addAttribute("articleComments", articleComments);

        //获取评论头像地址
        ArrayList<String> portraitList = new ArrayList<>();
        for (TblArticleComment articleComment : articleComments) {
            String url = "/img/portrait/" + articleComment.getId() % 17 + ".jpg";
            portraitList.add(url);
        }
        model.addAttribute("portraitList", portraitList);

        //评论flag
        model.addAttribute("token", token);

        //获取推荐文章
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "article";
    }

    /**
     * 在特定文章添加评论
     *
     * @param articleId 评论的文章Id
     * @param email 评论者的邮箱
     * @param comment 评论体
     * @param model
     * @return
     */
    @OperateLog(operateDesc = "添加评论")
    @RequestMapping(value = "/article/insert", method = RequestMethod.GET)
    public String addComment(@RequestParam(value = "article_id") Long articleId, @RequestParam(value = "email", defaultValue = "") String email, @RequestParam(value = "comment", defaultValue = "") String comment, Model model) {
        //将传入的表单数据封装成comment对象
        TblArticleComment articleComment = new TblArticleComment();
        articleComment.setArticleId(articleId);
        articleComment.setEmail(email);
        articleComment.setComment(comment);

        //添加至数据库,传送token
        boolean flag = articleCommentService.insertComment(articleComment);
        String token;
        if (flag){
            token = new String("true");
        } else {
            token = new String("false");
        }

        //重定向至文章请求
        return "redirect:/article?id=" + articleId + "&token=" + token;
    }

    /**
     * 按字段搜索获取文章并返回页面
     *
     * @param contentLike 搜索的词汇
     * @param page 页码
     * @param model
     * @return Template
     */
    @OperateLog(operateDesc = "搜索文章")
    @RequestMapping(value = "/article_search", method = RequestMethod.GET)
    public String searchArticle(@RequestParam(value = "content_like", required = false) String contentLike, @RequestParam(value = "page", required = false) Integer page, Model model) {
        //获取分页后的检索内容
        IPage<TblArticleInfo> infoPage = articleInfoService.searchArticleInfoByPage(contentLike, page);
        model.addAttribute("aInfoPage", infoPage.getRecords());
        model.addAttribute("content_like", contentLike);

        //底部分页导航栏
        ArrayList<Long> pageCount;
        if (infoPage.getPages() == 0) {//判断页码数是否为零，为零则值1
            pageCount = new ArrayList<>();
            pageCount.add(1L);
        } else {//非零则按长度初始化集合
            pageCount = new ArrayList<>((int) infoPage.getPages());
            for (long i = 1L; i <= infoPage.getPages(); i++) {
                pageCount.add(i);
            }
        }
        model.addAttribute("pageCount", pageCount);//全部页码列表
        model.addAttribute("pageCurrent", infoPage.getCurrent());//当前页码

        //获取相应的图片
        IPage<TblArticlePicture> picturePage = articlePictureService.searchArticlePicByPage(contentLike, page);
        model.addAttribute("pic", picturePage.getRecords());

        //获取推荐文章
        List<TblArticleInfo> recommendArticles = articleInfoService.getRecommendArticle();
        model.addAttribute("recommendArticles", recommendArticles);

        return "article_search";
    }

}
