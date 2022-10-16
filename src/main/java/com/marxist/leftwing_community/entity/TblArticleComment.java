package com.marxist.leftwing_community.entity;

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
@TableName("tbl_article_comment")
@ApiModel(value = "TblArticleComment对象", description = "")
public class TblArticleComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章ID")
    private Long articleId;

    @ApiModelProperty("评论者邮箱")
    private String email;

    @ApiModelProperty("对应的留言")
    private String comment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("是否有效，默认为1有效，置0无效")
    private Boolean isEffective;
}
