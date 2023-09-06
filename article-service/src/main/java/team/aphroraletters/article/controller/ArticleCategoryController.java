package team.aphroraletters.article.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.aphroraletters.article.pojo.entity.Classification;
import team.aphroraletters.article.pojo.entity.TblArticleCategory;
import team.aphroraletters.article.pojo.entity.TblArticleInfo;
import team.aphroraletters.article.pojo.response.ArticleInfoListVO;
import team.aphroraletters.article.pojo.response.ResultVO;
import team.aphroraletters.article.service.IClassificationService;
import team.aphroraletters.article.service.ITblArticleCategoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleCategoryController {

    @Autowired
    private IClassificationService classificationService;

    @Autowired
    private ITblArticleCategoryService articleCategoryService;

    @ApiOperation("通过标签id获取标签对象")
    @GetMapping("/getArticleCategoryById/{categoryId}")
    public ResultVO getArticleCategoryById(@PathVariable("categoryId") Long categoryId) {
        TblArticleCategory articleCategory = articleCategoryService.getArticleCategoryById(categoryId);
        if (articleCategory == null) {
            return ResultVO.errorException("找不到id={" + categoryId + "}对应的标签对象");
        }

        return ResultVO.ok(articleCategory);
    }

    @ApiOperation("通过文章ID获取对应的标签集合")
    @GetMapping("/getArticleCategoryListByArticleId/{articleId}")
    public ResultVO getArticleCategoryListByArticleId(@PathVariable("articleId") Long articleId) {
        List<TblArticleCategory> categoryList = classificationService.getArticleCategoryListByArticleId(articleId);
        if (categoryList.isEmpty()) {
            return ResultVO.errorMsg("Error:文章没有对应的标签");
        }

        return ResultVO.ok(categoryList);
    }

    @ApiOperation("通过category和page获取文章标签分类数据集合")
    @GetMapping("/getArticleListByCategoryIdAndPage/{categoryId}/{page}")
    public ResultVO getArticleInfoListByPage(@PathVariable("categoryId") Long categoryId, @PathVariable("page") Long page) {
        IPage<TblArticleInfo> articleInfoByCategoryIdAndPage = classificationService.getArticleInfoByCategoryIdAndPage(categoryId, page);
        if (articleInfoByCategoryIdAndPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("无效的页码或标签");
        }

        // 封装新的分页对象：ArticleListResponse
        IPage<ArticleInfoListVO> articleListVOIPage = new Page<>();
        List<ArticleInfoListVO> articleInfoListVOS = new ArrayList<>();
        for (TblArticleInfo articleInfo : articleInfoByCategoryIdAndPage.getRecords()) {
            ArticleInfoListVO articleInfoListVO = new ArticleInfoListVO();
            BeanUtils.copyProperties(articleInfo, articleInfoListVO); // 拷贝数据至response对象中

            articleInfoListVOS.add(articleInfoListVO);
        }
        BeanUtils.copyProperties(articleInfoByCategoryIdAndPage, articleListVOIPage); // 拷贝分页对象其他参数
        articleListVOIPage.setRecords(articleInfoListVOS);

        return ResultVO.ok(articleListVOIPage);
    }

    @ApiOperation("模糊查询获取推荐标签")
    @GetMapping("/getRecommendCategory/{categoryName}")
    public ResultVO getRecommendCategory(@PathVariable String categoryName) {
        List<TblArticleCategory> recommendCategory = articleCategoryService.getRecommendCategory(categoryName);
        if (recommendCategory.isEmpty()) {
            return ResultVO.errorMsg("无推荐标签");
        }

        return ResultVO.ok(recommendCategory);
    }

    @ApiOperation("添加新的标签")
    @PostMapping("/admin/insertArticleCategory")
    public ResultVO insertArticleCategory(@RequestBody TblArticleCategory articleCategory) {
        try {
            Long insertedId = articleCategoryService.insertCategory(articleCategory);
            if (insertedId != -1L) {
                return ResultVO.ok(insertedId);
            } else {
                return ResultVO.errorMsg("标签{" + articleCategory.getCategoryName() + "}已存在");
            }
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }
    }

    @ApiOperation("向文章添加对应标签")
    @PostMapping("/admin/insertClassification")
    public ResultVO insertClassification(@RequestBody Classification classification) {
        try {
            classificationService.insertArticleCategoryToArticle(classification);
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("添加成功");
    }

    @ApiOperation("删除id对应的标签映射")
    @DeleteMapping("/admin/deleteClassification/{articleId}/{categoryId}")
    public ResultVO deleteClassification(@PathVariable("articleId") Long articleId,
                                         @PathVariable("categoryId") Long categoryId) {
        try {
            classificationService.deleteCategory(articleId, categoryId);
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("删除对应标签成功");
    }

}
