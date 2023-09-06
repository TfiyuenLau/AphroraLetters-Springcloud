package team.aphroraletters.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2022-10-20
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Admin对象", description = "")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("管理员名称")
    private String realname;

    @ApiModelProperty("账户号")
    private String username;

    @ApiModelProperty("管理员密码")
    private String password;

    @ApiModelProperty("个人描述信息")
    private String description;

    @ApiModelProperty("权限（默认admin）")
    private String authority;

    @ApiModelProperty("权限列表")
    @TableField(exist = false) // 忽略字段的数据库映射
    private List<String> roles;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;

}
