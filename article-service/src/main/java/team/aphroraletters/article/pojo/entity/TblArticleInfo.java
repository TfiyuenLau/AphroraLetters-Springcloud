package team.aphroraletters.article.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * @author @TfiyuenLau
 * @since 2022-09-21
 */
@Getter
@Setter
@TableName("tbl_article_info")
@ApiModel(value = "TblArticleInfo对象", description = "")
public class TblArticleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章简介，默认100个汉字以内")
    private String summary;

    @ApiModelProperty("图片url")
    private String pictureUrl;

    @ApiModelProperty("文章访问量")
    private Integer traffic;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("修改日期")
    private LocalDateTime modifiedBy;

    @ApiModelProperty("文章是否通过审核，0为否，1为是")
    private Boolean isPassed;

    @ApiModelProperty("是否有效")
    private Boolean isEffective;

    @TableField(exist = false)
    @ApiModelProperty("代表当前文章所属的所有标签")
    private List<TblArticleCategory> categoryList;

    /**
     * 获取格式化后的时间字符串
     *
     * @return String
     */
    public String getTime() {

        return this.getCreateBy().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

}
