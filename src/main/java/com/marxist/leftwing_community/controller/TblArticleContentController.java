package com.marxist.leftwing_community.controller;

import com.marxist.leftwing_community.service.ITblArticleContentService;
import com.marxist.leftwing_community.service.ITblArticleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Deprecated
@Controller
public class TblArticleContentController {
    @Autowired
    private ITblArticleContentService articleContentService;

    @Autowired
    private ITblArticleInfoService articleInfoService;

    //测试文章，解析得html静态资源上传至服务器
    @ResponseBody
    @RequestMapping(value = "/testArticle/{id}", method = RequestMethod.GET)
    public String testArticle(@PathVariable Long id) throws IOException {
        //转换为html保存在本地
        articleContentService.toHtmlArticleContent(id);

        String articleTitle = articleInfoService.getArticleTitle(id);
        String url = "../static/page/" + articleTitle;
        url += ".html";

        return url + "测试完成!";
    }

}
