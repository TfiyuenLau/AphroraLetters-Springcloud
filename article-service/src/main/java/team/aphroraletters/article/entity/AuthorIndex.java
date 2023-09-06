package team.aphroraletters.article.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-10-29
 */
@Getter
@Setter
@TableName("author_index")
@ApiModel(value = "AuthorIndex对象", description = "")
public class AuthorIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文章PDF地址")
    private String pdfUrl;

    @ApiModelProperty("外键映射library_author(id)")
    private Long authorId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Boolean isEffective;

    @TableField(exist = false)
    @ApiModelProperty("代表当前对象所属作者")
    private LibraryAuthor libraryAuthor;
}
