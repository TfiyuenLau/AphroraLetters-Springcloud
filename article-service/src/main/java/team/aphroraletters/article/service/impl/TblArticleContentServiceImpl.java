package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import team.aphroraletters.article.dao.TblArticleContentMapper;
import team.aphroraletters.article.entity.MarkdownEntity;
import team.aphroraletters.article.entity.TblArticleContent;
import team.aphroraletters.article.entity.TblArticleInfo;
import team.aphroraletters.article.service.ITblArticleContentService;
import team.aphroraletters.article.util.MarkDown2HtmlWrapper;

import java.io.*;

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

    @Override
    public TblArticleContent getArticleContentById(Long id) {
        return articleContentMapper.selectById(id);
    }

    /**
     * 根据articleId键名获取对象
     *
     * @param articleId
     * @return
     */
    @Override
    public TblArticleContent getArticleContentByArticleId(Long articleId) {
        LambdaQueryWrapper<TblArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TblArticleContent::getArticleId, articleId);

        return articleContentMapper.selectOne(queryWrapper);
    }

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
        MarkdownEntity markdownEntity = MarkDown2HtmlWrapper.ofContent(content.getContent());
        String htmlContent = MarkDown2HtmlWrapper.htmlAppend(markdownEntity);

        //获取文件名
        String fileName = articleInfoService.getArticleTitle(id) + ".html";
        //获取上传文件的地址
        File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径绝对my-web-project\target\classes
        String absolutePath = projectPath.getAbsolutePath();
        //放入/static/page/目录下
        String path = absolutePath + "/static/page/";
        File targetFile = new File(path, fileName);

        //写入文件至target
        BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile));
        bw.write(htmlContent);
        bw.flush();
        bw.close();

        //复制target文件到/static/page/(仅idea开发时使用)
        //Files.copy(new File(targetFile.getAbsolutePath()).toPath(), new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\page\\" + fileName).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 按字段搜索文章content并返回列表对象
     *
     * @param contentLike
     * @return QueryWrapper
     */
    @Override
    public IPage<TblArticleContent> searchContent(String contentLike, Integer page) {
        //模糊查询content内容
        QueryWrapper<TblArticleContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("content", contentLike);

        //分页查询
        Page<TblArticleContent> articleContentPage = new Page<>(page, 10);
        articleContentMapper.selectPage(articleContentPage, queryWrapper);

        return articleContentPage;
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
        articleInfoService.insertArticleInfo(articleInfo);

        return articleContent.getId();
    }

    @Override
    public int insertArticleContent(TblArticleContent articleContent) {
        return articleContentMapper.insert(articleContent);
    }

    /**
     * 逻辑删除内容
     *
     * @param id
     * @return
     */
    @Override
    public int delContent(Long id) {

        return articleContentMapper.deleteById(id);
    }

    @Override
    public int updateArticleContent(TblArticleContent articleContent) {
        return articleContentMapper.updateById(articleContent);
    }

}
