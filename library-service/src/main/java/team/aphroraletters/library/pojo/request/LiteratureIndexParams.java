package team.aphroraletters.library.pojo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteratureIndexParams {

    @ApiModelProperty("主键")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文章PDF地址")
    private String pdfUrl;

    @ApiModelProperty("外键映射library_author(id)")
    private Long authorId;

}
