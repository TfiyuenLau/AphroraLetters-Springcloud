package team.aphroraletters.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * tbl_article_info与classification多对多中间表
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-11-21
 */
@Getter
@Setter
@ApiModel(value = "Classification对象", description = "tbl_article_info与classification多对多中间表")
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章外键")
    private Long articleId;

    @ApiModelProperty("分类标签外键")
    private Long categoryId;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Boolean isEffective;
}
