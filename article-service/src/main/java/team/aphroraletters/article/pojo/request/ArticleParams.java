package team.aphroraletters.article.pojo.request;

import lombok.Data;

@Data
public class ArticleParams {

    private String title;

    private String pictureUrl;

    private String summary;

    private String content;

}
