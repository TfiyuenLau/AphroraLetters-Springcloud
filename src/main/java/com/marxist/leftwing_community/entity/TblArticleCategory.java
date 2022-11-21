package com.marxist.leftwing_community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@TableName("tbl_article_category")
@ApiModel(value = "TblArticleCategory对象", description = "")
public class TblArticleCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分类标签名称")
    private String categoryName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime modifiedBy;

    @TableField(exist = false)
    @ApiModelProperty("标识当前标签的所有文章")
    private List<TblArticleInfo> articleInfoList;

    @TableField(exist = false)
    @ApiModelProperty("标识当前文章所属的所有标签")
    private List<TblArticleCategory> articleCategoryList;
}
