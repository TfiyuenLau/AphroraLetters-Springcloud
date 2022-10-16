package com.marxist.leftwing_community.util;

import com.google.common.base.Joiner;
import com.marxist.leftwing_community.domain.MarkdownEntity;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Markdown转Html包装器
 *
 * @Author:MatikaneSpartakusbund
 */
public class MarkDown2HtmlWrapper {

    private static String MD_CSS = null;

    static {
        try {
            MD_CSS = FileReadUtil.readAll("D:\\Projects\\LeftwingCommunity-Springboot\\src\\main\\resources\\static\\css\\markdown.css");
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }


    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream
     * @return
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(lines);
        return ofContent(content);
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }


    /**
     * markdown to image
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

    //FileReadUtil内部工具类
    private static class FileReadUtil {
        public static String readAll(String fileName) throws IOException {
            BufferedReader reader = createLineRead(fileName);
            List<String> lines = reader.lines().collect(Collectors.toList());
            return Joiner.on("\n").join(lines);
        }

        /**
         * 以行为单位读取文件，常用于读面向行的格式化文件
         *
         * @param fileName 文件名
         */
        public static BufferedReader createLineRead(String fileName) throws IOException {
            return new BufferedReader(new InputStreamReader(getStreamByFileName(fileName), StandardCharsets.UTF_8));
        }

        private static InputStream getStreamByFileName(String fileName) throws IOException {
            return Files.newInputStream(Paths.get(fileName));
        }

    }
}
