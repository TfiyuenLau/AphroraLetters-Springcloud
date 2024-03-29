package team.aphroraletters.article.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-09-21
 */
@Getter
@Setter
@TableName("tbl_article_content")
@ApiModel(value = "TblArticleContent对象", description = "")
public class TblArticleContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("对应文章ID")
    private Long articleId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime modifiedBy;

    @ApiModelProperty("是否有效")
    private Boolean isEffective;
}
