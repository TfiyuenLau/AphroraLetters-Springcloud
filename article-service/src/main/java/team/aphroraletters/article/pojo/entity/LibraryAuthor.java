package team.aphroraletters.article.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
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
 * @since 2022-10-29
 */
@Getter
@Setter
@TableName("library_author")
@ApiModel(value = "LibraryAuthor对象", description = "")
public class LibraryAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("索引作者名")
    private String characterName;

    @ApiModelProperty("图片地址")
    private String picUrl;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;

    @TableField(exist = false)//mybatis-plus忽略映射字段:表示该属性不为数据库表字段，但又是必须使用的。
    @ApiModelProperty("作者下拥有的所有文章索引")
    private List<AuthorIndex> authorIndices;
}
