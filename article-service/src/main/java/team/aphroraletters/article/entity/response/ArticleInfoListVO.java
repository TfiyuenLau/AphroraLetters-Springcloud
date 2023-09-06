package team.aphroraletters.article.entity.response;

import lombok.Data;
import team.aphroraletters.article.entity.TblArticleCategory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class ArticleInfoListVO {
    private Long id;

    private String title;

    private String pictureUrl;

    private String summary;

    private Boolean isPassed;

    private Integer traffic;

    private LocalDateTime createBy;

    private LocalDateTime modifiedBy;

    private List<TblArticleCategory> categoryList;

    public String getTime() {
        return this.getModifiedBy().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }
}
