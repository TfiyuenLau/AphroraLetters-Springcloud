package com.marxist.leftwing_community;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.dao.TblArticleInfoMapper;
import com.marxist.leftwing_community.domain.MarkdownEntity;
import com.marxist.leftwing_community.entity.TblArticleComment;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.entity.TblArticlePicture;
import com.marxist.leftwing_community.service.impl.TblArticleCommentServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticleInfoServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticlePictureServiceImpl;
import com.marxist.leftwing_community.util.MarkDown2HtmlWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class LeftwingCommunityApplicationTests {
    @Autowired
    private TblArticlePictureServiceImpl articlePictureService;

    @Autowired
    private TblArticleInfoMapper articleInfoMapper;

    @Autowired
    private TblArticleInfoServiceImpl articleInfoService;

    @Autowired
    TblArticleCommentServiceImpl articleCommentService;

    @Test
    void contextLoads() {
    }

    //测试MarkDown2HtmlWrapper工具类
    @Test
    public void markdown2html() throws IOException {
        String file = "D:\\Projects\\LeftwingCommunity-Springboot\\src\\main\\resources\\static\\md\\test_Иванович.md";
        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);

        String html_content = new String();
        String head = "<!DOCTYPE html>\n" +
                "<!-- 加载thymeleaf模板 -->\n" +
                "<html lang=\"zh-CN\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Default</title>\n" +
                "\n" +
                "    <link rel=\"icon\" href=\"../img/logo.png\" type=\"image/x-icon\">\n" +
                "    <link href=\"../css/markdown.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "\n" +
                "    <script>\n" +
                "        function IFrameResize() {\n" +
                "            //alert(this.document.body.scrollHeight); //弹出当前页面的高度\n" +
                "            var obj = parent.document.getElementById(\"childFrame\"); //取得父页面IFrame对象\n" +
                "            //alert(obj.height); //弹出父页面中IFrame中设置的高度\n" +
                "            obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body onload=\"IFrameResize()\">";
        html_content += head;//拼接head标签与js语法
        html_content += html.getHtml();//拼接html

        System.out.println(html_content);
    }

    //测试按id获取图片对象List集合
    @Test
    public void testGetPic() {
        List<TblArticlePicture> pictureUrl = articlePictureService.getPictureUrl(7L);

        System.out.println(pictureUrl.get(0).getPictureUrl());
        System.out.println(pictureUrl.get(1).getPictureUrl());
    }

    //测试分页插件
    @Test
    public void testPage() {
        //查询TblArticleInfo表的第1页，一页10条数据
        IPage<TblArticleInfo> infoPage = new Page<>(1, 10);
        articleInfoMapper.selectPage(infoPage, null);

        System.err.println("查询结果列表大小:" + infoPage.getRecords().size());
        System.err.println("总条数:" + infoPage.getTotal());
        System.err.println("当前页:" + infoPage.getCurrent());
        System.err.println("总页数:" + infoPage.getPages());

        //遍历第一页，打印数据项
        for (TblArticleInfo pageRecord : infoPage.getRecords()) {
            System.err.println(pageRecord.getId() + "." + pageRecord.getTitle() + "：\n" + pageRecord.getSummary());
        }

    }

    //测试Search检索与分页
    @Test
    public void testSearch() {
        String contentLike = new String("");
        IPage<TblArticleInfo> infoIPage = articleInfoService.searchArticleInfoByPage(contentLike, 2);
        IPage<TblArticlePicture> pictureIPage = articlePictureService.searchArticlePicByPage(contentLike, 1);

        for (TblArticleInfo infoIPageRecord : infoIPage.getRecords()) {
            System.err.println(infoIPageRecord.getId() + "." + infoIPageRecord.getTitle() + "： \n" + infoIPageRecord.getSummary());
        }

        for (TblArticlePicture pictureIPageRecord : pictureIPage.getRecords()) {
            System.err.println(pictureIPageRecord.getPictureUrl());
        }

    }

    //测试comment查询数据库对象
    @Test
    public void testComment(){
        List<TblArticleComment> comments = articleCommentService.getComment(1L);
        for (TblArticleComment comment : comments) {
            System.out.println(comment.getId() + "." + comment.getEmail() + "的评论内容:\n" + comment.getComment());
        }

    }

}
