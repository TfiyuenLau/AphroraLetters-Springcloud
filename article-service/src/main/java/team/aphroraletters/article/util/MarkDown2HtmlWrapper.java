package team.aphroraletters.article.util;

import com.google.common.base.Joiner;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.jetbrains.annotations.NotNull;
import team.aphroraletters.article.pojo.entity.MarkdownEntity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Markdown转Html包装器
 * 使用flexmark-java库（v0.64.0）实现markdown2html
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
     * @return MarkdownEntity
     * @throws IOException IO异常
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return MarkdownEntity
     * @throws IOException IO异常
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream InputStream对象
     * @return MarkdownEntity
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
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
        String html = parse(content);//转换为html文本
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body");
        return entity;
    }


    /**
     * 拼接html的首尾
     *
     * @param entity MarkdownEntity
     * @return htmlContent
     */
    public static String htmlAppend(MarkdownEntity entity) {
        String htmlContent = new String();
        String head = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
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
                "            var obj = parent.document.getElementById(\"childFrame\"); //取得父页面IFrame对象\n" +
                "            obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body onload=\"IFrameResize()\">\n\n";
        htmlContent += head;//拼接head标签与js语法
        htmlContent += entity.getHtml();//拼接html
        htmlContent += "\n</body>\n" + "</html>";//拼接尾部

        return htmlContent;
    }


    /**
     * 将markdown文本转换为html文本
     * （flexmark的基本使用）
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        //配置转换器的相关参数
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Arrays.asList(
                EmojiExtension.create(),//表情转换扩展
                TablesExtension.create(),//表格转换扩展
                LinkTargetExtension.create(),//为<a>标签属性target添加_blank
                HeadingIdExtension.create()//为<h>标签添加id
        ));

        //创建转换器
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();//markdown2html渲染器
        Node document = parser.parse(content);
        return renderer.render(document);
    }


    /**
     * 为元素添加属性：为<a></a>标签的target属性添加_blank
     * 自定义解析扩展——通过实现AttributeProvider来修改元素是flexmark-java常用的扩展方式之一
     */
    static class LinkTargetAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
            // 定位到<a>标签元素进行修改
            if (node instanceof Link && part == AttributablePart.LINK) {
                attributes.addValue("target", "_blank");
            }
        }

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @NotNull
                @Override
                public AttributeProvider apply(@NotNull LinkResolverContext context) {
                    return new LinkTargetAttributeProvider();
                }
            };
        }
    }


    /**
     * 为标题标签<a></a>添加统一的class属性
     */
    static class HeadingIdAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
            // 定位到<h>元素进行修改
            if (node instanceof Heading && part == AttributablePart.NODE) {
                attributes.addValue("class", "heading");
            }
        }

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @Override
                public @NotNull AttributeProvider apply(@NotNull LinkResolverContext linkResolverContext) {
                    return new HeadingIdAttributeProvider();
                }
            };
        }
    }


    /**
     * 注册LinkTargetExtension
     * 使用前须通过HtmlRenderer.Builder.attributeProviderFactory的方式注册自定义的扩展
     */
    static class LinkTargetExtension implements HtmlRenderer.HtmlRendererExtension {
        @Override
        public void rendererOptions(@NotNull MutableDataHolder options) {
            // add any configuration settings to options you want to apply to everything, here
        }

        @Override
        public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
            htmlRendererBuilder.attributeProviderFactory(LinkTargetAttributeProvider.Factory());
        }

        static LinkTargetExtension create() {
            return new LinkTargetExtension();
        }
    }


    /**
     * 注册LinkTargetExtension
     * 使用前须通过HtmlRenderer.Builder.attributeProviderFactory的方式注册自定义的扩展
     */
    static class HeadingIdExtension implements HtmlRenderer.HtmlRendererExtension {
        @Override
        public void rendererOptions(@NotNull MutableDataHolder options) {
            // add any configuration settings to options you want to apply to everything, here
        }

        @Override
        public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
            htmlRendererBuilder.attributeProviderFactory(HeadingIdAttributeProvider.Factory());
        }

        static HeadingIdExtension create() {
            return new HeadingIdExtension();
        }
    }


    /**
     * 内部工具类——FileReadUtil
     */
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
