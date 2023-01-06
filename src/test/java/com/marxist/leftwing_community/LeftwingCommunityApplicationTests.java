package com.marxist.leftwing_community;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.dao.TblArticleCategoryMapper;
import com.marxist.leftwing_community.dao.TblArticleInfoMapper;
import com.marxist.leftwing_community.entity.MarkdownEntity;
import com.marxist.leftwing_community.entity.*;
import com.marxist.leftwing_community.service.IAuthorIndexService;
import com.marxist.leftwing_community.service.ILibraryAuthorService;
import com.marxist.leftwing_community.service.ITblArticleContentService;
import com.marxist.leftwing_community.service.IUserService;
import com.marxist.leftwing_community.service.impl.TblArticleCommentServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticleInfoServiceImpl;
import com.marxist.leftwing_community.service.impl.TblArticlePictureServiceImpl;
import com.marxist.leftwing_community.util.MarkDown2HtmlWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class LeftwingCommunityApplicationTests {
    @Autowired
    private TblArticlePictureServiceImpl articlePictureService;

    @Autowired
    private ITblArticleContentService articleContentService;

    @Autowired
    private TblArticleInfoMapper articleInfoMapper;

    @Autowired
    private TblArticleInfoServiceImpl articleInfoService;

    @Autowired
    TblArticleCommentServiceImpl articleCommentService;

    @Autowired
    IAuthorIndexService authorIndexService;

    @Autowired
    ILibraryAuthorService libraryAuthorService;

    @Autowired
    IUserService userService;

    @Autowired
    TblArticleCategoryMapper articleCategoryMapper;

    @Test
    void contextLoads() {
    }

    //测试MarkDown2HtmlWrapper工具类
    @Test
    public void markdown2html() throws IOException {
        File file = new File("D:\\Projects\\LeftwingCommunity-Springboot\\src\\main\\resources\\static\\md"); //需要获取的文件的路径
        String[] fileNameLists = file.list();//存储文件名的String数组
        File[] fileLists = file.listFiles();//存储文件路径的File数组
        assert fileNameLists != null;
        for (int i = 0; i < fileNameLists.length; i++) {
            assert fileLists != null;
            MarkdownEntity markdownEntity = MarkDown2HtmlWrapper.ofFile(fileLists[i].getPath());

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
            htmlContent += markdownEntity.getHtml();//拼接html
            htmlContent += "\n</body>\n" + "</html>";//拼接尾部

            BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\Projects\\LeftwingCommunity-Springboot\\src\\main\\resources\\static\\page\\" + fileLists[i].getName().substring(0, fileLists[i].getName().length()-3) + ".html"));
            bw.write(htmlContent);
            bw.flush();
            bw.close();
        }

    }

    //测试按id获取图片对象List集合
    @Test
    public void testGetPic() {

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

    }

    //测试comment查询数据库对象
    @Test
    public void testComment(){
        List<TblArticleComment> comments = articleCommentService.getComment(1L);
        for (TblArticleComment comment : comments) {
            System.out.println(comment.getId() + "." + comment.getEmail() + "的评论内容:\n" + comment.getComment());
        }

    }

    //测试登录功能
    @Test
    public void testLogin() {
        //测试正确的账号密码
        User user01 = new User();
        user01.setUserAccount("admin");
        user01.setPassword("123456");

        User userLogin01 = userService.userLogin(user01);
        System.err.println(userLogin01);

        //测试错误的账号
        User user02 = new User();
        user01.setUserAccount("root");
        user01.setPassword("password");

        User userLogin02 = userService.userLogin(user02);
        System.err.println(userLogin02);

        //测试错误的密码
        User user03 = new User();
        user01.setUserAccount("admin");
        user01.setPassword("password");

        User userLogin03 = userService.userLogin(user03);
        System.err.println(userLogin03);
    }

    //测试用户分页查询
    @Test
    public void testAdminByPage() {
        IPage<User> listByPage = userService.getUserListByPage(1L);
        for (User user : listByPage.getRecords()) {
            System.err.println(user.getUserAccount() + ":" + user.getPassword());
        }
    }

    //测试按author_id查询文库作者作品索引
    @Test
    public void testGetByAuthorId() {
        List<AuthorIndex> listByAuthorId = authorIndexService.getListByAuthorId(4L);
        for (AuthorIndex authorIndex : listByAuthorId) {
            System.out.println(authorIndex.getArticleId() + "." + authorIndex.getTitle() + ":\n" + authorIndex.getPdfUrl());
        }
    }

    //测试按author_id查询文库作者作品索引并封装为LibraryAuthor对象
    @Test
    public void testGetAuthorByAuthorId() {
        LibraryAuthor authorById = libraryAuthorService.getAuthorById(4L);

        System.err.println(authorById.getId() + "." + authorById.getCharacterName() + ":" + authorById.getPicUrl());
        for (AuthorIndex index : authorById.getAuthorIndices()) {
            System.err.println(index.getArticleId() + ":" + index.getTitle());
        }
    }

    //测试多表查询所有文库作者作品索引并封装为LibraryAuthor对象
    @Test
    public void testGetAllAuthor() {
        List<LibraryAuthor> index = libraryAuthorService.getAllAuthorIndex();
        for (LibraryAuthor libraryAuthor : index) {
            System.err.println(libraryAuthor.getId() + "." + libraryAuthor.getCharacterName());
            for (AuthorIndex authorIndex : libraryAuthor.getAuthorIndices()) {
                System.err.println(authorIndex.getArticleId() + "." + authorIndex.getTitle() + ":\n" + authorIndex.getPdfUrl());
            }

        }
    }

    //测试查询封装了LibraryAuthor对象的authorIndex对象
    @Test
    public void testGetAllIndex() {
        List<AuthorIndex> allAuthorIndex = authorIndexService.getAllAuthorIndex();
        for (AuthorIndex authorIndex : allAuthorIndex) {
            System.err.println(authorIndex.getArticleId() + "." + authorIndex.getTitle() + "：\nPDF地址:" + authorIndex.getPdfUrl() +
                    "\t其所属的作者是 " + authorIndex.getLibraryAuthor().getCharacterName());
        }

    }

    //测试分页查询封装了LibraryAuthor对象的authorIndex对象
    @Test
    public void testGetAllIndexByPage() {
        IPage<AuthorIndex> allAuthorIndex = authorIndexService.getAllAuthorIndexByPage(1L);
        for (AuthorIndex authorIndex : allAuthorIndex.getRecords()) {
            System.err.println(authorIndex.getArticleId() + "." + authorIndex.getTitle() + "：\nPDF地址:" + authorIndex.getPdfUrl() +
                    "\t其所属的作者是 " + authorIndex.getLibraryAuthor().getCharacterName());
        }

    }

    //测试通过文章id获取所有标签并封装
    @Test
    public void testGetCategories() {
        List<TblArticleCategory> categories = articleCategoryMapper.getCategoriesByArticleId(4L);
        for (TblArticleCategory category : categories) {
            System.err.println(category.getId() + "." + category.getCategoryName());
        }

    }

    //测试通过分类标签名字封装文章对象列表
    @Test
    public void textGetInfoByCategory() {
        List<TblArticleInfo> tblArticleInfos = articleCategoryMapper.getInfoByCategory("马尔库塞");
        for (TblArticleInfo articleInfo : tblArticleInfos) {
            System.out.println(articleInfo.getId() + "." + articleInfo.getTitle());
        }

    }

}
