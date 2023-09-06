package team.aphroraletters.article.entity.response;

import lombok.Data;
import team.aphroraletters.article.entity.TblArticleCategory;
import team.aphroraletters.article.entity.TblArticleComment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class ArticleVO {
    private Long id;

    private String title;

    private String pictureUrl;

    private String summary;

    private String content;

    private Integer traffic;

    private LocalDateTime createBy;

    private LocalDateTime modifiedBy;

    private Boolean isPassed;

    private List<TblArticleCategory> categoryList;

    private List<TblArticleComment> articleCommentList;

    public String getTime() {
        return this.getModifiedBy().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }
}
