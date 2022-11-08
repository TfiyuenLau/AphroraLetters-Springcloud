package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.domain.MarkdownEntity;
import com.marxist.leftwing_community.entity.TblArticleContent;
import com.marxist.leftwing_community.dao.TblArticleContentMapper;
import com.marxist.leftwing_community.entity.TblArticleInfo;
import com.marxist.leftwing_community.service.ITblArticleContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marxist.leftwing_community.util.MarkDown2HtmlWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 * 对文章内容进行操作
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Service
public class TblArticleContentServiceImpl extends ServiceImpl<TblArticleContentMapper, TblArticleContent> implements ITblArticleContentService {
    @Autowired
    private TblArticleContentMapper articleContentMapper;

    @Autowired
    private TblArticleInfoServiceImpl articleInfoService;

    /**
     * 将md文本转换为html并保存于本地
     *
     * @param id
     * @throws IOException
     */
    @Override
    public void toHtmlArticleContent(Long id) throws IOException {
        TblArticleContent content = articleContentMapper.selectById(id);

        //使用工具类将md转为html格式
        MarkdownEntity html = MarkDown2HtmlWrapper.ofContent(content.getContent());

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

        //获取文件名
        String fileName = articleInfoService.getArticleTitle(id) + ".html";
        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对mywebproject\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //放入/static/page/目录下
        String path = absolutePath + "/static/page/";
        File targetFile = new File(path, fileName);

        //写入文件至target
        BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile));
        bw.write(html_content);
        bw.flush();
        bw.close();

        //复制target文件到/static/page/(仅idea开发时使用)
        //Files.copy(new File(targetFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\page\\" + fileName).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 按字段搜索文章content并返回分页对象
     *
     * @param contentLike
     * @return QueryWrapper
     */
    @Override
    public IPage<TblArticleContent> searchContentByPage(String contentLike, Integer page) {
        //模糊查询content内容
        QueryWrapper<TblArticleContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("content", contentLike);

        //分页查询
        IPage<TblArticleContent> contentIPage = new Page<>(page, 10);
        articleContentMapper.selectPage(contentIPage, queryWrapper);

        return contentIPage;
    }

    /**
     * 添加内容至数据库
     *
     * @param file
     * @param summary
     * @return
     * @throws IOException
     */
    @Override
    public Long addContentByFile(File file, String summary) throws IOException {
        //读取获取的文件
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append('\n');
        }

        //添加Content到数据库
        TblArticleContent articleContent = new TblArticleContent();
        //设置Id为上一个id+1
        articleContent.setId(articleContentMapper.selectList(new QueryWrapper<TblArticleContent>().orderByDesc("id")).get(0).getId() + 1);
        articleContent.setArticleId(articleContent.getId());//设置与id相等
        articleContent.setContent(content.toString());
        articleContentMapper.insert(articleContent);

        //添加Info到数据库
        TblArticleInfo articleInfo = new TblArticleInfo();
        articleInfo.setTitle(file.getName().substring(0, file.getName().length() - 3));//去除.md后缀
        articleInfo.setSummary(summary);
        articleInfoService.addInfo(articleInfo);

        return articleContent.getId();
    }

}
